package com.hw2.josh.calendarapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";


    private long calendar;
    private int month;
    private int year;
    private int day;

    private EditText eventName;
    private EditText eventTime;
    private EditText eventLocation;
    private Button add;



    private OnFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(long param1, int param2, int param3, int param4) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        args.putInt(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            calendar = getArguments().getLong(ARG_PARAM1);
            year = getArguments().getInt(ARG_PARAM4);
            month = getArguments().getInt(ARG_PARAM2);
            day = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);


        eventName = (EditText) view.findViewById(R.id.editTextEventName);
        eventTime = (EditText)view.findViewById(R.id.editTextTime);
        eventLocation = (EditText)view.findViewById(R.id.editTextLocation);
        add = (Button)view.findViewById(R.id.addEvent);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String event = eventName.getText().toString().trim();
                String time = eventTime.getText().toString().trim();
                int value;
                try{
                    value = Integer.parseInt(time);
                }catch(NumberFormatException ex){
                    //on error set default time to beginning of day.
                    value = 0;
                }
                String location = eventLocation.getText().toString().trim();

                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                cal.setTimeZone(TimeZone.getTimeZone("CST"));
                cal.set(Calendar.HOUR_OF_DAY, value);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long start = cal.getTimeInMillis();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.TITLE, event);
                values.put(CalendarContract.Events.DTSTART, start);
                values.put(CalendarContract.Events.DTEND, start);
                values.put(CalendarContract.Events.EVENT_LOCATION, location);
                values.put(CalendarContract.Events.CALENDAR_ID, calendar);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, "CST");
                values.put(CalendarContract.Events.DESCRIPTION, "Event Created.");

                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_CALENDAR);
                Uri uri = getActivity().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
                long eventID = new Long(uri.getLastPathSegment());
//                Toast.makeText(getActivity().getBaseContext(), "" + eventID + " " + month + "/" + day, Toast.LENGTH_LONG).show();

                getActivity().finish();


            }
        });



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
