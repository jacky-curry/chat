package com.caiyanjia.utils;

import com.caiyanjia.Bean.Msg;
import com.caiyanjia.Bean.groupMsg;

import java.util.Comparator;

public class ComparatorDate2 implements Comparator<groupMsg> {


    @Override
    public int compare(groupMsg o1, groupMsg o2) {
        if(o1.getTime().before(o2.getTime())){
            return -1;
        } else {
            return 1;
        }
    }
}
