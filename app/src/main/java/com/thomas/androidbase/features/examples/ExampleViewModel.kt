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

    override fun onVisible() {
        Store.rootVM?.setTitle(currentData()?.title ?: "")
    }
}