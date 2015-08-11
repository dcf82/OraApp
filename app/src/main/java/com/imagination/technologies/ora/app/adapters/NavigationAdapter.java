package com.imagination.technologies.ora.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.beans.Option;

import java.util.ArrayList;

public class NavigationAdapter extends ArrayAdapter<Option> {
    Option option;
    LayoutInflater li;
    Holder holder;

    public NavigationAdapter(Context ctx, int textViewResourceId,
                           ArrayList<Option> e) {
        super(ctx, textViewResourceId, e);
        li = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null || !(view.getTag() instanceof Holder)) {
            view = li.inflate(R.layout.navigation_option, parent, false);

            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.option);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        option = getItem(position);
        holder.name.setText(option.getName());

        return view;
    }

    private class Holder {
        TextView name;
    }
}
