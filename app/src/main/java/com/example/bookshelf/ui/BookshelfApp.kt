package com.example.bookshelf.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.bookshelf.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.ui.screens.BookshelfViewModel
import com.example.bookshelf.ui.screens.HomeScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp() {
    val bookshelfViewModel: BookshelfViewModel =
        viewModel(factory = BookshelfViewModel.Factory)

    val searchTerm by bookshelfViewModel.searchTerm

    val controller = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier
                ) {
                Column {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar(
                    query = searchTerm,
                    onQueryChange = { bookshelfViewModel.updateSearchTerm(it) },
                    onSearch = {
                        bookshelfViewModel.getBooks()
                        if (controller != null) {
                            controller.hide()
                        }
                    },
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text(text = "Search for books") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null)},
                    trailingIcon = {
                        if (searchTerm.isNotEmpty()) {
                            IconButton(onClick = {
                                bookshelfViewModel.updateSearchTerm("")
                                bookshelfViewModel.getBooks()
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    }
                ) {

                }
                HomeScreen(
                    retryAction = bookshelfViewModel::getBooks,
                    bookshelfUiState = bookshelfViewModel.bookshelfUiState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    onBookSelected = { bookId ->
                        TODO()
                    }
                )
            }
        }
    }
}