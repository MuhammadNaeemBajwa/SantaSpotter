package com.smlab.santaspotter.filter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.smlab.santaspotter.R;

public class UnlockStickersDialog extends Dialog {
    private EditText editTextEnterCode;
    private TextView errorMessageTextView;
    private OnUnlockListener onUnlockListener;
    public UnlockStickersDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_popup_dialog);

        editTextEnterCode = findViewById(R.id.editText_enterCode);
        errorMessageTextView = findViewById(R.id.error_message);

        ImageView closeButton = findViewById(R.id.imageView_cross);
        Button submitButton = findViewById(R.id.submit_button);

        closeButton.setOnClickListener(view -> dismiss());

        submitButton.setOnClickListener(view -> {
            String enteredCode = editTextEnterCode.getText().toString().trim();
            if (!enteredCode.isEmpty()) {
                if (onUnlockListener != null) {
                    onUnlockListener.onUnlock(enteredCode);
                }
            } else {
                showError(getContext().getString(R.string.enter_code));
            }
        });
    }
    public void setOnUnlockListener(OnUnlockListener listener) {
        this.onUnlockListener = listener;
    }

    public void showError(String errorMessage) {
        errorMessageTextView.setText(errorMessage);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    public interface OnUnlockListener {
        void onUnlock(String enteredCode);
    }
}

