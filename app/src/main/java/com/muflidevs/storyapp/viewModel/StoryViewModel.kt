package com.muflidevs.storyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<List<Story>?>()
    val stories: LiveData<List<Story>?> get() = _stories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchStory(token: String) {
        viewModelScope.launch {
            try {
                val storyList = repository.getStories(token)
                _stories.postValue(storyList)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}