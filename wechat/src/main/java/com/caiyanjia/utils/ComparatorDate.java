package com.caiyanjia.utils;

import com.caiyanjia.Bean.Msg;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ComparatorDate implements Comparator<Msg> {

    SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss");


    @Override
    public int compare(Msg o1, Msg o2) {
        if(o1.getTime().before(o2.getTime())){
            return -1;
        } else {
            return 1;
        }
    }
}
