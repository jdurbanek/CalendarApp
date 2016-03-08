package com.hw2.josh.calendarapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.jar.Manifest;


public class MainActivity extends AppCompatActivity {

    CalendarView calendar;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = (CalendarView)findViewById(R.id.calendarView);
        listView = (ListView)findViewById(R.id.listView);

        ArrayList<Long> myIds = getEvents(1);
        if(myIds.size() == 0)
            Log.d("NO IDS FOUND.", "AHHHH");

        ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<Long>(
                this, android.R.layout.simple_list_item_1, myIds);

        listView.setAdapter(arrayAdapter);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                long date = calendar.getDate();
                // Toast.makeText(getApplicationContext(), dayOfMonth + "/"  + month  + "/" + year, Toast.LENGTH_LONG).show();
                 ArrayList<Long> id = getEvents(dayOfMonth);
                 ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(getBaseContext(), android.R.layout.simple_list_item_1,id);
                 listView.setAdapter(adapter);

            }
        });


    }

    long findCalendar() {

        Cursor cursor;
        ContentResolver cr = getContentResolver();

        String[] returnColumns = new String[] {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE
        };

        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND " +
                 "(" + CalendarContract.Calendars.ACCOUNT_TYPE + " = ? AND"
                + "(" + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ? ))";

        String[]  selectionArgs = {"urby33@gmail.com", "google.com", "urby33@gmail.com"};


        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR);

        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, selection,
                selectionArgs, null);
        long id = 0;
        String displayName = null;
        String accountName = null;
        String accountType = null;

        if(cursor.moveToFirst()) {

            id = cursor.getLong(0);
            accountName = cursor.getString(1);
            displayName = cursor.getString(2);
            accountType = cursor.getString(3);
            cursor.close();
            return id;

        }
        //if no calendar found.
        cursor.close();
        return -1;
    }

    ArrayList<Long> getEvents(int dayOfMonth) {

    //    Cursor cursor = cr.getContentResolver().query()
        ArrayList<Long> eventIDs = new ArrayList<Long>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.add(Calendar.DAY_OF_MONTH, dayOfMonth);
        beginCalendar.set(Calendar.HOUR_OF_DAY, 0);
        beginCalendar.set(Calendar.MINUTE, 0);
        beginCalendar.set(Calendar.MILLISECOND, 0);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_MONTH, dayOfMonth);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.MILLISECOND, 0);

        long begin = beginCalendar.getTimeInMillis();
        long end = endCalendar.getTimeInMillis();

        String[]  proj = new String[] {
                CalendarContract.Instances._ID,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.EVENT_ID
        };
        Cursor cursor = CalendarContract.Instances.query(getContentResolver(), proj, begin, end);
        if(cursor.moveToFirst()) {
            do {
                eventIDs.add(cursor.getLong(3));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return eventIDs;
    }
}
