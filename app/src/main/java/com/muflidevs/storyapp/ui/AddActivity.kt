package com.muflidevs.storyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityAddBinding
import com.muflidevs.storyapp.helper.HelperCustomView.showToast
import com.muflidevs.storyapp.helper.HelperPostStory.createCustomTempFile
import com.muflidevs.storyapp.helper.HelperPostStory.getImageUri
import com.muflidevs.storyapp.viewModel.StoryViewModel
import com.muflidevs.storyapp.viewModel.StoryViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var cameraBtn: Button
    private lateinit var galeriBtn: Button
    private lateinit var backButton: Button
    private lateinit var descriptionEdtTxt: EditText
    private lateinit var submitBtn: Button
    private lateinit var viewModel: StoryViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var factory: StoryViewModelFactory
    private lateinit var authRepository: AuthRepository
    private var curretImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        descriptionEdtTxt = binding.descriptionInput
        backButton = binding.back
        galeriBtn = binding.galeri
        submitBtn = binding.submitButton
        cameraBtn = binding.camera
        progressBar = binding.progressBar2
        authRepository = AuthRepository(ApiConfig.getApiService(),this)
        factory = StoryViewModelFactory(StoryRepository(ApiConfig.getApiService(authRepository.getToken())))
        viewModel = ViewModelProvider(this,factory)[StoryViewModel::class.java]
        backButton.setOnClickListener{
            finish()
        }
        galeriBtn.setOnClickListener{
            startGallery()
        }
        cameraBtn.setOnClickListener{
            startCamera()
        }
        submitBtn.setOnClickListener{
            val description = binding.descriptionInput.text.toString()
            if(curretImage != null) {
                val file = uriToFile(curretImage!!,this)
                Log.e("DATA YANG AKAN DI KIRIM : ","${file.name} || $description")
                viewModel.uploadStory(description,file)
            } else {
                showToast(this,"Masukkan Gambar Terlebih dahulu")
            }
        }
        viewModel.imageUri.observe(this) { uri ->
            uri.let {
                curretImage = it
                showImage()
            }
        }

        viewModel.uploadStory.observe(this) { response ->
            showToast(this,response.message)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
            if(it == false) finish()
        }

    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        uri: Uri? ->
        if(uri != null) {
            viewModel.setImageUri(uri)
        } else {
            Log.d("Add Activity","No media selected")
        }
    }

    private fun showImage() {
       viewModel.imageUri.value.let { uri ->
           try {
               binding.imageView.setImageURI(null)
               binding.imageView.setImageURI(uri)
           }catch (e: Exception) {
               Log.e("AddActivity","gagal menampilkan gambar ${e.message}")
           }

       }
    }
    private fun startCamera() {
        val newImage = getImageUri( this)
        curretImage = newImage
        viewModel.setImageUri(curretImage!!)
        launchCamera.launch(curretImage!!)
    }
    private val launchCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if(isSuccess) {
            val uri = viewModel.imageUri.value
            if(uri != null) {
                val inputStream = try {
                    contentResolver.openInputStream(uri)
                } catch (e: Exception) {
                    Log.e("AddActivity","Gagal membuka input stream")
                    null
                }

                if(inputStream != null) {
                    showImage()
                } else {
                    Log.e("AddActivity","Terjadi kesalahan dalam menyimpan gambar")
                }
            }
        }
        else {
            curretImage = null
            viewModel.setImageUri(null)
        }
    }
    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while( inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile

    }

    private fun showLoading(loading: Boolean) {
        progressBar.visibility = if(loading) View.VISIBLE else View.GONE
    }
}