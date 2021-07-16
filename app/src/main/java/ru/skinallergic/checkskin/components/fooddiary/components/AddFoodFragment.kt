package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductAdapter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.view_models.AddingFoodViewModel
import ru.skinallergic.checkskin.databinding.FragmentAddFood2Binding
import java.util.*

class AddFoodFragment : BaseFoodFragment(), RecyclerProductAdapter.OnTextChangedListener , RecyclerProductAdapter.OnDeleteListener{
    lateinit var backStack: ImageButton
    lateinit var saveButton: ImageView
    lateinit var spinner: Spinner
    lateinit var addButton: Button
    lateinit var binding: FragmentAddFood2Binding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerProductAdapter
    val viewModel by lazy {ViewModelProvider(this, viewModelFactory).get(AddingFoodViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RecyclerProductAdapter(mutableListOf())
        adapter.setListeners(this,this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddFood2Binding.inflate(inflater)
        val view= binding.root
        binding.apply {
            backStack=backBtn
            saveButton=okBtn
            spinner=eatTime
            addButton=buttonAdd
            fragment=this@AddFoodFragment
            recyclerView=recycler
        }

        recyclerView.adapter=adapter

        spinner.setAdapter(listOf("Выберите прием пищи", "Завтрак", "Обед", "Ланч", "Ужин"))
        subscribeDate()
        return view
    }

    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener {
            quitSaveLogic({
                quitSaveCondition()
            }, {
                popBack(it)
            }, {
                save()
            })
        }
        addButton.setOnClickListener {

            adapter.addPosition(ProductEntity())
            adapter.notifyItemInserted(adapter.getDataList().size-1)

            /*println("click")
            Loger.log("adapter.getList 1 "+adapter.getDataList())
            viewModel.addProduct(adapter)
            Loger.log("adapter.getList 4 "+adapter.getDataList())*/
        }

        viewModel.addProduct(adapter)
        viewModel.productList.observe(viewLifecycleOwner, {
           /* Loger.log("adapter.getList 2 "+adapter.getDataList())
            val productDiffUtilCallback : DiffUtil.Callback= ProductFoodDiffUtilCallback(adapter.getDataList(), it)
            val productDiffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

            adapter.addDataList(it)
            Loger.log("adapter.getList 3 "+adapter.getDataList())
            productDiffResult.dispatchUpdatesTo(adapter)
            Loger.log("adapter.getList 2 "+adapter.getDataList())*/

            //val adapter=RecyclerProductAdapter(it)
            adapter.addPosition(it[it.size-1])
            adapter.setListeners(this,this)
            adapter.notifyItemInserted(adapter.getDataList().size-1)
            //recyclerView.adapter=adapter
        })
    }

        fun save(){

    }
        fun quitSaveCondition(): Boolean {
        return false
    }

    override fun textChange(str: String, id: Int) {
        when(id){
            R.id.name-> println(str)
            R.id.weight-> println(str)
        }
    }

    override fun delete(entity: ProductEntity, position: Int) {
        //viewModel.deletePosition(entity)
        adapter.removePosition(entity)
        adapter.notifyItemRemoved(position)
    }
}