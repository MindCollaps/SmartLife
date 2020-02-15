package de.mindcollaps.smartlife;

import android.util.Log;

import java.lang.reflect.Method;

import de.mindcollaps.smartlife.files.Device;
import de.mindcollaps.smartlife.files.Room;

public class UpdateHandler {

    static Room room;
    static boolean roomChanged = false;
    static Device device;
    static boolean deviceChanged = false;

    public static void setRoom(Room croom){
        room = croom;
        roomChanged = true;
        Log.d("UpdateHandler", "setRoom");
    }

    public static void setDevice(Device cdevice){
        device = cdevice;
        deviceChanged = true;
        Log.d("UpdateHandler", "setDevice");
    }

    public static Room getRoom() {
        Room mRoom = room;
        roomChanged = false;
        Log.d("UpdateHandler", "getRoom");
        return mRoom;
    }

    public static Device getDevice() {
        Device mDevice = device;
        deviceChanged = false;
        Log.d("UpdateHandler", "getDevice");
        return mDevice;
    }
}
