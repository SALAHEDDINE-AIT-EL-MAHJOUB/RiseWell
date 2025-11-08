package com.example.risewell.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.risewell.data.model.UserProfile
import com.example.risewell.data.repository.RiseWellRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: RiseWellRepository
) : ViewModel() {

    val userProfile: Flow<UserProfile?> = repository.getUserProfile()

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }
}
