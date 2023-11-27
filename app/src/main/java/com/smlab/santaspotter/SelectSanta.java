package com.smlab.santaspotter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectSanta extends AppCompatActivity {
    ConstraintLayout includePickMe;
    RecyclerView recyclerView;
    SelectSantaAdapter adapter;
    ArrayList<SelectSantaModel> selectSantaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_santa);
        getSupportActionBar().hide();

        selectSantaList = new ArrayList<>();

        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker_1));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_img));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.select_santa));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker_1));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectSantaAdapter(SelectSanta.this, selectSantaList);
        recyclerView.setAdapter(adapter);

        includePickMe = findViewById(R.id.pick_me_include);
        recyclerView = findViewById(R.id.recyclerView2);

        includePickMe.findViewById(R.id.pick_me_button).setOnClickListener(view -> showCodeDialog());

        includePickMe.findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());
    }
    Dialog dialogCode;
    private void showCodeDialog(){
             dialogCode = new Dialog(SelectSanta.this, R.style.dialog);
        dialogCode.setContentView(R.layout.unlock_popup_dialog);
            ImageView close;
            TextView dontHaveCode;
            close = dialogCode.findViewById(R.id.imageView_cross);
            dontHaveCode = dialogCode.findViewById(R.id.dont_have_code_textView);
            close.setOnClickListener(view -> dialogCode.dismiss());
            dontHaveCode.setOnClickListener(view -> showAccessCodeDialog());
        dialogCode.show();


    }

    private void showAccessCodeDialog(){
        Dialog dialog = new Dialog(SelectSanta.this, R.style.dialog);
        dialog.setContentView(R.layout.access_code_dialog);

        ImageView close;
        close = dialog.findViewById(R.id.imageView_cross);
        close.setOnClickListener(view -> dialog.dismiss());

        dialog.show();

        dialogCode.dismiss();

    }
}
