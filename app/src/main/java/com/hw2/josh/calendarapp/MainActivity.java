package com.hw2.josh.calendarapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.jar.Manifest;


public class MainActivity extends AppCompatActivity {

    private CalendarView calendar;
    private ListView listView;
    private long myCalendar = 1;
    private int selectedMonth;
    private int selectedDay;
    private int selectedYear;
    private ArrayList<String> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = (CalendarView)findViewById(R.id.calendarView);
        listView = (ListView)findViewById(R.id.listView);


      /*  ArrayList<Long> myIds = getEvents(Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                                            Calendar.getInstance().get(Calendar.MONTH),);
        if(myIds.size() == 0)
            Log.d("NO IDS FOUND.", "AHHHH");

        ArrayList<String> d = eventData(myIds);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,d);
        listView.setAdapter(adapter);  */

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedMonth = month;
                selectedDay = dayOfMonth;
                selectedYear = year;
                int m = month + 1;
                 Toast.makeText(getApplicationContext(), m + "/"  + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                 ArrayList<Long> id = getEvents(dayOfMonth, month, year);
                    data = eventData(id);
                 ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,data);
                 listView.setAdapter(adapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected.", Toast.LENGTH_LONG).show();
            }

        });




    }

    long findCalendar() {

        Cursor cursor;
      //  ContentResolver cr = getContentResolver();

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

        cursor = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, returnColumns, selection,
                selectionArgs, null);
        long id;
        //String displayName = null;
        //String accountName = null;
        //String accountType = null;

        if(cursor.moveToFirst()) {

            id = cursor.getLong(0);
           // accountName = cursor.getString(1);
           // displayName = cursor.getString(2);
           // accountType = cursor.getString(3);
            cursor.close();
            return id;

        }
        //if no calendar found.
        cursor.close();
        return -1;
    }

    public ArrayList<Long> getEvents(int dayOfMonth, int month, int year) {

    //    Cursor cursor = cr.getContentResolver().query()
        ArrayList<Long> eventIDs = new ArrayList<>();
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.set(year, month, dayOfMonth);
        beginCalendar.setTimeZone(TimeZone.getTimeZone("CST"));
        beginCalendar.set(Calendar.HOUR_OF_DAY, 0);
        beginCalendar.set(Calendar.MINUTE, 0);
        beginCalendar.set(Calendar.MILLISECOND, 0);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month, dayOfMonth);
        endCalendar.setTimeZone(TimeZone.getTimeZone("CST"));
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

    ArrayList<String> eventData(ArrayList<Long> myEvents) {

        ArrayList<String> myEventData = new ArrayList<>();
        String[] proj = new String[] {
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.EVENT_LOCATION
        };
        for(int i = 0; i < myEvents.size(); i++) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR);
            Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, proj,
                    CalendarContract.Events._ID + " = ? ", new String[]{Long.toString(myEvents.get(i))}, null);

            if (cursor.moveToFirst()) {
                myEventData.add(cursor.getString(1) + "\n" + cursor.getString(2));
            }

            cursor.close();
        }
        return myEventData;
    }

    public void addEvent(View view) {
        Intent intent = new Intent(this, AddEvent.class);
        //send the calendar id we want to add the event too.
        intent.putExtra("calendar", myCalendar);
        intent.putExtra("month", selectedMonth);
        intent.putExtra("day", selectedDay);
        intent.putExtra("year", selectedYear);
        startActivity(intent);
    }

    public void deleteEvent(View view) {
        Intent intent = new Intent(this, DeleteEvent.class);

        intent.putStringArrayListExtra("events", data);
        startActivity(intent);
    }
}
