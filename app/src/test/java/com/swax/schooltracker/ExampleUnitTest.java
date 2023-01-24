package com.swax.schooltracker;

import org.junit.Test;

import static org.junit.Assert.*;

import com.swax.schooltracker.UI.MiscHelper;
import com.swax.schooltracker.UI.TermActivity;

import java.time.LocalDate;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void dateCheck_isCorrect(){
        LocalDate start = LocalDate.of(2023, 1, 20);
        LocalDate end = LocalDate.of(2023, 1, 27);
        Boolean result = MiscHelper.checkDates(start, end);
        Boolean expected = false;
        assertEquals(result, expected);
    }

    @Test
    public void dateCheck_isBad(){
        LocalDate start = LocalDate.of(2023, 2, 20);
        LocalDate end = LocalDate.of(2023, 1, 27);
        Boolean result = MiscHelper.checkDates(start, end);
        Boolean expected = true;
        assertEquals(result, expected);
    }

    @Test
    public void phoneCheck_isCorrect(){
        String testPhone = "2065551212";
        Boolean result = MiscHelper.checkPhone(testPhone);
        Boolean expected = true;
        assertEquals(result, expected);
    }

    @Test
    public void phoneCheck_isBad(){
        String testPhone = "5551212";
        Boolean result = MiscHelper.checkPhone(testPhone);
        Boolean expected = false;
        assertEquals(result, expected);
    }

    @Test
    public void emailCheck_isCorrect(){
        String testEmail = "test@test.com";
        Boolean result = MiscHelper.checkEmail(testEmail);
        Boolean expected = true;
        assertEquals(result, expected);
    }

    @Test
    public void emailCheck_isBad(){
        String testEmail = "test@test";
        Boolean result = MiscHelper.checkEmail(testEmail);
        Boolean expected = false;
        assertEquals(result, expected);
    }
}