package com.smlab.santaspotter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectSanta extends AppCompatActivity {
    ConstraintLayout includePickMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_santa);
        getSupportActionBar().hide();

        includePickMe = findViewById(R.id.pick_me_include);

        includePickMe.findViewById(R.id.pick_me_button).setOnClickListener(view -> showCodeDialog());

//        includePickMe.findViewById(R.id.pick_me_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SelectSanta.this, Unlock_popup_dialog.class);
//                startActivity(intent);
//            }
//        });

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
