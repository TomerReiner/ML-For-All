package com.example.mlforall;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class MyModelsListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LinearEquation> linearEquations;

    public MyModelsListViewAdapter(Context context, ArrayList<LinearEquation> linearEquations) {
        this.context = context;
        this.linearEquations = linearEquations;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
