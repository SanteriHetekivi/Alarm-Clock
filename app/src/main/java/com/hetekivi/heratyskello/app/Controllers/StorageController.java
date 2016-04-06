package com.hetekivi.heratyskello.app.Controllers;
import android.content.SharedPreferences;
import com.hetekivi.heratyskello.app.Activities.ClockActivity;
import com.hetekivi.heratyskello.app.Data.Alarm;

import java.util.*;

/**
 * Created by Sante on 5.2.2016.
 */
public class StorageController
{
    public static SharedPreferences preferences;
    public static boolean Save(String name, Set<String> data)
    {
        if(preferences != null)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putStringSet(name, data).apply();
            return true;
        }
        else return false;
    }
    public static boolean Save(String name, String data)
    {
        if(preferences != null)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString(name, data).apply();
            return true;
        }
        else return false;
    }
    public static boolean Save(String name, Boolean data)
    {
        if(preferences != null)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putBoolean(name, data).apply();
            return true;
        }
        else return false;
    }
    public static boolean Save(String name, Long data)
    {
        if(preferences != null)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putLong(name, data).apply();
            return true;
        }
        else return false;
    }
    public static Set<String> LoadSet(String name)
    {
        Set<String> data = null;
        if(preferences != null)
        {
            data = preferences.getStringSet(name, data);
        }
        return data;
    }

    public static String LoadString(String name)
    {
        String data = null;
        if(preferences != null)
        {
            data = preferences.getString(name, data);
        }
        return data;
    }

    public static Boolean LoadBoolean(String name)
    {
        Boolean data = null;
        if(preferences != null)
        {
            data = preferences.getBoolean(name, data);
        }
        return data;
    }
    public static Long LoadLong(String name)
    {
        Long data = null;
        if(preferences != null)
        {
            data = preferences.getLong(name, data);
        }
        return data;
    }

    public static Set<String> Alarms()
    {
        Set<String> alarms = LoadSet(ClockActivity.ALARMS_NAME);
        if (alarms == null)
        {
            alarms = new LinkedHashSet<String>();
            StorageController.Save(ClockActivity.ALARMS_NAME, alarms);
        }
        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
        for (String date: alarms) {
            alarmList.add(new Alarm(date));
        }
        alarms = new LinkedHashSet<String>();
        if(alarmList.isEmpty() == false)
        {
            Collections.sort(alarmList);
            for (Alarm alarm: alarmList) {
                alarms.add(alarm.toString());
            }
        }
        return alarms;
    }
}
