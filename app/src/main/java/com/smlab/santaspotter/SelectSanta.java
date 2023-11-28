package com.smlab.santaspotter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
//        Nov 28, 2023  -   For now the dialog isn't be showed on click of pickMeButton
//        includePickMe.findViewById(R.id.pick_me_button).setOnClickListener(view -> showCodeDialog());
        includePickMe.findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());
        selectedSantaSticker = binding.pickMeInclude.selectSantaSticker;

    }

    private void initialized() {
        selectSantaList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        selectSantaList.add(new SelectSantaModel(R.drawable.santa1));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa2));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa3));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa4));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa5));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa6));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa7));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa8));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa9));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa10));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa11));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa12));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa13));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa14));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa15));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa16));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa17));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa18));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa19));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa20));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa21));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa22));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa23));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa24));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa25));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SelectSantaAdapter(SelectSanta.this, selectSantaList, this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(int position) {
        SelectSantaModel selectedSanta = selectSantaList.get(position);
        selectedSantaSticker.setImageResource(selectedSanta.getSantaSticker());

        binding.pickMeInclude.pickMeButton.setOnClickListener(view -> {
            int selectedStickerResId = selectedSanta.getSantaSticker();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedSticker", selectedStickerResId);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

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

    private int findStickerPosition(int selectedStickerResId) {
        for (int i = 0; i < selectSantaList.size(); i++) {
            if (selectSantaList.get(i).getSantaSticker() == selectedStickerResId) {
                return i;
            }
        }
        return -1; // Return -1 if the sticker is not found in the list
    }
}
