package com.thomas.androidbase

import androidx.lifecycle.SavedStateHandle
import com.thomas.base.viewmodel.BaseDataContract
import com.thomas.base.viewmodel.BaseDataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by thomas on 5/2/2026.
 */

interface RootContract : BaseDataContract<RootData> {
    fun setTitle(title: String)
    fun toggleNavigationBar(show: Boolean)
}

data class RootData(
    val title: String = "Home",
    val showNavigationBar: Boolean = true
)

@HiltViewModel
class RootViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseDataViewModel<RootData>(savedStateHandle), RootContract {
    override fun initialData() = RootData()

    override fun setTitle(title: String) {
        updateData { copy(title = title) }
    }

    override fun toggleNavigationBar(show: Boolean) {
        updateData { copy(showNavigationBar = show) }
    }
}