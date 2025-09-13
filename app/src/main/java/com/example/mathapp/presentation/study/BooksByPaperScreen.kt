package com.example.mathapp.presentation.study

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Book
import com.example.mathapp.domain.model.Paper
import com.example.mathapp.presentation.components.TopAppBarNavIcon
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.utils.PDF_DIRECTORY
import com.example.mathapp.utils.createPrivateDir
import com.ketch.Ketch
import com.ketch.Status
import java.io.File

@Composable
fun BooksByPaperScreen(
    semester: String,
    paperCode: String,
    ketch: Ketch,
    bookViewModel: BookViewModel = hiltViewModel(),
    paperViewModel: PaperViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current

    val pdfDir = File(context.getExternalFilesDir(null), PDF_DIRECTORY)
    val downloadedFiles = pdfDir.listFiles()

    val bookState = bookViewModel.booksState.collectAsState()

    val paperState = paperViewModel.paperState.collectAsState()

    var status by remember { mutableStateOf(Status.DEFAULT) }
    var progress by remember { mutableIntStateOf(0) }
    var isCollecting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        bookViewModel.getAllBooks(semester)
        paperViewModel.getPapers(semester)
    }

    LaunchedEffect(status) {
        // bcz the screen was flickering
        if (status == Status.SUCCESS) {
            bookViewModel.getAllBooks(semester)
            paperViewModel.getPapers(semester)
        }
    }

    LaunchedEffect(key1 = isCollecting) {
        if (isCollecting) {
            ketch.observeDownloadByTag(tag = "pdf")
                .collect { downloadModels ->
                    downloadModels.forEach {
                        status = it.status
                        progress = it.progress
                    }
                }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarNavIcon(title = paperCode, navController = navController)
        }
    ) { innerPadding ->

        val result = bookState.value

        when {
            result.exception != null -> {
                Column(
                    modifier = Modifier.padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(result.exception.message!!)
                }

            }

            // commented out bcz it was causing flickering
//            result.isLoading -> {
//                LinearProgressIndicator(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(innerPadding)
//                )
//            }

            result.bookList.isNotEmpty() && paperState.value.papers.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    val matchingPaper = paperState.value.papers.find { it.paperCode == paperCode }

                    if (matchingPaper != null) {
                        items(result.bookList.filter { it.bookPaper == paperCode }) { book ->
                            BookItem(book, matchingPaper, onDownloadClick = {
                                val directory = context.createPrivateDir()
                                val fileName = book.bookUrl.substringAfterLast("/")
                                val file = File(directory, fileName)
                                if (file.exists().not()) {
                                    isCollecting = true
                                    ketch.download(
                                        tag = "pdf",
                                        url = book.bookUrl,
                                        fileName = fileName,
                                        path = directory.path
                                    )
                                    Log.d("File-Down", "File saved")
                                } else {
                                    Log.d("File-Down", "File exists")
                                }
                            },
                                onClick = {
                                    val file = downloadedFiles?.firstOrNull {
                                        it.name == book.bookUrl.substringAfterLast("/")
                                    }
                                    navController.navigate(
                                        Routes.PdfViewerScreen(
                                            pdfUrl = if (file != null) null else book.bookUrl,
                                            downloadedPdf = file?.name,
                                            bookName = book.bookName
                                        )
                                    )
                                }) {
                                val directory = context.createPrivateDir()
                                val fileName = book.bookUrl.substringAfterLast("/")
                                val file = File(directory, fileName)
                                if (file.exists().not()) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookItem(
    book: Book,
    paper: Paper,
    onDownloadClick: () -> Unit,
    onClick: () -> Unit,
    downloadStateIcon: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
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
                    model = paper.paperImage,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(35.dp)
                        .background(Color.LightGray.copy(alpha = 0.4f), shape = CircleShape)
                        .clickable(
                            onClick = onDownloadClick
                        ),
                    contentAlignment = Alignment.Center
                )
                {
                    downloadStateIcon()
                }
            }
            Spacer(Modifier.width(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
            ) {
                Text(
                    text = book.bookName.trim(),
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
