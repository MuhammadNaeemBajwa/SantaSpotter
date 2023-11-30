package com.smlab.santaspotter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectSantaAdapter extends RecyclerView.Adapter<SelectSantaAdapter.viewholder> {
    SelectSanta selectSanta;
    ArrayList<SelectSantaModel> selectSantaModelArrayList;
    private int selectedItem = RecyclerView.NO_POSITION;
    private OnItemClickListener onItemClickListener;
    private Context context;



    public SelectSantaAdapter(SelectSanta selectSanta, ArrayList<SelectSantaModel> selectSantaModelArrayList,  OnItemClickListener listener) {
        this.selectSanta = selectSanta;
        this.selectSantaModelArrayList = selectSantaModelArrayList;
        this.onItemClickListener = listener;

    }

    @NonNull
    @Override
    public SelectSantaAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(selectSanta).inflate(R.layout.select_santa_item_view,parent, false);
        return new viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SelectSantaAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {
        SelectSantaModel item = selectSantaModelArrayList.get(position);

        // Check if the item is locked
        if (item.isLocked()) {
            // Item is locked, show the lockSanta layout
            holder.unlockSanta.setVisibility(View.GONE);
            holder.lockSanta.setVisibility(View.VISIBLE);
            // Set the appropriate image for the locked state
            holder.lockSantaSticker.setImageResource(item.getStickerImageResource());

            // Set the visibility of the lock icon
            holder.lockIcon.setVisibility(View.VISIBLE);

            // Highlight the selected item for locked state
            if (selectedItem == position) {
                holder.lockSanta.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_selected_santa));
            } else {
                holder.lockSanta.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_round_santa_sticker));
            }
        } else {
            // Item is unlocked, show the unlockSanta layout
            holder.unlockSanta.setVisibility(View.VISIBLE);
            holder.lockSanta.setVisibility(View.GONE);
            // Set the appropriate image for the unlocked state
            holder.unlockSantaSticker.setImageResource(item.getSantaSticker());

            // Set the visibility of the lock icon
            holder.lockIcon.setVisibility(View.GONE);

            // Highlight the selected item for unlocked state
            if (selectedItem == position) {
                holder.unlockSanta.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_selected_santa));
            } else {
                holder.unlockSanta.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_round_santa_sticker));
            }
        }

        // Set click listener for both layouts
        View.OnClickListener clickListener = v -> {
            selectedItem = position;
            notifyDataSetChanged();
            onItemClickListener.onItemClick(position);
        };

        holder.unlockSanta.setOnClickListener(clickListener);
        holder.lockSanta.setOnClickListener(clickListener);
    }



    @Override
    public int getItemCount() {
        return selectSantaModelArrayList.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        ImageView unlockSantaSticker, lockIcon,lockSantaSticker;
        ConstraintLayout unlockSanta,lockSanta;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            unlockSantaSticker = itemView.findViewById(R.id.unlockSantaSticker);
            lockIcon = itemView.findViewById(R.id.lockIcon);
            unlockSanta = itemView.findViewById(R.id.unlockSanta);
            lockSantaSticker = itemView.findViewById(R.id.lockSantaSticker);
            lockSanta = itemView.findViewById(R.id.lockSanta);
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
