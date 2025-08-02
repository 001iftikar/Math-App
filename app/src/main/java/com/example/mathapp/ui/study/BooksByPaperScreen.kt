package com.example.mathapp.ui.study

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Book
import com.example.mathapp.domain.model.Paper
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.example.mathapp.ui.navigation.Routes

@Composable
fun BooksByPaperScreen(
    semester: String,
    paperCode: String,
    bookViewModel: BookViewModel = hiltViewModel(),
    paperViewModel: PaperViewModel = hiltViewModel(),
    navController: NavController
) {

    val bookState = bookViewModel.booksState.collectAsState()

    val paperState = paperViewModel.paperState.collectAsState()

    LaunchedEffect(Unit) {
        bookViewModel.getAllBooks(semester)
        paperViewModel.getPapers(semester)
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

            result.isLoading -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding)
                )
            }

            result.bookList.isNotEmpty() && paperState.value.papers.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    val matchingPaper = paperState.value.papers.find { it.paperCode == paperCode }

                    if (matchingPaper != null) {
                        items(result.bookList.filter { it.bookPaper == paperCode }) { book ->
                            BookItem(book, matchingPaper) {
                                navController.navigate(
                                    Routes.PdfViewerScreen(
                                        book.bookUrl,
                                        book.bookName
                                    )
                                )
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
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = onClick,
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
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(5.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = book.bookName.trim(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
































