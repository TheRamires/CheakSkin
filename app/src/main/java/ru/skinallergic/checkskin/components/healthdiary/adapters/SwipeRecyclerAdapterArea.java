package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.ItemAreaSwipeBinding;

public class SwipeRecyclerAdapterArea extends RecyclerSwipeAdapter<SwipeRecyclerAdapterArea.Item> {
    private final RecyclerCallback bindingInterface;
    List<Rash> list;
    FragmentManager fragmentManager;
    AffectedAreaCommonViewModel viewModel;

    public SwipeRecyclerAdapterArea(AffectedAreaCommonViewModel viewModel,
                                    FragmentManager fragmentManager,
                                    List<Rash> list,
                                    RecyclerCallback bindingInterface){
        this.bindingInterface = bindingInterface;
        this.list=list;
        this.fragmentManager=fragmentManager;
        this.viewModel=viewModel;
    }

    @NonNull
    @Override
    public SwipeRecyclerAdapterArea.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemAreaSwipeBinding binding=ItemAreaSwipeBinding.inflate(inflater,parent,false);
        return new SwipeRecyclerAdapterArea.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeRecyclerAdapterArea.Item holder, int position) {
        Rash item=list.get(position);
        holder.bindData(item);

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //dari kanan
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wraper));

        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                ActionFunction positive= ()-> {
                    deleteFunction(holder, item, position, item.getId());
                };
                ActionFunction negative= ()-> {

                };
                NavigationFunction stump=()->{};

                if (xvel<-300){
                    DialogTwoFunctionFragment dialog=new DialogTwoFunctionFragment("Удалить",negative,positive,stump);
                    dialog.show(fragmentManager,"dialog");
                }
            }
        });


        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class Item extends RecyclerView.ViewHolder{
        ItemAreaSwipeBinding binding;
        public SwipeLayout swipeLayout;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
            swipeLayout=binding.swipe;
        }
        public void bindData(Rash entity){
            bindingInterface.bind(binding,entity);
            binding.executePendingBindings();
        }
    }

    public interface RecyclerCallback{
        void bind(ItemAreaSwipeBinding binder, Rash entity);
    }
    public void deleteFunction(Item viewHolder, Rash entity, int position, int id){
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        if (position<list.size()){
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            mItemManger.closeAllItems();
            viewModel.delete(id, entity.getArea(), entity.getView());
        }
    }
}