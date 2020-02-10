package com.example.cabpool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class CompareTime {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY-HH-mm");
    String currentTime,year,date,month,hour,minutes;

    void init(String time)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(time,"-");
        this.date=stringTokenizer.nextToken();
        this.month=stringTokenizer.nextToken();
        this.year=stringTokenizer.nextToken();
        this.hour=stringTokenizer.nextToken();
        this.minutes=stringTokenizer.nextToken().substring(0,2);

        if(time.contains("PM"))
            this.hour = Integer.toString((Integer.parseInt(this.hour) + 12));
    }

    static boolean isValid(String cabpoolDate,String cabpoolTime)
    {
        cabpoolDate = cabpoolDate.replaceAll("/","-");
        cabpoolTime = cabpoolTime.replace(':','-');

        CompareTime currentTime = new CompareTime();
        CompareTime cabpool = new CompareTime();
        currentTime.init(dateFormat.format(Calendar.getInstance().getTime()));
        cabpool.init(cabpoolDate+"-"+cabpoolTime);

        if (cabpool.year.compareTo(currentTime.year)>0)
            return true;
        else if(cabpool.year.compareTo(currentTime.year)<0)
            return false;
        else {
            if (cabpool.month.compareTo(currentTime.month)>0)
                return true;
            else if(cabpool.month.compareTo(currentTime.month)<0)
                return false;
            else {
                if (cabpool.date.compareTo(currentTime.date)>0)
                    return true;
                else if(cabpool.date.compareTo(currentTime.date)<0)
                    return false;
                else {
                    if (cabpool.hour.compareTo(currentTime.hour)>0)
                        return true;
                    else if(cabpool.hour.compareTo(currentTime.hour)<0)
                        return false;
                    else {
                        if (cabpool.minutes.compareTo(currentTime.minutes)>0)
                            return true;
                        else if(cabpool.minutes.compareTo(currentTime.minutes)<0)
                            return false;
                        else
                            return false;
                    }
                }
            }
        }

    }

}
