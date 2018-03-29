package com.example.jession_ding.newsapplication.utils;

import com.example.jession_ding.newsapplication.constant.Constants;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/3/12 11:12
 * Description 分离字符串
 */
public class DetachingString {
    public String inString;
    public DetachingString(String inString) {
        this.inString = inString;
        detaching();
    }

    public String detaching() {
        String s = inString.replaceAll(Constants.MOBILE_TEST_ENGINE, Constants.TEST_ENGINE);
        return s;
    }
}
