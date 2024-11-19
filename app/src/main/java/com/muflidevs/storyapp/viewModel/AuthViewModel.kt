package com.muflidevs.storyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.response.User
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {
    private val _loginResult = MutableLiveData<User>()
    val loginResult: LiveData<User> get() = _loginResult

    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.login(email,password)
                _loginResult.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.register(username,email,password)
                _registerResult.postValue(result)
            }catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}