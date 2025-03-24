package com.example.tapplication.management

import com.example.tapplication.shop.Shop
import com.example.tapplication.library.LibraryItem

class PurchaseManager() {
    fun <T: LibraryItem> buy(shop: Shop<T>): T {
        return shop.sell()
    }
}