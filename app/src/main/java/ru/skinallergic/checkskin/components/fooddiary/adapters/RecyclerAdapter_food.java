package ru.skinallergic.checkskin.components.fooddiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.components.fooddiary.data.EntityFoodPosition;
import ru.skinallergic.checkskin.databinding.ItemFoodBinding;

import java.util.List;

public class RecyclerAdapter_food extends RecyclerView.Adapter<RecyclerAdapter_food.ItemList> {
    private List<EntityFoodPosition> list;
    private Boolean crosVisible=false;

    public RecyclerAdapter_food(List<EntityFoodPosition> list){
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerAdapter_food.ItemList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemFoodBinding binding=ItemFoodBinding.inflate(inflater,parent,false);
        return new RecyclerAdapter_food.ItemList(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_food.ItemList holder, int position) {
        holder.binding.name.setText(list.get(position).getName());
        holder.binding.weight.setText(list.get(position).getWeight());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemList extends RecyclerView.ViewHolder{
        ItemFoodBinding binding;

        public ItemList(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}