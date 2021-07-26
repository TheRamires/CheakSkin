package ru.skinallergic.checkskin.components.fooddiary.components

import android.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.components.healthdiary.BackNavigation
import ru.skinallergic.checkskin.components.healthdiary.QuitSaveLogic
import ru.skinallergic.checkskin.components.profile.ActionFunction
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment
import ru.skinallergic.checkskin.components.profile.NavigationFunction
import ru.skinallergic.checkskin.view_models.DateViewModel

abstract class BaseFoodFragment : Fragment(){
    val dateObservable = ObservableField<String>()
    val viewModelFactory by lazy {App.getInstance().appComponent.viewModelFactory}
    val dateViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel::class.java)
    }
    val toastyManager by lazy {App.getInstance().appComponent.toastyManager }
    val fManager by lazy { parentFragmentManager }

    fun subscribeDate(){
        with(dateViewModel){
            dateLive.observe(viewLifecycleOwner, {
                dateObservable.set(getDate(it))
            })
        }
    }

    open fun popBack(view: View){
        Navigation.findNavController(view).popBackStack()
    }

    fun quitSaveLogic(quitSaveCondition:()->Boolean ,popBack: () -> Unit, save: () -> Unit){
        if (quitSaveCondition()){
            quitSaveDialog(popBack, save)
        } else popBack()
    }

    fun quitSaveDialog(popBack: () -> Unit, save: () -> Unit) {
        val negative = object : ActionFunction {
            override fun action() {
                popBack()
            }
        }
        val positive = object : ActionFunction {
            override fun action() {
                save()
            }
        }
        val navigation = object : NavigationFunction {
            override fun navigate() {
                //empty
            }
        }
        val dialog = DialogTwoFunctionFragment(
                "Сохранить изменения", negative, positive, navigation)
        dialog.show(fManager, "dialog")
    }
    fun Spinner.setAdapter(list: List<String>){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }
}