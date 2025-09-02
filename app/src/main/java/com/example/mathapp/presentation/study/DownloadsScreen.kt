package com.example.mathapp.presentation.study

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun DownloadsScreen(navController: NavController) {
    val context = LocalContext.current
    val pdfDir = File(context.getExternalFilesDir(null), PDF_DIRECTORY)
    val list = mutableListOf<Book>()
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

    Scaffold { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(list) {
                BookItem(
                    bookName = it.bookName
                ) {
                    navController.navigate(Routes.PdfViewerScreen(downloadedPdf = it.bookUrl, bookName = it.bookName))
                }
            }
        }
    }
}

@Composable
private fun BookItem(
    bookName: String,
    onClick: () -> Unit
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = bookName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}



























