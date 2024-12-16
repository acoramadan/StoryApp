package com.muflidevs.storyapp.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.muflidevs.storyapp.data.local.DatabaseProvider
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityDetailBinding
import com.muflidevs.storyapp.helper.HelperCustomView.showToast
import com.muflidevs.storyapp.viewModel.StoryViewModel
import com.muflidevs.storyapp.viewModel.StoryViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var image: ImageView
    private lateinit var usernameTv: TextView
    private lateinit var deskripsiTv: TextView
    private lateinit var viewModel: StoryViewModel
    private lateinit var factory: StoryViewModelFactory
    private lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authRepository = AuthRepository(ApiConfig.getApiService(), this)
        factory =
            StoryViewModelFactory(
                StoryRepository(
                    ApiConfig.getApiService(authRepository.getToken()),
                    DatabaseProvider.getDatabase(this).storyDao()
                )
            )
        viewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]
        image = binding.ivDetailPhoto
        usernameTv = binding.tvDetailName
        deskripsiTv = binding.tvDetailDescription

        val storyId = intent.getStringExtra("EXTRA_ID") ?: " "

        viewModel.getDetailStory(storyId)

        viewModel.story.observe(this) { story ->
            if (story != null) {
                storyDetail(story)
            }
        }
        viewModel.error.observe(this) { errorMsg ->
            showToast(this, errorMsg)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun storyDetail(story: Story) {
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)
    }
}