package com.example.tapplication.common.extensions


import android.os.Bundle
import android.view.View
import com.example.tapplication.presentation.MainActivity
import com.example.tapplication.R
import com.example.tapplication.databinding.ActivityMainBinding
import com.example.tapplication.presentation.fragments.detail.DetailFragment


fun MainActivity.showDetail(binding: ActivityMainBinding, itemId: Int) {
    binding.detailContainer?.let { container ->
        container.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailContainer, DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("itemId", itemId)
                    putBoolean("isTwoPane", true)
                }
            })
            .commit()
    }
}