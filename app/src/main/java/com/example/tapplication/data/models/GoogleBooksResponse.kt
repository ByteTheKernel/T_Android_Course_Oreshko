package com.example.tapplication.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    @SerialName("items")
    val items: List<GoogleBookItem>? = null
)

@Serializable
data class GoogleBookItem(
    @SerialName("volumeInfo")
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo(
    @SerialName("title")
    val title: String? = null,

    @SerialName("authors")
    val authors: List<String>? = null,

    @SerialName("pageCount")
    val pageCount: Int? = null,

    @SerialName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>? = null,

    @SerialName("imageLinks")
    val imageLinks: ImageLinks? = null
)

@Serializable
data class IndustryIdentifier(
    @SerialName("type")
    val type: String? = null,

    @SerialName("identifier")
    val identifier: String? = null
)

@Serializable
data class ImageLinks(
    @SerialName("smallThumbnail")
    val smallThumbnail: String? = null,

    @SerialName("thumbnail")
    val thumbnail: String? = null
)
