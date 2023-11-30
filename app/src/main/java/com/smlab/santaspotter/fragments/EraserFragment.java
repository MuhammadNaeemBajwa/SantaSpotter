package com.smlab.santaspotter.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.smlab.santaspotter.R;
import com.smlab.santaspotter.utlis.EraserView;

public class EraserFragment extends Fragment {

    Listener listener;
    Bitmap bitmap;
    EraserView eraserView;

    EraserVM viewModel;

    public EraserFragment() {

    }

    public EraserFragment(Listener listener, Bitmap bitmap) {
        this.listener = listener;
        this.bitmap = bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eraser, container, false);
        eraserView = view.findViewById(R.id.eraserView);
        eraserView.addSticker(requireContext(), bitmap);
        TextView btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view1 -> {
            listener.onSaveBitmap(eraserView.getResultingBitmap());
            bitmap = eraserView.getResultingBitmap();
        });
        viewModel = new ViewModelProvider(requireActivity()).get(EraserVM.class);
        viewModel.getEraserSize().observe(getViewLifecycleOwner(), size -> eraserView.setBrushSize(size));
        return view;
    }

    public interface Listener {
        void onSaveBitmap(Bitmap bitmap);
    }
}