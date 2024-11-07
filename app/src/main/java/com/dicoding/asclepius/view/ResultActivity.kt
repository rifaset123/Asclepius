package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.gms.vision.classifier.Classifications
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var imageUri: Uri? = null
    private var result: String? = null
    private var confidant: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        result = intent.getStringExtra(EXTRA_RESULT)
        confidant = intent.getStringExtra(EXTRA_CONFIDANT)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
        binding.resultName.text = result
        binding.resultInferenceTime.text = confidant

    }

    private fun convertImageProxyToUri(image: ImageProxy): Uri {
        // Implement the conversion logic here
        // This is a placeholder implementation
        val tempFile = File.createTempFile("image", ".jpg", cacheDir)
        val outputStream = FileOutputStream(tempFile)
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        outputStream.write(bytes)
        outputStream.close()
        return Uri.fromFile(tempFile)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CONFIDANT = "extra_confidant"
    }
}