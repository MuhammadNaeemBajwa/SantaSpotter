package com.smlab.santaspotter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;
import com.smlab.santaspotter.databinding.ActivitySelectSantaBinding;

import java.util.ArrayList;

public class SelectSanta extends AppCompatActivity implements SelectSantaAdapter.OnItemClickListener {
    ConstraintLayout includePickMe;
    RecyclerView recyclerView;
    SelectSantaAdapter adapter;
    ArrayList<SelectSantaModel> selectSantaList;
    Dialog dialogCode;
    private ActivitySelectSantaBinding binding;
    private ImageView selectedSantaSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectSantaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setIds();
        initialized();
        setUpRecyclerView();

    }


    private void setIds() {
        includePickMe = findViewById(R.id.pick_me_include);
        recyclerView = findViewById(R.id.recyclerView2);
        includePickMe.findViewById(R.id.pick_me_button).setOnClickListener(view -> showCodeDialog());
        includePickMe.findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        selectedSantaSticker = binding.pickMeInclude.selectSantaSticker;

    }

    private void initialized() {
        selectSantaList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker_1));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_img));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.select_santa));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker_1));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa_sticker));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SelectSantaAdapter(SelectSanta.this, selectSantaList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(int position) {
        SelectSantaModel selectedSanta = selectSantaList.get(position);
        Log.d(TAG, "onItemClick: " + selectedSanta);
        selectedSantaSticker.setImageResource(selectedSanta.getSantaSticker());
        Log.d(TAG, "onItemClick: getSantaSticker:" + selectedSanta.getSantaSticker());
    }
    private void showCodeDialog() {
        dialogCode = new Dialog(SelectSanta.this, R.style.dialog);
        dialogCode.setContentView(R.layout.unlock_popup_dialog);
        ImageView close;
        Button submit;
        TextView dontHaveCode, errorMessage;
        EditText enterCode;
        submit = dialogCode.findViewById(R.id.submit_button);
        errorMessage = dialogCode.findViewById(R.id.error_message);
        close = dialogCode.findViewById(R.id.imageView_cross);
        dontHaveCode = dialogCode.findViewById(R.id.dont_have_code_textView);
        enterCode = dialogCode.findViewById(R.id.editText_enterCode);

        close.setOnClickListener(view -> dialogCode.dismiss());

        String enterCodeValue = enterCode.getText().toString();
        submit.setOnClickListener(view -> {
            if (enterCodeValue.isEmpty() || enterCodeValue.isBlank() || enterCodeValue == null) {
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
        dontHaveCode.setOnClickListener(view -> showAccessCodeDialog());
        dialogCode.show();

    }

    private void showAccessCodeDialog() {
        Dialog dialog = new Dialog(SelectSanta.this, R.style.dialog);
        dialog.setContentView(R.layout.access_code_dialog);
        ImageView close;
        close = dialog.findViewById(R.id.imageView_cross);
        close.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
        dialogCode.dismiss();

    }


}
