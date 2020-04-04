package com.joypad;

import java.util.HashMap;

public class parameter {
	//-------------------------receive data--------------------------------//

	//-------------------------sent data--------------------------------//
	public static final String str_left_down="0000";
	public static final String str_right_down="0001";
	public static final String str_up_down="0002";
	public static final String str_down_down="0003";
	public static final String str_red_down="0004";
	public static final String str_blue_down="0005";
	public static final String str_green_down="0006";
	public static final String str_orange_down="0007";
	public static final String str_f1_down="0008";
	public static final String str_f2_down="0009";
	
	public static final String str_left_up="0010";
	public static final String str_right_up="0011";
	public static final String str_up_up="0012";
	public static final String str_down_up="0013";
	public static final String str_red_up="0014";
	public static final String str_blue_up="0015";
	public static final String str_green_up="0016";
	public static final String str_orange_up="0017";
	public static final String str_f1_up="0018";
	public static final String str_f2_up="0019";
	
	private static HashMap<String, String> attributes = new HashMap();
    public static String DEVICE_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String DEVICE_CHARACTERISTIC_SERVICE = "0000ffe1-0000-1000-8000-00805f9b34fb";
    static {
        attributes.put("0000ffe0-0000-1000-8000-00805f9b34fb", "Device Service");
        attributes.put("0000ffe1-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
