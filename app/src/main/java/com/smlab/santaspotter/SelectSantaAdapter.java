package com.smlab.santaspotter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectSantaAdapter extends RecyclerView.Adapter<SelectSantaAdapter.viewholder> {
    SelectSanta selectSanta;
    ArrayList<SelectSantaModel> selectSantaModelArrayList;
    private int selectedItem = RecyclerView.NO_POSITION;
    private OnItemClickListener onItemClickListener;
    private Context context;


    public SelectSantaAdapter(Context context, SelectSanta selectSanta, ArrayList<SelectSantaModel> selectSantaModelArrayList, OnItemClickListener listener) {
        this.context = context;
        this.selectSanta = selectSanta;
        this.selectSantaModelArrayList = selectSantaModelArrayList;
        this.onItemClickListener = listener;

    }

    @NonNull
    @Override
    public SelectSantaAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(selectSanta).inflate(R.layout.select_santa_item_view, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectSantaAdapter.viewholder holder, @SuppressLint("RecyclerView") int position) {

        SelectSantaModel item = selectSantaModelArrayList.get(position);
        holder.santaSticker.setImageResource(item.getSantaSticker());


        if (item.isLocked()) {

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
            holder.santaSticker.setColorFilter(colorFilter);

            holder.santaSticker.setImageResource(item.getStickerImageResource());
            holder.lockIcon.setVisibility(View.VISIBLE);
            holder.lockColor.setVisibility(View.VISIBLE);

        } else {
            Log.d("TAG", "onBindViewHolder: "+item.getStickerImageResource());
            holder.santaSticker.setImageResource(item.getStickerImageResource());
            holder.lockIcon.setVisibility(View.GONE);
            holder.lockColor.setVisibility(View.GONE);

        }
        if (selectedItem == position) {
            holder.santaSticker.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_selected_santa));
        } else {
            holder.santaSticker.setBackground(ContextCompat.getDrawable(selectSanta, R.drawable.background_round_santa_sticker));
        }

        holder.santaSticker.setOnClickListener(v -> {
            selectedItem = position;
            notifyDataSetChanged();
            onItemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return selectSantaModelArrayList.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        ImageView santaSticker, lockIcon, lockColor;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            //comment
            santaSticker = itemView.findViewById(R.id.santaSticker);
            lockIcon = itemView.findViewById(R.id.lockIcon);
            lockColor = itemView.findViewById(R.id.lockColor);

        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
