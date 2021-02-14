package com.example.mlforall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyModelsListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MachineLearningModel> models;

    public MyModelsListViewAdapter(Context context, ArrayList<MachineLearningModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return this.models.size();
    }

    @Override
    public MachineLearningModel getItem(int position) {
        return this.models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_models_list_view, parent, false);

        MachineLearningModel currentModel = this.models.get(position);

        TextView tvSlope = view.findViewById(R.id.tvModelsListViewSlope);
        tvSlope.setText(tvSlope.getText().toString() + " " + currentModel.getLinearEquation().getSlope());

        TextView tvIntercept = view.findViewById(R.id.tvModelsListViewIntercept);
        tvIntercept.setText(tvIntercept.getText().toString() + " " + currentModel.getLinearEquation().getIntercept());

        TextView tvScore = view.findViewById(R.id.tvModelsListViewScore);
        tvScore.setText(tvScore.getText().toString() + " " + currentModel.getScore() * 100 + "%"); // Showing the score in percentage format.

        return view;
    }
}
