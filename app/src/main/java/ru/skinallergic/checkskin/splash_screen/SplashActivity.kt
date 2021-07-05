package ru.skinallergic.checkskin.splash_screen

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ru.skinallergic.checkskin.*
import ru.skinallergic.checkskin.databinding.ActivitySplashBinding
import ru.skinallergic.checkskin.di.MyViewModelFactory
import ru.skinallergic.checkskin.entrance.EntranceActivity
import ru.skinallergic.checkskin.intro.IntroActivity
import ru.skinallergic.checkskin.utils.NEWS_BUNDLE
import ru.skinallergic.checkskin.utils.PROFILE_BUNDLE
import ru.skinallergic.checkskin.utils.SPLASH_BUNDLE
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

//Ramires------------/////////
class SplashActivity : AppCompatActivity() {
    private var newsComplite=false
    private var profileComplite=false
    private lateinit var viewModel: SplashViewModel

    private fun printKeyHash() {
        try {
            val info = packageManager
                    .getPackageInfo("ru.skinallergic.checkskin",
                            PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Loger.log( "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Loger.log( "KeyHash: " + e)
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            Loger.log( "KeyHash: " + e)
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val binding=DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

                                        //**********************************************************************
                                        val intent=Intent(this,MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        //***********************************************************************


        printKeyHash()


        if (!hasConnection(this)) {
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_LONG).show()
        }

        val viewModelFactory: MyViewModelFactory= App.instance.appComponent.viewModelFactory
        viewModel= ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
        binding.viewModel=viewModel

        var bundle = Bundle()

        viewModel.checkEntrance()
        with(viewModel){
            newsLive.observe(this@SplashActivity, {
                if (it != null) {
                    newsComplite = true

                    val newsJson = ConverterString.newsToString(it)
                    Loger.log("responseString" + newsJson)

                    bundle.putString(NEWS_BUNDLE, newsJson)
                    checkData()

                    /*val list: List<Datum?> = ConverterString.stringToSomeObjectList(responseString) as List<Datum?>

                for (ent in list){
                    Loger.log(ent?.name)
                }*/
                }
            })
            profileLive.observe(this@SplashActivity, {
                if (it != null) {
                    profileComplite = true
                    val profileJson = ConverterString.profileToString(it)
                    Loger.log("profileLive " + it.toString())
                    Loger.log("profileLive: " + profileJson)

                    bundle.putString(PROFILE_BUNDLE, profileJson)

                    checkData()
                }
            })
        }

        with(viewModel){
            goToHome.observe(this@SplashActivity, {
                if (it) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra(SPLASH_BUNDLE, bundle)
                    startActivity(intent, bundle)
                    //Toast.makeText(this@SplashActivity, "goToHome", Toast.LENGTH_SHORT).show();
                    //goToHome.value=false
                }
            })

            goToIntro.observe(this@SplashActivity, {
                if (it) {
                    Loger.log("Go To Intro")
                    val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(intent)
                    //Toast.makeText(this@SplashActivity,"goToIntro",Toast.LENGTH_SHORT).show();
                    //goToIntro.value=false
                }
            })

            goToLogIn.observe(this@SplashActivity, {
                if (it) {
                    val intent = Intent(this@SplashActivity, EntranceActivity::class.java)
                    startActivity(intent)
                    //Toast.makeText(this@SplashActivity, "goToLogIn", Toast.LENGTH_SHORT).show();
                    //goToLogIn.value=false
                }
            })
            refreshTokenFailed.observe(this@SplashActivity, {
                if (it) {
                    val builder = AlertDialog.Builder(this@SplashActivity)
                            .setTitle("Ошибка обновления токена!")
                            .setNegativeButton("Выйти!") { dialog, id ->
                                dialog.cancel()
                                finish()
                            }
                            .setPositiveButton("Повторить попытку!") { dialog, id ->
                                viewModel.refreshToken()
                            }
                            .create()
                    builder.show()
                }
            })
        }

        viewModel.complete.observe(this, {
            if (it) {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            }
        })
    }
    fun checkData(){
        if (newsComplite==true && profileComplite==true){
            newsComplite=false; profileComplite=false
            viewModel.goToHome.value=true
        }
    }

    override fun onPause() {
        super.onPause()
        Loger.log("onPause")
    }

    override fun onStop() {
        super.onStop()
        Loger.log("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Loger.log("onDestroy")
    }
}