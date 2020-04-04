package com.joypad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class scan_activity extends Activity {
	private final static String TAG = scan_activity.class.getSimpleName();
	private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> devices=new ArrayList<BluetoothDevice>();
    private boolean mScanning;
    private Handler mHandler;
    private ListView deviceListview;
    private List<String> deviceList=new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    // Stops scanning after 2 seconds.
    private long mExitTime;
    private static final long SCAN_PERIOD = 2000;
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        mHandler = new Handler();
        BluetoothLE_Init();
        UI_Init();
    }
    
    public void UI_Init(){
    	deviceListview=(ListView)findViewById(R.id.listview);  
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList);  
        deviceListview.setAdapter(adapter);  
        deviceListview.setOnItemClickListener(listener);  
    }
    public void BluetoothLE_Init()
    {
    	Log.i(TAG, "BluetoothLE_Init");
    	if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    	mBluetoothManager =(BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
        		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        		startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        deviceList.clear();
    	adapter.notifyDataSetChanged();
    	devices.clear();
    	getBondedDevice();
    	scanLeDevice(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
            	deviceList.clear();
            	adapter.notifyDataSetChanged();
            	devices.clear();
            	getBondedDevice();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case android.R.id.home:
            	scan_activity.this.finish();
            	//onBackPressed();
            	break;
        }
        return true;
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                } 
                else {
                		if(mBluetoothAdapter!=null){			//退出关闭蓝牙
                			mBluetoothAdapter.disable();
                		}
                		finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if(mScanning)
        	scanLeDevice(false);
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	deviceList.clear();
     	devices.clear();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        getBondedDevice();
        scanLeDevice(true);
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void getBondedDevice(){
    	if(mBluetoothAdapter==null)
    		return;
    	Set<BluetoothDevice> bonded_device=mBluetoothAdapter.getBondedDevices();
    	if(bonded_device.size()>0){ //存在已经配对过的蓝牙设备  
            for(Iterator<BluetoothDevice> it=bonded_device.iterator();it.hasNext();){  
            	BluetoothDevice device=it.next();
            	devices.add(device);  
            	deviceList.add(device.getName()+'\n'+device.getAddress());  
            }
            adapter.notifyDataSetChanged();
        } 
    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    //扫描回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	if(!devices.contains(device))
                	{
                		Log.i(TAG, "device");
                		devices.add(device);
                    	deviceList.add(device.getName()+'\n'+device.getAddress());  
                        adapter.notifyDataSetChanged();
                	}
                }
            });
        }
    };
    
    private OnItemClickListener listener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			BluetoothDevice device=	(BluetoothDevice) devices.get(position);
			if (device == null) 
				return;
            final Intent intent = new Intent(scan_activity.this, play_activity.class);
            intent.putExtra("name", device.getName());
            intent.putExtra("address", device.getAddress());
            startActivity(intent);
		}
	};
}
