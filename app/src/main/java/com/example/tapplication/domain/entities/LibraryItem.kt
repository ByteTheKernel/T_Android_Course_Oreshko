package com.example.tapplication.domain.entities

import android.os.Parcelable

abstract class LibraryItem: Returnable, TakeItemHome, ReadItemInLibrary, Parcelable {
    abstract val id: Int
    abstract var isAvailable: Boolean
    abstract val name: String

    fun getShortInfo(): String = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    abstract fun getDetailedInfo(): String

    override fun returnItem(): String {
         return if (this.isAvailable) {
             "Ошибка: ${this.name} уже доступен!"
         } else  {
             isAvailable = true
             "${this::class.simpleName} ${this.id} возвращён!"
         }
    }

     override fun takeHome(): String {
         getNotAvailableText()?.let { return it }
         this.isAvailable = false
         return "${this::class.simpleName} ${this.id} взяли домой"
    }

    override fun readInLibrary(): String {
        getNotAvailableText()?.let { return it }
        this.isAvailable = false
        return "${this::class.simpleName} ${this.id} взяли в читальный зал"
    }

    protected fun getNotAvailableText(): String? {
        return if (!this.isAvailable) {
            "Ошибка: ${this.name} уже занята!"
        } else {
            null
        }
    }
}