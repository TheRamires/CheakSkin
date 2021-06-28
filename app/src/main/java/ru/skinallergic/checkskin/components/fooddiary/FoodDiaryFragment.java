package ru.skinallergic.checkskin.components.fooddiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentFoodDiaryBinding;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerAdapter_food;
import ru.skinallergic.checkskin.components.fooddiary.data.EntityFoodPosition;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel;

import java.util.Date;
import java.util.List;

import static ru.skinallergic.checkskin.components.fooddiary.Util.TIME_OF_MEALS;

public class FoodDiaryFragment extends Fragment {
    private int rawPosition=0;
    private DialogFragment dialogfragment;
    private FoodDiaryViewModel viewModel;
    private RecyclerView recyclerView;
    private MutableLiveData<Integer> rawPositionLive=new MutableLiveData<>();
    private FragmentFoodDiaryBinding binding;
    private String date;
    private DateViewModel dateViewModel;
    public ObservableField<String> dateObservable=new ObservableField<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Loger.log("• onCreateView");
        viewModel = new ViewModelProvider(requireActivity()).get(FoodDiaryViewModel.class);
        dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        date=dateViewModel.getDate();
        viewModel.refreshFoodList(date,TIME_OF_MEALS[rawPosition]);

        binding=FragmentFoodDiaryBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        recyclerView=binding.recycler;

        dateObservable.set(dateViewModel.getDate());

        //календарь--------нужно поулчить Long
        //dialogfragment = new DatePickerTheme(dateViewModel.dateLive);

        viewModel.foodLive.observe(getViewLifecycleOwner(),(List<EntityFoodPosition> list)-> {
                RecyclerView.Adapter adapter=new RecyclerAdapter_food(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
                recyclerView.setHasFixedSize(false);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        });
        dateViewModel.dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=dateViewModel.getDate(d);
            dateObservable.set(date);
            viewModel.refreshFoodList(date,TIME_OF_MEALS[rawPosition]);
        });
        rawPositionLive.observe(getViewLifecycleOwner(), (Integer raw)-> {
            rawPosition=raw;
            String date=dateViewModel.getDate();
            String timeOfMeals=TIME_OF_MEALS[rawPosition];
            Loger.log("rawPositionLive date: "+date+", timeOfMeals: "+timeOfMeals);
            binding.timeOfMeal.setText(timeOfMeals);
            viewModel.refreshFoodList(date, timeOfMeals);
        });
        return view;
    }
    public void clickDate(View view){
        dialogfragment.show(requireActivity().getSupportFragmentManager(), "theme");
    }
    public void add(View view){
        Navigation.findNavController(view).navigate(R.id.addFoodFragment);
    }
    public void toSearch(View view){
        Navigation.findNavController(view).navigate(R.id.searchFoodFragment);
    }
    public void chooseTimeOfMeal(View view){
        switch (view.getId()){
            case R.id.brackfast:
                rawPositionLive.setValue(0);
                break;
            case R.id.snack:
                rawPositionLive.setValue(1);
                break;
            case R.id.lunch:
                rawPositionLive.setValue(2);
                break;
            case R.id.dinner:
                rawPositionLive.setValue(3);
                break;
        }
        String date=dateViewModel.getDate();
        String timeOfMeals=TIME_OF_MEALS[rawPosition];
        Loger.log("viewModel.dateLive date: "+date+", timeOfMeals: "+timeOfMeals);
        binding.date.setText(date);
        viewModel.refreshFoodList(date,timeOfMeals);
    }

    public void toRedact (View view){
        Loger.log("onClick");
        Bundle bundle=new Bundle();
        bundle.putInt("position", rawPosition);
        bundle.putString("date",dateViewModel.getDate());
        bundle.putString("timeOfMeal",TIME_OF_MEALS[rawPosition]);
        Navigation.findNavController(view).navigate(R.id.redactFoodFragment, bundle);
    }
}