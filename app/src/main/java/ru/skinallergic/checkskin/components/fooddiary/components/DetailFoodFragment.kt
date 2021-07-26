package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.FoodPositionAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.FoodPositionAdapter2
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel
import ru.skinallergic.checkskin.databinding.FragmentDetailFoodBinding
import ru.skinallergic.checkskin.databinding.ItemOneEat2Binding
import ru.skinallergic.checkskin.databinding.ItemOneEatBinding

const val BUNDLE_ID_OF_REMIND = "idOfRemind"
const val FROM_SEARCHING="fromSearching"
class DetailFoodFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    lateinit var redactButton: ImageView
    var idPostion: Int?= null
    var fromSearching =false
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FoodPositionAdapter2
    lateinit var binding: FragmentDetailFoodBinding
    val foodDiaryViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(FoodDiaryViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idPostion=arguments?.getInt(BUNDLE_ID_OF_REMIND)
        fromSearching = arguments?.getBoolean(FROM_SEARCHING) == true
        Loger.log("idPostion $idPostion \n fromSearching$fromSearching")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentDetailFoodBinding.inflate(inflater)
        binding.apply {
            backStack=backBtn
            redactButton=redactBtn
            fragment=this@DetailFoodFragment
            recyclerView=recycler
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener { popBack(it) }
        redactButton.setOnClickListener {
            val bundle = Bundle()
            idPostion?.let { it1 -> bundle.putInt(BUNDLE_ID_OF_REMIND, it1) }
            if (fromSearching){bundle.putBoolean(FROM_SEARCHING, true)}
            navFunction(it, R.id.action_detailFoodFragment_to_redactFoodFragment,bundle )
        }
        subscribeDate()

        idPostion?.let {
            var list: List<FoodMealForMain?>?=null
            if (fromSearching){
                list=foodDiaryViewModel.searchingDiaryList.value
            } else {
                list =foodDiaryViewModel.foodDiaryList.value
            }
            list?.let {
                Loger.log("list      ------------------- $it")
                binding.title.text= it[0]?.meal.toString()
                for (foodEntity in list){
                    if (foodEntity?.id == idPostion){
                        displayPosition(foodEntity)
                        break
                    }
                }
            }
        }
    }
    fun displayPosition(foodEntity: FoodMealForMain?){
        foodEntity?.let {
            adapter= FoodPositionAdapter2(it.list as ArrayList<FoodEntity>, object : FoodPositionAdapter2.RecyclerCallback<ItemOneEat2Binding, FoodEntity>{
                override fun bind(binder: ItemOneEat2Binding, food: FoodEntity) {
                    binder.food=food
                }
            })
            recyclerView.adapter=adapter
        }
    }
    fun navFunction(view: View, fragmentId: Int, bundle: Bundle){
        Navigation.findNavController(view).navigate(fragmentId, bundle)
    }
}