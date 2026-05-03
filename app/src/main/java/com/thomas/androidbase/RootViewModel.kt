package com.thomas.androidbase

import androidx.lifecycle.SavedStateHandle
import com.thomas.androidbase.ui.components.NavigationData
import com.thomas.base.viewmodel.BaseDataContract
import com.thomas.base.viewmodel.BaseDataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by thomas on 5/2/2026.
 */

interface RootContract : BaseDataContract<RootData> {
    fun setTitle(title: String)
    fun useChildNavigation(childNavigation: NavigationData)
    fun clearChildNavigation()
}

data class RootData(
    val title: String = "Home",
    val showNavigationBar: Boolean = true,
    val childNavigation: NavigationData? = null,
)

@HiltViewModel
class RootViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseDataViewModel<RootData>(savedStateHandle), RootContract {
    override fun initialData() = RootData()

    override fun setTitle(title: String) {
        updateData { copy(title = title) }
    }

    override fun useChildNavigation(childNavigation: NavigationData) {
        updateData { copy(childNavigation = childNavigation) }
    }

    override fun clearChildNavigation() {
        updateData { copy(childNavigation = null) }
    }
}