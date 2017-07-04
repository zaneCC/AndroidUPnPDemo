package com.zane.androidupnpdemo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zane.androidupnpdemo.control.ClingPlayControl;
import com.zane.androidupnpdemo.entity.ClingDeviceList;
import com.zane.androidupnpdemo.entity.IResponse;
import com.zane.androidupnpdemo.listener.BrowseRegistryListener;
import com.zane.androidupnpdemo.listener.ControlCallback;
import com.zane.androidupnpdemo.listener.DeviceListChangedListener;
import com.zane.androidupnpdemo.service.manager.ClingUpnpServiceManager;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.entity.ClingDevice;
import com.zane.androidupnpdemo.entity.IDevice;
import com.zane.androidupnpdemo.service.ClingUpnpService;
import com.zane.androidupnpdemo.service.SystemService;
import com.zane.androidupnpdemo.util.Utils;

import org.fourthline.cling.model.meta.Device;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    private ListView mDeviceList;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mTVSelected;

    private ArrayAdapter<ClingDevice> mDevicesAdapter;
    /**
     * 投屏控制器
     */
    private ClingPlayControl mClingPlayControl = new ClingPlayControl();

    /** 用于监听发现设备 */
    private BrowseRegistryListener mBrowseRegistryListener = new BrowseRegistryListener();

    private ServiceConnection mUpnpServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "mUpnpServiceConnection onServiceConnected");

            ClingUpnpService.LocalBinder binder = (ClingUpnpService.LocalBinder) service;
            ClingUpnpService beyondUpnpService = binder.getService();

            ClingUpnpServiceManager clingUpnpServiceManager = ClingUpnpServiceManager.getInstance();
            clingUpnpServiceManager.setUpnpService(beyondUpnpService);

            clingUpnpServiceManager.getRegistry().addListener(mBrowseRegistryListener);
            //Search on service created.
            clingUpnpServiceManager.searchDevices();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "mUpnpServiceConnection onServiceDisconnected");

            ClingUpnpServiceManager.getInstance().setUpnpService(null);
        }
    };

    private ServiceConnection mSystemServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "mSystemServiceConnection onServiceConnected");

            SystemService.LocalBinder systemServiceBinder = (SystemService.LocalBinder) service;
            //Set binder to SystemManager
            ClingUpnpServiceManager clingUpnpServiceManager = ClingUpnpServiceManager.getInstance();
            clingUpnpServiceManager.setSystemService(systemServiceBinder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "mSystemServiceConnection onServiceDisconnected");

            ClingUpnpServiceManager.getInstance().setSystemService(null);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        bindServices();
    }


    private void bindServices() {
        // Bind UPnP service
        Intent upnpServiceIntent = new Intent(MainActivity.this, ClingUpnpService.class);
        bindService(upnpServiceIntent, mUpnpServiceConnection, Context.BIND_AUTO_CREATE);
        // Bind System service
        Intent systemServiceIntent = new Intent(MainActivity.this, SystemService.class);
        bindService(systemServiceIntent, mSystemServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind UPnP service
        unbindService(mUpnpServiceConnection);
        // Unbind System service
        unbindService(mSystemServiceConnection);

        ClingUpnpServiceManager.getInstance().destroy();
        ClingDeviceList.getInstance().destroy();
    }

    private void initView() {
        mDeviceList = (ListView) findViewById(R.id.lv_devices);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mTVSelected = (TextView) findViewById(R.id.tv_selected);

        mDevicesAdapter = new DevicesAdapter(mContext);
        mDeviceList.setAdapter(mDevicesAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 选择连接设备
                ClingDevice item = mDevicesAdapter.getItem(position);
                if (Utils.isNull(item))
                    return;

                ClingUpnpServiceManager.getInstance().setSelectedDevice(item);

                Device device = item.getDevice();
                if (Utils.isNull(device))
                    return;

                String selectedDeviceName = String.format(getString(R.string.selectedText), device.getDetails().getFriendlyName());
                mTVSelected.setText(selectedDeviceName);
            }
        });

        // 设置发现设备监听
        mBrowseRegistryListener.setOnDeviceListChangedListener(new DeviceListChangedListener() {
            @Override
            public void onDeviceAdded(final IDevice device) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mDevicesAdapter.add((ClingDevice) device);
                    }
                });
            }

            @Override
            public void onDeviceRemoved(final IDevice device) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mDevicesAdapter.remove((ClingDevice) device);
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mDeviceList.setEnabled(false);

        mRefreshLayout.setRefreshing(false);
        refreshDeviceList();
        mDeviceList.setEnabled(true);
    }

    /**
     * 刷新设备
     */
    private void refreshDeviceList() {
        Collection<ClingDevice> devices = ClingUpnpServiceManager.getInstance().getDmrDevices();
        ClingDeviceList.getInstance().setClingDeviceList(devices);
        if (devices != null){
            mDevicesAdapter.clear();
            mDevicesAdapter.addAll(devices);
        }
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.bt_play:
                play();
                break;

            case R.id.bt_pause:
                pause();
                break;

            case R.id.bt_stop:
                stop();
                break;
        }
    }

    /**
     * 停止
     */
    private void stop() {
        mClingPlayControl.stop(new ControlCallback() {
            @Override
            public void success(IResponse response) {
                Log.e(TAG, "stop success");
            }

            @Override
            public void fail(IResponse response) {
                Log.e(TAG, "stop fail");
            }
        });
    }

    /**
     * 暂停
     */
    private void pause() {
        mClingPlayControl.pause(new ControlCallback() {
            @Override
            public void success(IResponse response) {
                Log.e(TAG, "pause success");
            }

            @Override
            public void fail(IResponse response) {
                Log.e(TAG, "pause fail");
            }
        });
    }

    /**
     * 播放视频
     */
    private void play() {
        mClingPlayControl.playNew("http://mp4.res.hunantv.com/video/1155/79c71f27a58042b23776691d206d23bf.mp4",
                new ControlCallback() {

                    @Override
                    public void success(IResponse response) {
                        Log.e(TAG, "play success");
                    }

                    @Override
                    public void fail(IResponse response) {
                        Log.e(TAG, "play fail");
                    }
                });
    }
}
