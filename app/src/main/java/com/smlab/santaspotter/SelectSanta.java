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
import com.smlab.santaspotter.filter.UnlockStickersDialog;

import java.util.ArrayList;

public class SelectSanta extends AppCompatActivity implements SelectSantaAdapter.OnItemClickListener {
    ConstraintLayout includePickMe;
    RecyclerView recyclerView;
    SelectSantaAdapter adapter;
    ArrayList<SelectSantaModel> selectSantaList;
    Dialog dialogCode;
    private ActivitySelectSantaBinding binding;
    private ImageView selectedSantaSticker;
    private boolean isItemSelectedLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectSantaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setIds();
        initialized();
        setListener();
        setUpRecyclerView();

    }

    private void setIds() {
        Log.d(TAG, "setIds: ");

        includePickMe = findViewById(R.id.pick_me_include);
        recyclerView = findViewById(R.id.recyclerView2);
        includePickMe.findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());
        selectedSantaSticker = binding.pickMeInclude.selectSantaSticker;

    }

    private void initialized() {
        selectSantaList = new ArrayList<>();
        binding.pickMeInclude.unLock.setVisibility(View.GONE);
    }

    private void setListener() {
        binding.pickMeInclude.unLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    private void setUpRecyclerView() {
        selectSantaList.add(new SelectSantaModel(R.drawable.santa1));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa2));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa3, true, R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa4, true, R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa5,true, R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa6,true,R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa7,true,R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa8,true, R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa9,true, R.drawable.background_round_santa_sticker));
        selectSantaList.add(new SelectSantaModel(R.drawable.santa10,true, R.drawable.background_round_santa_sticker));
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
//    @Override
//    public void onItemClick(int position) {
//        SelectSantaModel selectedSanta = selectSantaList.get(position);
//        selectedSantaSticker.setImageResource(selectedSanta.getSantaSticker());
//
//        binding.pickMeInclude.pickMeButton.setOnClickListener(view -> {
//            int selectedStickerResId = selectedSanta.getSantaSticker();
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("selectedSticker", selectedStickerResId);
//            setResult(Activity.RESULT_OK, resultIntent);
//            finish();
//        });
//
//    }

//    @Override
//    public void onItemClick(int position) {
//        SelectSantaModel selectedSanta = selectSantaList.get(position);
//        selectedSantaSticker.setImageResource(selectedSanta.getSantaSticker());
//
//        isItemSelectedLocked = selectedSanta.isLocked();
//
//        if (isItemSelectedLocked) {
//            // If the selected item is locked, show the Unlock button and hide the Pick Me button
//            binding.pickMeInclude.pickMeButton.setVisibility(View.GONE);
//            binding.pickMeInclude.unLock.setVisibility(View.VISIBLE);
//        } else {
//            // If the selected item is not locked, show the Pick Me button and hide the Unlock button
//            binding.pickMeInclude.pickMeButton.setVisibility(View.VISIBLE);
//            binding.pickMeInclude.unLock.setVisibility(View.GONE);
//        }
//
//        binding.pickMeInclude.pickMeButton.setOnClickListener(view -> {
//            int selectedStickerResId = selectedSanta.getSantaSticker();
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("selectedSticker", selectedStickerResId);
//            setResult(Activity.RESULT_OK, resultIntent);
//            finish();
//        });
//
//        binding.pickMeInclude.unLock.setOnClickListener(view -> {
//            if (isItemSelectedLocked) {
//                showUnlockStickersDialog();
//            }
//        });
//    }


    @Override
    public void onItemClick(int position) {
        SelectSantaModel selectedSanta = selectSantaList.get(position);
        selectedSantaSticker.setImageResource(selectedSanta.getSantaSticker());

        isItemSelectedLocked = selectedSanta.isLocked();

        if (isItemSelectedLocked) {
            // If the selected item is locked, show the locked sticker in the select_santa_sticker ImageView
            selectedSantaSticker.setImageResource(selectedSanta.getStickerImageResource());
            // Show the Unlock button and hide the Pick Me button
            binding.pickMeInclude.pickMeButton.setVisibility(View.GONE);
            binding.pickMeInclude.unLock.setVisibility(View.VISIBLE);
        } else {
            // If the selected item is not locked, show the Pick Me button and hide the Unlock button
            binding.pickMeInclude.pickMeButton.setVisibility(View.VISIBLE);
            binding.pickMeInclude.unLock.setVisibility(View.GONE);
        }

        binding.pickMeInclude.pickMeButton.setOnClickListener(view -> {
            int selectedStickerResId = selectedSanta.getSantaSticker();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedSticker", selectedStickerResId);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        binding.pickMeInclude.unLock.setOnClickListener(view -> {
            if (isItemSelectedLocked) {
                showUnlockStickersDialog();
            }
        });
    }



    private void showUnlockStickersDialog() {
        UnlockStickersDialog unlockStickersDialog = new UnlockStickersDialog(this);
        unlockStickersDialog.setOnUnlockListener(new UnlockStickersDialog.OnUnlockListener() {
            @Override
            public void onUnlock(String enteredCode) {
                // Implement logic to validate the entered code and unlock stickers
                if (isValidCode(enteredCode)) {
                    // Unlock stickers
                    unlockStickers();
                    unlockStickersDialog.dismiss();
                } else {
                    // Show an error message in the dialog
                    unlockStickersDialog.showError(getString(R.string.please_enter_a_valid_code));
                }
            }
        });
        unlockStickersDialog.show();
    }


    private boolean isValidCode(String enteredCode) {
        // Implement code validation logic here
        return enteredCode.equals("COPPERFIELD");
    }

    private void unlockStickers() {
        // Debug log
        Log.d(TAG, "unlockStickers: Unlocking stickers");

        // Implement logic to unlock stickers here
        // For example, update the locked status of stickers
        for (SelectSantaModel model : selectSantaList) {
            if (model.isLocked()) {
                // Debug log
                Log.d(TAG, "unlockStickers: Unlocking sticker - " + model.getSantaSticker());

                model.setLocked(false);
            }
        }

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();

        // Check if all stickers are unlocked
        boolean allStickersUnlocked = areAllStickersUnlocked();

        // Debug log
        Log.d(TAG, "unlockStickers: All stickers unlocked - " + allStickersUnlocked);

        // Update the visibility of buttons based on sticker unlock status
        updateButtonVisibility(allStickersUnlocked);
    }



    private boolean areAllStickersUnlocked() {
        for (SelectSantaModel model : selectSantaList) {
            if (model.isLocked()) {
                return false; // At least one sticker is still locked
            }
        }
        return true; // All stickers are unlocked
    }

    private void updateButtonVisibility(boolean allStickersUnlocked) {
        if (allStickersUnlocked) {
            // If all stickers are unlocked, show the "Pick Me" button and hide the "Unlock" button
            binding.pickMeInclude.pickMeButton.setVisibility(View.VISIBLE);
            binding.pickMeInclude.unLock.setVisibility(View.GONE);
        } else {
            // If some stickers are still locked, hide the "Pick Me" button and show the "Unlock" button
            binding.pickMeInclude.pickMeButton.setVisibility(View.GONE);
            binding.pickMeInclude.unLock.setVisibility(View.VISIBLE);
        }
    }
}
