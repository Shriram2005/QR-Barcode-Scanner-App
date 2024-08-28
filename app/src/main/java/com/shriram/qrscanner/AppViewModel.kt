package com.shriram.qrscanner

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class AppViewModel : ViewModel() {

    var displayText by mutableStateOf("")

    // function to scan using google scan library
    fun startScan(context: Context) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
//                Barcode.FORMAT_QR_CODE,
//                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_ALL_FORMATS
            )
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(context)


        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                val rawValue: String? = barcode.rawValue
                displayText = rawValue ?: "000000"
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

            }
            .addOnCanceledListener {
                // Task canceled
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Toast.makeText(context, "Error! $e", Toast.LENGTH_SHORT).show()
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
