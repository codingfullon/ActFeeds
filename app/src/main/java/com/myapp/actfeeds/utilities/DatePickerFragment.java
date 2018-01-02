package com.myapp.actfeeds.utilities;

/**
 * Created by KavitaPC on 12/18/2017.
 * Open date picker dialog to select the date
 */


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.myapp.actfeeds.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String TEXT_VIEW_ID = "textViewId";
    int id;
    SimpleDateFormat dateFormat= new SimpleDateFormat("MMM, dd yyyy", Locale.ENGLISH);


    public DatePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        Calendar c = new GregorianCalendar();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        //set minimum date as current date
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());


        id = getArguments().getInt(TEXT_VIEW_ID);
        TextView textViewStartDate = (TextView)getActivity().findViewById(R.id.tvStartDate);
        TextView textViewEndDate = (TextView)getActivity().findViewById(R.id.tvEndDate);


        if((id == R.id.tvEndDate)&& (textViewStartDate.getText().toString()).equals("Start Date")){
            textViewEndDate.setText("End Date");
        }
        else if(id == R.id.tvEndDate && !(textViewStartDate.getText().toString()).equals("Start Date")){
            String dateText = textViewStartDate.getText().toString();
            Calendar c1 = new GregorianCalendar();
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

        } else if(id == R.id.tvStartDate && !(textViewEndDate.getText().toString()).equals("End Date")) {
            String dateText = textViewEndDate.getText().toString();
            Calendar c2 = new GregorianCalendar();
            Date date;
            try {
                date = dateFormat.parse(dateText);
                c2.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year1 = c2.get(Calendar.YEAR);
            int month1 = c2.get(Calendar.MONTH);
            int day1 = c2.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), this, year1, month1, day1);
            datePickerDialog.getDatePicker().setMaxDate(c2.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        }
        return datePickerDialog;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        final TextView textView= (TextView)getActivity().findViewById(getArguments().getInt(TEXT_VIEW_ID));
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
