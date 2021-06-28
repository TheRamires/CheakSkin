package ru.skinallergic.checkskin.intro

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(@NonNull fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FragmentIntro01()
            1 -> return FragmentIntro02()
            2 -> return FragmentIntro03()
        }
        return FragmentIntro01()
    }

    override fun getItemCount(): Int {
        return 3
    }
}