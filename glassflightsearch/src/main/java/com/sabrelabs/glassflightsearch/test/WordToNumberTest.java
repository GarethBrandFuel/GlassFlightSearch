package com.sabrelabs.glassflightsearch.test;

import com.sabrelabs.glassflightsearch.util.WordToNumber;

import junit.framework.TestCase;

/**
 * Created by barrettclark on 4/30/14.
 */
public class WordToNumberTest extends TestCase {
    public void testTranslateWithNumbers() {
        assertEquals("1", new WordToNumber().translate("1st").toString());
        assertEquals("2", new WordToNumber().translate("2nd").toString());
        assertEquals("3", new WordToNumber().translate("3rd").toString());
        assertEquals("4", new WordToNumber().translate("4th").toString());
        assertEquals("5", new WordToNumber().translate("5th").toString());
        assertEquals("6", new WordToNumber().translate("6th").toString());
        assertEquals("7", new WordToNumber().translate("7th").toString());
        assertEquals("8", new WordToNumber().translate("8th").toString());
        assertEquals("9", new WordToNumber().translate("9th").toString());
        assertEquals("10", new WordToNumber().translate("10th").toString());
        assertEquals("11", new WordToNumber().translate("11th").toString());
        assertEquals("12", new WordToNumber().translate("12th").toString());
        assertEquals("13", new WordToNumber().translate("13th").toString());
        assertEquals("14", new WordToNumber().translate("14th").toString());
        assertEquals("15", new WordToNumber().translate("15th").toString());
        assertEquals("16", new WordToNumber().translate("16th").toString());
        assertEquals("17", new WordToNumber().translate("17th").toString());
        assertEquals("18", new WordToNumber().translate("18th").toString());
        assertEquals("19", new WordToNumber().translate("19th").toString());
        assertEquals("20", new WordToNumber().translate("20th").toString());
        assertEquals("21", new WordToNumber().translate("21st").toString());
        assertEquals("22", new WordToNumber().translate("22nd").toString());
        assertEquals("23", new WordToNumber().translate("23rd").toString());
        assertEquals("24", new WordToNumber().translate("24th").toString());
        assertEquals("25", new WordToNumber().translate("25th").toString());
        assertEquals("26", new WordToNumber().translate("26th").toString());
        assertEquals("27", new WordToNumber().translate("27th").toString());
        assertEquals("28", new WordToNumber().translate("28th").toString());
        assertEquals("29", new WordToNumber().translate("29th").toString());
        assertEquals("30", new WordToNumber().translate("30th").toString());
        assertEquals("31", new WordToNumber().translate("31st").toString());
    }

    public void testTranslateWithWords() {
        assertEquals("1", new WordToNumber().translate("first").toString());
        assertEquals("7", new WordToNumber().translate("seventh").toString());
        assertEquals("21", new WordToNumber().translate("twenty first").toString());
        assertEquals("12", new WordToNumber().translate("twelfth").toString());
    }
}
