package com.example.tapplication.shop

import com.example.tapplication.library.Disk

class DiskShop: Shop<Disk> {
    override fun sell(): Disk {
        return Disk(500, true, "1917", "DVD")
    }
}