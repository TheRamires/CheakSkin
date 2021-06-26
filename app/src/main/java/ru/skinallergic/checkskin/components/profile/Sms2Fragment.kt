package ru.skinallergic.checkskin.components.profile

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
import ru.skinallergic.checkskin.view_models.DateViewModel
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentSms2Binding
import ru.skinallergic.checkskin.di.MyViewModelFactory
import ru.skinallergic.checkskin.view_models.AccountViewModel
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl

class Sms2Fragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var mainViewModel: DateViewModel
    private var mHandler: Handler? = null
    private var timerOn: Boolean? = null
    private var startTime: Long? = null
    private var sec:Int?=60
    private lateinit var binding: FragmentSms2Binding
    private lateinit var number:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewModelFactory: MyViewModelFactory = App.instance.appComponent.viewModelFactory
        accountViewModel= ViewModelProvider(requireActivity(), viewModelFactory).get(AccountViewModelImpl::class.java)

        mainViewModel = ViewModelProvider(requireActivity()).get(DateViewModel::class.java)

        val newUser=NewUser()
        number= newUser.number!!
        (accountViewModel as AccountViewModelImpl).redactComplete.value=false
        accountViewModel.sendingCode(number)

        //---------------------------------Прячем бар
        mainViewModel.barVisibility.set(false);

        binding= FragmentSms2Binding.inflate(inflater).apply {
            fragment=this@Sms2Fragment
            number.setText(newUser.number)
            code.doAfterTextChanged {
                accountViewModel.apply{
                    fieldIsFull = it.toString().length==4
                    if (fieldIsFull){code=it.toString()}

                    registrationCompleted.observe(viewLifecycleOwner, {
                        if (it) {
                            //registrationComplite.value = false
                            //accountViewModel.redactProfile(accountViewModel.newUser)
                        }
                    })
                }
            }
            resubmit.isEnabled=false
        }
        (accountViewModel as AccountViewModelImpl).redactComplete.observe(viewLifecycleOwner, {
            if (it) {
                 //На всякий слуучай редактируем profile
                (accountViewModel as AccountViewModelImpl).redactUser()
                //может 2 раза срабатывать, если не стереть sms2_frag из стэка
                Navigation.findNavController(binding.root)
                        .navigate(R.id.action_sms2Fragment_to_profileFragment)
            }
        })

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
                        setTextColor(ContextCompat.getColor(context,R.color.red))
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

    override fun onDestroyView() {
        super.onDestroyView()

        //---------------------------------Включаем бар
        mainViewModel.barVisibility.set(true);
    }

    fun backstack(v: View){
        Navigation.findNavController(v).popBackStack()
    }

    fun resubmit(v: View){
        Loger.log("click")
        val number= AccountViewModelImpl.NewUser.entity.tel
        accountViewModel.sendingCode(number!!)
        binding.resubmit.setTextColor(Color.GRAY)
        binding.resubmit.isClickable=false
        timerOn=true
    }

    fun clickNext(v: View){
        if (accountViewModel.fieldIsFull) {
            //accountViewModel.redactProfile(AccountViewModelImpl.NewUser.entity)
            accountViewModel.redactNumber(number,accountViewModel.code)
            Loger.log(number +", "+accountViewModel.code)
        } else Loger.log("Неверно введен")

        Loger.log(number +", "+accountViewModel.code)

        //Navigation.findNavController(v).navigate(R.id.registrationFragment)
    }
}