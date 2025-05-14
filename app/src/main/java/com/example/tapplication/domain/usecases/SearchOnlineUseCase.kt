package com.example.tapplication.domain.usecases

import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.repositories.RemoteBooksRepository

class SearchOnlineUseCase(
    private val remoteBooksRepository: RemoteBooksRepository
) {
    suspend operator fun invoke(title: String?, author: String?): List<LibraryItem> {
        return remoteBooksRepository.searchBooksOnline(title, author)
    }
}