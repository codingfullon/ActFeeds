package com.inkeep.actfeeds.utilities;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.TimePicker;

import com.crashlytics.android.Crashlytics;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TEXT_VIEW_ID = "textViewId";
    int id;


    public TimePickerFragment() {
        // Required empty public constructor
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       Calendar c = new GregorianCalendar();
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
