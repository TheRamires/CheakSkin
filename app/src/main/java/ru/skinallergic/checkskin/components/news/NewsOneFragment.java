package ru.skinallergic.checkskin.components.news;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.news.pojo.OneNewsEntity;
import ru.skinallergic.checkskin.databinding.FragmentNewsOneBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import us.feras.mdv.MarkdownView;

public class NewsOneFragment extends Fragment {
    public ObservableField<OneNewsEntity> oneNews=new ObservableField<>();
    private FragmentNewsOneBinding binding;
    private NewsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(NewsViewModel.class);
        binding=FragmentNewsOneBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        binding.layout.setClipToOutline(true);

        View view=binding.getRoot();

        String idPosition= getArguments().get("idPosition").toString();
        viewModel.getOneNews(idPosition);
        /*
        List<OneNewsEntity> list=viewModel.getOneNews(idPosition);
        Iterator iterator=list.iterator();
        while (iterator.hasNext()){
            OneNewsEntity entity= (OneNewsEntity) iterator.next();
            if (entity.getId()==idPosition){
                entityNewsObservable.set(entity);
                break;
            }
        }*/
        viewModel.oneNewsLive.observe(getViewLifecycleOwner(), (OneNewsEntity oneNewsEntity)-> {
            if (oneNewsEntity!=null) {
                picasso(Objects.requireNonNull(oneNewsEntity.getData()).getCover(),
                        binding.image);
                MarkdownView descriptionMD=new MarkdownView(getContext());
                descriptionMD.loadMarkdown(oneNewsEntity.getData().getBody());

                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                params.leftMargin=25;
                params.rightMargin=20;
                descriptionMD.setLayoutParams(params);

                binding.layout.addView(descriptionMD,3);
                oneNews.set(oneNewsEntity);
            }
        });

        return view;
    }
    public void backStack (View view){
        Navigation.findNavController(view).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*binding.date.setText("");
        binding.name.setText("");
        binding.date.setText("");
        binding.title.setText("");*/
        //viewModel.oneNewsLive.setValue(null);
    }
    private void picasso(String url, ImageView imageView){
        Picasso.with(requireContext())
                .load(url)
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.my_icon_news_2)
                .into(imageView);
    }
}