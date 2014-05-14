package com.sabrelabs.glassflightsearch.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by barrettclark on 4/30/14.
 */
public class DateMaker {
    String[] monthsArray = {
            "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"
    };
    List<String> months = Arrays.asList(monthsArray);

    public Calendar translate(String monthName, Integer day) {
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        if (months.contains(monthName)) {
            month = months.indexOf(monthName);
        }
        return new GregorianCalendar(year, month, day);
    }

    public String stringify(Calendar date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date.getTime());
    }

    public java.util.Date dateify(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            java.util.Date date = df.parse(dateString);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return (new Date(2000,6,10));
        }
    }

    public String dateStringForCard(java.util.Date date) {
        String dateString = "";
        DateFormat df = new SimpleDateFormat("MMM d");
        dateString = df.format(date);
        return dateString;
    }

    public String dateStringForCardFromResult(String dateString) {
        return dateStringForCard(dateify(dateString));
    }
}
