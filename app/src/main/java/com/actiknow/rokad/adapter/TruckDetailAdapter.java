package com.actiknow.rokad.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.model.TruckDetail;
import com.actiknow.rokad.utils.SetTypeFace;

import java.util.ArrayList;
import java.util.List;


public class TruckDetailAdapter extends RecyclerView.Adapter<TruckDetailAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private List<TruckDetail> truckDetailList = new ArrayList<TruckDetail> ();
    
    public TruckDetailAdapter (Activity activity, List<TruckDetail> truckDetailList) {
        this.activity = activity;
        this.truckDetailList = truckDetailList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.list_item_truck_list, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final TruckDetail truckDetail = truckDetailList.get (position);
        
        holder.tvTruckNumber.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvOwnerName.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvOwnerMobile.setTypeface (SetTypeFace.getTypeface (activity));
        
        holder.tvTruckNumber.setText (truckDetail.getTruck_number ());
        holder.tvOwnerName.setText (truckDetail.getOwner_name ());
        holder.tvOwnerMobile.setText (truckDetail.getOwner_mobile ());
    }
    
    @Override
    public int getItemCount () {
        return truckDetailList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTruckNumber;
        TextView tvOwnerName;
        TextView tvOwnerMobile;
        
        public ViewHolder (View view) {
            super (view);
            tvTruckNumber = (TextView) view.findViewById (R.id.tvTruckNumber);
            tvOwnerName = (TextView) view.findViewById (R.id.tvOwnerName);
            tvOwnerMobile = (TextView) view.findViewById (R.id.tvOwnerNumber);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
            mItemClickListener.onItemClick (v, getLayoutPosition ());
//            TruckDetail truckDetail = truckDetailList.get (getLayoutPosition ());
        }
    }
}