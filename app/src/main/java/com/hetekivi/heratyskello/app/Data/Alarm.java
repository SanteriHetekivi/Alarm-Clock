package com.hetekivi.heratyskello.app.Data;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Created by Sante on 4.2.2016.
 */
public class Alarm implements Comparable<Alarm> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm";
    private LocalDateTime dateTime;
    public LocalDateTime DateTime(){ return this.dateTime; }
    public void DateTime(LocalDateTime dateTime){ this.dateTime = dateTime; }

    private int Seconds(){ return this.DateTime().getSecondOfMinute(); }
    public int Minutes(){ return this.DateTime().getMinuteOfHour(); }
    public int Hours(){ return this.DateTime().getHourOfDay(); }
    public int Day(){ return this.DateTime().getDayOfMonth(); }
    public int Month(){ return this.DateTime().getMonthOfYear(); }
    public int Year(){ return this.DateTime().getYear(); }

    public void Date(int year, int month, int day)
    {
        this.DateTime(new LocalDateTime(year, month, day, this.Hours(), this.Minutes(), this.Seconds()));
    }

    public void Time(int hours, int minutes, int seconds)
    {
        this.DateTime(new LocalDateTime(this.Year(), this.Month(), this.Day(), hours, minutes, seconds));
    }

    public void DateTime(int year, int month, int day, int hours, int minutes, int seconds)
    {
        this.Date(year, month, day);
        this.Time(hours, minutes, seconds);
    }
    public Alarm(LocalDateTime dateTime)
    {
        this.DateTime(dateTime);
    }

    public Alarm()
    {
        this.DateTime(new LocalDateTime());
    }

    public Alarm(String date)
    {
        this.fromString(date);
    }

    public long MillisFromCurrent()
    {
        DateTimeZone zone = DateTimeZone.getDefault();
        long millis = this.DateTime().toDateTime(zone).getMillis();
        return millis;
    }

    public String toString()
    {
        return this.DateTime().toString(this.PATTERN);
    }

    public void fromString(String date)
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(this.PATTERN);
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        this.DateTime(dateTime);
    }

    @Override
    public int compareTo(Alarm alarm) {
        return this.DateTime().compareTo(alarm.DateTime());
    }
}
