package com.hetekivi.heratyskello.app.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.hetekivi.heratyskello.app.Data.Alarm;
import com.hetekivi.heratyskello.app.R;
import com.hetekivi.heratyskello.app.Controllers.StorageController;
import org.joda.time.LocalDateTime;

import java.util.*;

public class ClockActivity extends AppCompatActivity {
    public static Alarm CurrentAlarm;
    final static int ADD_ALARM = 1;
    final static int SHOW_ALARM = 2;
    private final Handler handler = new Handler();
    private Set<String> Alarms;
    public static final String ALARMS_NAME = "alarms";
    public static Context context;
    public static final String PREFERENCES_NAME = "MyPreferences";
    public static final String ALARM_ACTION = "com.hetekivi.heratyskello.app.AlarmActivity.class";

    private Button addAlarmButton;
    private ListView upComingAlarmsList;
    private TextView upComingAlarmsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Map map = preferences.getAll();
        StorageController.preferences = preferences;
        context = this;
        this.Alarms = StorageController.Alarms();
        CurrentAlarm = new Alarm();
        setContentView(R.layout.activity_clock);
        this.addAlarmButton = (Button) findViewById(R.id.addAlarm);
        this.addAlarmButton.setText(R.string.button_addAlarm);
        this.addAlarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addAlarm(v);
            }
        });
        this.upComingAlarmsList = (ListView) findViewById(R.id.upComingAlarms);
        this.upComingAlarmsTitle = (TextView) findViewById(R.id.upComingAlarmsTitle);
        this.upComingAlarmsTitle.setText(R.string.upComingAlarmsTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(this.broadcastReceiver, new IntentFilter(ALARM_ACTION));
        this.Alarms = StorageController.Alarms();
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(this.Alarms));
        this.upComingAlarmsList.setAdapter(listAdapter);
        this.upComingAlarmsList.refreshDrawableState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alarms) {
            Intent intent = new Intent(this, AlarmsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, DateActivity.class);
        startActivityForResult(intent, ADD_ALARM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ADD_ALARM) : {
                if (resultCode == Activity.RESULT_OK)
                {
                    if(this.checkAlarm(CurrentAlarm))
                    {
                        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(this, AlarmActivity.class);
                        intent.setAction(ALARM_ACTION);
                        final int id = (int) CurrentAlarm.MillisFromCurrent();
                        PendingIntent alarmIntent = PendingIntent.getActivity(this, id, intent, 0);
                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, CurrentAlarm.MillisFromCurrent(), alarmIntent);
                        this.addAlarm(CurrentAlarm, alarmMgr, alarmIntent);
                        CurrentAlarm = new Alarm();
                    }
                }
                break;
            }
        }
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast toast = Toast.makeText(ClockActivity.this, R.string.toast_alarmSaved ,Toast.LENGTH_SHORT);
            toast.show();
            Intent alarmIntent = new Intent(ClockActivity.context, AlarmActivity.class);
            startActivity(alarmIntent);

        }
    };
    private boolean checkAlarm(Alarm alarm)
    {
        LocalDateTime now = new LocalDateTime();
        boolean result = true;
        int message = 0;

        if(Alarms.contains(alarm.toString()))
        {
            message = R.string.toast_overlappingAlarm;
            result = false;
        }
        else if(alarm.DateTime().isBefore(now))
        {
            message = R.string.toast_alarmInPast;
            result = false;
        }

        if(result == false && message != 0)
        {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(result)
        {
            Toast toast = Toast.makeText(this, R.string.toast_alarmSaved ,Toast.LENGTH_SHORT);
            toast.show();
        }

        return result;

    }

    private void addAlarm(Alarm alarm, AlarmManager alarmManager, PendingIntent pendingIntent)
    {
        this.Alarms.add(alarm.toString());
        StorageController.Save(ALARMS_NAME, this.Alarms);
    }



}
