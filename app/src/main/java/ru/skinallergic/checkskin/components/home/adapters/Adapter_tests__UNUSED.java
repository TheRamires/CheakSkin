package ru.skinallergic.checkskin.components.home.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.databinding.ItemTestBinding;

import java.util.List;

public class Adapter_tests__UNUSED extends RecyclerView.Adapter<Adapter_tests__UNUSED.Item> {
    private List<EntityTest> list;

    public Adapter_tests__UNUSED(List<EntityTest> list){
        this.list=list;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemTestBinding binding= ItemTestBinding.inflate(inflater,parent,false);
        return new Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.binding.setEntity(list.get(position));
        holder.binding.clickable.setOnClickListener((View v)-> {
            Bundle bundle=new Bundle();
            bundle.putInt("idPosition", (int) list.get(position).getId());
            // ВКЛЮЧИТЬ КНОПКУ--------------------------------------------------------------------
            /*
            Navigation.findNavController(v).navigate(R.id.testsOneFragment,bundle);
            */
            //------------------------------------------------------------------------------------
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        ItemTestBinding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}