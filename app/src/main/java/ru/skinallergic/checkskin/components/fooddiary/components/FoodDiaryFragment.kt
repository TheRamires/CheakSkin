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
import ru.skinallergic.checkskin.components.fooddiary.adapters.BaseAdapterForDiff
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.adapters.SwipeRecyclerAdapterFood
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel
import ru.skinallergic.checkskin.components.healthdiary.adapters.SwipeRecyclerAdapterReminder
import ru.skinallergic.checkskin.components.healthdiary.components.reminders.RemindersFragment
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity
import ru.skinallergic.checkskin.databinding.FragmentFoodDiary2Binding
import ru.skinallergic.checkskin.databinding.ItemFoodBinding
import ru.skinallergic.checkskin.databinding.SwipeLayoutBinding

class FoodDiaryFragment : BaseFoodFragment(), SwipeRecyclerAdapterFood.OnSwipeItemClickListener, SwipeRecyclerAdapterFood.DeleteItemClickListener {
    lateinit var buttonProfile: ImageButton
    lateinit var buttonDate: ImageButton
    lateinit var buttonAllergy: ImageButton
    lateinit var buttonAdd: ViewGroup
    lateinit var recycler: RecyclerView
    lateinit var adapter :SwipeRecyclerAdapterFood
    val foodDiaryViewModel by lazy { ViewModelProvider(this, viewModelFactory).get(FoodDiaryViewModel::class.java) }

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

        adapter = SwipeRecyclerAdapterFood( parentFragmentManager, arrayListOf()
        ) { binder: ItemFoodBinding, entity: FoodEntity ->
            binder.entity = entity
            binder.containerLayout.setOnClickListener { view: View? ->
                val bundle = Bundle()
                bundle.putInt(RemindersFragment.BUNDLE_ID_OF_REMIND, entity.id)
                Loger.log("positionId for bundle " + entity.id)
                //navFunction(view, R.id.action_remindersFragment3_to_remindersDetailFragment, bundle)
            }
        }

        adapter .apply {
            setOnItemClickListener(this@FoodDiaryFragment)
            setDeleteItemClickListener(this@FoodDiaryFragment)
            recycler.adapter = this
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

        foodDiaryViewModel.getFoodDiaryByDate(dateViewModel.dateUnix).observe(viewLifecycleOwner, {list->

            val diffUtil = list?.let { DiffUtilFromMySelf<FoodEntity?>(adapter.list, it) }
            diffUtil?.calculate(adapter)

        })
    }

    fun navFunction(view: View?, fragmentId: Int, bundle: Bundle){

    }

    override fun onItemClick(view: View?, id: Int) {
        Loger.log("onItemClick, id $id")
    }

    override fun onItemDelete(id: Int) {
        Loger.log("onItemDelete, id $id")
    }
}