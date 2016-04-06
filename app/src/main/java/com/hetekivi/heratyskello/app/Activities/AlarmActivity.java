package com.hetekivi.heratyskello.app.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hetekivi.heratyskello.app.Data.Alarm;
import com.hetekivi.heratyskello.app.R;
import com.hetekivi.heratyskello.app.Controllers.StorageController;
import org.joda.time.LocalDateTime;

import java.util.Set;

public class AlarmActivity extends AppCompatActivity {


    private MediaPlayer mediaPlayer;
    private Button snoozeButton;
    private Button stopButton;
    private TextView titleView;
    private final int SNOOZE_MINUTES = 1;
    private Set<String> Alarms;
    private String time;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        setContentView(R.layout.activity_alarm);
        this.snoozeButton = (Button) findViewById(R.id.snoozeButton);
        this.stopButton = (Button) findViewById(R.id.stopButton);
        this.titleView = (TextView) findViewById(R.id.alarmTitle);
        this.time = new LocalDateTime().toString(Alarm.PATTERN);
        this.stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Stop();
            }
        });
        this.snoozeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snooze();
            }
        });
        this.stopButton.setText(R.string.button_stop);
        String snoozeText = res.getString(R.string.button_snooze) +
                "(" + SNOOZE_MINUTES + " " + res.getString(R.string.time_min) +  ")";
        this.snoozeButton.setText(snoozeText);
        this.titleView.setText(time);
        startAlarm();
    }

    private void startAlarm()
    {
        if(this.mediaPlayer == null)
        {
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            this.mediaPlayer.setLooping(true);
        }
        this.mediaPlayer.start();
    }

    private void stopAlarm()
    {
        if(this.mediaPlayer != null) this.mediaPlayer.stop();
        this.mediaPlayer = null;
        if(this.Alarms.contains(this.time))
        {
            this.Alarms.remove(this.time);
        }
        StorageController.Save(ClockActivity.ALARMS_NAME, this.Alarms);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.Alarms = StorageController.Alarms();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.Stop();
    }

    private void Stop()
    {
        this.stopAlarm();
    }

    private void Snooze()
    {
        Alarm alarm = new Alarm();
        alarm.DateTime(alarm.DateTime().plusMinutes(this.SNOOZE_MINUTES));
        if(this.Alarms.contains(alarm.toString()) == false)
        {
            AlarmManager alarmMgr = (AlarmManager) ClockActivity.context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ClockActivity.context, AlarmActivity.class);
            final int id = (int) alarm.MillisFromCurrent();
            PendingIntent alarmIntent = PendingIntent.getActivity(this, id, intent, 0);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarm.MillisFromCurrent(), alarmIntent);
            this.Alarms.add(alarm.toString());
            StorageController.Save(ClockActivity.ALARMS_NAME, this.Alarms);
        }
        this.stopAlarm();
    }

}
