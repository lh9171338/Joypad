package com.joypad;


import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class play_activity extends Activity {
	private final static String TAG = play_activity.class.getSimpleName();
	
	/*****************UI*********************/ 
	private RelativeLayout m_layout;
	private ImageButton ib_left,ib_right,ib_up,ib_down;
	private ImageButton ib_red,ib_blue,ib_green,ib_orange;
	private ImageButton ib_f1,ib_f2;
	private int r;
	private int detra=0;
	private int width,height;
	//读数据的服务和characteristic
	private String name;
	private String address;
    private BluetoothGattService rd_wr_GattService;
    private BluetoothGattCharacteristic rd_wr_Characteristic; 
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("未连接");
//		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.play);
		WindowManager wm = this.getWindowManager();
		 
	    width = wm.getDefaultDisplay().getWidth();
	    height = wm.getDefaultDisplay().getHeight();
	    if(width==1920){
	    	detra=10;
	    }
	    r=width/16;
		Intent intent = getIntent();
        name=intent.getStringExtra("name");
        address=intent.getStringExtra("address");
		UI_Init();
	}
	private void UI_Init(){
		m_layout=(RelativeLayout)findViewById(R.id.relativelayout);
		ib_left=new ImageButton(this);
		ib_right=new ImageButton(this);
		ib_up=new ImageButton(this);
		ib_down=new ImageButton(this);
		ib_red=new ImageButton(this);
		ib_blue=new ImageButton(this);
		ib_green=new ImageButton(this);
		ib_orange=new ImageButton(this);
		ib_f1=new ImageButton(this);
		ib_f2=new ImageButton(this);

		ib_left.setBackgroundColor(Color.WHITE);
		ib_left.setImageDrawable(getResources().getDrawable(R.drawable.left));
		SetViewPosition(ib_left,width/8-r-detra,height*2/4-r,2*r,2*r);
		ib_left.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_left_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_left_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_left);
    	
    	ib_right.setBackgroundColor(Color.WHITE);
    	ib_right.setImageDrawable(getResources().getDrawable(R.drawable.right));
		SetViewPosition(ib_right,width*3/8-r-detra*5,height*2/4-r,2*r,2*r);
		ib_right.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_right_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_right_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_right);
    	
    	ib_up.setBackgroundColor(Color.WHITE);
    	ib_up.setImageDrawable(getResources().getDrawable(R.drawable.up));
		SetViewPosition(ib_up,width*2/8-r-detra*3,height/4-r,2*r,2*r);
		ib_up.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_up_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_up_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_up);
    	
    	ib_down.setBackgroundColor(Color.WHITE);
    	ib_down.setImageDrawable(getResources().getDrawable(R.drawable.down));
		SetViewPosition(ib_down,width*2/8-r-detra*3,height*3/4-r,2*r,2*r);
		ib_down.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_down_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_down_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_down);
    	
    	ib_red.setBackgroundColor(Color.WHITE);
    	ib_red.setImageDrawable(getResources().getDrawable(R.drawable.red));
		SetViewPosition(ib_red,width*5/8-r+detra*5,height*2/4-r,2*r,2*r);
		ib_red.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_red_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_red_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_red);
    	
    	ib_blue.setBackgroundColor(Color.WHITE);
    	ib_blue.setImageDrawable(getResources().getDrawable(R.drawable.blue));
		SetViewPosition(ib_blue,width*7/8-r+detra,height*2/4-r,2*r,2*r);
		ib_blue.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_blue_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_blue_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_blue);
    	
    	ib_green.setBackgroundColor(Color.WHITE);
    	ib_green.setImageDrawable(getResources().getDrawable(R.drawable.green));
		SetViewPosition(ib_green,width*6/8-r+detra*3,height/4-r,2*r,2*r);
		ib_green.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_green_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_green_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_green);
    	
    	ib_orange.setBackgroundColor(Color.WHITE);
    	ib_orange.setImageDrawable(getResources().getDrawable(R.drawable.orange));
		SetViewPosition(ib_orange,width*6/8-r+detra*3,height*3/4-r,2*r,2*r);
		ib_orange.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_orange_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_orange_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_orange);
    	
    	ib_f1.setBackgroundColor(Color.WHITE);
    	ib_f1.setImageDrawable(getResources().getDrawable(R.drawable.f2));
		SetViewPosition(ib_f1,width*3/8,height*3/4,160,80);
		ib_f1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_f1_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_f1_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_f1);
    	
    	ib_f2.setBackgroundColor(Color.WHITE);
    	ib_f2.setImageDrawable(getResources().getDrawable(R.drawable.f2));
		SetViewPosition(ib_f2,width*5/8-160,height*3/4,160,80);
		ib_f2.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int iAction = event.getAction();  
		        if (iAction == MotionEvent.ACTION_DOWN) {   	// 按下  
		        	Send_Data(parameter.str_f2_down);
		        } else if (iAction == MotionEvent.ACTION_UP) {  // 弹起  
		        	Send_Data(parameter.str_f2_up);  
		        }  
				return false;
			}
		});
    	m_layout.addView(ib_f2);
		
	}
//	//设置控件坐标
//    private void SetViewPosition(View view,int x,int y){
//    	int width=RelativeLayout.LayoutParams.WRAP_CONTENT;
//    	int height=RelativeLayout.LayoutParams.WRAP_CONTENT;
//    	RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width,height); 
//    	layoutParams.leftMargin=x;
//    	layoutParams.topMargin=y;
//    	view.setLayoutParams(layoutParams);
//    }
    private void SetViewPosition(View view,int x,int y,int width,int height){
    	RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width,height); 
    	layoutParams.leftMargin=x;
    	layoutParams.topMargin=y-60;
    	view.setLayoutParams(layoutParams);
    }
    
    //service回调
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
           @Override
           public void onServiceConnected(ComponentName componentName, IBinder service) {
               mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
               if (!mBluetoothLeService.initialize()) {
                   Log.e(TAG, "Unable to initialize Bluetooth");
                   finish();
               }
               //自动连接蓝牙设备
               mBluetoothLeService.connect(address);
           }
           @Override
           public void onServiceDisconnected(ComponentName componentName) {
               mBluetoothLeService = null;
           }
   };
   
   //广播回调
   private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
           final String action = intent.getAction();
           if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
               mConnected = true;	//连接
               setTitle("已连接");
               Log.i(TAG, "mConnected:"+mConnected);
           } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
               mConnected = false;	//断开连接
               setTitle("未连接");
               Log.i(TAG, "mConnected:"+mConnected);
           } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
           	rd_wr_GattService= mBluetoothLeService.getSupportedGattServices(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
           	if(rd_wr_GattService==null)
           		Log.i(TAG, "rd_wr_GattService:null");
           	else
           	{
           		rd_wr_Characteristic=rd_wr_GattService.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
           	    if(rd_wr_Characteristic==null)
           	    	Log.i(TAG, "rd_wr_Characteristic:null");
           	    else
           	    {										
           	    	final int charaProp = rd_wr_Characteristic.getProperties();
           	        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
           	            if (mNotifyCharacteristic != null) {
           	                mBluetoothLeService.setCharacteristicNotification(
           	                        mNotifyCharacteristic, false);
           	                mNotifyCharacteristic = null;
           	            }
           	            mBluetoothLeService.readCharacteristic(rd_wr_Characteristic);
           	        }
           	        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
           	            mNotifyCharacteristic = rd_wr_Characteristic;
           	            mBluetoothLeService.setCharacteristicNotification(
           	            		rd_wr_Characteristic, true);
           	        } 
           	    }
           	}
           } else if (BluetoothLeService.ACTION_DATA_CHANGED.equals(action)) {
           	//接收数据
           }
           else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
           	String str=intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
           	Log.i(TAG, "ACTION_DATA_READ+"+str);
           }
       }
    };
   
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//横屏
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		//绑定服务
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(address);
            Log.d(TAG, "Connect request result=" + result);
        }
		super.onResume();
	}
	
	private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_READ);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_CHANGED);
        return intentFilter;
    }
	
	//发送数据
	private void Send_Data(String str)
    {
		if(rd_wr_Characteristic!=null){
			rd_wr_Characteristic.setValue(str);
			mBluetoothLeService.writeCharacteristic(rd_wr_Characteristic);
			Log.i(TAG, "sendmessage:"+str);
		}
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindService(mServiceConnection);
        mBluetoothLeService = null;
        unregisterReceiver(mGattUpdateReceiver);
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

