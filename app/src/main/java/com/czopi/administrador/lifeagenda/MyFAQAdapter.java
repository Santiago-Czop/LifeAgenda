package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;


public class MyFAQAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> FAQListParent, FAQListChild;

    public MyFAQAdapter(Context context0, List<String> FAQListParent0, List<String> FAQListChild0) {
        super();

        context = context0;
        FAQListParent = FAQListParent0;
        FAQListChild = FAQListChild0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return FAQListParent.get(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String titleText = String.valueOf(getGroup(groupPosition));

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_title, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(titleText);

        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return FAQListParent.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return FAQListChild.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = String.valueOf(getChild(groupPosition, childPosition));

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_child, parent, false);
        }

        TextView tvChild = (TextView) convertView.findViewById(R.id.tvChild);
        tvChild.setText(childText);

        return convertView;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
