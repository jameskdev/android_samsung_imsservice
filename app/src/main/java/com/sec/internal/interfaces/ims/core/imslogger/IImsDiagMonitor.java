package com.sec.internal.interfaces.ims.core.imslogger;

import com.sec.ims.ImsRegistration;

public interface IImsDiagMonitor {
    public static final int DM_HTTP = 1;
    public static final int DM_SIP = 0;
    public static final int HTTP_NORMAL = 100;
    public static final int SIP_NORMAL = 0;

    void handleRegistrationEvent(ImsRegistration imsRegistration, boolean z);

    void notifyCallStatus(int i, String str, int i2, String str2);

    void onIndication(int i, String str, int i2, int i3, int i4, String str2, String str3, String str4, String str5);

    void onIndication(int i, String str, int i2, int i3, String str2, String str3, String str4, String str5);
}
