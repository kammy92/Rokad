package com.actiknow.rokad.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.model.Destination;
import com.actiknow.rokad.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;


public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<Destination> destinationList = new ArrayList<Destination> ();
    
    public DestinationAdapter (Activity activity, List<Destination> destinationList) {
        this.activity = activity;
        this.destinationList = destinationList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_destination, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        final Destination destination = destinationList.get (position);
        holder.tvDestinationName.setTypeface (SetTypeFace.getTypeface (activity));
    
        holder.tvDestinationName.setText (destination.getName ());
    }
    
    @Override
    public int getItemCount () {
        return destinationList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDestinationName;
        
        public ViewHolder (View view) {
            super (view);
            tvDestinationName = (TextView) view.findViewById (R.id.tvDestination);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
        }
    }
}