package com.smlab.santaspotter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectSantaAdapter extends RecyclerView.Adapter<SelectSantaAdapter.viewholder> {
    SelectSanta selectSanta;
    ArrayList<SelectSantaModel> selectSantaModelArrayList;

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
    public void onBindViewHolder(@NonNull SelectSantaAdapter.viewholder holder, int position) {

        SelectSantaModel item = selectSantaModelArrayList.get(position);
        holder.santaSticker.setImageResource(item.getSantaSticker());
    }

    @Override
    public int getItemCount() {
        return selectSantaModelArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView santaSticker;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            santaSticker = itemView.findViewById(R.id.firstSantaSticker);
//            santaSticker = itemView.findViewById(R.id.secondSantaSticker);
//            santaSticker = itemView.findViewById(R.id.thirdSantaSticker);
        }
    }
}
