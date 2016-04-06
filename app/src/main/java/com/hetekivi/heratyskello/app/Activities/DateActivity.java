package com.hetekivi.heratyskello.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.hetekivi.heratyskello.app.Activities.ClockActivity;
import com.hetekivi.heratyskello.app.Activities.TimeActivity;
import com.hetekivi.heratyskello.app.R;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DateActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Button setTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        this.datePicker = (DatePicker) findViewById(R.id.datePicker);
        this.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        this.datePicker.updateDate(ClockActivity.CurrentAlarm.Year(),
                ClockActivity.CurrentAlarm.Month() - 1,
                ClockActivity.CurrentAlarm.Day()
        );
        this.setTimeButton = (Button) findViewById(R.id.setTime);
        this.setTimeButton.setText(R.string.button_setTime);
        this.setTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectTime(v);
            }
        });

    }

    public void selectTime(View view) {

        int year = this.datePicker.getYear();
        int month = this.datePicker.getMonth() + 1;
        int day = this.datePicker.getDayOfMonth();
        ClockActivity.CurrentAlarm.Date(
                year,
                month,
                day
        );
        Intent intent = new Intent(this, TimeActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ClockActivity.ADD_ALARM) : {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK, new Intent(this, ClockActivity.class));
                    finish();
                }
                break;
            }
        }
    }
}
