package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.repository.HistoryRepo
import com.dicoding.asclepius.data.local.room.HistoryDao
import com.dicoding.asclepius.data.local.room.HistoryDatabase
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.AppExecutors
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var historyDao: HistoryDao

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            analyzeButton.isEnabled = false

            informationButton.setOnClickListener{
                moveToTes()
            }

            // open gallery
            galleryButton.setOnClickListener {
                startGallery()
            }

            // analyze image
            analyzeButton.setOnClickListener{
                currentImageUri?.let {
                    analyzeButton.isEnabled = false
                    analyzeButton.text = getString(R.string.analyzing_image)
                    analyzeImage(it)
                } ?: run {
                    showToast(getString(R.string.empty_image_warning))
                }
            }
        }

        // database
        val db = HistoryDatabase.getInstance(this)
        historyDao = db.historyDao()
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

//    private val launcherGallery = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        if (uri != null) {
//            currentImageUri = uri
//            showImage()
//        } else {
//            Log.d("Photo Picker", "No media selected")
//        }
//    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage_${System.currentTimeMillis()}.jpg"))
        val options = UCrop.Options().apply {
            setCompressionQuality(80)
            setFreeStyleCropEnabled(true)
        }
        UCrop.of(uri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("UCrop", "Crop error: ${cropError?.message}")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.apply {
                setImageURI(it)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            binding.analyzeButton.isEnabled = true
        }
    }
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
    private fun analyzeImage(uri: Uri) {
        val imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        val intent = Intent(this@MainActivity, ResultActivity::class.java)
                        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
                        intent.putExtra(ResultActivity.EXTRA_RESULT, results?.let { it[0].categories[0].label })
                        intent.putExtra(ResultActivity.EXTRA_CONFIDANT, results?.let {
                            val sortedCategories =
                                it[0].categories.sortedByDescending { it?.score }
                            val displayResult =
                                sortedCategories.joinToString("\n") {
                                    "${it.label} " + NumberFormat.getPercentInstance()
                                        .format(it.score).trim()
                                }
                            displayResult
                        })

                        // Save result to database
                        results?.let {
                            val historyEntity = HistoryEntity(
                                id = 0,
                                uriImage = uri.toString(),
                                result = it[0].categories[0].label,
                                detail = it[0].categories.maxByOrNull { it.score }?.let { category ->
                                    NumberFormat.getPercentInstance().format(category.score).trim()
                                } ?: ""
                            )
                            HistoryRepo.getInstance(historyDao).saveHistoryToDatabase(listOf(historyEntity))
                        }

                        runOnUiThread {
                            startActivity(intent)
                            with(binding){
                                analyzeButton.isEnabled = true
                                analyzeButton.text = getString(R.string.analyze)
                            }
                        }
                    }
                }
            }
        )

        // Wait until TfLite is initialized
        Thread {
            while (!TfLiteVision.isInitialized()) {
                Thread.sleep(100)
            }
            runOnUiThread {
                imageClassifierHelper.classifyStaticImage(uri)
            }
        }.start()
    }

//    private fun moveToResult() {
//        val intent = Intent(this, ResultActivity::class.java)
//        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
//        startActivity(intent)
//    }

    private fun moveToTes() {
        val intent = Intent(this, TabActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
//        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}