package com.example.enfoquesalud;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RvAdapterAvailableHours extends RecyclerView.Adapter<RvAdapterAvailableHours.ViewHolder>{
    List<String> listItems;
    public RvAdapterAvailableHours(List<String> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_a_h_items,parent, false);
        return new RvAdapterAvailableHours.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String originalDate = listItems.get(position);
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm", new Locale("es", "ES"));
        try {
            Date parsedDate = inputFormat.parse(originalDate);
            String formattedDate = outputFormat.format(parsedDate);
            holder.mbItem.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialButton mbItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mbItem = itemView.findViewById(R.id.rv_a_h_item);
        }
    }
}
