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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewGoals.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewGoals#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewGoals extends Fragment implements View.OnClickListener {

    private Toast mToast;
    private OnFragmentInteractionListener mListener;
    private TextView mTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public AddNewGoals() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewGoals.
     */
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
        mTextView = (TextView)view.findViewById(R.id.tvDailyHabits);
        mTextView.setOnClickListener(this);
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




    @Override
    public void onClick (final View v){
        int id = v.getId();
        Context context = getContext();
        if (id == R.id.tvDailyHabits) {
            mToast = Toast.makeText(context, "Item was clicked", Toast.LENGTH_LONG);
            mToast.show();
            Intent intent = new Intent(context, DailyHabitsDetails.class);
            startActivity(intent);
        } else if (id == R.id.tvWeeklyHabits) {
            mToast = Toast.makeText(context, "Item was clicked", Toast.LENGTH_LONG);
            mToast.show();
        } else if (id == R.id.tvMonthlyHabits) {
            mToast = Toast.makeText(context, "Item was clicked", Toast.LENGTH_LONG);
            mToast.show();

        } else if (id == R.id.tvCreateOwn) {
            mToast = Toast.makeText(context, "Item was clicked", Toast.LENGTH_LONG);
            mToast.show();

        }

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
