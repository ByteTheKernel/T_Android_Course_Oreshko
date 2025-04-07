package com.example.tapplication.management

import com.example.tapplication.library.*
import java.util.concurrent.atomic.AtomicInteger

class DigitalizationService {
    private val idGenerator = AtomicInteger(1000)

    fun <T> digitize(item: T): Disk
        where T: LibraryItem,
              T: Digitalizable<T> {
        return item.toDisk(idGenerator.incrementAndGet())
    }
}