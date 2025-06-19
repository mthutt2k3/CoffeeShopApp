package com.android.coffeeshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RoleSpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> roleNames;
    private final List<Integer> roleIds;

    public RoleSpinnerAdapter(Context context, List<String> roleNames, List<Integer> roleIds) {
        super(context, android.R.layout.simple_spinner_item, roleNames);
        this.context = context;
        this.roleNames = roleNames;
        this.roleIds = roleIds;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setText(roleNames.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setText(roleNames.get(position));
        return view;
    }

    public int getRoleId(int position) {
        return roleIds.get(position);
    }
}