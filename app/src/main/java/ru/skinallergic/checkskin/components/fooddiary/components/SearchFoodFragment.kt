package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentFoodDiary2Binding
import ru.skinallergic.checkskin.databinding.FragmentSearchFood2Binding

class SearchFoodFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentSearchFood2Binding.inflate(inflater)
        val view= binding.root
        backStack=binding.backBtn
        binding.fragment=this

        return view
    }
    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener { popBack(it) }
        subscribeDate()
    }
}