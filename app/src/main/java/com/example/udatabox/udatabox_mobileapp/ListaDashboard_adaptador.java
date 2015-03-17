package com.example.udatabox.udatabox_mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Carlos on    17-02-2015.
 */
public class ListaDashboard_adaptador extends ArrayAdapter<ListaDashboard_model> {
    private final Context context;
    private final ArrayList<ListaDashboard_model> modelsArrayList;

    public ListaDashboard_adaptador(Context context, ArrayList<ListaDashboard_model> modelsArrayList) {
        super(context, R.layout.fila_estudiante_dashboard, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        if(!modelsArrayList.get(position).isGroupHeader()){
            rowView = inflater.inflate(R.layout.fila_estudiante_dashboard, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);
            titleView.setText(modelsArrayList.get(position).getTitle());
            counterView.setText(modelsArrayList.get(position).getCounter());
        }
        else{
            rowView = inflater.inflate(R.layout.header_estudiante_dashboard, parent, false);
            rowView.setOnClickListener(null);
            TextView titleView = (TextView) rowView.findViewById(R.id.tw_Dashboard_Header);
            titleView.setText(modelsArrayList.get(position).getTitle());
        }
        return rowView;
    }
}
