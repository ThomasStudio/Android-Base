package com.thomas.androidbase.features.webpage

import androidx.lifecycle.SavedStateHandle
import com.thomas.base.viewmodel.BaseDataViewModel

/**
 * Created by thomas on 5/2/2026.
 */

class WebViewModel(savedStateHandle: SavedStateHandle) :
    BaseDataViewModel<WebData>(savedStateHandle), WebContract {

    init {
        payload<WebData>()?.let {
            updateData(it)
        }
    }

}