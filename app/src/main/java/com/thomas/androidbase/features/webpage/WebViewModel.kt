package com.thomas.androidbase.features.webpage

import androidx.lifecycle.SavedStateHandle
import com.thomas.androidbase.Store
import com.thomas.base.viewmodel.BaseDataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by thomas on 5/2/2026.
 */

@HiltViewModel
class WebViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseDataViewModel<WebData>(savedStateHandle), WebContract {

    init {
        payload<WebData>()?.let {
            it.title?.let { title ->
                Store.rootVM?.setTitle(title)
            }
            updateData(it.copy(title = null))
        }
    }

}