package com.hgt.project.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
public class EmployeeConstant {

    public static final Map<Integer, String> STATUS = new HashMap<>(3);

    public static final Map<String, String>  STATION = new HashMap<>(5);

    static {
        STATUS.put(0, "正式");
        STATUS.put(1, "实习");
        STATUS.put(2, "离职");

        STATION.put("developer", "研发");
        STATION.put("tester", "测试");
        STATION.put("chief", "主管");
        STATION.put("manager", "经理");
        STATION.put("chairman", "董事长");
    }
}
