package com.example.enfoquesalud;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enfoquesalud.Model.Notification;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class RvAdapterNotification extends RecyclerView.Adapter<RvAdapterNotification.ViewHolder>{
    List<Notification> listItems;
    public RvAdapterNotification(List<Notification> listItems){ this.listItems = listItems;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_n_items,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(listItems.get(position).getTitle());
        holder.tvdescription.setText(listItems.get(position).getDescription());
        holder.tvMinutesAgo.setText(listItems.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView tvTitle;
        MaterialTextView tvdescription;
        MaterialTextView tvMinutesAgo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvdescription = itemView.findViewById(R.id.tvdescription);
            tvMinutesAgo = itemView.findViewById(R.id.tvMinutesAgo);
        }
    }
}
