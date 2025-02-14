package com.shriram.qrscanner

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    var displayText by mutableStateOf("")
    private var isFirstLaunch = true

    // Auto-start scanner when app opens
    fun checkAndStartScanner(context: Context) {
        if (isFirstLaunch) {
            isFirstLaunch = false
            startScan(context)
        }
    }

    // Enhanced scanning function with better options
    fun startScan(context: Context) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAutoZoom() // Enable auto zoom for better accuracy
            .allowManualInput() // Allow manual input if scanning fails
            .build()

        val scanner = GmsBarcodeScanning.getClient(context, options)

        viewModelScope.launch {
            try {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        when (barcode.valueType) {
                            Barcode.TYPE_URL -> {
                                displayText = barcode.url?.url ?: barcode.rawValue ?: ""
                            }
                            Barcode.TYPE_TEXT -> {
                                displayText = barcode.rawValue ?: ""
                            }
                            else -> {
                                displayText = barcode.rawValue ?: ""
                            }
                        }
                        Toast.makeText(context, "Scan Successful", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener {
                        Toast.makeText(context, "Scanning canceled", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Scanning failed: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error initializing scanner: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // share text to other apps
    fun shareText(context: Context) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, displayText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    // check if the value is Url or not, & open it in browser
    fun openLink(context: Context) {
        if (displayText.startsWith("http")) {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(displayText))
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Not a valid URL", Toast.LENGTH_SHORT).show()
        }
    }
}
