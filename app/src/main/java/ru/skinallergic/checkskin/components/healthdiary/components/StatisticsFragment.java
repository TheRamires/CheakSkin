package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;
import java.util.List;

import ru.skinallergic.checkskin.Adapters.MyRecyclerAdapter;
import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityStatistic;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StatisticsViewModel;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerCallback;
import ru.skinallergic.checkskin.databinding.FragmentStatisticsBinding;
import ru.skinallergic.checkskin.databinding.ItemStatisticBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.entrance.EntranceActivity;
import ru.skinallergic.checkskin.view_models.DateViewModel;

public class StatisticsFragment extends Fragment {
    private StatisticsViewModel viewModel;
    private DateViewModel dateViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        dateViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(StatisticsViewModel.class);
        viewModel.createStartDateLive(
                dateViewModel.dateLive.getValue());
        viewModel.createEndDateLive(
                dateViewModel.dateLive.getValue());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStatisticsBinding binding=FragmentStatisticsBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        binding.setViewModel(viewModel);

        Button btnDateStart= binding.dateStart;
        Button btnDateEnd=binding.dateEnd;


        btnDateStart.setText(stringBuild("c",viewModel.getStart()));
        btnDateEnd.setText(stringBuild("по",viewModel.getEnd()));

        btnDateStart.setOnClickListener((View v)-> {
            calendarListener(btnDateStart, "c");
        });

        btnDateEnd.setOnClickListener((View v)->{
            calendarListener(btnDateEnd, "по");
        });

        subscribeMediator(viewModel.getMediatorLiveData());
        RecyclerView recyclerView=binding.recycler;
        subscribeStatistic(viewModel.getStatistic(),recyclerView);
        subscribeExpiredRefreshToken(viewModel.getExpiredRefreshToken());

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    private String stringBuild(String position, String date){
        StringBuilder sb=new StringBuilder();
        sb.append(position).append(" ").append(date);
        return sb.toString();
    }

    private void calendarListener(Button view, String position){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerThemeSmall calendar = new DatePickerThemeSmall();
                calendar.show(requireActivity().getSupportFragmentManager(), "theme2");
                calendar.setCalendarListener(new DatePickerThemeSmall.CalendarListener() {
                    @Override
                    public void getDate(String date) {
                        String dateWithMin;
                        if (v.getId()==R.id.date_end){
                            dateWithMin=viewModel.endWithMin(date);
                        } else dateWithMin=viewModel.startWithMin(date);
                        Loger.log(dateWithMin);
                        view.setText(
                                stringBuild(position,date)
                        );
                        Loger.log("getDate "+dateWithMin);
                        Date formattedDate=dateViewModel.simpleFormattingToDateWithMin(dateWithMin);
                        Loger.log("getDate "+formattedDate);
                        switchDate(v,formattedDate);
                    }
                });
            }
        });
    }

    public void switchDate(View v, Date formattedDate ){
        switch (v.getId()){
            case R.id.date_start:
                Loger.log("date_start "+formattedDate);
                viewModel.getDateStart().setValue(formattedDate);
                break;
            case R.id.date_end:
                Loger.log("date_end "+formattedDate);
                viewModel.getDateEnd().setValue(formattedDate);
                break;
        }
    }
    public void toEntrance() {
        Intent intent = new Intent(requireActivity(), EntranceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void subscribeMediator(MediatorLiveData<Boolean> mediatorLiveData){
        mediatorLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    viewModel.getMediatorLiveData().setValue(false);
                    viewModel.statisticRequest();
                }
            }
        });
    }

    private void subscribeExpiredRefreshToken(MutableLiveData<Boolean> liveData){
        liveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    viewModel.getExpiredRefreshToken().setValue(false);
                    toEntrance();
                }
            }
        });

    }

    private void subscribeStatistic(MutableLiveData<List<EntityStatistic>> liveData, RecyclerView recyclerView){
        liveData.observe(getViewLifecycleOwner(), new Observer<List<EntityStatistic>>() {

            @Override
            public void onChanged(List<EntityStatistic> entityStatistics) {
                Loger.log("statistics "+entityStatistics.size());
                MyRecyclerAdapter<EntityStatistic, ItemStatisticBinding> adapter=new MyRecyclerAdapter<>(
                        entityStatistics, R.layout.item_statistic, new RecyclerCallback<ItemStatisticBinding, EntityStatistic>() {
                    @Override
                    public void bind(ItemStatisticBinding binder, EntityStatistic entity) {
                       /* if (entity.getCount()==0){
                            binder.statisticItem.setVisibility(View.INVISIBLE);
                        }*/

                        Drawable drawable= AppCompatResources.getDrawable(getContext(),R.drawable.smile_01);
                        String name="";
                        switch (entity.getStatus()){
                            case  0:
                                drawable= AppCompatResources.getDrawable(getContext(),R.drawable.smile_00);
                                name="Ремиссия";
                                break;
                            case 1:
                                drawable= AppCompatResources.getDrawable(getContext(),R.drawable.smile_01);
                                name="Подострое состояние (хроническое)";
                                break;
                            case 2:
                                drawable= AppCompatResources.getDrawable(getContext(),R.drawable.smile_02);
                                name="Острое состояние";
                                break;
                        }
                        binder.count.setText(String.valueOf(entity.getCount()));
                        binder.name.setText(name);
                        binder.img.setBackground(drawable);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }
}