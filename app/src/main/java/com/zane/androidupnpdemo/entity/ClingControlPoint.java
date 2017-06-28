package com.zane.androidupnpdemo.entity;

import org.fourthline.cling.controlpoint.ControlPoint;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/28 17:03
 */

public class ClingControlPoint implements IControlPoint {

    private static ClingControlPoint INSTANCE = null;
    public ControlPoint controlPoint;

    private ClingControlPoint() {
    }

    public static ClingControlPoint getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClingControlPoint();
        }
        return INSTANCE;
    }


}
