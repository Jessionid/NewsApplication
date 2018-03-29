package com.example.jession_ding.newsapplication.constant;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/2/5 13:04
 * Description 存放接口信息
 */
public interface Constants {
    // 手机虚拟器，连接测试服务器
    String MOBILE_TEST_ENGINE = "http://10.0.2.2:8080";
    String TEST_ENGINE = "http://10.0.2.2:8080";
    // 网页，连接本地测试服务器
    //String TEST_ENGINE = "http://127.0.0.1:8080";
    // 手机，连接本地测试服务器
    // String TEST_ENGINE = "http://localhost:8080";
    // String TEST_ENGINE = "http://192.168.23.1:8080";

    // 新闻列表
    String newsList = TEST_ENGINE + "/zhbj/categories.json";

    // 组图列表
    String pictureList = TEST_ENGINE + "/zhbj/photos/photos_1.json";
}