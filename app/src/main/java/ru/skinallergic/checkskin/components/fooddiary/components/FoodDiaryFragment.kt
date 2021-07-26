package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.FoodPositionAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.SwipeRecyclerAdapterFood
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel
import ru.skinallergic.checkskin.databinding.FragmentFoodDiary2Binding
import ru.skinallergic.checkskin.databinding.ItemFoodBinding
import ru.skinallergic.checkskin.databinding.ItemOneEatBinding

class FoodDiaryFragment : BaseFoodFragment(), SwipeRecyclerAdapterFood.OnSwipeItemClickListener, SwipeRecyclerAdapterFood.DeleteItemClickListener {
    lateinit var buttonProfile: ImageButton
    lateinit var buttonDate: ImageButton
    lateinit var buttonAllergy: ImageButton
    lateinit var buttonAdd: ViewGroup
    lateinit var recycler: RecyclerView
    lateinit var adapter :SwipeRecyclerAdapterFood
    ////////////////////////////////////////////////// it was "this"  = owner
    val foodDiaryViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(FoodDiaryViewModel::class.java) }

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
            recycler=recyclerView
        }

        /*adapter = SwipeRecyclerAdapterFood( parentFragmentManager, arrayListOf()
        ) { binder: ItemFoodBinding, entity: FoodMealForMain ->
            binder.entity = entity
            binder.containerLayout.setOnClickListener { view: View ->
                val bundle = Bundle()
                bundle.putInt(BUNDLE_ID_OF_REMIND, entity.id)
                Loger.log("positionId for bundle " + entity.id)
                navFunction(view, R.id.action_remindersFragment3_to_remindersDetailFragment, bundle)
            }
        }

        adapter .apply {
            setOnItemClickListener(this@FoodDiaryFragment)
            setDeleteItemClickListener(this@FoodDiaryFragment)
            recycler.adapter = this
        }*/

        return view
    }

    override fun onStart() {
        super.onStart()
        subscribeDate()
        buttonProfile.setOnClickListener { Navigation.findNavController(it).navigate(R.id.profileFragment)}
        buttonDate.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodDiaryFragment_to_foodCalendarFragment)}
        buttonAllergy.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodDiaryFragment_to_allergicListFragment)}
        buttonAdd.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_foodDiaryFragment_to_addFoodFragment2) }

        foodDiaryViewModel.getFoodDiaryByDate(dateViewModel.dateUnix).observe(viewLifecycleOwner, {list->
            for (pos in list){
                println("${pos?.created} ${pos?.list}")
            }

            /*val diffUtil = list?.let { DiffUtilFromMySelf<FoodMealForMain?>(adapter.list, it) }
            diffUtil?.calculate(adapter)*/

            adapter = SwipeRecyclerAdapterFood( parentFragmentManager, list as java.util.ArrayList<FoodMealForMain>?
            ) { binder: ItemFoodBinding, entity: FoodMealForMain ->
                binder.entity = entity
                binder.clickable.setOnClickListener { view: View ->
                    val bundle = Bundle()
                    bundle.putInt(BUNDLE_ID_OF_REMIND, entity.id)
                    Loger.log("positionId for bundle " + entity.id)
                    navFunction(view, R.id.action_foodDiaryFragment_to_detailFoodFragment, bundle)
                }
                val innerRecycler=binder.recycler
                createInnerAdapter(innerRecycler, entity.list as ArrayList<FoodEntity>)
            }

            adapter .apply {
                setOnItemClickListener(this@FoodDiaryFragment)
                setDeleteItemClickListener(this@FoodDiaryFragment)
                recycler.adapter = this
            }

        })
    }

    fun navFunction(view: View, fragmentId: Int, bundle: Bundle){
        Navigation.findNavController(view).navigate(fragmentId, bundle)
    }

    override fun onItemClick(view: View?, id: Int) {
        Loger.log("onItemClick, id $id")
    }

    override fun onItemDelete(id: Int) {
        Loger.log("onItemDelete, id $id")
    }

    fun createInnerAdapter(recyclerView: RecyclerView, list: ArrayList<FoodEntity>){
        val adapter=FoodPositionAdapter(list,object : FoodPositionAdapter.RecyclerCallback<ItemOneEatBinding, FoodEntity>{
            override fun bind(binder: ItemOneEatBinding, food: FoodEntity) {
                binder.food=food
            }
        })
        recyclerView.adapter=adapter
    }
}