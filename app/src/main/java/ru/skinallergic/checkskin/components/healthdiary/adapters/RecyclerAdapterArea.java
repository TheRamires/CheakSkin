package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash;
import ru.skinallergic.checkskin.databinding.ItemAreaBinding;

public class RecyclerAdapterArea extends RecyclerView.Adapter<RecyclerAdapterArea.Item> {
    private final RecyclerCallback bindingInterface;
    List<Rash> list;

    public RecyclerAdapterArea(List<Rash> list, RecyclerCallback bindingInterface){
        this.bindingInterface = bindingInterface;
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerAdapterArea.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemAreaBinding binding=ItemAreaBinding.inflate(inflater,parent,false);
        return new RecyclerAdapterArea.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterArea.Item holder, int position) {
        Rash item=list.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Item extends RecyclerView.ViewHolder{
        ItemAreaBinding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
        public void bindData(Rash entity){
            bindingInterface.bind(binding,entity);
            binding.executePendingBindings();
        }
    }

    public interface RecyclerCallback{
        public void bind(ItemAreaBinding binder, Rash entity);
    }
}