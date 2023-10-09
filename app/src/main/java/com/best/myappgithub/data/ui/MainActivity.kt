package com.best.myappgithub.data.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.best.myappgithub.R
import com.best.myappgithub.data.adapter.GitHubAdapter
import com.best.myappgithub.data.response.ItemsItem
import com.best.myappgithub.data.settings.SettingPreferences
import com.best.myappgithub.data.settings.dataStore
import com.best.myappgithub.data.viewmodel.MainViewModel
import com.best.myappgithub.data.viewmodel.SettingViewModel
import com.best.myappgithub.data.viewmodel.SettingViewModelFactory
import com.best.myappgithub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val ViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            srView.setupWithSearchBar(srBar)
            srView.editText.setOnEditorActionListener { _, _, _ ->
                srBar.text = srView.text
                srView.hide()
                ViewModel.findUser(srView.text.toString())
                ViewModel.User.observe(this@MainActivity) {
                    if (it.isNullOrEmpty()) {
                        ifUserNotFound(true)
                    } else {
                        ifUserNotFound(false)
                    }
                }
                false
            }
        }
        ViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        ViewModel.User.observe(this) {
            if (it != null) {
                setUserData(it)
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuitem ->
            when (menuitem.itemId) {
                R.id.favorites -> {
                    val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.settings -> {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setUserData(dataUser: List<ItemsItem>) {
        val adapter = GitHubAdapter()
        adapter.submitList(dataUser)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun ifUserNotFound(isDataNotFound: Boolean) {
        binding.apply {
            if (isDataNotFound) {
                rvUser.visibility = View.GONE
                tvNotFound.visibility = View.VISIBLE
            } else {
                rvUser.visibility = View.VISIBLE
                tvNotFound.visibility = View.GONE
            }
        }
    }

}