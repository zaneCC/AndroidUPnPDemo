package com.zane.androidupnpdemo;

import com.zane.androidupnpdemo.service.ClingUpnpService;
import com.zane.androidupnpdemo.service.SystemService;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/28 16:30
 */

public interface IClingUpnpServiceManager extends IUpnpServiceManager {

    void setUpnpService(ClingUpnpService upnpService);

    void setSystemService(SystemService systemService);
}
