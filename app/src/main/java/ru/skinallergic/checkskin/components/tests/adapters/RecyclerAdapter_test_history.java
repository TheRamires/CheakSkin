package ru.skinallergic.checkskin.components.tests.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.data.TestResult;
import ru.skinallergic.checkskin.databinding.ItemTestHistoryBinding;

import java.util.List;

public class RecyclerAdapter_test_history extends RecyclerView.Adapter<RecyclerAdapter_test_history.Item> {
    private List<TestResult> list;

    public RecyclerAdapter_test_history(List<TestResult> list){
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerAdapter_test_history.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemTestHistoryBinding binding=ItemTestHistoryBinding.inflate(inflater,parent,false);
        return new RecyclerAdapter_test_history.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_test_history.Item holder, int position) {
        TestResult entity=list.get(position);
        holder.binding.setEntity(entity);
        holder.binding.buttonLayout.setOnClickListener((View v)-> {
            Bundle bundle=new Bundle();
            //bundle.putInt("idPosition",list.get(position).getId());
            bundle.putString("sumResult",entity.getResult());
            Navigation.findNavController(v).navigate(R.id.action_testHistoryFragment_to_testHistoryOneFragment,bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Item extends RecyclerView.ViewHolder{
        ItemTestHistoryBinding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}