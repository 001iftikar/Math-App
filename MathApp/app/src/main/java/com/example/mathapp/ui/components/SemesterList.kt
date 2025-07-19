package com.example.mathapp.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Paper

fun LazyGridScope.bookList(
    sem: String,
    papers: List<Paper>
) {
    items(papers) {paper ->
        ContentScreen(
            sem = sem,
            paper = paper
        )
    }
}

@Composable
fun ContentScreen(
    sem: String,
    paper: Paper

    ) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
           // .size(250.dp)
    ) {
        SubcomposeAsyncImage(
            model = paper.paperImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text("${paper.paperName} of semester $sem")
    }
}

























