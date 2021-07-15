package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.Navigation
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentDetailFoodBinding

class DetailFoodFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    lateinit var redactButton: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailFoodBinding.inflate(inflater)
        binding.apply {
            backStack=backBtn
            redactButton=redactBtn
            fragment=this@DetailFoodFragment
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener { popBack(it) }
        redactButton.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_detailFoodFragment_to_redactFoodFragment) }
        subscribeDate()
    }
}