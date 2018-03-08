package com.example.jession_ding.newsapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jession Ding
 * @email jession_ding@foxmail.com
 * created at  2018/2/7 14:00
 * Description 新闻菜单分类
 */
// 用 Gson 填充 JavaBean 的原则
    // 1.类里出现的成员变量的命名，需要与 json 字符串里的 key 一样；若不一样，不会报错，只是不能找到同名的 key，去给他赋值
    // 2.如果你的 bean 里面多写了某个字段，也不会报错，只是不能找到同名的 key，去给他赋值
    // 3.如果你的 bean 里少了某个字段，也不会报错，只是无法得到该字段的值
public class Categories {
    int retcode;
    public ArrayList<MenuDataInfo> data;

    public class ChildrenInfo {
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "ChildrenInfo{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class MenuDataInfo {
        int id;
        public String title;
        int type;
        String url;
        String url1;
        public List<ChildrenInfo> children;

        @Override
        public String toString() {
            return "MenuDataInfo{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    ", url1='" + url1 + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Categories{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}