package com.muflidevs.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityMainBinding
import com.muflidevs.storyapp.helper.HelperCustomView.showToast
import com.muflidevs.storyapp.ui.adapter.StoryListAdapater
import com.muflidevs.storyapp.viewModel.StoryViewModel
import com.muflidevs.storyapp.viewModel.StoryViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var factory: StoryViewModelFactory
    private lateinit var viewModel: StoryViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var logoutBtn: Button
    private lateinit var addBtn: FloatingActionButton
    private lateinit var authRepository: AuthRepository
    private lateinit var storiesAdapter: StoryListAdapater
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var handler: Handler
    private lateinit var pollingRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingProgressBar = binding.progressBar
        logoutBtn = binding.logoutBtn
        addBtn = binding.addBtn
        bottomNav = binding.bottomNavigation
        authRepository = AuthRepository(ApiConfig.getApiService(), this)
        factory =
            StoryViewModelFactory(StoryRepository(ApiConfig.getApiService(authRepository.getToken())))
        viewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]

        storiesAdapter = StoryListAdapater(this@MainActivity) { stories ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("EXTRA_ID", stories.id)
            intent.putExtra("EXTRA_TOKEN", authRepository.getToken())
            Log.d("Main Activity", stories.id!!)
            startActivity(intent)
        }
        logoutBtn.setOnClickListener {
            authRepository.clearToken()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        addBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }

        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storiesAdapter
        }
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.bottom_navigation_logout -> {
                    authRepository.clearToken()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.bottom_navigation_maps -> {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        viewModel.stories.observe(this) { storyList ->
            Log.d("MainActivity", "Stories received: ${storyList?.size ?: 0}")
            if (storyList != null) storiesAdapter.submitList(storyList)
        }
        viewModel.error.observe(this) { errorMsg ->
            Log.e("MainActivity", "Error fetching stories: $errorMsg")
            showToast(this, errorMsg)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.fetchStory()
    }

    override fun onResume() {
        super.onResume()
        resumeFetchStory()
    }

    override fun onPause() {
        super.onPause()
        stopFetchStory()
    }

    private fun showLoading(loading: Boolean) {
        loadingProgressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun resumeFetchStory() {
        handler = Handler(Looper.getMainLooper())
        pollingRunnable = object : Runnable {
            override fun run() {
                viewModel.fetchStory()
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(pollingRunnable)
    }

    private fun stopFetchStory() {
        if (::handler.isInitialized && ::pollingRunnable.isInitialized) {
            handler.removeCallbacks(pollingRunnable)
        }
    }

}