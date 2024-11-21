package com.muflidevs.storyapp.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.response.PostStoryResponse
import com.muflidevs.storyapp.data.remote.response.Story
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val repository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<List<Story>?>()
    val stories: LiveData<List<Story>?> get() = _stories

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> get() = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private val _uploadStory = MutableLiveData<PostStoryResponse>()
    val uploadStory: LiveData<PostStoryResponse> get() = _uploadStory

    fun uploadStory(description: String, filePhoto: File) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.postNewStories(description, filePhoto)
                if(response.isSuccessful && response.body() != null) {
                    _uploadStory.postValue(response.body())
                    Log.d("Upload", "Success: ${response.body()?.message}")
                } else {
                    _error.postValue(response.errorBody()?.string())
                    Log.e("Upload", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchStory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val storyList = repository.getStories()
                Log.d("StoryViewModel", "Fetched stories: ${storyList!!.size}")
                _stories.postValue(storyList)
            } catch (e: Exception) {
                _error.postValue(e.message)
                Log.e("StoryViewModel",e.message!!)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun getDetailStory(id: String) {
        viewModelScope.launch {
            try {
                val storyDetail = repository.getDetailStories(id)
                _story.postValue(storyDetail)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri!!
    }
}