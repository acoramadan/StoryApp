package com.muflidevs.storyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityAddBinding
import com.muflidevs.storyapp.helper.HelperPostStory.createCustomTempFile
import com.muflidevs.storyapp.helper.HelperPostStory.getImageUri
import com.muflidevs.storyapp.viewModel.StoryViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var cameraBtn: Button
    private lateinit var galeriBtn: Button
    private lateinit var descriptionEdtTxt: EditText
    private lateinit var submitBtn: Button
    private lateinit var viewModel: StoryViewModel
    private var curretImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        descriptionEdtTxt = binding.descriptionInput
        galeriBtn = binding.galeri
        submitBtn = binding.submitButton
        cameraBtn = binding.camera
        viewModel = StoryViewModel(StoryRepository(ApiConfig.getApiService()))
        galeriBtn.setOnClickListener{
            startGallery()
        }
        cameraBtn.setOnClickListener{
            startCamera()
        }
        viewModel.imageUri.observe(this) { uri ->
            uri.let {
                curretImage = it
                showImage()
            }
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

}