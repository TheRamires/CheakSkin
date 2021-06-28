package ru.skinallergic.checkskin.components.healthdiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggerRedactViewModel
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggersViewModel
import ru.skinallergic.checkskin.components.profile.ActionFunction
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment
import ru.skinallergic.checkskin.components.profile.NavigationFunction
import ru.skinallergic.checkskin.databinding.FragmentTriggersRedactBinding
import ru.skinallergic.checkskin.view_models.DateViewModel

class TriggersRedactFragment : TriggerBaseFrag(), View.OnClickListener {
    lateinit var viewModel : TriggerRedactViewModel
    lateinit var dateViewModel: DateViewModel
    lateinit var triggersViewModel: TriggersViewModel
    var fragmentManagr : FragmentManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.getInstance().appComponent.viewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(TriggerRedactViewModel::class.java)
        dateViewModel=ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel::class.java)
        triggersViewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(TriggersViewModel::class.java)
        fragmentManagr=childFragmentManager
        val triggers=getArguments()?.getIntArray(TRIGGERS_BUNDLE)
        triggers?.let { initTriggerLists(it)}
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTriggersRedactBinding = FragmentTriggersRedactBinding.inflate(inflater)
        binding.setFragment(this)
        val view=binding.getRoot()
        initButtons(view)

        for (button in allButtons){
            button.setOnClickListener(this)
        }

        subscribeNewChoosingTriggers(viewModel.newChoosingTriggers)
        with(viewModel){
            saved.observe(viewLifecycleOwner,{
                Loger.log("saved "+it)
                if (it) Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary,false)
            })
            backSaved.observe(viewLifecycleOwner, {
                if (it){ baclLoad(dateViewModel.dateUnix)}
            })
            backLoaded.observe(viewLifecycleOwner, {
                it?.let {
                    triggersViewModel.loaded.value=it
                    backLoaded.value=null
                    Navigation.findNavController(view).popBackStack()
                }
            })
            expiredRefreshToken.observe(viewLifecycleOwner, Observer {
                if (it){
                    toEntrance()
                }
            })
        }

        return view
    }

    override fun onClick(v: View) {
        val button = v as AppCompatButton
        val id = button.id
        when (id) {
            R.id.trig_0 -> {
                addTrigger(0)
            }
            R.id.trig_1 -> {
                addTrigger(1)
            }
            R.id.trig_2 -> {
                addTrigger(2)
            }
            R.id.trig_3 -> {
                addTrigger(3)
            }
            R.id.trig_4 -> {
                addTrigger(4)
            }
            R.id.trig_5 -> {
                addTrigger(5)
            }
            R.id.trig_6 -> {
                Loger.log("trig_6  oldChoosingTriggers"+viewModel.oldChoosingTriggers)
                addTrigger(6)
            }
        }
    }

    override fun changeVisibility(button: AppCompatButton, visible: Boolean) {
        val drawables = button.compoundDrawables
        val icon = drawables[0]
        val checkMark_ = checkMark!!
        val checkMarkOff_=checkMark_off
        if (visible) {
            button.setCompoundDrawables(icon, null, checkMark_, null)
        } else button.setCompoundDrawables(icon, null, checkMarkOff_, null)
    }

    fun backstack(view: View) {
        val changed=viewModel.compareChanging()
        if (changed){
            createBackDialog{
                Navigation.findNavController(view).popBackStack()
            }
        } else Navigation.findNavController(view).popBackStack()

    }
    fun save(view: View?) {
        with(viewModel){
            val changed=compareChanging()
            if (changed){
                val triggers : Array<Int> = newChoosingTriggers.value?.toTypedArray()!!
                save(date=dateViewModel.dateUnix, triggers = triggers
                )}
            Loger.log("oldChoosingTriggers"+oldChoosingTriggers)
            Loger.log("newChoosingTriggers"+newChoosingTriggers.value)

        }
    }

    //-----------------------------****************Ok***
    private fun initTriggerLists(triggers: IntArray){
        val listTrig = ArrayList<Int>()
        for (x in triggers){
            listTrig.add(x)
        }
        viewModel.oldChoosingTriggers.addAll(listTrig)
        viewModel.newChoosingTriggers.value=listTrig

    }

    private fun addTrigger(number: Int) {
        val list_ : MutableList<Int> = viewModel.newChoosingTriggers.value ?: return
        if (list_.size==0){
            list_.add(number)
        } else if (list_.contains(number)){
            list_.remove(number)
        } else {
            list_.add(number)
        };
        viewModel.newChoosingTriggers.value=list_
    }

    private fun subscribeNewChoosingTriggers(livaData :MutableLiveData<MutableList<Int>>){
        livaData.observe(viewLifecycleOwner,{newChoosingTriggers->
            for (buttonCount in allButtons.indices){
                if (newChoosingTriggers.contains(buttonCount)){
                    val button=allButtons[buttonCount]
                    changeVisibility(button, true)
                } else {
                    val button=allButtons[buttonCount]
                    changeVisibility(button, false)
                }
            }
        })
    }
    private fun createBackDialog(popBack: ()->Unit){
        val negative = object : ActionFunction {
            override fun action() {
                popBack()
            }
        }
        val positive =object  : ActionFunction {
            override fun action() {
                val triggers : Array<Int> = viewModel.newChoosingTriggers.value?.toTypedArray()!!
                viewModel.backSave(date=dateViewModel.dateUnix, triggers = triggers
                )}
            }

        val navigation = object : NavigationFunction {
            override fun navigate() {
            }
        }

        val dialog = DialogTwoFunctionFragment(
                message = "Сохранить изменения",
                negativeAction = negative,
                positiveAction = positive ,
                navigationFunction = navigation,
        )
        dialog.show(fragmentManagr!!, "dialog")
    }
}