package com.example.tapplication.utils

fun sanitizeImageUrl(url: String?): String? {
    return url?.replace("http://", "https://")
}