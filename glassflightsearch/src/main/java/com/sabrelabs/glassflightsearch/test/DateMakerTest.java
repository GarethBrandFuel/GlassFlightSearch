package com.sabrelabs.glassflightsearch.test;

import com.sabrelabs.glassflightsearch.util.DateMaker;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by barrettclark on 4/30/14.
 */
public class DateMakerTest extends TestCase {
    Calendar cal;

    protected void setUp() {
        cal = Calendar.getInstance();
    }

    public void testDateMaker() {
        Integer year = cal.get(Calendar.YEAR);
        Calendar date = new GregorianCalendar(year, 0, 25);
        Calendar testDate = new DateMaker().translate("January", 25);
        assertEquals(date, testDate);
    }

    public void testDateMakerWithSeasonDefaultsToCurrentMonth() {
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        Calendar date = new GregorianCalendar(year, month, 25);
        Calendar testDate = new DateMaker().translate("spring", 25);
        assertEquals(date, testDate);
    }

    public void testDateFormat() {
        Calendar date = new GregorianCalendar(2014, 0, 1);
        assertEquals("2014-01-01", new DateMaker().stringify(date));
    }
}
