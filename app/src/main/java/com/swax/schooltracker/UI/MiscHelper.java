package com.swax.schooltracker.UI;

import java.time.LocalDate;

public class MiscHelper {

    public static Boolean checkDates(LocalDate start, LocalDate end){
        return start.isAfter(end);
    }

    public static Boolean checkPhone(String phone){
        //Check that the phone number matches expected patterns
        //Regex care of: https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers
        String phoneRegex = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        return phone.matches(phoneRegex);
    }

    public static Boolean checkEmail(String email){
        //Check that the email matches expected patterns
        //Regex care of: https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
        String emailRegex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}
