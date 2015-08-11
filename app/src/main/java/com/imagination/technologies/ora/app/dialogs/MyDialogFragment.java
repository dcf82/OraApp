package com.imagination.technologies.ora.app.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.utilities.Utility;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDialogFragment extends DialogFragment {
    private static final String LOG = MyDialogFragment.class.getName();
    MyDialogFragmentInterface mListener;
    @Bind(R.id.name)
    EditText name;

    public static MyDialogFragment newDialog(MyDialogFragmentInterface listener) {
        MyDialogFragment dialog = new MyDialogFragment();
        dialog.mListener = listener;
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        View view = getView();
        ButterKnife.bind(this, view);
    }

    @OnClick({ R.id.ok, R.id.cancel })
    public void onClickView(View view) {
        switch(view.getId()) {
            case R.id.ok:
                String n = name.getText().toString().trim();

                if (n.equals("")) {
                    Utility.showToast(getResources().getString(R.string.write_name));
                    return;
                }

                dismiss();

                if (mListener != null) {
                    mListener.ok(n);
                }

                break;

            case R.id.cancel:
                dismiss();
                break;
        }
    }

    public interface MyDialogFragmentInterface {
        void ok(String name);
    }
}
