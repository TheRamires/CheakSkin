package ru.skinallergic.checkskin.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.entrance.EntranceActivity
import ru.skinallergic.checkskin.view_models.AccountViewModel
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.appComponent.viewModelFactory
        accountViewModel = ViewModelProvider(this, viewModelFactory).get(AccountViewModelImpl::class.java)

        val binding : ActivityIntroBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_intro)

        val tabLayout: TabLayout=binding.tabs
        viewPager=binding.pager
        val adapter: FragmentStateAdapter = MyPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(
                tabLayout, viewPager
        ) { tab, position -> }.attach()
        viewPager.isUserInputEnabled=false
    }

    fun swipe(fragment: Int){
        when(fragment){
            1 -> {
                viewPager.setCurrentItem(0, true)
            }
            2 -> {
                viewPager.setCurrentItem(1, true)
            }
            3 -> {
                viewPager.setCurrentItem(2, true)
            }
            4 -> {
                val intent = Intent(this, EntranceActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                accountViewModel.saveFirstEntrance()
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        var backPressedListener: OnBackPressedListener? = null
        for (fragment in fm.getFragments()) {
            if (fragment is OnBackPressedListener) {
                backPressedListener = fragment
                break
            }
        }
        if (backPressedListener != null) {
            backPressedListener.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}