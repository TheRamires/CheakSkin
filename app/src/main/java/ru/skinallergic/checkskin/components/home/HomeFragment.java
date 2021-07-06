package ru.skinallergic.checkskin.components.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.BuildConfig;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.news.adapters.SpaceItemDecoration;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.FragmentHomeBinding;
import ru.skinallergic.checkskin.databinding.ItemLpuBinding;
import ru.skinallergic.checkskin.databinding.ItemNewsBinding;
import ru.skinallergic.checkskin.databinding.ItemTestBinding;
import ru.skinallergic.checkskin.entrance.data.UserEntity;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Adapters.MyRecyclerAdapter;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerAdapter_lpu_UNUSED;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerCallback;
import ru.skinallergic.checkskin.components.home.data.LPU;
import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.components.news.NewsViewModel;
import ru.skinallergic.checkskin.components.news.pojo.Datum;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.AccountViewModel;
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyRecyclerAdapter.OnItemClickListener {
    private HomeViewModel homeViewModel;
    private TestsViewModel testsViewModel;
    private NewsViewModel newsViewModel;
    private DateViewModel dateViewModel;
    private AccountViewModel accountViewModel;

    private RecyclerView recyclerNews;
    private RecyclerView recyclerTests;
    private RecyclerView recyclerLpu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DateViewModel mainViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);

        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        testsViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        newsViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(NewsViewModel.class);
        accountViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AccountViewModelImpl.class);
        homeViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(HomeViewModel.class);

        FragmentHomeBinding binding= FragmentHomeBinding.inflate(inflater);
        View view=binding.getRoot();

        dateViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        binding.date.setText(dateViewModel.getDate());
        accountViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                String hello="Привет, "+userEntity.getName()+"!";
                binding.name.setText(hello);
            }
        });

        //--------------------------снова показываем бар
        mainViewModel.barVisibility.set(true);
        binding.setFragment(this);
        HomeViewModel viewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        viewModel.getLPUList();

        recyclerNews=binding.recyclerNews;
        recyclerTests=binding.recyclerTest;
        recyclerLpu=binding.recuclerLpu;

        //Настройка кнопок--------------------------------------------------

        AppCompatButton btnHealth = binding.healthyBtn;
        Drawable icon1 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_home_1);
        Drawable cheakBox1 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon1.setBounds(0, 0, 75, 75);
        cheakBox1.setBounds(0, 0, 45, 45);
        btnHealth.setCompoundDrawables(icon1, null, cheakBox1, null);

        AppCompatButton btnEat=binding.eatBtn;
        Drawable icon2=ContextCompat.getDrawable(getActivity(),R.drawable.my_icon_home_2);
        Drawable cheakBox2 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_red_off);
        icon2.setBounds(0,0,75,75);
        cheakBox2.setBounds(0, 0, 45, 45);
        btnEat.setCompoundDrawables(icon2,null,cheakBox2,null);

        AppCompatButton btnLpu=binding.btnLpu;
        Drawable icon3=ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_home_3);
        icon3.setBounds(0,0,75,75);
        btnLpu.setCompoundDrawables(icon3,null,null,null);
        //------------------------------------------------------------------------------------

        newsViewModel.getNewsList(1,5);
        testsViewModel.getDate();

        subscribeNews(newsViewModel.newsListLive);
        subscribeTests(testsViewModel.getTestsLive());
        subscribeLPU(homeViewModel.lpuLive);

        accountViewModel.logInSave();

        return view;
    }
//ВКЛЮЧИТЬ КНОПКИ-----------------------------------------------------------------------------------

    public void toLPUDoc (View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_LPUDocumentsFrag);
    }
    public void toLPU(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_LPUConteinerFrag);
    }
    public void toHealthyDiary(View view){
        Navigation.findNavController(view).navigate(R.id.navigation_health_diary);
    }
    public void toFoodDiary(View view){
        //Navigation.findNavController(view).navigate(R.id.navigation_food_diary);
    }

//ВКЛЮЧИТЬ КНОПКИ-----------------------------------------------------------------------------------

    private void subscribeNews(LiveData<List<Datum>> liveData){
        liveData.observe(getViewLifecycleOwner(),list->{
            if (list==null){return;}
            MyRecyclerAdapter<Datum, ItemNewsBinding> adapter=new MyRecyclerAdapter<>(
                    list, R.layout.item_news, new RecyclerCallback<ItemNewsBinding, Datum>() {
                @Override
                public void bind(ItemNewsBinding binder, Datum entity) {
                    binder.setEntity(entity);
                    binder.clickable.setClipToOutline(true);
                    binder.imageAlpha.setAlpha(0.6f);
                    picasso(entity.getCover(), binder.image );
                }
            });
            adapter.setPositionListener(this);
            recyclerNews.setAdapter(adapter);
            recyclerNews.addItemDecoration(new SpaceItemDecoration());
            newsViewModel.newsListLive.setValue(null);
        });
    }
    private void subscribeTests(LiveData<List<EntityTest>> liveData){
        liveData.observe(getViewLifecycleOwner(),list->{
            MyRecyclerAdapter<EntityTest, ItemTestBinding> adapter=new MyRecyclerAdapter<>(
                    list, R.layout.item_test, new RecyclerCallback<ItemTestBinding, EntityTest>() {
                @Override
                public void bind(ItemTestBinding binder, EntityTest entity) {
                    binder.setEntity(entity);

                    int image=entity.getImage();
                    //Uri otherPath = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/mipmap-xxxhdpi/"+imageUrl);
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),image);
                    binder.testImage.setImageBitmap(bitmap);
                    binder.setEntity(entity);

                    binder.clickable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putLong("idPosition",entity.getId());
                            Navigation.findNavController(v).navigate(R.id.testsOneFragment, bundle);
                        }
                    });
                }
            });
            adapter.setPositionListener(this);
            recyclerTests.setAdapter(adapter);
           // recyclerTests.addItemDecoration(new SpaceItemDecoration());
            //recyclerTests.addItemDecoration(new SpaceItemDecoration(list.size()-1,false,true));
        });
    }
    private void subscribeLPU(LiveData<List<LPU>> liveData){
        liveData.observe(getViewLifecycleOwner(),list->{
            List<LPU> listLpu= list;
            List<LPU> listLpuPreview=new ArrayList<>(listLpu.subList(0,3));

            MyRecyclerAdapter<LPU, ItemLpuBinding> adapter=new MyRecyclerAdapter<>(
                    listLpuPreview, R.layout.item_lpu, new RecyclerCallback<ItemLpuBinding, LPU>() {

                @Override
                public void bind(ItemLpuBinding binder, LPU entity) {
                    binder.setEntity(entity);
                }

            });
            adapter.setPositionListener(this);
            recyclerLpu.setAdapter(adapter);
        });
    }

    @Override
    public void onItemClick(View view, int id, int toLayout) {
        Bundle bundle=new Bundle();
        bundle.putInt("idPosition",id);
        Navigation.findNavController(view).navigate(toLayout, bundle);
    }

    private void picasso(String url, ImageView imageView){
        Picasso.with(requireContext())
                .load(url)
                .into(imageView);
    }
    public void toProfile(View view){
        Navigation.findNavController(view).navigate(R.id.profileFragment);
    }
}