package com.ws.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * bean工具类
 *
 * @author wangsen
 * @date 2022/03/08
 */
public class MyBeanUtils {

    /**
     * 工具类中都是静态方法时，为了避免别的类会创建该工具类对象 ，应该将构造方法改为私有
     */
    private MyBeanUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取所有的属性值为空属性名数组
     *
     * @param source 源
     * @return {@link String}
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }

}
