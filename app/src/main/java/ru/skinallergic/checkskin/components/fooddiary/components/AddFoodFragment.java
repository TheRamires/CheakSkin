package ru.skinallergic.checkskin.components.fooddiary.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentAddFoodBinding;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AddViewModel;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel;

public class AddFoodFragment extends Fragment {
    private String[] array;
    private AddViewModel viewModel;
    private FragmentAddFoodBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        array=getResources().getStringArray(R.array.array_teme_of_meals);
        viewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);
        FoodDiaryViewModel foodDiaryViewModel=new ViewModelProvider(requireActivity()).get(FoodDiaryViewModel.class);
        DateViewModel dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        binding=FragmentAddFoodBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        View view=binding.getRoot();
        dateViewModel.dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=dateViewModel.getDate(d);
            binding.date.setText(date);
            viewModel.date=date;
        });

        Spinner spinner = binding.spinner;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.contact_spinner_row_nothing_selected) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(array);

        spinner.setAdapter(adapter);
        try {
            int position=getArguments().getInt("position");
            spinner.setSelection(position);
        }
        catch (NullPointerException exception){
            spinner.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch.
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==4){
                    viewModel.timeOfMeal="";
                } else {
                    viewModel.timeOfMeal=array[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void editName(CharSequence s,int start, int count, int after){
        viewModel.name=s.toString();
    }
    public void editWeight(CharSequence s,int start, int count, int after){
        viewModel.weight=s.toString();
    }
    public void addPosition(View view){
        if (viewModel.validatePos()){
            viewModel.addposition()
            .subscribe((Boolean isSaved)->{
                //Clean edit Texts
                binding.editName.setText("");
                binding.editWeight.setText("");
                Toast.makeText(getContext(),"Позиция добавлена", Toast.LENGTH_LONG).show();
            });

        } else {
            Loger.log("Заполните все поля");
            Toast.makeText(getContext(),"Заполните все поля", Toast.LENGTH_LONG).show();
        }
    }
}