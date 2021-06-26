package ru.skinallergic.checkskin.components.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.home.adapters.ExpandableListAdapter;
import ru.skinallergic.checkskin.components.home.data.Document;
import ru.skinallergic.checkskin.components.news.adapters.SpaceItemDecoration;
import ru.skinallergic.checkskin.databinding.FragmentLpuDocumentsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.skinallergic.checkskin.components.home.adapters.ExpandableListAdapter;

public class LPUDocumentsFrag extends Fragment {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<Document>> listDataChild;
    private FragmentLpuDocumentsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLpuDocumentsBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        expListView = binding.lvExp;
        //Подготавливаем список данных:
        prepareListData();

        listAdapter = new ExpandableListAdapter(requireContext(), listDataHeader, listDataChild);

        //Настраиваем listAdapter:
        expListView.setAdapter(listAdapter);
        //Индикатор
        Display display = requireActivity().getDisplay();
        int width=display.getWidth()-100;

        expListView.setIndicatorBounds(width-100,width-20);

        /*if (android.os.Build.VERSION.SDK_INT <
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width-80, width);
        } else {
            expListView.setIndicatorBoundsRelative(width-80, width);
        }*/

        return view;
    }
    //------------------------------------------------------------------------------------

    private void prepareListData() {
        Log.d("myLog","prepareListData");
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Document>>();

        //Добавляем данные о пунктах списка:
        listDataHeader.add("Пакет документов №1");
        listDataHeader.add("Пакет документов №2");
        listDataHeader.add("Пакет документов №3");

        //Добавляем данные о подпунктах:
        List<Document> top250 = new ArrayList<Document>();
        top250.add(new Document(1,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));
        top250.add(new Document(2,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));
        top250.add(new Document(3,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));

        List<Document> nowShowing = new ArrayList<Document>();
        nowShowing.add(new Document(1,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));
        nowShowing.add(new Document(2,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));
        nowShowing.add(new Document(3,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));

        List<Document> comingSoon = new ArrayList<Document>();
        comingSoon.add(new Document(1,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));
        comingSoon.add(new Document(2,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));
        comingSoon.add(new Document(3,"Оказывает высокотехнологическую медицинскую помощь по профилю “Дерматовенерология"));

        listDataChild.put(listDataHeader.get(0), top250);
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        //При раскрывании списка
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Раскрыт",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //Сворачиваем список
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Свернут",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void backstack(View view){
        Navigation.findNavController(view).popBackStack();
    }
}