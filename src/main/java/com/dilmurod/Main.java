package com.dilmurod;

import lombok.SneakyThrows;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        int date = localDate.getDayOfMonth();

        System.out.println("\n \t  localDate => "+ localDate +" \n \t date => "+ date);

//        LocalDate localDate = LocalDate.now();
//        int year = localDate.getYear();
//        String sDate = year+"/01/01";
//        String sDate2 = year+"/03/31";
//
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
//
//        Date date1 = formatter1.parse(sDate);
//        Date date02 = formatter1.parse(sDate2);
//
//        System.out.println("\n \t  date1 => " + date1 + " \n \t date02 => " + date02);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
        System.out.println(calendar.get(Calendar.DATE));
    }
}
