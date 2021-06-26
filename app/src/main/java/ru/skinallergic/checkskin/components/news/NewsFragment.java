package ru.skinallergic.checkskin.components.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.components.news.adapters.ProductDiffUtilCallback;
import ru.skinallergic.checkskin.components.news.pagedLibrary.NewsAdapter_UNUSED;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.Adapters.MyRecyclerAdapter;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerCallback;
import ru.skinallergic.checkskin.components.news.adapters.EndlessRecyclerViewScrollListener;
import ru.skinallergic.checkskin.components.news.pojo.Datum;
import ru.skinallergic.checkskin.databinding.ItemNews2Binding;
import ru.skinallergic.checkskin.databinding.NewsFragmentBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsFragment extends Fragment implements MyRecyclerAdapter.OnItemClickListener, NewsAdapter_UNUSED.BindInterface<ItemNews2Binding> {
    private NewsViewModel viewModel;
    private DateViewModel dateViewModel;
    private NewsFragmentBinding binding;
    private EditText searchEdit;
    private Observable<Object> observable;
    final private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private List<Datum> allData=new ArrayList<>();
    private EndlessRecyclerViewScrollListener scrollListener;
    private String search;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter<Datum, ItemNews2Binding> adapter;
    private int scrollPosition;
    private PagedList<Datum> lastNews=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(NewsViewModel.class);
        search="";
        viewModel.getNewsList(search,true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Loger.log("onCreateView");

        binding = NewsFragmentBinding.inflate(inflater);
        View view = binding.getRoot();
        searchEdit= binding.searchEditText;
        binding.setViewModel(viewModel);

        dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        binding.date.setText(dateViewModel.getDate());

        recyclerView=binding.recycler;
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter=new MyRecyclerAdapter<>(allData, R.layout.item_news_2,
                new RecyclerCallback<ItemNews2Binding, Datum>() {
            @Override
            public void bind(ItemNews2Binding binder, Datum entity) {
                binder.setEntity(entity);
                binder.layout.setClipToOutline(true);
                picasso(entity.getCover(),
                        binder.image);
            }
        });

        adapter.setPositionListener(this);
        recyclerView.setAdapter(adapter);

        if (lastNews!=null){
            fillingInTheList(lastNews);
        } else pagingListSubscribe(viewModel.pagedListLiveData);
        //pagingListSubscribe(viewModel.pagedListLiveData);

        //------------------------------------------------------------------------
        /*MyDiffUtilCallback diffUtilCallback=new MyDiffUtilCallback();
        newsAdapter= new NewsAdapter(diffUtilCallback);
        newsAdapter.setBindingInterface(this);

        recyclerView.setAdapter(newsAdapter);*/

        /*viewModel.pagedListLiveData.observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<PagedList<Datum>>() {
            @Override
            public void onChanged(PagedList<Datum> data) {
                Loger.log("pagedListLiveData "+data);
                //newsAdapter.submitList(data);
                ProductDiffUtilCallback productDiffUtilCallback =
                        new ProductDiffUtilCallback(adapter.getData(), data);
                DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
                adapter.setData(data);
                productDiffResult.dispatchUpdatesTo(adapter);
            }
        });*/

        //------------------------------------------------------------------------

        //subscribeNews(viewModel.newsListLive);

        observable=Observable.create(emitter -> {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (!emitter.isDisposed()) { //если еще не отписались
                        emitter.onNext(editable.toString()); //отправляем текущее состояние
                    }
                }
            };
            emitter.setCancellable(() -> searchEdit.removeTextChangedListener(watcher)); //удаляем листенер при отписке от observable
            searchEdit.addTextChangedListener(watcher);
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBtn();
            }
        });

        //For endlessRecycler-------------------------------------------------------------------------
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Loger.log("onLoadMore. Search ="+search+". " +"Page = "+page);
                viewModel.getNewsList(search, page,false);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        return view;
    }

    public void searchBtn(){
        if (searchEdit.getVisibility()==View.GONE){

            binding.searchEditText.setVisibility(View.VISIBLE);
            binding.searchEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager)requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            observable.debounce(0, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Object o) {
                            String text=o.toString();
                            if (text.length()>=3) {
                                Loger.log("3"+text);
                                search=text;
                                pagingListSubscribe(
                                        viewModel.getNewsList(search,false)
                                );
                            } else if (text.length()<1){
                                Loger.log("1"+text);
                                search="";
                                pagingListSubscribe(
                                        viewModel.getNewsList(search,false)
                                );
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            compositeDisposable.clear();
                        }

                        @Override
                        public void onComplete() {
                            compositeDisposable.clear();
                        }
                    });
        } else {
            binding.searchEditText.setVisibility(View.GONE);
            compositeDisposable.clear();
        }
    }

    private void subscribeNews(MutableLiveData<List<Datum>> liveData){
        liveData.observe(getViewLifecycleOwner(),list->{
            if (list==null){return;}
            allData.addAll(list);

            ProductDiffUtilCallback productDiffUtilCallback =
                    new ProductDiffUtilCallback(adapter.getData(), allData);
            DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
            adapter.setData(allData);
            productDiffResult.dispatchUpdatesTo(adapter);

            if (scrollPosition!=0){
                recyclerView.scrollToPosition(scrollPosition);
            }
            //viewModel.newsListLive.setValue(null);

            int curSize = adapter.getItemCount();
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    productDiffResult.dispatchUpdatesTo(adapter);

                    adapter.notifyItemRangeInserted(curSize, allData.size() - 1);
                }
            });
        });
        allData.clear();
        liveData.setValue(null);
    }
    private void picasso(String url, ImageView imageView){
        Picasso.with(requireContext())
                .load(url)
                .placeholder(R.drawable.no_photo)
                //.error(R.drawable.my_icon_news_1)
                .into(imageView);
    }

    @Override
    public void onDestroyView() {
        Loger.log("onDestroyView");
        super.onDestroyView();
        compositeDisposable.clear();
        allData.clear();
        scrollPosition= ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

    }

    @Override
    public void onItemClick(View view, int id, int toLayout) {
        Bundle bundle=new Bundle();
        bundle.putInt("idPosition",id);
        Navigation.findNavController(view).navigate(toLayout, bundle);
    }

    @Override
    public void bind(@org.jetbrains.annotations.Nullable Datum entity, @NotNull ItemNews2Binding binder) {
        binder.setEntity(entity);
        binder.layout.setClipToOutline(true);
        picasso(entity.getCover(),binder.image);
    }

    private void pagingListSubscribe(LiveData<PagedList<Datum>> liveData){
        liveData.observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<PagedList<Datum>>() {
            @Override
            public void onChanged(PagedList<Datum> data) {
                if (data.size()!=0){
                    fillingInTheList(data);
                }
            }
        });
    }
    private void fillingInTheList(PagedList<Datum> data){
        Loger.log("pagedListLiveData "+data);
        //newsAdapter.submitList(data);
        ProductDiffUtilCallback productDiffUtilCallback =
                new ProductDiffUtilCallback(adapter.getData(), data);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        adapter.setData(data);
        productDiffResult.dispatchUpdatesTo(adapter);
        lastNews=data;
    }
}