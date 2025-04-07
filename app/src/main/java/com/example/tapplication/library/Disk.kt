package com.example.tapplication.library

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disk(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val type: String
): LibraryItem(), Parcelable {
    override fun getDetailedInfo() = "$type $name доступен: ${if (isAvailable) "Да" else "Нет"}"

    override fun readInLibrary(): String {
        return "Ошибка: Диски нельзя читать в зале!"
    }
}