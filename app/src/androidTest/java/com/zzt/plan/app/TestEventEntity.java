package com.zzt.plan.app;

import android.test.InstrumentationTestCase;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-6-20.
 */
public class TestEventEntity extends InstrumentationTestCase {
    public void test() {
        List<Long> list = new ArrayList<>();
        list.add(1l);
        list.add(2l);
        list.add(15846464988466468l);
        JSONArray array = new JSONArray(list);
        System.out.println(array);
    }
}
