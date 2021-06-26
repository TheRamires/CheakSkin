package ru.skinallergic.checkskin.components.home.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.home.data.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    //Названия заголовков
    private List<String> _listDataHeader;
    //Данные для элементов подпунктов:
    private HashMap<String, List<Document>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Document>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Document document= (Document) getChild(groupPosition, childPosition);

        final String childCount = String.valueOf(document.getId());
        final String childText = document.getText();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
        }
        TextView countListChild = (TextView) convertView
                .findViewById(R.id.expandbl_count);
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.expandbl_text);

        countListChild.setText(childCount);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
        }
        Button button = (Button) convertView.findViewById(R.id.btnLpu);
        Drawable icon3= parent.getContext().getResources().getDrawable(R.drawable.my_icon_home_3);
        myButtonConfig(button,icon3,headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void myButtonConfig(Button button, Drawable icon3, String headerTitle ){
        button.setFocusable(false);
        button.setTypeface(null, Typeface.BOLD);
        button.setText(headerTitle);

        icon3.setBounds(0,0,75,75);
        button.setCompoundDrawables(icon3,null,null,null);
    }
}