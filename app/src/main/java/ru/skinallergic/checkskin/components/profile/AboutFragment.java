package ru.skinallergic.checkskin.components.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ru.skinallergic.checkskin.databinding.FragmentAboutBinding;

import static ru.skinallergic.checkskin.utils.UtilsKt.AXAS;
import static ru.skinallergic.checkskin.utils.UtilsKt.GOOGLE_PLAY;
import static ru.skinallergic.checkskin.utils.UtilsKt.POLITICS;

public class AboutFragment extends Fragment {
    private FragmentAboutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAboutBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void contactTheDevelopers(View view){
        Uri address = Uri.parse(AXAS);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);
    }
    public void feedback(View view){
        Uri address = Uri.parse(GOOGLE_PLAY);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);

    }
    public void politics(View view){
        Uri address = Uri.parse(POLITICS);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);

    }
    public void termsOfUuse(View view){
        Uri address = Uri.parse(POLITICS);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);

    }
}