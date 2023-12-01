package com.smlab.santaspotter.filter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.smlab.santaspotter.R;
import com.smlab.santaspotter.SelectSanta;

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
        ConstraintLayout submitButton = findViewById(R.id.submit_button);

        closeButton.setOnClickListener(view -> dismiss());

        submitButton.setOnClickListener(view -> {
            String enteredCode = editTextEnterCode.getText().toString().trim();
            if (!enteredCode.isEmpty()) {
                if (onUnlockListener != null) {
                    onUnlockListener.onUnlock(enteredCode);
                }
            } else {
                showError(getContext().getString(R.string.please_enter_a_valid_code));
            }
        });
                findViewById(R.id.dont_have_code_textView).setOnClickListener(view -> showAccessCodeDialog());


    }
    public void setOnUnlockListener(OnUnlockListener listener) {
        this.onUnlockListener = listener;
    }

    public void showError(String errorMessage) {
        errorMessageTextView.setText(errorMessage);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }
    private void dimBackground() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.5f; // Adjust the dim amount as per your preference (0.0f to 1.0f)
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(layoutParams);
    }
    private void showAccessCodeDialog() {
        Dialog dialog = new Dialog(getContext(), R.style.CustomDialog);
        dialog.setContentView(R.layout.access_code_dialog);
        ImageView close;
        TextView email;
        email = dialog.findViewById(R.id.textView_email);
        email.setOnClickListener(view -> openEmail());
        close = dialog.findViewById(R.id.imageView_cross);
        close.setOnClickListener(view -> dialog.dismiss());
        dimBackground(); // Call this method before dialog.show()
        dialog.show();
//        dialogCode.dismiss();

        UnlockStickersDialog unlockStickersDialog = new UnlockStickersDialog(getContext());
        unlockStickersDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        unlockStickersDialog.dismiss();

    }

    private void openEmail() {
        String[] TO_EMAIL = {"santa@copperfield.com", "a@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAIL);
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        getContext().startActivity(Intent.createChooser(intent, ""));
    }

    public interface OnUnlockListener {
        void onUnlock(String enteredCode);
    }
}

