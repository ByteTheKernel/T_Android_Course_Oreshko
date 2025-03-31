package com.example.tapplication.management

import com.example.tapplication.shop.Shop
import com.example.tapplication.library.LibraryItem

class PurchaseManager() {
    fun <T: LibraryItem> sell(shop: Shop<T>): T {
        return shop.sell()
    }
}