package com.hetekivi.heratyskello.app.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.hetekivi.heratyskello.app.Data.Alarm;
import com.hetekivi.heratyskello.app.R;
import com.hetekivi.heratyskello.app.Controllers.StorageController;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AlarmsActivity extends AppCompatActivity {

    private ArrayList<String> Alarms;
    private ListView alarmList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
        this.alarmList = (ListView) findViewById(R.id.alarmList);
        this.alarmList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkRemoveAlarm(i);
            }
        });
   }

    @Override
    protected void onResume() {
        super.onResume();
        this.setList();
    }

    private void setList()
    {
        this.Alarms = new ArrayList<String>(StorageController.Alarms());
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.Alarms);
        this.alarmList.setAdapter(listAdapter);
        this.alarmList.refreshDrawableState();
    }

    private void checkRemoveAlarm(final int index)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.removeAlarmTitle);
        builder.setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeAlarm(index);
            }
        });
        builder.setNegativeButton("Ei", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeAlarm(int index)
    {
        if(this.Alarms.size() >= index)
        {
            String date = this.Alarms.get(index);
            this.Alarms.remove(index);
            StorageController.Save(ClockActivity.ALARMS_NAME, new LinkedHashSet<String>(this.Alarms));

            AlarmManager alarmMgr = (AlarmManager) ClockActivity.context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ClockActivity.context, AlarmActivity.class);
            intent.setAction(ClockActivity.ALARM_ACTION);
            final int id = (int) new Alarm(date).MillisFromCurrent();
            PendingIntent alarmIntent = PendingIntent.getActivity(ClockActivity.context, id, intent, 0);
            alarmMgr.cancel(alarmIntent);
            alarmIntent.cancel();
            this.setList();
        }

    }
}
