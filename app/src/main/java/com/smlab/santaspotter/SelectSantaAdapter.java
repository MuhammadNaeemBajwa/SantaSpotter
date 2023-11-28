package com.smlab.santaspotter;

import android.annotation.SuppressLint;
import android.graphics.Color;
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


    public SelectSantaAdapter(SelectSanta selectSanta, ArrayList<SelectSantaModel> selectSantaModelArrayList) {
        this.selectSanta = selectSanta;
        this.selectSantaModelArrayList = selectSantaModelArrayList;
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
        holder.santaSticker.setImageResource(item.getSantaSticker());

        if (selectedItem == position) {
            holder.santaCardView.setBackground(ContextCompat.getDrawable(selectSanta,R.drawable.background_selected_santa));
        } else {
            holder.santaCardView.setBackground(ContextCompat.getDrawable(selectSanta,R.drawable.background_round_santa_sticker));
        }

        // Set an item click listener
        holder.santaCardView.setOnClickListener(v -> {
            selectedItem = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return selectSantaModelArrayList.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        ImageView santaSticker;
        ConstraintLayout santaCardView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            santaSticker = itemView.findViewById(R.id.firstSantaSticker);
            santaCardView = itemView.findViewById(R.id.cardViewSanta);
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
