package ru.skinallergic.checkskin.entrance

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.MainActivity
import android.R
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import ru.skinallergic.checkskin.databinding.FragmentRegistrationBinding
import ru.skinallergic.checkskin.di.MyViewModelFactory
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField
import ru.skinallergic.checkskin.entrance.helper_classes.removeSpaces
import ru.skinallergic.checkskin.utils.GENDER_FEMALE
import ru.skinallergic.checkskin.utils.GENDER_MALE
import ru.skinallergic.checkskin.utils.GENDER_NONE
import ru.skinallergic.checkskin.view_models.AccountViewModel
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl
import java.util.regex.Pattern

class RegistrationFragment : Fragment() {
    lateinit var accountViewModel: AccountViewModel
    private lateinit var radioGroup:RadioGroup
    private lateinit var validateField: ValidatorField

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentRegistrationBinding.inflate(inflater)
        val view = binding.root

        validateField= App.instance.appComponent.validatorField
        val viewModelFactory: MyViewModelFactory = App.instance.appComponent.viewModelFactory
        accountViewModel= ViewModelProvider(requireActivity(), viewModelFactory).get(AccountViewModelImpl::class.java)
        with(binding){
            fragment=this@RegistrationFragment
            viewModel= accountViewModel as AccountViewModelImpl?
            if (accountViewModel.currentUser.value?.name!=null || accountViewModel.currentUser.value?.name!="" ){
                editName.setText(accountViewModel.currentUser.value?.name)
            }
        }

        with(accountViewModel){
            getRegions()
            getDiagnosis()
            subscribeLiveDiagnosis(this, binding)
            subscribeLiveRegions(this, binding)
        }

        with(binding){
            editName.doAfterTextChanged {
                val temp:String=it.toString()
                val name=removeSpaces(temp)

                Loger.log(name)
                var userTemp=accountViewModel.currentUser.value
                userTemp?.name=name
                accountViewModel.currentUser.value=userTemp
            }
            if (radioGroup.checkedRadioButtonId==-1){
                var userTemp=accountViewModel.currentUser.value
                userTemp?.gender=GENDER_NONE
                accountViewModel.currentUser.value=userTemp
            }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                var userTemp=accountViewModel.currentUser.value
                when(checkedId){
                    radioGroup[0].id -> {
                        var userTemp=accountViewModel.currentUser.value
                        userTemp?.gender=GENDER_MALE
                    }
                    radioGroup[2].id -> {
                        var userTemp=accountViewModel.currentUser.value
                        userTemp?.gender=GENDER_FEMALE
                    }
                    else->{
                        var userTemp=accountViewModel.currentUser.value
                        userTemp?.gender=GENDER_NONE
                    }
                }
                accountViewModel.currentUser.value=userTemp
            }
        }
        //accountViewModel.logInSave()

        return view
    }

    fun backstack(v: View){
        accountViewModel.deleteAccount(true)
        Navigation.findNavController(v).popBackStack(ru.skinallergic.checkskin.R.id.logInFragment, false)
    }

    fun clickNext(v: View){
        Loger.log("Заполненные данные пользователя: " + accountViewModel.currentUser.toString())
        var fieldIsTrue: Boolean=validateField.validate(accountViewModel.currentUser.value!!)

        if(!fieldIsTrue){
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            Loger.log(" Redact Profile failed - Заполните все поля")
        } else{
            accountViewModel.redactProfile(accountViewModel.currentUser.value!!)
            Loger.log("redact profile, Current User "+accountViewModel.currentUser)
            val intent= Intent(context, MainActivity::class.java)
                    //.putExtra("id", "3")
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun Spinner.setAdapter(list: List<String>){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }

    /*private fun Spinner.setAdapterRefions(list: List<DatumR>){
        val temp: MutableList<String> =ArrayList()
        for (i in 0..list.size){
            temp[i]=list[i].name!!
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, temp)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }*/

    private fun subscribeLiveDiagnosis(viewModel: AccountViewModel, binding: FragmentRegistrationBinding){
        viewModel.liveDianosis.observe(viewLifecycleOwner, {
            if (it==null) return@observe
            val spinner = binding.spinnerDiagnosis
            spinner.setAdapter(it)
            viewModel.liveDianosis.value=null
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                            position: Int, id: Long) {
                    var userTemp=accountViewModel.currentUser.value
                    userTemp?.diagnosisId=position
                    accountViewModel.currentUser.value=userTemp
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
        })
    }

    private fun subscribeLiveRegions(viewModel: AccountViewModel, binding: FragmentRegistrationBinding){
        viewModel.liveRegions.observe(viewLifecycleOwner, {
            if (it==null) return@observe
            val spinner: Spinner = binding.spinnerRegistr
            spinner.setAdapter(it)
            viewModel.liveRegions.value=null
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View,
                                            position: Int, id: Long) {
                    var userTemp=accountViewModel.currentUser.value
                    userTemp?.regionId=position
                    accountViewModel.currentUser.value=userTemp
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
        })
    }
    companion object Validator{
        val regx="^[а-яА-Я][а-яА-Я0-9-_.]{1,20}$"
        var pattern = Pattern.compile(regx)

        fun validateName(text: String):Boolean{
            var matcher = pattern.matcher(text)
            var matches = matcher.matches()
            return matches
        }
    }
}