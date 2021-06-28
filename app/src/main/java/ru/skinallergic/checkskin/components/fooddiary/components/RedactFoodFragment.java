package ru.skinallergic.checkskin.components.fooddiary.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentRedactFoodBinding;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.fooddiary.viewModels.RedactViewModel;
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerAdapter_redact;
import ru.skinallergic.checkskin.components.fooddiary.data.EntityFoodPosition;

import java.util.Date;
import java.util.List;

public class RedactFoodFragment extends Fragment {
    private String[] array;
    int position;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DateViewModel dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        RedactViewModel viewModel=new ViewModelProvider(requireActivity()).get(RedactViewModel.class);
        FragmentRedactFoodBinding binding=FragmentRedactFoodBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        View view=binding.getRoot();

        dateViewModel.dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=dateViewModel.getDate(d);
            binding.date.setText(date);
            viewModel.date=date;
        });
        array=getResources().getStringArray(R.array.array_teme_of_meals);

        //Spinner --------------------------------------------------------------------------------
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
        position=getArguments().getInt("position");
        viewModel.timeOfMeal=array[position];
        viewModel.date=getArguments().getString("date");
        Loger.log("••date "+viewModel.date+" timeOfMeal "+viewModel.timeOfMeal);

        spinner.setSelection(position); //set the hint the default selection so it appears on launch.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionOfSpinner, long id) {
                position=positionOfSpinner;
                if (positionOfSpinner==4){
                    viewModel.timeOfMeal="";
                } else {
                    viewModel.timeOfMeal=array[positionOfSpinner];
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //----------------------------------------------------------------------------------------

        RecyclerView recyclerView=binding.recycler;
        viewModel.entityList.observe(getViewLifecycleOwner(), (List<EntityFoodPosition> list)->{

            Loger.log("•list "+list.size());
            RecyclerView.Adapter recyclerAdapter=new RecyclerAdapter_redact(list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            recyclerView.setHasFixedSize(false);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerAdapter.notifyDataSetChanged();

        });
        viewModel.getEntityList();
        return view;
    }

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }

    public void add (View view){
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        Navigation.findNavController(view).navigate(R.id.addFoodFragment,bundle);
    }
}