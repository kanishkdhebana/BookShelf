package com.example.bookshelf.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.Book

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit,
    bookshelfUiState: BookshelfUiState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onBookSelected: (String) -> Unit
) {
    when(bookshelfUiState) {
        is BookshelfUiState.Loading -> LoadingScreen()
        is BookshelfUiState.Success ->
            BookListScreen(
                books = bookshelfUiState.books,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    ),
                contentPadding = contentPadding,
                onBookSelected = onBookSelected
            )
        else -> ErrorScreen(retryAction = retryAction, modifier)
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )

        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier
    )
}

@Composable
fun BookListScreen(
    books: List<Book>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onBookSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
            top = contentPadding.calculateTopPadding(),
            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
            bottom = contentPadding.calculateBottomPadding() + 16.dp // Add extra bottom padding here
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = books,
            key = { book -> book.id }
        ) { book ->
            BookCard(
                book = book,
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    onBookSelected(book.id)
                }
            )
        }
    }
}


@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(dimensionResource(R.dimen.card_image_height))
        ) {
            BookListImageItem(
                book = book,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.card_image_height))
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
                    .weight(1f)
                ,
                context = context
            )

            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(R.dimen.padding_small),
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                    .weight(2.5f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.card_text_vertical_space))
                )

                book.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                }
            }
        }
    }
}


@Composable
private fun BookListImageItem(
    book: Book,
    modifier: Modifier = Modifier,
    context: Context
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(book.thumbnailUrl)
                .crossfade(true)
                .build(),
            contentDescription = book.title,
            alignment = Alignment.Center,
            //contentScale = ContentScale.FillWidth,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.ic_broken_image),
            modifier = Modifier.fillMaxSize()
        )
    }
}




