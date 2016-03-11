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



public class MainActivity extends AppCompatActivity {

    private CalendarView calendar;
    private ListView listView;
    private long myCalendar = 1;
    private int selectedMonth;
    private int selectedDay;
    private int selectedYear;
    private ArrayList<String> data;
    private ArrayList<Integer> eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = (CalendarView)findViewById(R.id.calendarView);
        listView = (ListView)findViewById(R.id.listView);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedMonth = month;
                selectedDay = dayOfMonth;
                selectedYear = year;
                int m = month + 1;
                Toast.makeText(getApplicationContext(), m + "/"  + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                ArrayList<Long> id = getEvents(dayOfMonth, month, year);
                eventID = new ArrayList<>();
                for(int i = 0; i < id.size(); i++){
                    eventID.add(id.get(i).intValue());
                }
                    data = eventData(id);
                 ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,data);
                 listView.setAdapter(adapter);

            }
        });
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
        intent.putIntegerArrayListExtra("ids", eventID);
        startActivity(intent);
    }
}
