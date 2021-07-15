package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentAllergicListBinding
import ru.skinallergic.checkskin.databinding.FragmentFoodDiary2Binding

class AllergicListFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentAllergicListBinding.inflate(inflater)
        val view= binding.root
        backStack=binding.backBtn

        return view
    }

    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener {
            quitSaveLogic({
                quitSaveCondition()},{
                popBack(it)},{
                save()})
        }
    }
    fun save(){

    }

    fun quitSaveCondition(): Boolean {
        return false
    }
}