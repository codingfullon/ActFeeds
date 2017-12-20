package com.example.myapp.actfeeds.utilities;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.myapp.actfeeds.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TEXT_VIEW_ID = "textViewId";
    int id;

    public TimePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour,minute, false );
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView= (TextView)getActivity().findViewById(getArguments().getInt(TEXT_VIEW_ID));
        String amPm ="AM";
        int currentHour;
        String hourStr ;
        String minuteStr;

        amPm = (hourOfDay<12)?amPm ="AM" : "PM";
        currentHour = (hourOfDay>11)?(hourOfDay -12) : hourOfDay;
        hourStr = (currentHour<10)? "0"+currentHour : ""+currentHour;
        minuteStr = (minute<10)? "0"+minute : ""+minute;

        String timeString = hourStr+":"+minuteStr+ " "+amPm;
        textView.setText(timeString);
    }
}
