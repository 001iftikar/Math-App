package com.example.mathapp.presentation.study

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.R
import com.example.mathapp.domain.model.Book
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.utils.PDF_DIRECTORY
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(navController: NavController) {
    val context = LocalContext.current
    val pdfDir = File(context.getExternalFilesDir(null), PDF_DIRECTORY)
    val list = remember { mutableStateListOf<Book>() }

    LaunchedEffect(Unit) {
        if (pdfDir.exists()) {
            val files = pdfDir.listFiles()
            files?.map {
                list.add(
                    Book(
                        bookName = it.name.replace("%20", " ").removeSuffix(".pdf")
                            .replace("pdf", "", ignoreCase = true),
                        bookUrl = it.name
                    )
                )
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Downloads") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        if (list.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No downloads yet!")
            }
        }
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(list) {
                BookItem(
                    bookName = it.bookName,
                    onClick = {
                        navController.navigate(
                            Routes.PdfViewerScreen(
                                downloadedPdf = it.bookUrl,
                                bookName = it.bookName
                            )
                        )
                    },
                    onDeleteClick = {
                        Log.d("Delete", "DownloadsScreen: ${it.bookUrl}")
                       val deleted = File(pdfDir, it.bookUrl).delete()
                        if(deleted) {
                            list.remove(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BookItem(
    bookName: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                onClick = onClick
            ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = R.drawable.book_img,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bookName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}



























