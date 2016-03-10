package com.hw2.josh.calendarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class AddEvent extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, AddFragment.newInstance(getIntent().getLongExtra("calendar", 1),
                                        getIntent().getIntExtra("month", 2), getIntent().getIntExtra("day", 0),
                                        getIntent().getIntExtra("year", 2016)))
                .addToBackStack(null)
                .commit();
    }

}
