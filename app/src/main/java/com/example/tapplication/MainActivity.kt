package com.example.tapplication

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.tapplication.databinding.ActivityMainBinding
import com.example.tapplication.ui.fragments.AddFragment
import com.example.tapplication.ui.fragments.ListFragment
import com.example.tapplication.ui.viewmodels.MainViewModel
import com.example.tapplication.utils.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!isTwoPaneMode()) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.navHostFragment) as NavHostFragment
            navController = navHostFragment.navController
        }

        if (isTwoPaneMode()) {
            setupTwoPaneMode()
        }

        viewModel.selectedItemId.observe(this) { itemId ->
            itemId?.let {
                showDetail(binding, it)
            }
        }
    }

    private fun isTwoPaneMode(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private fun setupTwoPaneMode() {

        if (supportFragmentManager.findFragmentById(R.id.listContainer) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.listContainer, ListFragment().apply {
                    arguments = Bundle().apply { putBoolean("isTwoPane", true) }
                })
                .commit()
        }

        viewModel.selectedItemId.observe(this) { itemId ->
            itemId?.let {
                if (isTwoPaneMode()) {
                    showDetail(binding, it)
                } else {
                    // В книжной ориентации переходим к фрагменту с детальной информацией
                    findNavController(R.id.navHostFragment).navigate(
                        R.id.action_listFragment_to_detailFragment,
                        Bundle().apply { putInt("itemId", it) }
                    )
                }
            }
        }
    }

    fun showAddForm() {
        binding.detailContainer?.let { container ->
            container.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.detailContainer, AddFragment().apply {
                    arguments = Bundle().apply { putBoolean("isTwoPane", true) }
                })
                .commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (binding.detailContainer?.visibility == View.VISIBLE) {
            hideDetail()
            true
        } else {
            navController.navigateUp() || super.onSupportNavigateUp()
        }
    }

    override fun onBackPressed() {
        if (binding.detailContainer?.visibility == View.VISIBLE) {
            hideDetail()
        } else {
            super.onBackPressed()
        }
    }

    internal fun hideDetail() {
        binding.detailContainer?.let { container ->
            container.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.findFragmentById(R.id.detailContainer)!!)
                .commit()
        }
        viewModel.clearSelectedItem()
    }
}