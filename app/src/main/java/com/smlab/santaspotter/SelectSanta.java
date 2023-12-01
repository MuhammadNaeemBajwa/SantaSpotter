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

import com.smlab.santaspotter.baseclasses.BaseActivity;
import com.smlab.santaspotter.databinding.ActivityAddSantaBinding;
import com.smlab.santaspotter.databinding.ActivitySelectSantaBinding;
import com.smlab.santaspotter.filter.UnlockStickersDialog;

import java.util.ArrayList;

public class SelectSanta extends BaseActivity implements SelectSantaAdapter.OnItemClickListener {
    ConstraintLayout includePickMe;
    RecyclerView recyclerView;
    SelectSantaAdapter adapter;
    ArrayList<SelectSantaModel> selectSantaModelArrayList;
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
        selectSantaModelArrayList = new ArrayList<>();
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
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa1, false, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa2, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa3, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa4, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa5, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa6, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa7, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa8, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa9, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa10, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa11, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa12, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa13, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa14, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa15, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa16, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa17, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa18, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa19, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa20, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa21, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa22, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa23, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa24, true, R.drawable.background_round_santa_sticker));
        selectSantaModelArrayList.add(new SelectSantaModel(R.drawable.santa25, true, R.drawable.background_round_santa_sticker));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SelectSantaAdapter(this, SelectSanta.this, selectSantaModelArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        SelectSantaModel selectedSanta = selectSantaModelArrayList.get(position);
        selectedSantaSticker.setImageResource(selectedSanta.getSantaSticker());

        isItemSelectedLocked = selectedSanta.isLocked();

        if (isItemSelectedLocked) {
            selectedSantaSticker.setImageResource(selectedSanta.getStickerImageResource());
            binding.pickMeInclude.pickMeButton.setVisibility(View.GONE);
            binding.pickMeInclude.unLock.setVisibility(View.VISIBLE);
        } else {
            selectedSantaSticker.setImageResource(selectedSanta.getStickerImageResource());
            binding.pickMeInclude.pickMeButton.setVisibility(View.VISIBLE);
            binding.pickMeInclude.unLock.setVisibility(View.GONE);
        }

        binding.pickMeInclude.pickMeButton.setOnClickListener(view -> {
            int selectedStickerResId = selectedSanta.getStickerImageResource();
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
                if (isValidCode(enteredCode)) {
                    unlockStickers();
                    unlockStickersDialog.dismiss();
                } else {
                    unlockStickersDialog.showError(getString(R.string.please_enter_a_valid_code));
                }
            }
        });
        unlockStickersDialog.show();
    }


    private boolean isValidCode(String enteredCode) {
        return enteredCode.equals(getCouponCode());
    }

    private void unlockStickers() {
        Log.d(TAG, "unlockStickers: Unlocking stickers");

        for (SelectSantaModel model : selectSantaModelArrayList) {
            if (model.isLocked()) {
                Log.d(TAG, "unlockStickers: Sticker - " + model.getSantaSticker() + ", Locked - " + model.isLocked());

                model.setLocked(false);
            }
            adapter.notifyDataSetChanged();
        }
        boolean allStickersUnlocked = areAllStickersUnlocked();
        Log.d(TAG, "unlockStickers: All stickers unlocked - " + allStickersUnlocked);
        updateButtonVisibility(allStickersUnlocked);
    }

    private boolean areAllStickersUnlocked() {
        for (SelectSantaModel model : selectSantaModelArrayList) {
            if (model.isLocked()) {
                return false; // At least one sticker is still locked
            }
        }
        return true; // All stickers are unlocked
    }

    private void updateButtonVisibility(boolean allStickersUnlocked) {
        if (allStickersUnlocked) {
            binding.pickMeInclude.pickMeButton.setVisibility(View.VISIBLE);
            binding.pickMeInclude.unLock.setVisibility(View.GONE);
        } else {
            binding.pickMeInclude.pickMeButton.setVisibility(View.GONE);
            binding.pickMeInclude.unLock.setVisibility(View.VISIBLE);
        }
    }
}
