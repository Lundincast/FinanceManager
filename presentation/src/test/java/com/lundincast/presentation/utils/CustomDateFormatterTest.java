package com.lundincast.presentation.utils;

import org.junit.Test;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the CustomDateFormatter logic.
 */
@SmallTest
public class CustomDateFormatterTest {

    @Test
    public void dateFormatter_CorrectDateIsToday_ReturnsTrue() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        assertEquals(CustomDateFormatter.getShortFormattedDate(cal), "Today");
    }

    @Test
    public void dateFormatter_CorrectDateIsYesterday_ReturnsTrue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        assertEquals(CustomDateFormatter.getShortFormattedDate(cal), "Yesterday");
    }

    @Test
    public void dateFormatter_CorrectNormalDate_ReturnsTrue() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 21);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.YEAR, 2016);
        assertEquals(CustomDateFormatter.getShortFormattedDate(cal), "21 Mar 2016");
    }
}
