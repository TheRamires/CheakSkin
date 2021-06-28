package ru.skinallergic.checkskin.components.fooddiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import ru.skinallergic.checkskin.components.fooddiary.data.EntityFoodPosition;
import ru.skinallergic.checkskin.databinding.ItemFoodRedactBinding;

import java.util.List;

public class RecyclerAdapter_redact extends RecyclerView.Adapter<RecyclerAdapter_redact.Item> {
    private List<EntityFoodPosition> list;

    public RecyclerAdapter_redact (List<EntityFoodPosition> list){
        this.list=list;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemFoodRedactBinding binding=ItemFoodRedactBinding.inflate(inflater,parent,false);
        return new RecyclerAdapter_redact.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.binding.name.setText(list.get(position).getName());
        holder.binding.weight.setText(list.get(position).getWeight());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Item extends RecyclerView.ViewHolder {
        ItemFoodRedactBinding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
