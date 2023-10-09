package com.best.myappgithub.data.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.best.myappgithub.R
import com.best.myappgithub.data.adapter.SectionPagerAdapter
import com.best.myappgithub.data.database.FavoriteUser
import com.best.myappgithub.data.viewmodel.DetailViewModel
import com.best.myappgithub.data.viewmodel.ViewModelFactory
import com.best.myappgithub.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
        const val EXTRA_USERNAME = "USERNAME"
        const val EXTRA_AVATAR ="AVATAR"
    }

    private lateinit var detailViewModel : DetailViewModel

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val getUsername = intent.getStringExtra(EXTRA_USERNAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (getUsername != null) {
            val sectionPagerAdapter = SectionPagerAdapter(this, getUsername)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tab)

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        detailViewModel = obtainedViewModel(this@DetailUserActivity)

        var favorUser: FavoriteUser = FavoriteUser().apply {

            if (getUsername != null) {
                this.username = getUsername
            }
            this.avatarUrl = avatar

        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isFavorite.observe(this){
            setIfFavorite(it)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        if (getUsername != null) {
            detailViewModel.getDetailUser(getUsername)
        }

        detailViewModel.detailUser.observe(this) {
            if (it != null) {
                Glide.with(this@DetailUserActivity)
                    .load(it.avatarUrl)
                    .centerCrop()
                    .into(binding.imgUserDetail)
                binding.tvName.text = it.name
                binding.tvUsername.text = it.login
                binding.tvFollower.text = "${it.followers} Followers"
                binding.tvFollowing.text = "${it.following} Following"
            }
        }
        detailViewModel.getListFav().observe(this) {favoritedUsers ->
            favoritedUsers?.forEach {
                if (it.username == getUsername) {
                    detailViewModel.setIsFavorite(true)
                    favorUser = it
                }
            }
        }
        binding.fabBeforeFavorite?.setOnClickListener {
            detailViewModel.updateFavUser(favorUser, this)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun setIfFavorite(ifFavorite: Boolean){
        if(ifFavorite){
            binding.fabBeforeFavorite?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_after_favorite))
        }else{
            binding.fabBeforeFavorite?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unfavorite))
        }
    }
    private fun obtainedViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }
}