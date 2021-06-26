package ru.skinallergic.checkskin.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerCallback;
import ru.skinallergic.checkskin.components.home.data.BaseItem;

import java.util.List;

public class MyRecyclerAdapter<T extends BaseItem, VM extends ViewDataBinding> extends RecyclerView.Adapter<MyRecyclerAdapter.MyHolder>  {
    private List<T> list;
    private final int layoutId;
    private final RecyclerCallback<VM, T> bindingInterface;
    private OnItemClickListener itemListener;

    public void setData(List<T> list){
        this.list=list;
    }
    public List<T> getData(){
        return list;
    }
    public MyRecyclerAdapter(List<T> list, int layoutId, RecyclerCallback<VM,T> binding){
        this.list=list;
        this.layoutId=layoutId;
        this.bindingInterface=binding;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new MyHolder(view);
    }

    @Override
    public int getItemCount() {
        if (list==null){return 0;}
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.MyHolder holder, int position) {
        T item=list.get(position);
        holder.bindData(item);
    }
    public void setPositionListener(OnItemClickListener listener){
        itemListener=listener;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        VM binding;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }
        public void bindData(T entity){
            bindingInterface.bind(binding,entity);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
           // if (v.getId()== R.id.clickable) {
                int toLayout=chooseNavPath(layoutId);

                itemListener.onItemClick(v, list.get(getPosition()).getId(),toLayout);
            //}
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int id,int toLayout);
    }

    private int chooseNavPath(int layoutId){
        int toLayout=0;
        switch (layoutId){
            case R.layout.item_news:
                toLayout=R.id.newsOneFragment;
                break;
            case R.layout.item_test: case R.layout.item_test_version_2:
                //toLayout=R.id.testsOneFragment;        //ВКЛЮЧИТЬ NAVIGATION
                toLayout=R.id.navigation_home;
                break;
            case R.layout.item_lpu:
                toLayout=R.id.LPUDetailedFragment;     //ВКЛЮЧИТЬ NAVIGATION
                //toLayout=R.id.navigation_home;
                break;
            case R.layout.item_news_2:
                toLayout=R.id.action_navigation_news_to_newsOneFragment;
                break;
        }
        return toLayout;
    }
}
