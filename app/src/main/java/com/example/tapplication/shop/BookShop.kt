package com.example.tapplication.shop

import com.example.tapplication.library.Book

class BookShop: Shop<Book> {
    override fun sell(): Book {
        return Book(400, true, "Овод", 352,"Э.Л. Войнич")
    }
}