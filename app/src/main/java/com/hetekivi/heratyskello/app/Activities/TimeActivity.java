package com.hetekivi.heratyskello.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import com.hetekivi.heratyskello.app.R;

public class TimeActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button saveAlarmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        this.timePicker = (TimePicker) findViewById(R.id.timePicker);
        this.saveAlarmButton = (Button) findViewById(R.id.saveAlarm);
        this.saveAlarmButton.setText(R.string.button_saveAlarm);
        this.saveAlarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAlarm(v);
            }
        });
    }

    public void saveAlarm(View view) {
        int hours = 0, minutes = 0, seconds = 0;
        if (Build.VERSION.SDK_INT >= 23 )
        {
            hours = this.timePicker.getHour();
            minutes = this.timePicker.getMinute();
        }
        else
        {
            hours = this.timePicker.getCurrentHour();
            minutes = this.timePicker.getCurrentMinute();
        }

        ClockActivity.CurrentAlarm.Time(
                hours,
                minutes,
                seconds
        );
        setResult(Activity.RESULT_OK, new Intent(this, DateActivity.class));
        finish();
    }
}
