package ru.skinallergic.checkskin.components.fooddiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.ItemFoodBinding;

public class SwipeRecyclerAdapterFood extends RecyclerSwipeAdapter<SwipeRecyclerAdapterFood.Item> implements BaseAdapterForDiff<FoodEntity> {
    private final SwipeRecyclerAdapterFood.RecyclerCallback bindingInterface;
    ArrayList<FoodEntity> list;
    FragmentManager fragmentManager;
    SwipeRecyclerAdapterFood.OnSwipeItemClickListener onItemClickListener;
    SwipeRecyclerAdapterFood.DeleteItemClickListener deleteItemClickListener;

    public SwipeRecyclerAdapterFood(
            FragmentManager fragmentManager,
            ArrayList<FoodEntity> list,
            SwipeRecyclerAdapterFood.RecyclerCallback bindingInterface){
        this.bindingInterface = bindingInterface;
        this.list=list;
        this.fragmentManager=fragmentManager;
    }

    public void setOnItemClickListener(SwipeRecyclerAdapterFood.OnSwipeItemClickListener listener){
        onItemClickListener=listener;
    }

    public void setDeleteItemClickListener(SwipeRecyclerAdapterFood.DeleteItemClickListener deleteItemClickListener){
        this.deleteItemClickListener=deleteItemClickListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemFoodBinding binding=ItemFoodBinding.inflate(inflater,parent,false);
        return new SwipeRecyclerAdapterFood.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeRecyclerAdapterFood.Item holder, int position) {
        FoodEntity item=list.get(position);
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
                /*if (xvel<-300){
                    deleteDialog(holder, item, position, item.getId());
                }*/
            }
        });

        holder.binding.deleteReminder.setOnClickListener((View v)-> {
            deleteDialog(holder, item, position, item.getId());

        });

       /* holder.binding.clickable.setOnClickListener((View v)-> {
            onItemClickListener.onItemClick(v, item.getId());

        });
*/

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

    @NotNull
    @Override
    public ArrayList<FoodEntity> getList() {
        return list;
    }

    @Override
    public void setList(@NotNull ArrayList<FoodEntity> list) {
        this.list=list;
    }

    @Override
    public void addData(FoodEntity myData) {
        this.list.add(myData);
        notifyItemInserted(list.indexOf(myData));
    }

    @Override
    public void removeData(FoodEntity myData) {
        int index=list.indexOf(myData);
        list.remove(index);
        notifyItemRemoved(index);
    }

    public class Item extends RecyclerView.ViewHolder{
        ItemFoodBinding binding;
        public SwipeLayout swipeLayout;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
            swipeLayout=binding.swipe;
        }
        public void bindData(FoodEntity entity){
            bindingInterface.bind(binding,entity);
            binding.executePendingBindings();
        }
    }

    public interface RecyclerCallback{
        void bind(ItemFoodBinding binder, FoodEntity entity);
    }

    public void deleteDialog(SwipeRecyclerAdapterFood.Item holder, FoodEntity item, int position, int id){
        ActionFunction positive1= ()-> {
            deleteFunction(holder, item, position, item.getId());
        };
       /* ActionFunction positive2= ()-> {
            offFunction(holder, item, position, item.getId());
        };*/
        ActionFunction negative= ()-> {

        };
        NavigationFunction stump=()->{};
        //DialogThreeDeleteFunctionFragment dialog=new DialogThreeDeleteFunctionFragment("Удалить",negative,positive1,positive2,stump);
        DialogTwoFunctionFragment dialog=new DialogTwoFunctionFragment("Удалить",negative,positive1,stump);
        dialog.show(fragmentManager,"dialog");
    }

    public void deleteFunction(SwipeRecyclerAdapterFood.Item viewHolder, FoodEntity entity, int position, int id){
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        if (position<list.size()){
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            mItemManger.closeAllItems();
            //viewModel.deletePosition(id);

            deleteItemClickListener.onItemDelete(id);
        }
    }

    public interface OnSwipeItemClickListener{
        void onItemClick(View view, int id);
    }
    public interface DeleteItemClickListener{
        void onItemDelete(int id);
    }
}
