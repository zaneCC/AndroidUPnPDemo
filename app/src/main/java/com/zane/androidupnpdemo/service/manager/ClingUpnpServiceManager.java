package com.zane.androidupnpdemo.service.manager;

import android.support.annotation.Nullable;

import com.zane.androidupnpdemo.entity.ClingControlPoint;
import com.zane.androidupnpdemo.entity.ClingDevice;
import com.zane.androidupnpdemo.entity.IControlPoint;
import com.zane.androidupnpdemo.entity.IDevice;
import com.zane.androidupnpdemo.service.ClingUpnpService;
import com.zane.androidupnpdemo.service.SystemService;
import com.zane.androidupnpdemo.util.ListUtils;
import com.zane.androidupnpdemo.util.Utils;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.Registry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/27 18:12
 */

public class ClingUpnpServiceManager implements IClingUpnpServiceManager {

    public static final ServiceType CONTENT_DIRECTORY_SERVICE = new UDAServiceType("ContentDirectory");
    public static final ServiceType AV_TRANSPORT_SERVICE = new UDAServiceType("AVTransport");
    public static final ServiceType RENDERING_CONTROL_SERVICE = new UDAServiceType("RenderingControl");
    public static final DeviceType dmrDeviceType = new UDADeviceType("MediaRenderer");

    private static ClingUpnpServiceManager INSTANCE = null;
    //Service
    private ClingUpnpService mUpnpService;
    private SystemService mSystemService;

    private ClingUpnpServiceManager() {
    }

    public static ClingUpnpServiceManager getInstance() {
        if (Utils.isNull(INSTANCE)) {
            INSTANCE = new ClingUpnpServiceManager();
        }
        return INSTANCE;
    }


    @Override
    public void searchDevices() {
        if (!Utils.isNull(mUpnpService)) {
            mUpnpService.getControlPoint().search();
        }
    }

    @Override
    @Nullable
    public Collection<ClingDevice> getDmrDevices() {
        if (Utils.isNull(mUpnpService))
            return null;

        Collection<Device> devices = mUpnpService.getRegistry().getDevices(dmrDeviceType);
        if (ListUtils.isEmpty(devices)) {
            return null;
        }

        Collection<ClingDevice> clingDevices = new ArrayList<>();
        for (Device device : devices) {
            ClingDevice clingDevice = new ClingDevice(device);
            clingDevices.add(clingDevice);
        }
        return clingDevices;
    }

    @Override
    @Nullable
    public IControlPoint getControlPoint() {
        if (Utils.isNull(mUpnpService))
            return null;
        ClingControlPoint.getInstance().setControlPoint(mUpnpService.getControlPoint());

        return ClingControlPoint.getInstance();
    }

    @Override
    public Registry getRegistry() {
        return mUpnpService.getRegistry();
    }

    @Override
    public IDevice getSelectedDevice() {
        if (Utils.isNull(mSystemService)){
            return null;
        }
        return mSystemService.getSelectedDevice();
    }

    @Override
    public void setSelectedDevice(IDevice device) {
        mSystemService.setSelectedDevice(device, mUpnpService.getControlPoint());
    }

    @Override
    public void setUpnpService(ClingUpnpService upnpService) {
        mUpnpService = upnpService;
    }

    @Override
    public void setSystemService(SystemService systemService) {
        mSystemService = systemService;
    }

    @Override
    public void destroy() {
        mUpnpService.onDestroy();
    }
}
