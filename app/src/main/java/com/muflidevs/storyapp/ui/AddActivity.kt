package com.muflidevs.storyapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.muflidevs.storyapp.data.local.DatabaseProvider
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.databinding.ActivityAddBinding
import com.muflidevs.storyapp.helper.HelperCustomView.showToast
import com.muflidevs.storyapp.helper.HelperPostStory.getImageUri
import com.muflidevs.storyapp.viewModel.StoryViewModel
import com.muflidevs.storyapp.viewModel.StoryViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class AddActivity : AppCompatActivity() {
    private val maximalSize = 1000000

    private lateinit var binding: ActivityAddBinding
    private lateinit var cameraBtn: Button
    private lateinit var galeriBtn: Button
    private lateinit var backButton: Button
    private lateinit var submitBtnWithLocation: Button
    private lateinit var descriptionEdtTxt: EditText
    private lateinit var submitBtn: Button
    private lateinit var viewModel: StoryViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var factory: StoryViewModelFactory
    private lateinit var authRepository: AuthRepository
    private var curretImage: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double? = 0.0
    private var lon: Double? = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        descriptionEdtTxt = binding.edAddDescription
        backButton = binding.back
        galeriBtn = binding.galeri
        submitBtn = binding.buttonAdd
        cameraBtn = binding.camera
        progressBar = binding.progressBar2
        submitBtnWithLocation = binding.buttonAddWithLocation

        authRepository = AuthRepository(ApiConfig.getApiService(), this)
        factory =
            StoryViewModelFactory(
                StoryRepository(
                    ApiConfig.getApiService(authRepository.getToken()),
                    DatabaseProvider.getDatabase(this).storyDao()
                )
            )
        viewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        backButton.setOnClickListener {
            finish()
        }
        galeriBtn.setOnClickListener {
            startGallery()
        }
        cameraBtn.setOnClickListener {
            startCamera()
        }
        submitBtn.setOnClickListener {
            val description = binding.edAddDescription.text.toString()
            if (curretImage != null && description.isNotEmpty()) {
                val file = uriToFile(curretImage!!, this).reduceFileImage()
                Log.e("DATA YANG AKAN DI KIRIM : ", "${file.name} || $description")
                viewModel.uploadStory(description, file)
                Log.d(
                    "AddActivity",
                    "File Path: ${file.absolutePath}, Exists: ${file.exists()}, Size: ${file.length()}"
                )
            } else {
                showToast(this, "Gambar dan Deskripsi tidak boleh kosong")
            }
        }
        submitBtnWithLocation.setOnClickListener {
            val description = binding.edAddDescription.text.toString()
            if (curretImage != null && description.isNotEmpty()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    showToast(this, "Izin diperlukan untuk mengakses lokasi")

                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                } else {
                    fusedLocationClient.lastLocation
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val location = task.result
                                if (location != null) {
                                    val file = uriToFile(curretImage!!, this).reduceFileImage()
                                    lat = location.latitude
                                    lon = location.longitude
                                    viewModel.uploadStoryWithLocation(
                                        description,
                                        file,
                                        lat ?: 0.0,
                                        lon ?: 0.0
                                    )
                                    Log.d(
                                        "AddActivity",
                                        "File Path: ${file.absolutePath}, Exists: ${file.exists()}, Size: ${file.length()}"
                                    )
                                } else {
                                    showToast(this, "Lokasi tidak ditemukan, pastikan GPS aktif")
                                }
                            } else {
                                showToast(this, "Gagal mendapatkan lokasi")
                            }
                        }
                }
            } else {
                showToast(this, "Gambar dan Deskripsi tidak boleh kosong")
            }
        }
        viewModel.imageUri.observe(this) { uri ->
            uri.let {
                curretImage = it
                showImage()
            }
        }

        viewModel.uploadStory.observe(this) { response ->
            showToast(this, response.message)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
            if (it == false) finish()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                showToast(this, "Izin kamera diperlukan untuk mengambil gambar")
            }
        }
    }


    private fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > maximalSize)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
        } else {
            Log.d("Add Activity", "No media selected")
        }
    }

    private fun showImage() {
        viewModel.imageUri.value.let { uri ->
            try {
                binding.imageView.setImageURI(null)
                binding.imageView.setImageURI(uri)
            } catch (e: Exception) {
                showToast(this, "Gagal Menampilkan Gambar")
                Log.e("AddActivity", "gagal menampilkan gambar ${e.message}")
            }

        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
                return
            }
        }
        val newImage = getImageUri(this)
        curretImage = newImage
        viewModel.setImageUri(curretImage!!)
        launchCamera.launch(curretImage!!)
    }

    private val launchCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                val uri = viewModel.imageUri.value
                if (uri != null) {
                    val inputStream = try {
                        contentResolver.openInputStream(uri)
                    } catch (e: Exception) {
                        Log.e("AddActivity", "Gagal membuka input stream")
                        null
                    }

                    if (inputStream != null) {
                        showImage()
                    } else {
                        Log.e("AddActivity", "Terjadi kesalahan dalam menyimpan gambar")
                    }
                }
            } else {
                curretImage = null
                viewModel.setImageUri(null)
            }
        }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val tempFile = File(context.cacheDir, "temp_file.jpg")

        try {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                Log.d("AddActivity", "File successfully created: ${tempFile.absolutePath}")
            } else {
                Log.e("AddActivity", "InputStream is null for URI: $uri")
            }
        } catch (e: FileNotFoundException) {
            Log.e("AddActivity", "File not found for URI: $uri", e)
        } catch (e: Exception) {
            Log.e("AddActivity", "Failed to convert URI to File: ${e.message}", e)
        }
        return tempFile
    }

    private fun showLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                else -> {
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location.let {
                    lat = it?.latitude
                    lon = it?.longitude
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}