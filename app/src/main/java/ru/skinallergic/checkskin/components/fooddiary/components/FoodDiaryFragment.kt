package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentFoodDiary2Binding

class FoodDiaryFragment : BaseFoodFragment() {
    lateinit var buttonProfile: ImageButton
    lateinit var buttonDate: ImageButton
    lateinit var buttonAllergy: ImageButton
    lateinit var buttonAdd: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding =FragmentFoodDiary2Binding.inflate(inflater)
        val view= binding.root
        binding.apply {
            fragment=this@FoodDiaryFragment
            buttonProfile = profileBtn
            buttonDate = dateBtn
            buttonAllergy = allergyBtn
            buttonAdd = addBtn
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        subscribeDate()
        buttonProfile.setOnClickListener { Navigation.findNavController(it).navigate(R.id.profileFragment)}
        buttonDate.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodDiaryFragment_to_foodCalendarFragment)}
        buttonAllergy.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodDiaryFragment_to_allergicListFragment)}
        buttonAdd.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodDiaryFragment_to_addFoodFragment2) }
    }
}