package com.example.bookshelf.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import com.example.bookshelf.R
import com.example.bookshelf.model.Book

@Composable
fun BookDetailsScreen(
    selectedBook: Book,
    onBackPressed: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onBackPressed()
    }
    val scrollState = rememberScrollState()
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .padding(
                    bottom = contentPadding.calculateTopPadding(),
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                )
        ) {
            Box {
                Row {
                    Text(
                        text = selectedBook.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_small))

                    )

                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedBook.selfLink))
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = "Link")
                    }
                }

            }

            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))

            selectedBook.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}