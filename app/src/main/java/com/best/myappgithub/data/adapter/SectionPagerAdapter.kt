package com.best.myappgithub.data.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.best.myappgithub.data.ui.FollowerPage

class SectionPagerAdapter (activity: AppCompatActivity, val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowerPage()
        fragment.arguments = Bundle().apply {
            putInt(FollowerPage.ARG_POSITION, position + 1)
            putString(FollowerPage.ARG_USERNAME, username)
        }
        return fragment
    }
}