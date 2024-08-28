package com.shriram.qrscanner

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun MyApp(viewModel: AppViewModel = AppViewModel()) {

    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
//                .background(Color.Black)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "QR Code Scanner",
            fontSize = 32.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        Text(
            text = viewModel.displayText,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    clipboardManager.setText(AnnotatedString(viewModel.displayText))
                    Toast
                        .makeText(context, "copied", Toast.LENGTH_SHORT)
                        .show()
                }
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            ),
            modifier = Modifier
                .width(280.dp),
            onClick = {
                viewModel.startScan(context)
            }
        ) {
            Text(
                text = "SCAN NOW", modifier = Modifier.padding(30.dp, 0.dp),
            )
        }

        Row(modifier = Modifier.padding(4.dp)) {
            Icon(Icons.Default.Share, contentDescription = "",
                modifier = Modifier
                    .clickable {
                        // share text outside app
                        viewModel.shareText(context)
                    }
                    .padding(8.dp)
                    .size(30.dp)

            )
            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.copy_icon),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        clipboardManager.setText(AnnotatedString(viewModel.displayText))
                        Toast
                            .makeText(context, "copied", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .padding(8.dp)
                    .size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(painter = painterResource(id = R.drawable.open_icon),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        viewModel.openLink(context)
                    }
                    .padding(8.dp)
                    .size(30.dp)
            )

        }
    }


}


