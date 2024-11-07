package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.gms.vision.TfLiteVision
import org.tensorflow.lite.task.gms.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            tes.setOnClickListener{
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
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
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

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        startActivity(intent)
    }

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