package com.mysite.expense.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeUtil {

    public static String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        return sdf.format(date);
    }
    
    // 문자열 날짜 => sql 날짜
    public static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = sdf.parse(dateString);
        return new Date(utilDate.getTime());
    }
}
