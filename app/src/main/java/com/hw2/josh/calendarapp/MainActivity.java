package com.hw2.josh.calendarapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

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

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Toast.makeText(getApplicationContext(), dayOfMonth + "/"  + month  + "/" + year, Toast.LENGTH_LONG).show();
                long date = calendar.getDate();

            }
        });
    }

    void findCalendar() {

        Cursor cursor = null;
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

        while(cursor.moveToNext()) {
            long id = 0;
            String displayName = null;
            String accountName = null;
            String accountType = null;

            id = cursor.getLong(0);
            accountName = cursor.getString(1);
            displayName = cursor.getString(2);
            accountType = cursor.getString(3);

        }


    }
}
