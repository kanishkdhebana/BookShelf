package com.example.bookshelf.model

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookSearchResponse(
    val kind: String,
    val items: List<BookItem>,
    val totalItems: Int
)

@Serializable
data class BookItem(
    val id: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
)

@Serializable
data class VolumeInfo(
    val title: String,
    val imageLinks: ImageLinks? = null,
    val description: String? = null
)

@Serializable
data class ImageLinks (
    @SerialName("thumbnail") val thumbnail: String? = null,
    @SerialName("extraLarge") val extraLarge: String? = null,
    @SerialName("large") val large: String? = null,
    @SerialName("medium") val medium: String? = null
)

@Serializable
data class Book(
    val id: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val selfLink: String,
    val description: String?
)
