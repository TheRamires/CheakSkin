package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.FoodPositionAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.SearchingRecyclerAdapterFood
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel
import ru.skinallergic.checkskin.databinding.*

class SearchFoodFragment : BaseFoodFragment() , SearchingRecyclerAdapterFood.OnItemClickListener {
    lateinit var backStack: ImageButton
    lateinit var recyclerView: RecyclerView
    lateinit var adapter : SearchingRecyclerAdapterFood
    val foodDiaryViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(FoodDiaryViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentSearchFood2Binding.inflate(inflater)
        recyclerView=binding.recycler
        binding.editText.doAfterTextChanged {
            if (it?.length!! >=3){
                search(it.toString())

            } else if (it.isEmpty()){
                search("")
            }
        }
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
    fun search(search :String){
        foodDiaryViewModel.getFoodDiarySearchByDate(dateViewModel.dateUnix,search).observe(viewLifecycleOwner,{list->
            for (pos in list){
                println("${pos?.created} ${pos?.list}")
            }

            /*val diffUtil = list?.let { DiffUtilFromMySelf<FoodMealForMain?>(adapter.list, it) }
            diffUtil?.calculate(adapter)*/

            adapter = SearchingRecyclerAdapterFood(list as ArrayList<FoodMealForMain>, this,
                   object: SearchingRecyclerAdapterFood.RecyclerCallback{
                       override fun bind(binder: ItemFoodSearchingBinding?, entity: FoodMealForMain?) {
                           binder?.date?.visibility=View.VISIBLE
                           binder?.date?.text=entity?.created.toString()
                           binder?.entity = entity
                           binder?.clickable?.setOnClickListener { view: View ->
                               val bundle = Bundle()
                               bundle.putInt(BUNDLE_ID_OF_REMIND, entity!!.id)
                               bundle.putBoolean(FROM_SEARCHING,true)
                               Loger.log("positionId for bundle " + entity.id)
                               navFunction(view, R.id.detailFoodFragment, bundle)
                           }
                           val innerRecycler=binder!!.recycler
                           createInnerAdapter(innerRecycler, entity?.list as ArrayList<FoodEntity>)

                       }
                   }
            )

            adapter .apply {
                recyclerView.adapter = this
            }

        })
    }

    fun navFunction(view: View, fragmentId: Int, bundle: Bundle){
        Navigation.findNavController(view).navigate(fragmentId, bundle)
    }

    override fun onItemClick(view: View?, id: Int) {
        Loger.log("onItemClick, id $id")
    }
    fun createInnerAdapter(recyclerView: RecyclerView, list: ArrayList<FoodEntity>){
        val adapter= FoodPositionAdapter(list,object : FoodPositionAdapter.RecyclerCallback<ItemOneEatBinding, FoodEntity>{
            override fun bind(binder: ItemOneEatBinding, food: FoodEntity) {
                binder.food=food
            }
        })
        recyclerView.adapter=adapter
    }
}