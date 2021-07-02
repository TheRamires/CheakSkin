package ru.skinallergic.checkskin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.BaseViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TriggersViewModel;
import ru.skinallergic.checkskin.components.news.NewsViewModel;
import ru.skinallergic.checkskin.components.news.pojo.Datum;
import ru.skinallergic.checkskin.components.tests.viewModels.TestResultViewModel;
import ru.skinallergic.checkskin.databinding.ActivityMainBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.entrance.EntranceActivity;
import ru.skinallergic.checkskin.entrance.pojo.Datass;
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl;
import ru.skinallergic.checkskin.splash_screen.ConverterString;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static ru.skinallergic.checkskin.InternetConnectingKt.hasConnection;
import static ru.skinallergic.checkskin.utils.UtilsKt.NEWS_BUNDLE;
import static ru.skinallergic.checkskin.utils.UtilsKt.PROFILE_BUNDLE;
import static ru.skinallergic.checkskin.utils.UtilsKt.SPLASH_BUNDLE;
//Ramires //ramires
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AccountViewModelImpl accountViewModel;
    private NewsViewModel newsViewModel;
    private TestResultViewModel resultViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);

        MyViewModelFactory viewModelFactory = App.instance.getAppComponent().getViewModelFactory();
        accountViewModel= new ViewModelProvider(this,viewModelFactory).get(AccountViewModelImpl.class);
        resultViewModel =new ViewModelProvider(this,viewModelFactory).get(TestResultViewModel.class);
        newsViewModel=new ViewModelProvider(this,viewModelFactory).get(NewsViewModel.class);
        BaseViewModel baseViewModelByHealthy=new ViewModelProvider(this,viewModelFactory).get(AffectedAreaCommonViewModel.class);
        binding.setBaseViewModelByHealthy(baseViewModelByHealthy);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
        DateViewModel dateViewModel=new ViewModelProvider(this,viewModelFactory).get(DateViewModel.class);

        //**********************************************************************************
        Loger.log("//main activity loadAccesToken ********************** "+accountViewModel.loadAccesToken());
        //**********************************************************************************

        if (!hasConnection(this)){
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_LONG).show();
        }

        //Получить id пользоавтеля ----------------------------------------------------
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String id=extras.getString("id");
        }

        //Получение Bundle из Splash Activity -----------------------------------------
        try {
            Bundle bundle=getIntent().getExtras().getBundle(SPLASH_BUNDLE);
            String newsJson=bundle.getString(NEWS_BUNDLE);
            String profileJson=bundle.getString(PROFILE_BUNDLE);
            List<Datum> news=ConverterString.INSTANCE.stringToNews(newsJson);
            Datass profile= ConverterString.INSTANCE.stringToProfile(profileJson);

            Loger.log("profileJson "+profileJson);
            newsViewModel.newsListLive.setValue(news);

            Loger.log("profile "+profile);
            accountViewModel.getCurrentUser().setName(profile.getName());
            accountViewModel.getCurrentUser().setRegionId(profile.getRegion().getId());
            accountViewModel.getCurrentUser().setDiagnosisId(profile.getDiagnosis().getId());
            accountViewModel.getCurrentUser().setTel(profile.getTel());
            accountViewModel.getCurrentUser().setGender(profile.getGender());
        } catch (Throwable throwable){}
        //------------------------------------------------------------------------------

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Locale locale = getResources().getConfiguration().locale;
        locale.setDefault(new Locale("ru","RU"));

        binding.setViewModel(dateViewModel);
        dateViewModel.setCurrentDate();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        resultViewModel.getGoToVerificationNumber().observe(this,
                (Boolean go)->{
                    toEntrance();
                    resultViewModel.getGoToVerificationNumber().setValue(false);
                });

        accountViewModel.getExpiredRefreshToken().observe(this,
                (Boolean isExpired)-> {
                    if (isExpired){
                        toEntrance();
                        accountViewModel.getExpiredRefreshToken().setValue(false);
                    }});
    }

    private void toEntrance(){
        Intent intent=new Intent(this, EntranceActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void toProfile(View view){
        Navigation.findNavController(view).navigate(R.id.profileFragment);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accountViewModel.getTokenModel().deleteAccesToken();
        //accountViewModel.clearAllSubscribes();
    }
}