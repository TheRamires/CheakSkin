package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.AllergicAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicWriter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AllergenesViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.MealViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.add
import ru.skinallergic.checkskin.components.fooddiary.viewModels.delete
import ru.skinallergic.checkskin.databinding.FragmentAllergicListBinding
import ru.skinallergic.checkskin.databinding.FragmentFoodDiary2Binding

class AllergicListFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    val allergenesViewModel by lazy { ViewModelProvider(this,viewModelFactory).get(AllergenesViewModel::class.java) }
    lateinit var recyclerView : RecyclerView
    lateinit var adapter :AllergicAdapter
    lateinit var addButton: Button
    lateinit var thisView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentAllergicListBinding.inflate(inflater)
        addButton=binding.addBtn
        recyclerView=binding.recycler
        adapter= AllergicAdapter(arrayListOf())
        adapter.bind { item, entity ->
            item.name.doAfterTextChanged { entity.name=it.toString()  }
            item.delete.setOnClickListener { delete(entity) }
        }
        recyclerView.adapter=adapter
        thisView= binding.root
        backStack=binding.backBtn

        return thisView
    }

    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener {
            quitSaveLogic({
                quitSaveCondition()},{
                popBack(it)},{
                save()})
        }
        addButton.setOnClickListener {
            println("click")
            val list=getData()
            if (allergenesViewModel.conditionOfAdding(list)){
                add(AllergicWriter(name = ""))
            }
        }

        allergenesViewModel.productList.observe(viewLifecycleOwner, {newData->
            println("newData $newData")
            val diffUtil = DiffUtilFromMySelf<AllergicWriter>(adapter.list, newData)
            diffUtil.calculate(adapter)
        })
        allergenesViewModel.backSave.observe(viewLifecycleOwner,{
            if (it){
                Navigation.findNavController(thisView).popBackStack()
            }
        })
    }

    fun save(backSaved: MutableLiveData<Boolean>?=null){
        Loger.log("save")
        val list: List<AllergicWriter> =adapter.list
        allergenesViewModel.productList.value
        if (allergenesViewModel.saveCondition()){
            allergenesViewModel.addAllergens(list,backSaved)
        }
    }

    fun quitSaveCondition(): Boolean {
        return true

    }

    fun add(position: AllergicWriter){
        allergenesViewModel.productList.add(position)
    }

    fun delete(position: AllergicWriter){
        allergenesViewModel.productList.delete(position)
    }

    fun getData():List<AllergicWriter>{
        return adapter.list
    }
}