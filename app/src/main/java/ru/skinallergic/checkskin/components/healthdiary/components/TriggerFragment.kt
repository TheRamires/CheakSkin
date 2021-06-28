package ru.skinallergic.checkskin.components.healthdiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.App
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggersViewModel
import ru.skinallergic.checkskin.databinding.FragmentTriggersBinding
import ru.skinallergic.checkskin.view_models.DateViewModel

class TriggersFragment : TriggerBaseFrag() {

    lateinit var triggersViewModel: TriggersViewModel
    lateinit var dateViewModel: DateViewModel
    private var triggers: Array<Int>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.getInstance().appComponent.viewModelFactory
        triggersViewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(TriggersViewModel::class.java)
        dateViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel::class.java)

        triggersViewModel.getData(dateViewModel.dateUnix)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTriggersBinding = FragmentTriggersBinding.inflate(inflater)
        val view=binding.getRoot()
        binding.setFragment(this)
        binding.viewModel=triggersViewModel

        with(triggersViewModel){
            subscribeTriggers(loaded, splashScreenOn)

            expiredRefreshToken.observe(viewLifecycleOwner, Observer {
                if(it){
                    toEntrance()
                }
            })
        }

        initButtons(view)
        return view
    }

    fun backStack(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    fun toRedact(view: View) {
        val bundle=Bundle()
        bundle.putIntArray(TRIGGERS_BUNDLE,triggers?.toIntArray())
        Navigation.findNavController(view).navigate(R.id.action_triggersFragment_to_triggersRedactFragment,bundle)
    }

    private fun subscribeTriggers(livaData : MutableLiveData<GettingData?>, splashScreenOn: ObservableField<Boolean>){
        livaData.observe(viewLifecycleOwner,{
            splashScreenOn.set(false)
            it.let {triggers=it?.triggers
            triggers?.let {

                for (buttonCount in allButtons.indices){
                    if (triggers!!.contains(buttonCount)){
                        val button=allButtons[buttonCount]
                        changeVisibility(button, true)
                    } else {
                        val button=allButtons[buttonCount]
                        changeVisibility(button, false)
                    }
                }
            }
        }
        })
    }
}