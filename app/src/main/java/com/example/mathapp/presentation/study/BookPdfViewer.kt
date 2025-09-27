package com.example.mathapp.presentation.study

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.mathapp.presentation.components.TopAppBarNavIcon
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Composable
fun BookPdfViewer(pdfUrl: String, bookName: String, navController: NavController) {

    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    var pdfViewState by remember { mutableStateOf<PDFView?>(null) }

    LaunchedEffect(Unit) {
        val cacheFile = retrievePdfFromCacheOrUrl(
            context = context,
            pdfUrl = pdfUrl
        )
        cacheFile?.let { file ->
            withContext(Dispatchers.Main) {
                pdfViewState?.fromFile(file)
                    ?.scrollHandle(DefaultScrollHandle(context))
                    ?.enableAntialiasing(true)
                    ?.onLoad {
                        isLoading = false
                    }
                    ?.load() // Load the pdf
            }
        }
    }

    val shimmerColors = listOf(
        Color.LightGray.copy(0.6f),
        Color.LightGray.copy(0.2f),
        Color.LightGray.copy(0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(10f, 10f),
        end = Offset(translateAnimation.value, translateAnimation.value)
    )

    Scaffold(
        topBar = {
            TopAppBarNavIcon(
                title = bookName,
                navController = navController
            )
        }
    ) { innerPadding ->
        if (pdfUrl == "") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Apologies!! I could not find this book after a lots of searches. I will add this book whenever I find it.")
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    factory = {
                        PDFView(context, null).also {
                            pdfViewState = it
                        }  // Initialize PDFView and store it in the state
                    }
                )

                if (isLoading) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = brush),
                        color = Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = Color(0xFF6200EE)
                            )
                            Text(
                                text = "Loading PDF...",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
            // Dispose of resources when the Composable leaves the screen
            DisposableEffect(Unit) {
                onDispose {
                    pdfViewState?.recycle() // Recycle the PDFView to free up memory
                }
            }
        }
    }
}

private suspend fun retrievePdfFromCacheOrUrl(context: Context, pdfUrl: String?): File? {
    return withContext(Dispatchers.IO) {
        try {
            val cacheDir = File(context.cacheDir, "pdf_cache")
            if (cacheDir.exists().not()) cacheDir.mkdirs()

            val fileName = pdfUrl?.substringAfterLast("/") ?: "invalid.pdf"
            val cachedFile = File(cacheDir, fileName)

            if (cachedFile.exists()) {
                Log.d("PdfView", "Loading PDF from cache")
                return@withContext cachedFile
            }

            val urlConnection: HttpURLConnection =
                (URL(pdfUrl).openConnection() as HttpsURLConnection)

            if (urlConnection.responseCode == 200) {
                val inputStream = BufferedInputStream(urlConnection.inputStream) // Read the data
                val outputStream = FileOutputStream(cachedFile)

                inputStream.copyTo(outputStream)
                outputStream.close()
                inputStream.close()
                Log.d("PdfView", "PDF cached successfully")
                return@withContext cachedFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }
}





































