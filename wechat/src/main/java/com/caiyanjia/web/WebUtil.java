package com.caiyanjia.web;



import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class WebUtil {
    /**
     * 将所有请求参数注入到user对象中
     * @param value
     * @param bean
     */
    public static <T> T copyParamToBean(Map value, T bean) {
        try {
            BeanUtils.populate(bean,value);
//            System.out.println("注入之后" + bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

}
