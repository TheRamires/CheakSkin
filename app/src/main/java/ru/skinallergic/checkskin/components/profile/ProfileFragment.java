package ru.skinallergic.checkskin.components.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentProfileBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.entrance.EntranceActivity;
import ru.skinallergic.checkskin.entrance.helper_classes.RemoveSpacesKt;
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorField;
import ru.skinallergic.checkskin.entrance.helper_classes.ValidatorNumber;
import ru.skinallergic.checkskin.entrance.pojo.Datass;
import ru.skinallergic.checkskin.handlers.ToastyManager;
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl;

import java.util.List;
import java.util.Objects;

import static ru.skinallergic.checkskin.utils.UtilsKt.GENDER_FEMALE;
import static ru.skinallergic.checkskin.utils.UtilsKt.GENDER_MALE;

public class ProfileFragment extends Fragment {
    private Spinner spinner;
    private Spinner spinner2;
    private AccountViewModelImpl accountViewModel;
    private NewUser newUser;
    private FragmentManager manager;
    private NavController navController;
    private ValidatorField validateField;
    private ValidatorNumber validator;
    private NavigationFunction navigateToEntranceActivity;
    private FragmentProfileBinding binding;

    private Boolean regionsComplete=false;
    private Boolean diagnosisComplete=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator=ValidatorNumber.INSTANCE;
        validateField= App.instance.getAppComponent().getValidatorField();

        newUser=new NewUser();
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        accountViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AccountViewModelImpl.class);

        navigateToEntranceActivity=()->{
            Intent intent=new Intent(requireActivity(),EntranceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        };

        manager = requireActivity().getSupportFragmentManager();
        navController=Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(accountViewModel);
        View view=binding.getRoot();

        accountViewModel.setStubNumberChanging(false);
        Loger.log("☺•M••• stubNumberChanging=false");

        startLoad();

        accountViewModel.getLiveRegions().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                if (list==null) return;
                Loger.log("getLiveRegions "+list.size());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner=binding.spinnerRegion;
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        //Toast.makeText(requireContext(),list.get(position),Toast.LENGTH_SHORT).show();
                        newUser.setRegionId(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                accountViewModel.getLiveRegions().setValue(null);
                regionsComplete=true;
                if(checkLists()){
                    accountViewModel.getProfile();
                }
            }
        });
        accountViewModel.getLiveDianosis().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                if (list==null) return;
                Loger.log("getLiveDianosis "+list.size());
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner2 = binding.spinnerDiagnosis;
                spinner2.setAdapter(adapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        newUser.setDiagnosisId(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                accountViewModel.getLiveDianosis().setValue(null);
                diagnosisComplete=true;
                if(checkLists()){
                    accountViewModel.getProfile();
                }
            }
        });

        binding.switch1.setChecked(true);
        accountViewModel.getProfileObservable().observe(getViewLifecycleOwner(), new Observer<Datass>() {
            @Override
            public void onChanged(Datass userData) {
                if (userData!=null){
                    binding.changeName.setText(userData.getName());
                    if (userData.getTel()==null || userData.getTel().equals("")  ){
                        binding.editNumber.setText("7");
                    } else binding.editNumber.setText(userData.getTel());

                    if (Objects.requireNonNull(userData.getRegion()).getId()!=null){
                        binding.spinnerRegion.setSelection(userData.getRegion().getId());
                    }
                    if (Objects.requireNonNull(userData.getDiagnosis()).getId()!=null){
                        binding.spinnerDiagnosis.setSelection(userData.getDiagnosis().getId());
                    }

                    switch (userData.getGender()){
                        case GENDER_FEMALE: binding.female.setChecked(true);
                                break;
                        case GENDER_MALE:binding.male.setChecked(true);
                            break;
                        default:break;
                    }
                }
                if (regionsComplete && diagnosisComplete){
                    Loger.log(5);
                    regionsComplete=false; diagnosisComplete=false;
                    hideSplashScreen();
                }
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male: {
                        newUser.setGenderId(GENDER_MALE);
                        break;
                    }
                    case R.id.female: {
                        newUser.setGenderId(GENDER_FEMALE);
                        break;
                    }
                }
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionFunction deleteFunction=()->{
                    accountViewModel.deleteAccount();

                };

                String message ="Хотите ли вы удалить аккаунт?";

                DialogActionFragment dialogDeleteAccount=new DialogActionFragment(message, deleteFunction);
                dialogDeleteAccount.show(manager,"deleteAccount");
            }
        });

        accountViewModel.getNumberIsCheacked().observe(getViewLifecycleOwner(), (Boolean aBoolean)-> {
            if (aBoolean){
                accountViewModel.getNumberIsCheacked().setValue(false);
                accountViewModel.getChangeNumber().setValue(true);

                Toast.makeText(requireContext(),"Проверка номера", Toast.LENGTH_SHORT).show();
            }
        });

        accountViewModel.getChangeNumber().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    accountViewModel.getChangeNumber().setValue(false);

                    NavigationFunction navigationFunction=()->{
                        navController.navigate(R.id.action_profileFragment_to_sms2Fragment2);
                    };
                    ActionFunction negativeAction=()->{
                        binding.editNumber.setText(accountViewModel.lastUser().getTel());
                    };
                    ActionFunction positiveAction=()->{
                        accountViewModel.setStubNumberChanging(true);
                        Loger.log("☺•M••• positiveAction stubNumberChanging "+accountViewModel.getStubNumberChanging());

                    };
                    String message ="Хотите ли вы изменить номер телефона?";

                    Loger.log("newUser: "+newUser.getNumber());
                    Loger.log("lastUser: "+accountViewModel.lastUser().getTel());

                    DialogTwoFunctionFragment myDialogFragment = new DialogTwoFunctionFragment(message, negativeAction,positiveAction, navigationFunction);
                    myDialogFragment.show(manager, "myDialog");
                }
            }
        });

        accountViewModel.getLiveQuit().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    navigateToEntranceActivity.navigate();
                    accountViewModel.getLiveQuit().setValue(false);
                }
            }
        });

        binding.editNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.edit_number && !hasFocus) {
                    accountViewModel.changeNumber();
                    Toast.makeText(requireContext(),"Toasty ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void toAboutFragment (View view){
        Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_aboutFragment);
    }

    public void changeName(@NonNull CharSequence s,int start, int count, int after){
        String name= RemoveSpacesKt.removeSpaces(s.toString());
        Loger.log(name);
        if (name.equals(" ") || name.equals("")) {
            return;
        } else newUser.setName(name);
    }
    public void editNumber(@NonNull CharSequence s, int start, int count, int after){
        if (s.length()==16){
            Loger.log("last Char "+Character.isDigit(s.charAt(15)));
            boolean isDigit=Character.isDigit(s.charAt(15));
            if (isDigit){
                String number="+"+validator.helpExtractNumber(s);
                newUser.setNumber(number);
            }
        } else Loger.log("validate "+false);
    }

    public void  redactName(View view){
        ActionFunction action=()->{
            accountViewModel.redactUser();
        };
        DialogActionFragment dialogActionFragment=new DialogActionFragment("Хотите ли вы изменить данные?", action);
        dialogActionFragment.show(manager, "change");
    }
    public void quit(View view){
        ActionFunction actionFunction=()->{
            accountViewModel.quit();
        };

        String message ="Вы действительно хотите выйти из аккаунта?";
        DialogActionFragment dialogQuit=new DialogActionFragment(message,actionFunction);
        dialogQuit.show(manager, "quit");
    }

    public void backStack (View view){
        validateField.validateName(Objects.requireNonNull(newUser.getName()));
        if (!accountViewModel.getStubNumberChanging()) {
            if (accountViewModel.checkUser()){
                ActionFunction action=()->{
                    accountViewModel.redactUser();
                };
                ActionFunction actionNav=()->{
                    Navigation.findNavController(view).popBackStack();
                };
                NavigationFunction popBack=()->{
                    Navigation.findNavController(view).popBackStack();
                };
                DialogTwoFunctionFragment dialogActionFragment=new DialogTwoFunctionFragment("Хотите ли вы изменить данные?",actionNav,action,popBack);
                dialogActionFragment.show(manager, "change");
            }
            else {
                Navigation.findNavController(view).popBackStack();}
        }else {
            Navigation.findNavController(view).popBackStack();}

    }
    private boolean checkLists(){
        Loger.log("checkLists regionsComplete="+regionsComplete+"; diagnosisComplete="+diagnosisComplete);
        if (regionsComplete && diagnosisComplete){
            return true;
        }return false;
    }
    private void startLoad(){
        showSplashScreen();
        accountViewModel.getRegions();
        accountViewModel.getDiagnosis();
    }
    private void showSplashScreen(){
        binding.splashScreen.setVisibility(View.VISIBLE);
    }
    public void hideSplashScreen(){
        binding.splashScreen.setVisibility(View.GONE);
    }
}