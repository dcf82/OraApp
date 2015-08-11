package com.imagination.technologies.ora.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imagination.technologies.ora.app.R;
import com.imagination.technologies.ora.app.greendao.Locations;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationHolder> {
    private static String LOG = LocationsAdapter.class.getName();
    private ArrayList<Locations> myLocations;

    public static class LocationHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.zipCode)
        TextView zipCode;
        @Bind(R.id.zipClass)
        TextView zipClass;
        @Bind(R.id.county)
        TextView county;
        @Bind(R.id.city)
        TextView city;
        @Bind(R.id.state)
        TextView state;
        @Bind(R.id.longitude)
        TextView longitude;
        @Bind(R.id.latitude)
        TextView latitude;

        public LocationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public LocationsAdapter(ArrayList<Locations> locations) {
        myLocations = locations;
    }

    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationHolder holder, int position) {
        holder.zipCode.setText(myLocations.get(position).getKZipcode());
        holder.zipClass.setText(myLocations.get(position).getKZipClass());
        holder.county.setText(myLocations.get(position).getKCounty());
        holder.city.setText(myLocations.get(position).getKCity());
        holder.state.setText(myLocations.get(position).getKState());
        holder.longitude.setText(myLocations.get(position).getKLongitude());
        holder.latitude.setText(myLocations.get(position).getKLatitude());
    }

    public void addItem(Locations item) {
        myLocations.add(item);
        notifyDataSetChanged();
    }

    public void addItems(List<Locations> locations) {
        myLocations.addAll(locations);
        notifyDataSetChanged();
    }

    public void replaceItems(List<Locations> locations) {
        myLocations.clear();
        myLocations.addAll(locations);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return myLocations.size();
    }
}
