package com.example.bookshelf.data

import com.example.bookshelf.model.BookSearchResponse
import com.example.bookshelf.network.BookshelfApiService

interface BookshelfRepository {
    suspend fun getBooks(searchTerm: String): BookSearchResponse
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
): BookshelfRepository {

    override suspend fun getBooks(searchTerm: String): BookSearchResponse =
        bookshelfApiService.getBooks(searchTerm)
}