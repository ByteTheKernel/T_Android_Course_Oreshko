package com.example.tapplication

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.tapplication.databinding.ActivityMainBinding
import com.example.tapplication.ui.fragments.AddFragment
import com.example.tapplication.ui.fragments.DetailFragment
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

        if (isTwoPaneMode()) {
            setupTwoPaneMode()
        } else {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.navHostFragment) as NavHostFragment
            navController = navHostFragment.navController
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

    private fun replaceFragment(containerId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    private fun setupTwoPaneMode() {
        if (supportFragmentManager.findFragmentById(R.id.listContainer) == null) {
            replaceFragment(R.id.listContainer, ListFragment.getInstance(true))
        }

        viewModel.selectedItemId.observe(this) { itemId ->
            itemId?.let {
                if (isTwoPaneMode()) {
                    showDetail(it)
                } else {
                    findNavController(R.id.navHostFragment).navigate(
                        R.id.action_listFragment_to_detailFragment,
                        DetailFragment.createBundle(it, false)
                    )
                }
            }
        }
    }

    fun showAddForm() {
        binding.detailContainer?.let { container ->
            container.visibility = View.VISIBLE
            replaceFragment(R.id.detailContainer, AddFragment.getInstance(true))
        }
    }

    private fun showDetail(itemId: Int) {
        binding.detailContainer?.let { container ->
            container.visibility = View.VISIBLE
            replaceFragment(R.id.detailContainer, DetailFragment.getInstance(itemId, true))
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
            container.gone()

            val fragment = supportFragmentManager.findFragmentById(R.id.detailContainer)
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .remove(it)
                    .commit()
            }
        }
        viewModel.clearSelectedItem()
    }
}