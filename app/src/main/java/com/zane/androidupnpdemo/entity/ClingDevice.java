package com.zane.androidupnpdemo.entity;

import org.fourthline.cling.model.meta.Device;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/27 17:47
 */

public class ClingDevice  implements IDevice<Device> {

    private Device device;

    public ClingDevice(Device device) {
        this.device = device;
    }

    @Override
    public Device getDevice() {
        return device;
    }
}
