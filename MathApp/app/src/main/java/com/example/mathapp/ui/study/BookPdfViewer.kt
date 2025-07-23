package com.example.mathapp.ui.study

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState

@Composable
fun BookPdfViewer(pdfUrl: String, bookName: String, navController: NavController) {
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Remote(pdfUrl),
        isZoomEnable = true
    )
    Scaffold(
        topBar = { TopAppBarNavIcon(title = bookName.trim(), navController = navController) }
    ) { innerPadding ->
        if (pdfUrl == "") {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Apologies!! I could not find this book after a lots of searches. I will add this book whenever I find it.")
            }
        } else {
            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
                    .background(color = Color.Gray)
            )
        }


    }

}