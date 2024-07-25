package com.example.enfoquesalud;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RvAdapterHome extends RecyclerView.Adapter<RvAdapterHome.ViewHolder>{
    List<String> listItems;
    private OnItemClickListener onItemClickListener;

    public RvAdapterHome(List<String> listItems) {
        this.listItems = listItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mbItem.setText(listItems.get(position));
        holder.mbItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(listItems.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialButton mbItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mbItem = itemView.findViewById(R.id.rv_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String specialty);
    }
}
