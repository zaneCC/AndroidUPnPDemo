package com.zane.androidupnpdemo.entity;

import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.DeviceIdentity;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/27 17:47
 */

public abstract class ClingDevice extends Device {

    public ClingDevice(DeviceIdentity identity) throws ValidationException {
        super(identity);
    }
}
