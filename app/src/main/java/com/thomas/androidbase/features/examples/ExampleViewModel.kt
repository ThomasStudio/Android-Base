package com.thomas.androidbase.features.examples

import com.thomas.androidbase.Store
import com.thomas.base.viewmodel.BaseDataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by thomas on 5/3/2026.
 */

@HiltViewModel
class ExampleViewModel @Inject constructor() : BaseDataViewModel<ExampleData>(), ExampleContract {
    override fun initialData() = ExampleData()

    override fun onViewInit() {
        // hide home navigation bar
        Store.rootVM?.toggleNavigationBar(false)
    }

    override fun onVisible() {
        updateTitle()
    }

    override fun onClickExample(example: Examples) {
        updateData { copy(currentExample = example) }
        updateTitle()
    }

    override fun onCleared() {
        Store.rootVM?.toggleNavigationBar(true)
        super.onCleared()
    }

    override fun back() {
        currentData()?.currentExample?.let {
            updateData { copy(currentExample = null) }
        } ?: super.back()

        updateTitle()
    }

    private fun updateTitle() {
        currentData()?.currentExample?.let {
            updateData { copy(title = it.info.name) }
        } ?: updateData { copy(title = "Examples") }
    }
}