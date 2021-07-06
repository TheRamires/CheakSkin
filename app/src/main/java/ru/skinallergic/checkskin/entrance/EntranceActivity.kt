package ru.skinallergic.checkskin.entrance

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import ru.skinallergic.checkskin.*
import ru.skinallergic.checkskin.databinding.ActivityEntranceBinding
import ru.skinallergic.checkskin.di.MyViewModelFactory
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl
import java.util.*

class EntranceActivity : AppCompatActivity() {
    private lateinit var accountViewModel: AccountViewModelImpl

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText ) {
                v.clearFocus()
                val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                android.os.Handler().postDelayed({
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }, 100)
            }
        }

        return super.dispatchTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityEntranceBinding>(this, R.layout.activity_entrance)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        if (!hasConnection(this)) {
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_LONG).show()
        }

        Loger.log("pacage " + getApplicationContext().getPackageName())

        val viewModelFactory: MyViewModelFactory = App.instance.appComponent.viewModelFactory
        accountViewModel= ViewModelProvider(this, viewModelFactory).get(AccountViewModelImpl::class.java)

        val locale = Locale("ru")
        Locale.setDefault(locale)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        with(accountViewModel){
            registrationCompleted.observe(this@EntranceActivity, {
                Loger.log("registrationCompleted")
                Loger.log("ENTRANCE ACITVITY,  accountViewModel.registrationCompl, LIVE DATA " + it)
                if (it) {
                    accountViewModel.registrationCompleted.value = false
                    Loger.log("ENTRANCE ACITVITY,  accountViewModel.registrationCompl, LIVE DATA " + it)
                    Navigation.findNavController(this@EntranceActivity, R.id.nav_host_fragment).navigate(R.id.registrationFragment)
                }
            })
            authorizationCompleted.observe(this@EntranceActivity, {
                if (it) {

                    Loger.log("authorizationCompleted +\n "+"//entrance activity loadAccesToken ********************** " + accountViewModel.loadAccesToken())
                    accountViewModel.authorizationCompleted.value = false
                    accountViewModel.currentUser.value?.let { it1 -> accountViewModel.redactProfile(it1) }
                    Loger.log("redact profile, Current User " + accountViewModel.currentUser)
                    val intent = Intent(this@EntranceActivity, MainActivity::class.java)
                    //intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                    //------------------------------------*** Почему-то стерается hared pref,
                    //------------------------------------***  я заменил на android:noHistory="true" в манифесте

                    startActivity(intent)
                }
            })
        }

       /* accountViewModel.registrationCompleted.observe(this, {
            Loger.log("ENTRANCE ACITVITY,  accountViewModel.registrationCompl, LIVE DATA " + it)
            if (it) {
                accountViewModel.registrationCompleted.value = false
                Loger.log("ENTRANCE ACITVITY,  accountViewModel.registrationCompl, LIVE DATA " + it)
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.registrationFragment)
            }
        })*/

        //Вход для соцсетей
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val lsActiveFragments = supportFragmentManager.fragments
        for (fragmentActive in lsActiveFragments) {
            if (fragmentActive is NavHostFragment) {
                val lsActiveSubFragments = fragmentActive.getChildFragmentManager().fragments
                for (fragmentActiveSub in lsActiveSubFragments) {
                    (fragmentActiveSub as? LogInFragment)?.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }
}