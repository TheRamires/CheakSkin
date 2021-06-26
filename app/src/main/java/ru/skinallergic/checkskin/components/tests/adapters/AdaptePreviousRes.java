package ru.skinallergic.checkskin.components.tests.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.skinallergic.checkskin.components.tests.data.TestResult;
import ru.skinallergic.checkskin.databinding.ItemPreviousBinding;

public class AdaptePreviousRes extends RecyclerView.Adapter<AdaptePreviousRes.Item> {
    private List<TestResult> list;

    public AdaptePreviousRes(List<TestResult> list){
        this.list=list;
    }

    @NonNull
    @Override
    public AdaptePreviousRes.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemPreviousBinding binding=ItemPreviousBinding.inflate(inflater,parent,false);
        return new AdaptePreviousRes.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptePreviousRes.Item holder, int position) {
        TestResult entity=list.get(position);
        holder.binding.setEntity(entity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Item extends RecyclerView.ViewHolder{
        ItemPreviousBinding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}