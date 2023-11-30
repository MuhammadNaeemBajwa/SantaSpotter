package com.smlab.santaspotter.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EraserVM : ViewModel() {
    var eraserSize: MutableLiveData<Float> = MutableLiveData()
}