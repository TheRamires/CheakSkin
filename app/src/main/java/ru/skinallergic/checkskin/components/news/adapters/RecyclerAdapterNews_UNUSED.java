package ru.skinallergic.checkskin.components.news.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.news.pojo.Datum;
import ru.skinallergic.checkskin.databinding.ItemNews2Binding;

import java.util.List;

public class RecyclerAdapterNews_UNUSED extends RecyclerView.Adapter<RecyclerAdapterNews_UNUSED.Item> {
    private List<Datum> list;

    public RecyclerAdapterNews_UNUSED(List<Datum> list){
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerAdapterNews_UNUSED.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemNews2Binding binding=ItemNews2Binding.inflate(inflater,parent,false);
        return new RecyclerAdapterNews_UNUSED.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterNews_UNUSED.Item holder, int position) {
        holder.binding.setEntity(list.get(position));
        holder.binding.cardView.setOnClickListener((View v)-> {
            Bundle bundle=new Bundle();
            bundle.putInt("idPosition",list.get(position).getId());

            Navigation.findNavController(v).navigate(R.id.action_navigation_news_to_newsOneFragment,bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Item extends RecyclerView.ViewHolder{
        ItemNews2Binding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}