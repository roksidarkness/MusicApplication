package com.roksidark.feature

import androidx.lifecycle.ViewModel
import com.roksidark.core.domain.usecase.ArtistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: ArtistUseCases
) : ViewModel() {

}