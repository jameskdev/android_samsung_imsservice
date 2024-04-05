package com.sec.internal.google;

import android.app.PendingIntent;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.compat.feature.MMTelFeature;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsUtImplBase;

import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSessionListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsService;
import com.android.ims.internal.IImsUt;
import com.android.internal.telephony.gsm.SsData;

public class GoogleImsService {
    private static GoogleImsService mInstance;


    public int open(int phoneId, int serviceClass, PendingIntent incomingCallIntent, IImsRegistrationListener listener) {
        return 0;
    }

    public void close(int serviceId)  {

    }

    public boolean isConnected(int serviceId, int serviceType, int callType) {
        return false;
    }

    public boolean isOpened(int serviceId) {
        return false;
    }

    public MMTelFeature onCreateMMTelImsFeature(int slotId) {
        return new ImsMmtelFeature(this, slotId);
    }

    public static GoogleImsService getInstanceIfReady() {
        if (mInstance != null) {
            return mInstance;
        }
        return null;
    }

    public static boolean isReady() {
        if (mInstance == null) {
            return false;
        }
        return true;
    }

    public static int getRegistrationTech(int networkType) {
        if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {
            return ServiceState.RIL_RADIO_TECHNOLOGY_LTE;
        }
        return networkType;
    }

    public void setRegistrationListener(int serviceId, IImsRegistrationListener listener)  {

    }

    public void addRegistrationListener(int phoneId, int serviceClass, IImsRegistrationListener listener) {

    }

    public void removeRegistrationListener(int serviceId, IImsRegistrationListener var3) {

    }

    public ImsCallProfile createCallProfile(int serviceId, int serviceType, int callType) {
        return null;
    }

    public IImsCallSession createCallSession(int serviceId, ImsCallProfile profile, IImsCallSessionListener listener) {
        return null;
    }

    public IImsCallSession getPendingCallSession(int serviceId, String callId)  {
        return null;
    }

    public ImsUtImplBase getUtInterface(int serviceId)  {
        return null;
    }

    public IImsConfig getConfigInterface(int phoneId)  {
        return null;
    }

    public void turnOnIms(int phoneId)  {

    }

    public void turnOffIms(int phoneId)  {

    }

    public ImsEcbmImplBase getEcbmInterface(int serviceId)  {
        return null;
    }

    public void setUiTTYMode(int serviceId, int uiTtyMode, Message onComplete)  {

    }

    public IImsMultiEndpoint getMultiEndpointInterface(int serviceId)  {
        return null;
    }
}
