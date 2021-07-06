package ru.skinallergic.checkskin.entrance

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentSms1Binding
import ru.skinallergic.checkskin.di.MyViewModelFactory
import ru.skinallergic.checkskin.view_models.AccountViewModel
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl

class Sms1Fragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModelImpl
    private var mHandler: Handler? = null
    private var timerOn: Boolean? = null
    private var startTime: Long? = null
    private var sec:Int?=60
    private lateinit var binding: FragmentSms1Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewModelFactory: MyViewModelFactory = App.instance.appComponent.viewModelFactory
        accountViewModel= ViewModelProvider(requireActivity(),viewModelFactory).get(AccountViewModelImpl::class.java)

        binding= FragmentSms1Binding.inflate(inflater).apply {
            fragment=this@Sms1Fragment
            number.setText(accountViewModel.currentUser.value?.tel)
            code.doAfterTextChanged {
                accountViewModel.apply{
                    fieldIsFull = it.toString().length==4
                    if (fieldIsFull){code=it.toString()}
/*
                    registrationComplite.observe(viewLifecycleOwner,{
                        if (it){
                            registrationComplite.value=false
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_sms1Fragment_to_registrationFragment)}
                    })*/
                }
            }
            resubmit.isEnabled=false
        }

        startTime = System.currentTimeMillis()
        mHandler = object : Handler() {
            override fun handleMessage(message: Message) {
                super.handleMessage(message)
                if (timerOn!!) {
                    binding.sec.setText("($sec сек)")
                    sec=sec!!.minus(1)
                }
                if (sec==0){
                    sec=60
                    binding.sec.setText("")
                    timerOn=false
                    binding.resubmit.apply {
                        setTextColor(ContextCompat.getColor(context, R.color.red))
                        isEnabled=true
                        isClickable=true
                    }
                }
                mHandler!!.sendEmptyMessageDelayed(0, 1000)
            }
        }

        timerOn = true
        (mHandler as Handler).sendEmptyMessage(0)

        return binding.root
    }

    fun backstack(v: View){
        Navigation.findNavController(v).popBackStack()
    }

    fun resubmit (v:View){
        Loger.log("click")
        val number=accountViewModel.currentUser.value?.tel
        accountViewModel.sendingCode(number!!)
        binding.resubmit.setTextColor(Color.GRAY)
        binding.resubmit.isClickable=false
        timerOn=true
    }

    fun clickNext (v:View){
        if (accountViewModel.fieldIsFull) {
            val number=accountViewModel.currentUser.value?.tel
            Loger.log("log in "+number)
            accountViewModel.logIn(number!!)
        } else Loger.log("Неверно введен")

        //Navigation.findNavController(v).navigate(R.id.registrationFragment)
    }
}