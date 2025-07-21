package com.example.mathapp.ui.study

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
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
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Book
import com.example.mathapp.ui.components.TopAppBarNavIcon

@Composable
fun BooksByPaperScreen(
    semester: String,
    paperCode: String,
    bookViewModel: BookViewModel = hiltViewModel()
) {

    val bookState = bookViewModel.booksState.collectAsState()

    LaunchedEffect(Unit) {
        bookViewModel.getAllBooks(semester)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarNavIcon(title = paperCode) { }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            val result = bookState.value
            when {
                result.exception != null -> {
                    item { Text(result.exception.message!!) }
                }

                result.isLoading -> {
                    item { LinearProgressIndicator() }

                }

                result.bookList.isNotEmpty() -> {
                    items(result.bookList.filter { it.bookPaper == paperCode }) {book ->
                        Log.d("Tag-comp", book.toString())
                        BookItem(book) { }

                    }
                }
            }
        }
    }
}

@Composable
private fun BookItem(
    book: Book,
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
                    model = book.bookImage,
                    contentDescription = null,
                    loading = { CircularProgressIndicator() },
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(5.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = book.bookName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
































