package com.zane.androidupnpdemo.listener;

import com.zane.androidupnpdemo.entity.IResponse;

/**
 * 说明：设备控制操作 回调
 * 作者：zhouzhan
 * 日期：17/7/4 10:56
 */

public interface ControlCallback {

    void success(IResponse response);

    void fail(IResponse response);
}
