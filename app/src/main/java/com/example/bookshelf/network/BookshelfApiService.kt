package com.example.bookshelf.network

import com.example.bookshelf.model.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookshelfApiService {
    @GET("volumes")
    suspend fun getBooks(@Query(value = "q") query: String): BookSearchResponse
}