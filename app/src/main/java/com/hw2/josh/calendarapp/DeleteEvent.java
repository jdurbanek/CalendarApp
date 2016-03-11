package com.hw2.josh.calendarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DeleteEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.delete_fragment_container, DeleteFragment.newInstance(getIntent().getStringArrayListExtra("events")))
                .addToBackStack(null)
                .commit();
    }
}
