package com.hw2.josh.calendarapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeleteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
  //  private static final String ARG_PARAM2 = "param2";
   // private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private long calendar;
    private int month;
    private int day;
    private int year;
    private ArrayList<Integer> ids;
    private ArrayList<String> events;

    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public DeleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param5 Parameter 5.
     *
     * @return A new instance of fragment DeleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteFragment newInstance(ArrayList<String> param5, ArrayList<Integer> param4) {
        DeleteFragment fragment = new DeleteFragment();
        Bundle args = new Bundle();
       // args.putLong(ARG_PARAM1, param1);
      //  args.putInt(ARG_PARAM2, param2);
      //  args.putInt(ARG_PARAM3, param3);
        args.putIntegerArrayList(ARG_PARAM4, param4);
        args.putStringArrayList(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
     //       calendar= getArguments().getLong(ARG_PARAM1);
      //      month = getArguments().getInt(ARG_PARAM2);
      //      day = getArguments().getInt(ARG_PARAM3);
            ids = getArguments().getIntegerArrayList(ARG_PARAM4);
            events = getArguments().getStringArrayList(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        listView = (ListView)view.findViewById(R.id.listView2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1,events);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //delete event.
             //   Toast.makeText(getActivity(), "" + ids.get(position), Toast.LENGTH_LONG).show();
                String[] selArgs =
                        new String[]{Integer.toString(ids.get(position))};
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_CALENDAR);
                int deleted =
                        getActivity().getContentResolver().
                                delete(
                                        CalendarContract.Events.CONTENT_URI,
                                        CalendarContract.Events._ID + " =? ",
                                        selArgs);

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
