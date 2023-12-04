package com.smlab.santaspotter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectSantaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    SelectSanta selectSanta;
    ArrayList<SelectSantaModel> selectSantaModelArrayList;
    private int selectedItem = RecyclerView.NO_POSITION;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private static final int VIEW_TYPE_Locked_SANTA = 1;
    private static final int VIEW_TYPE_SANTA = 2;


    public SelectSantaAdapter(Context context, SelectSanta selectSanta, ArrayList<SelectSantaModel> selectSantaModelArrayList, OnItemClickListener listener) {
        this.context = context;
        this.selectSanta = selectSanta;
        this.selectSantaModelArrayList = selectSantaModelArrayList;
        this.onItemClickListener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_Locked_SANTA) {
            holder = new LockedSantaViewHolder(LayoutInflater.from(selectSanta).inflate(R.layout.santa_item_view_locked, parent, false));
        } else {
            holder = new SantaViewHolder(LayoutInflater.from(selectSanta).inflate(R.layout.santa_item_view, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SelectSantaModel selectSantaModel = selectSantaModelArrayList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_Locked_SANTA) {
            bindLockedSantaViewHolder((LockedSantaViewHolder) holder, selectSantaModel, position);
        } else {
            bindSantaViewHolder((SantaViewHolder) holder, selectSantaModel, position);
        }
    }

    private void bindSantaViewHolder(SantaViewHolder holder, SelectSantaModel selectSantaModel, int position) {
        holder.santaSticker.setImageResource(selectSantaModel.getSantaSticker());

        Log.d("TAG", "onBindViewHolder: " + selectSantaModel.getStickerImageResource());
        holder.santaSticker.setImageResource(selectSantaModel.getStickerImageResource());

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

    private void bindLockedSantaViewHolder(LockedSantaViewHolder holder, SelectSantaModel selectSantaModel, int position) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        holder.santaSticker.setColorFilter(colorFilter);
        holder.santaSticker.setImageResource(selectSantaModel.getStickerImageResource());

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
    public int getItemViewType(int position) {
        if (selectSantaModelArrayList.get(position).isLocked()) {
            return VIEW_TYPE_Locked_SANTA;
        } else {
            return VIEW_TYPE_SANTA;
        }
    }

    @Override
    public int getItemCount() {
        return selectSantaModelArrayList.size();
    }


    public static class SantaViewHolder extends RecyclerView.ViewHolder {
        ImageView santaSticker;

        public SantaViewHolder(@NonNull View itemView) {
            super(itemView);
            //comment
            santaSticker = itemView.findViewById(R.id.santaSticker);

        }


    }

    public static class LockedSantaViewHolder extends RecyclerView.ViewHolder {
        ImageView santaSticker, lockIcon, lockColor;

        public LockedSantaViewHolder(@NonNull View itemView) {
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
