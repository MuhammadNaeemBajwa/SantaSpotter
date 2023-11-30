package com.smlab.santaspotter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class EraserFragment extends Fragment {

    Listener listener;

    public EraserFragment() {

    }

    public EraserFragment(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eraser, container, false);
        PaintView eraserView = view.findViewById(R.id.eraserView);

        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.santa_sticker_1);
        eraserView.addSticker(requireContext(), bitmap);

        TextView btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view1 -> {
            listener.onSaveBitmap(eraserView.getResultingBitmap());
            bitmap = eraserView.getResultingBitmap();
        });

        return view;
    }

    interface Listener {
        void onSaveBitmap(Bitmap bitmap);
    }
}