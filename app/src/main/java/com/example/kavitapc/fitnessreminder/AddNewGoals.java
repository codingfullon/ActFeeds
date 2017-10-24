package com.example.kavitapc.fitnessreminder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class AddNewGoals extends Fragment {

    private Toast mToast;
    private OnFragmentInteractionListener mListener;
    private TextView tvDailyHabits;
    private TextView tvWeeklyHabits;
    private TextView tvMonthlyHabits;
    private TextView tvCreateOwn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: +]
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewGoals() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddNewGoals newInstance(String param1, String param2) {
        AddNewGoals fragment = new AddNewGoals();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_goals, container, false);
        tvDailyHabits = (TextView)view.findViewById(R.id.tvDailyHabits);
        tvWeeklyHabits = (TextView)view.findViewById(R.id.tvWeeklyHabits);
        tvMonthlyHabits = (TextView)view.findViewById(R.id.tvMonthlyHabits);
        tvCreateOwn = (TextView)view.findViewById(R.id.tvCreateOwn);

        TextView[] textViews = {tvDailyHabits, tvWeeklyHabits, tvMonthlyHabits, tvCreateOwn};
        for(TextView tv: textViews) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    Context context = getContext();
                    if (id == R.id.tvDailyHabits) {
                        mToast = Toast.makeText(context, "Item 1 was clicked", Toast.LENGTH_LONG);
                        mToast.show();
                        Intent intent = new Intent(context, DailyHabitsDetails.class);
                        startActivity(intent);
                    } else if (id == R.id.tvWeeklyHabits) {
                        mToast = Toast.makeText(context, "Item 2 was clicked", Toast.LENGTH_LONG);
                        mToast.show();
                        Intent intent = new Intent(context, WeeklyHabitsDetails.class);
                        startActivity(intent);
                    } else if (id == R.id.tvMonthlyHabits) {
                        mToast = Toast.makeText(context, "Item 3 was clicked", Toast.LENGTH_LONG);
                        mToast.show();
                        Intent intent = new Intent(context, MonthlyHabits.class);
                        startActivity(intent);

                    } else if (id == R.id.tvCreateOwn) {
                        mToast = Toast.makeText(context, "Item 4 was clicked", Toast.LENGTH_LONG);
                        mToast.show();
                        Intent intent = new Intent(context, CreateOwn.class);
                        startActivity(intent);

                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
