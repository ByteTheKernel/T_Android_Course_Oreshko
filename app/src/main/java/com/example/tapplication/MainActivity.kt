package com.example.tapplication

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.tapplication.data.local.AppDatabase
import com.example.tapplication.data.network.RetrofitHelper
import com.example.tapplication.data.repository.LibraryRepository
import com.example.tapplication.data.repository.RemoteBooksRepository
import com.example.tapplication.data.repository.SettingsRepository
import com.example.tapplication.databinding.ActivityMainBinding
import com.example.tapplication.ui.fragments.AddFragment
import com.example.tapplication.ui.fragments.AddFragmentArgs
import com.example.tapplication.ui.fragments.DetailFragment
import com.example.tapplication.ui.fragments.DetailFragmentArgs
import com.example.tapplication.ui.fragments.ListFragment
import com.example.tapplication.ui.fragments.ListFragmentDirections
import com.example.tapplication.ui.viewmodels.MainViewModel
import com.example.tapplication.ui.viewmodels.MainViewModelFactory
import com.example.tapplication.utils.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel

    val factory: MainViewModelFactory by lazy {
        val libraryRepository = LibraryRepository(AppDatabase.getDatabase(applicationContext).libraryDao())
        val remoteBooksRepository = RemoteBooksRepository(RetrofitHelper.createRetrofit())
        val settingsRepository = SettingsRepository(applicationContext)
        MainViewModelFactory(libraryRepository, remoteBooksRepository, settingsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

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
                        ListFragmentDirections.actionListFragmentToDetailFragment(itemId = it, isTwoPane = true)
                    )
                }
            }
        }
    }

    fun showAddForm() {
        if (isTwoPaneMode()) {
            binding.detailContainer?.let { container ->
                container.show()

                val args = AddFragmentArgs(true).toBundle()
                val fragment = AddFragment().apply {
                    arguments = args
                }
                replaceFragment(R.id.detailContainer, fragment)
            }
        } else {
            findNavController(R.id.navHostFragment).navigate(
                ListFragmentDirections.actionListFragmentToAddFragment(isTwoPane = false)
            )
        }
    }

    private fun showDetail(itemId: Int) {
        if (isTwoPaneMode()) {
            binding.detailContainer?.let { container ->
                container.show()

               val args = DetailFragmentArgs(itemId, true).toBundle()
                val fragment = DetailFragment().apply {
                    arguments = args
                }
                replaceFragment(R.id.detailContainer, fragment)
            }
        } else {
            findNavController(R.id.navHostFragment).navigate(
                ListFragmentDirections.actionListFragmentToDetailFragment(itemId = itemId, isTwoPane = false)
            )
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

    fun getMainViewModelFactory(): MainViewModelFactory = factory
}