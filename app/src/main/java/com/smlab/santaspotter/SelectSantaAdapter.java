package com.smlab.santaspotter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
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
        Log.d("onBindViewHolder", "Position: " + position + ", Locked: " + item.isLocked());

        if (item.isLocked()) {
//            holder.santaSticker.setVisibility(View.VISIBLE);
            holder.santaSticker.setImageResource(item.getStickerImageResource());
            holder.lockIcon.setVisibility(View.VISIBLE);

            if (selectedItem == position) {
                holder.santaSticker.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_selected_santa));
            } else {
                holder.santaSticker.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_round_santa_sticker));
            }
        } else {
//            holder.santaSticker.setVisibility(View.VISIBLE);
//            holder.santaSticker.setImageResource(item.getSantaSticker());
            holder.santaSticker.setImageResource(item.getStickerImageResource());

            holder.lockIcon.setVisibility(View.GONE);

            if (selectedItem == position) {
                holder.santaSticker.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_selected_santa));
            } else {
                holder.santaSticker.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_round_santa_sticker));
            }
        }

        View.OnClickListener clickListener = v -> {
            selectedItem = position;
            notifyDataSetChanged();
            onItemClickListener.onItemClick(position);
        };

        holder.santaSticker.setOnClickListener(clickListener);
    }



    @Override
    public int getItemCount() {
        return selectSantaModelArrayList.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        ImageView santaSticker,lockIcon;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            santaSticker = itemView.findViewById(R.id.santaSticker);
            lockIcon = itemView.findViewById(R.id.lockIcon);
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
