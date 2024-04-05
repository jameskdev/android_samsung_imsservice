package com.sec.internal.interfaces.ims.core.imslogger;

import android.os.SystemProperties;

public interface ISignallingNotifier {
    public static final String ACTION_SIP_MESSAGE = "com.sec.imsservice.sip.signalling";
    public static final boolean DEBUG = SystemProperties.getInt("ro.debuggable", 0) == 1;
    public static final boolean ENG = SystemProperties.get("ro.build.type", "user").equals("eng");
    public static final String PERMISSION = "com.sec.imsservice.sip.signalling.READ_PERMISSION";
    public static final boolean SHIPBUILD = Boolean.parseBoolean(SystemProperties.get("ro.product_ship", "false"));;

    /* loaded from: classes.dex */
    public enum PackageStatus {
        NOT_INSTALLED,
        INSTALLED,
        DM_CONNECTED,
        DM_DISCONNECTED,
        EMERGENCY_MODE
    }

    boolean send(Object obj);
}
