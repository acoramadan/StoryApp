package com.muflidevs.storyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.response.LoginResponse
import com.muflidevs.storyapp.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> get() = _loginResult

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> get() = _registerResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                _loginResult.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(username, email, password)
                _registerResult.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
}