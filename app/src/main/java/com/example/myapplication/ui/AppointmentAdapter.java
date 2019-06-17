package com.example.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Appointment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private final Context context;
    private final List<Appointment> appointments;
    private final int layoutResId;


    AppointmentAdapter(Context context, int resource, List<Appointment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResId = resource;
        this.appointments = objects;
    }

    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        ItemHolder itemHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            convertView = inflater.inflate(layoutResId, parent, false);
            itemHolder.menteeTV = convertView.findViewById(R.id.menteeCodeTV);
            itemHolder.locationTV = convertView.findViewById(R.id.locationTV);
            itemHolder.dateTV = convertView.findViewById(R.id.dateTV);

            convertView.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        Appointment appointment = appointments.get(position);

        itemHolder.menteeTV.setText(appointment.getM_code());
        String timesAndDate = appointment.getStart_time() + "-" + appointment.getEnd_time() + " " + appointment.getDate();
        itemHolder.dateTV.setText(timesAndDate);
        itemHolder.locationTV.setText(appointment.getLocation());

        return convertView;
    }

    private static class ItemHolder {
        TextView menteeTV;
        TextView locationTV;
        TextView dateTV;

    }
}
