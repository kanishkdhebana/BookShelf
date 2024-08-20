package com.example.bookshelf.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookshelfUiState {
    data class Success(val books: List<Book>): BookshelfUiState
    data object Error: BookshelfUiState
    data object Loading: BookshelfUiState
}

class BookshelfViewModel(
    private val bookshelfRepository: BookshelfRepository
): ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    private var _searchTerm = mutableStateOf("")
    val searchTerm: State<String> = _searchTerm

    fun updateSearchTerm(newTerm: String) {
        _searchTerm.value = newTerm
    }

    fun getBooks() {
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            bookshelfUiState = try {
                val response = bookshelfRepository.getBooks(searchTerm.value)
                Log.d("API Response", response.toString())
                val books: List<Book> = response.items.map {
                    bookItem -> Book(
                        id = bookItem.id,
                        title = bookItem.volumeInfo.title,
                        selfLink = bookItem.selfLink,
                        thumbnailUrl = bookItem.volumeInfo.imageLinks?.extraLarge?.replace("http", "https")
                            ?: bookItem.volumeInfo.imageLinks?.large?.replace("http", "https")
                            ?: bookItem.volumeInfo.imageLinks?.medium?.replace("http", "https")
                            ?: bookItem.volumeInfo.imageLinks?.thumbnail?.replace("http", "https"),
                        description = bookItem.volumeInfo.description
                    )
                }
                BookshelfUiState.Success(books)
            } catch (e: IOException) {
                BookshelfUiState.Error
            } catch (e: HttpException) {
                BookshelfUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                if (application is BookshelfApplication) {
                    val bookshelfRepository = application.container.bookshelfRepository
                    BookshelfViewModel(bookshelfRepository = bookshelfRepository)
                } else {
                    throw IllegalStateException("BookshelfViewModel not initialized properly")
                }
            }
        }
    }
}