package com.sec.internal.google;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.android.ims.internal.IImsRegistrationListener;

public class ServiceProfile {
    private final RemoteCallbackList<IImsRegistrationListener> mRegistrationListener;
    private final PendingIntent mIncomingCallIntent;
    private final int mPhoneId;
    private final int mServiceClass;

    ServiceProfile(int phoneId, int serviceClass, IImsRegistrationListener listener,
                   PendingIntent incomingCallIntent) {
        mRegistrationListener = new RemoteCallbackList<IImsRegistrationListener>();
        mIncomingCallIntent = incomingCallIntent;
        mPhoneId = phoneId;
        mServiceClass = serviceClass;
        mRegistrationListener.register(listener);
    }

    public PendingIntent getIncomingCallIntent() {
        return mIncomingCallIntent;
    }

    public int getPhoneId() {
        return mPhoneId;
    }

    public int getServiceClass() {
        return mServiceClass;
    }

    public void setRegistrationListener(IImsRegistrationListener listener) {
        mRegistrationListener.register(listener);
    }

    public void removeRegistrationListener(IImsRegistrationListener listener) {
        mRegistrationListener.unregister(listener);
    }

    public void notifyRegistrationListenerRegistered(int tech) {
        int i = mRegistrationListener.beginBroadcast();
        while (i > 0) {
            i--;
            try {
                mRegistrationListener.getBroadcastItem(i).registrationConnectedWithRadioTech(tech);
            } catch (RemoteException e) {
                StackTraceElement[] exMsg = e.getStackTrace();
                Log.e("ServiceProfile", e.getMessage());
                for (StackTraceElement ste: exMsg) {
                    Log.e("ServiceProfile", ste.toString());
                }
            }
        }
        mRegistrationListener.finishBroadcast();
    }

    public void notifyRegistrationListenerRegisteredUriChanged(int tech, Uri[] inUri) {
        int i = mRegistrationListener.beginBroadcast();
        while (i > 0) {
            i--;
            try {
                mRegistrationListener.getBroadcastItem(i).registrationConnectedWithRadioTech(tech);
                mRegistrationListener.getBroadcastItem(i).registrationAssociatedUriChanged(inUri);
            } catch (RemoteException e) {
                StackTraceElement[] exMsg = e.getStackTrace();
                Log.e("ServiceProfile", e.getMessage());
                for (StackTraceElement ste: exMsg) {
                    Log.e("ServiceProfile", ste.toString());
                }
            }
        }
        mRegistrationListener.finishBroadcast();
    }

    public void notifyRegistrationListenerDeregistered(android.telephony.ims.ImsReasonInfo ri) {
        int i = mRegistrationListener.beginBroadcast();
        while (i > 0) {
            i--;
            try {
                mRegistrationListener.getBroadcastItem(i).registrationDisconnected(ri);
                mRegistrationListener.getBroadcastItem(i).registrationAssociatedUriChanged(null);
            } catch (RemoteException e) {
                StackTraceElement[] exMsg = e.getStackTrace();
                Log.e("ServiceProfile", e.getMessage());
                for (StackTraceElement ste: exMsg) {
                    Log.e("ServiceProfile", ste.toString());
                }
            }
        }
        mRegistrationListener.finishBroadcast();
    }
}
