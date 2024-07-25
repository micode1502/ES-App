package com.example.enfoquesalud;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enfoquesalud.Model.Read.Medical;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class RvAdapterMedical extends RecyclerView.Adapter<RvAdapterMedical.ViewHolderM> {
    List<Medical> listItems;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int doctorId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public RvAdapterMedical(List<Medical> listItems){
        this.listItems = listItems;
    }
    @NonNull
    @Override
    public ViewHolderM onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_m_items,parent, false);
        return new ViewHolderM(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderM holder, int position) {
        holder.imageView.setImageResource(R.drawable.doctor);
        holder.tvName.setText(listItems.get(position).getName());
        holder.tvdescription.setText(listItems.get(position).getDescription());
        holder.tvRating.setText(String.valueOf(listItems.get(position).getRating()));
        holder.mbSeeMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int doctorId = listItems.get(position).getId();
                if (listener != null) {
                    listener.onItemClick(doctorId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolderM extends RecyclerView.ViewHolder{
        CardView cvItem;
        ImageView imageView;
        TextView tvName;
        TextView tvdescription;
        MaterialTextView tvRating;
        MaterialButton mbSeeMedical;


        public ViewHolderM(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.rv_item_m);
            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.tvTitle);
            tvdescription = itemView.findViewById(R.id.tvdescription);
            tvRating = itemView.findViewById(R.id.tvMinutesAgo);
            mbSeeMedical = itemView.findViewById(R.id.mbSeeMedical);
        }
    }
}
