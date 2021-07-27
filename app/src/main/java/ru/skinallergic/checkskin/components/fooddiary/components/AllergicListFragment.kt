package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
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
import ru.skinallergic.checkskin.components.fooddiary.adapters.AllergicAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicWriter
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AllergenesViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.add
import ru.skinallergic.checkskin.components.fooddiary.viewModels.delete
import ru.skinallergic.checkskin.databinding.FragmentAllergicListBinding

class AllergicListFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    val allergenesViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(AllergenesViewModel::class.java) }
    lateinit var recyclerView : RecyclerView
    lateinit var adapter :AllergicAdapter
    lateinit var addButton: Button
    lateinit var thisView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allergenesViewModel.initAndClear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentAllergicListBinding.inflate(inflater)
        binding.baseViewModel=allergenesViewModel
        addButton=binding.addBtn
        recyclerView=binding.recycler
        adapter= AllergicAdapter(arrayListOf())
        adapter.bind { item, entity ->
            item.name.doAfterTextChanged { entity.name=it.toString()  }
            item.delete.setOnClickListener { delete(entity) }
            item.name.isEnabled = entity.openRedacting

        }
        recyclerView.adapter=adapter
        thisView= binding.root
        backStack=binding.backBtn

        allergenesViewModel.getAllergens()

        return thisView
    }

    fun back(view: View){
        quitSaveLogic({
            quitSaveCondition()
        }, {
            popBack(view)
        }, {
            allergenesViewModel.saveAll()
            popBack(view)
        })
    }

    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener {
            Loger.log("--- \n oldList ${allergenesViewModel.oldList} \n newList ${allergenesViewModel.newList}")
            Loger.log("--- \n deleteList ${allergenesViewModel.getDeleteList()} \n addingList ${allergenesViewModel.getAddingList()}")
            back(it)
        }
        addButton.setOnClickListener {
            println("click")
            val list=getData()
            if (allergenesViewModel.conditionOfAdding(list)){
                add(AllergicWriter(name = ""))
            }
        }

        allergenesViewModel.productList.observe(viewLifecycleOwner, { newData ->
            println("newData $newData")
            val diffUtil = DiffUtilFromMySelf<AllergicWriter>(adapter.list, newData)
            diffUtil.calculate(adapter)

        })
        allergenesViewModel.backSave.observe(viewLifecycleOwner, {
            if (it) {
                Navigation.findNavController(thisView).popBackStack()
            }
        })

    }

    fun save(backSaved: MutableLiveData<Boolean>? = null){
        Loger.log("save")
        val list: List<AllergicWriter> =adapter.list
        allergenesViewModel.productList.value
       /* if (allergenesViewModel.saveCondition()){
            allergenesViewModel.addAllergens(list, backSaved)
        }*/

    }

    fun quitSaveCondition(): Boolean {
        return (allergenesViewModel.getAddingList().isNotEmpty()
                || allergenesViewModel.getDeleteList().isNotEmpty())
    }

    fun add(position: AllergicWriter){
        position.openRedacting=true
        allergenesViewModel.productList.add(position)
        allergenesViewModel.newList.add(position)
    }

    fun delete(position: AllergicWriter){
        allergenesViewModel.productList.delete(position)
        allergenesViewModel.newList.remove(position)
    }

    fun getData():List<AllergicWriter>{
        return adapter.list
    }
}