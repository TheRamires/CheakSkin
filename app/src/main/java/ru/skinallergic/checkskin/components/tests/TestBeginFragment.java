package ru.skinallergic.checkskin.components.tests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.data.Answer;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.data.Question;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.FragmentTestBeginBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import static ru.skinallergic.checkskin.utils.UtilsKt.ZERO_FLAG;

public class TestBeginFragment extends Fragment {
    private TestsViewModel viewModel;
    private EntityTest entityTest;
    private List<Question> listQuestions;
    private int count;
    private RadioGroup radioGroup;
    private SeekBar seekBar;
    private TextView seekBarCount;
    private boolean shifted;
    private List<Integer> allAnswers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        entityTest= viewModel.getEntityTestsObservable().get();
        listQuestions=entityTest.getQuestions();
        count=0;
        viewModel.getQuestionId().setValue(count);
        shifted=false;
        allAnswers=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTestBeginBinding binding =FragmentTestBeginBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        View view=binding.getRoot();
        if (!shifted){binding.shadow.setY(+110); shifted=true;}

        radioGroup=binding.radioGroup;
        Loger.log("count "+count);
        seekBar=binding.seekBar;
        seekBarCount=binding.seekBarCount;

        setPicture(binding.img);

        if (entityTest.getId()==4){
            seekBarCount.setText("0");
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setProgress(0);
            allAnswers.add(0);
            seekBarCount.setVisibility(View.VISIBLE);
        } else {radioGroup.setVisibility(View.VISIBLE);}

        viewModel.getQuestionId().observe(getViewLifecycleOwner(), (Integer id)-> {
            if (count==0){
                binding.buttonLayout.setVisibility(View.INVISIBLE);
                binding.btnNextBig.setVisibility(View.VISIBLE);
            } else  {
                binding.buttonLayout.setVisibility(View.VISIBLE);
                binding.btnNextBig.setVisibility(View.INVISIBLE);
            }

           LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           params.setMargins(5,5,5,5);

            Question oneQuestion=listQuestions.get(id);
            binding.question.setText(oneQuestion.getText());

            if (radioGroup.getVisibility()==View.VISIBLE){
                List<Answer> answerList= oneQuestion.getAnswer();

                for (Answer answer : answerList){
                    RadioButton radioButton=new RadioButton(requireContext());
                    radioButton.setId(answer.getWeight()); // именно weight! а не id
                    radioButton.setText(answer.getText());
                    radioButton.setTextColor(Color.BLACK);
                    Drawable drawable= AppCompatResources.getDrawable(requireContext(),R.drawable.radiobutton_drawable_02);
                    radioButton.setButtonDrawable(drawable);
                    radioButton.setLayoutParams(params);
                    radioButton.setPadding(20,20,20,20);
                    radioGroup.addView(radioButton);
                }
            }
            clickLastPosition();
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId!=-1){
                    sum(checkedId);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekBarCount.setText(String.valueOf(progress));
                sum(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    public void back(View view){
        if (count==0){
            Loger.log("back");
            backStack(view);
        } else {
            Loger.log("back");
            backQuestion();
        }
    }

    public void next(View view){
        if (count==listQuestions.size()-1)  {
            Loger.log("next");
            nextFragment(view);
        }else {
            nextQuestion();
        }
    }

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void nextFragment(View view){
        int sum=0;
        for (int x: allAnswers){
            sum+=x;
        }
        Loger.log("sum result = "+sum);
        Bundle bundle=new Bundle();
        bundle.putInt("sum", sum);
        Navigation.findNavController(view).navigate(R.id.action_testBeginFragment_to_testCompletionFragment,bundle);
    }
    private void nextQuestion(){
        if (radioGroup.getCheckedRadioButtonId()!=-1){
            clearRadioGroup();
            count++;
            Loger.log("nextQuestion radioGroup, count "+count);
            viewModel.getQuestionId().setValue(count);
        } else if (seekBar.getVisibility()==View.VISIBLE){
            count++;
            allAnswers.add(0);
            seekBar.setProgress(0);

            Loger.log("nextQuestion seekBar count "+count);
            viewModel.getQuestionId().setValue(count);
        }
    }
    private void backQuestion(){
        if (seekBar.getVisibility()==View.VISIBLE){
            seekBar.setProgress(0);
        } else if (radioGroup.getVisibility()==View.VISIBLE){
            clearRadioGroup();
        }

        if (allAnswers.size()==count+1){allAnswers.remove(count);}
        count--;
        Loger.log("count "+count);
        viewModel.getQuestionId().setValue(count);}

    private void clearRadioGroup(){
        radioGroup.clearCheck();
        radioGroup.removeAllViews();
    }

    private void clickLastPosition(){
        Loger.log("•clickLastPosition•");
        if (allAnswers.size()==count+1){
            Loger.log("clickLastPosition count+1");

            if (seekBar.getVisibility()==View.VISIBLE){
                Loger.log("seekBar clickLastPosition. seekBar.setProgress");
                seekBar.setProgress(allAnswers.get(count));

            } else if (radioGroup.getVisibility()==View.VISIBLE){
                radioGroup.check(allAnswers.get(count));
            }
        }
    }
    private void sum(int checkedId){
        int weight;
        if (checkedId==ZERO_FLAG){
            weight=0;
        } else {weight=checkedId;}
        if (allAnswers.size()==count+1){
            allAnswers.set(count,weight);
        }else allAnswers.add(weight);
        Loger.log("question № "+count+", check is "+weight+", sum= "+allAnswers);
    }
    private void setPicture(ImageView img){
        int image=entityTest.getImage();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        img.setImageBitmap(bitmap);
    }
}