package com.example.mathapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Paper

fun LazyGridScope.paperList(
    sem: String,
    papers: List<Paper>,
    onClick: (String) -> Unit
) {
    items(papers) { paper ->
        ContentScreen(
            sem = sem, paper = paper, onClick = { onClick(it) }
        )
    }
}

@Composable
fun ContentScreen(
    sem: String,
    paper: Paper,
    onClick: (String) -> Unit,
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                onClick = { onClick(paper.paperCode) }
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                SubcomposeAsyncImage(
                    model = paper.paperImage,
                    contentDescription = null,
                  //  loading = { Loader() },
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.6f) // semi-transparent dark background
                    )
                ) {
                    Text(
                        text = paper.paperName,
                        modifier = Modifier.padding(8.dp)
                            .background(color = Color.Transparent),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

























