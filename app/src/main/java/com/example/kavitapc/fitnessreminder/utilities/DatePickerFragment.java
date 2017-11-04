package com.example.kavitapc.fitnessreminder.utilities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kavitapc.fitnessreminder.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String TEXT_VIEW_ID = "textViewId";
    int id;


    public DatePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());


        id = getArguments().getInt(TEXT_VIEW_ID);
        TextView textViewStartDate = (TextView)getActivity().findViewById(R.id.tvStartDate);
        TextView textViewEndDate = (TextView)getActivity().findViewById(R.id.tvEndDate);


        if((id == R.id.tvEndDate)&& (textViewStartDate.getText().toString()).equals("Start Date")){
            textViewEndDate.setText("End Date");
        }
        else if(id == R.id.tvEndDate && !(textViewStartDate.getText().toString()).equals("Start Date")){
            String dateText = textViewStartDate.getText().toString();
            SimpleDateFormat dateFormat= new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);
            final Calendar c1 = Calendar.getInstance();
            Date date;
            try {
                date = dateFormat.parse(dateText);
                c1.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year1 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int day1 = c1.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), this, year1, month1, day1);
            datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());

        }

        return datePickerDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        final TextView textView= (TextView)getActivity().findViewById(getArguments().getInt(TEXT_VIEW_ID));
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);
        String mon = new DateFormatSymbols().getMonths()[month];
        String dateString = mon+", "+dayOfMonth+" "+year;
            Date date;
            try {
                date = dateFormat.parse(dateString);
                String dateStr = dateFormat.format(date);
                textView.setText(dateStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }




}
