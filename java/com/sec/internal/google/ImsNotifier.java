package com.sec.internal.google;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.util.Log;
import com.android.ims.internal.IImsExternalCallStateListener;
import com.sec.ims.DialogEvent;
import com.sec.ims.ImsRegistration;
import com.sec.ims.ImsRegistrationError;
import com.sec.ims.volte2.IImsCallSession;
import com.sec.ims.volte2.data.CallProfile;
import com.sec.ims.volte2.data.ImsCallInfo;
import com.sec.internal.helper.CollectionUtils;
import com.sec.internal.ims.registry.ImsRegistry;
import com.sec.internal.interfaces.google.IImsNotifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImsNotifier implements IImsNotifier {
    private static final String LOG_TAG = "ImsNotifier";
    private GoogleImsService mGoogleImsService;

    public ImsNotifier(GoogleImsService googleImsService) {
        this.mGoogleImsService = googleImsService;
    }

    @Override
    public void notifyImsRegistration(ImsRegistration reg, boolean registered, ImsRegistrationError error) {
        Log.i(LOG_TAG, "notifyImsRegistration");
        boolean isUriChanged = this.mGoogleImsService.isOwnUrisChanged(reg.getPhoneId(), reg);
        notifyRegistrationListener(reg, registered, error, isUriChanged);
    }

    private void notifyRegistrationListener(ImsRegistration reg, boolean registered, ImsRegistrationError error, boolean uriChanged) {
        int phoneId = reg.getPhoneId();
        for (Integer key : GoogleImsService.mServiceList.keySet()) {
            ServiceProfile service = GoogleImsService.mServiceList.get(key);
            if (phoneId == service.getPhoneId()) {
                Log.i(LOG_TAG, "notifying Listener with status " + (registered ? 1 : 0));
                if (registered) {
                    int registrationTech = GoogleImsService.getRegistrationTech(reg.getCurrentRat());
                    if (!uriChanged) {
                        service.notifyRegistrationListenerRegistered(registrationTech);
                    } else {
                        service.notifyRegistrationListenerRegisteredUriChanged(registrationTech, this.mGoogleImsService.mOwnUris.get(Integer.valueOf(phoneId)));
                    }
                } else {
                    ImsReasonInfo reasonInfo = DataTypeConvertor.convertToGoogleImsReason(error.getSipErrorCode());
                    service.notifyRegistrationListenerDeregistered(reasonInfo);
                }
            }
        }
    }

    @Override
    public void onIncomingCall(int phoneId, int callId) {
        if (SystemProperties.getBoolean("net.sip.vzthirdpartyapi", false)) {
            return;
        }
        for (Integer key : GoogleImsService.mServiceList.keySet()) {
            ServiceProfile service = GoogleImsService.mServiceList.get(key);
            if (service != null && service.getPhoneId() == phoneId) {
                Intent fillIn = new Intent();
                fillIn.putExtra("android:imsCallID", Integer.toString(callId));
                fillIn.putExtra("android:imsServiceId", key);
                try {
                    IImsCallSession secCallSession = this.mGoogleImsService.mVolteServiceModule.getSessionByCallId(callId);
                    if (secCallSession != null) {
                        CallProfile cp = secCallSession.getCallProfile();
                        if (cp.getHistoryInfo() != null) {
                            fillIn.putExtra("com.samsung.telephony.extra.SEM_EXTRA_FORWARDED_CALL", cp.getHistoryInfo());
                        }
                        if (cp.getCallType() == 12) {
                            fillIn.putExtra("android:ussd", true);
                        }
                        this.mGoogleImsService.getCmcImsServiceUtil().onIncomingCall(phoneId, fillIn, service, secCallSession);
                    }
                } catch (RemoteException e) {
                    StackTraceElement[] exMsg = e.getStackTrace();
                    Log.e(LOG_TAG, e.getMessage());
                    for (StackTraceElement ste: exMsg) {
                        Log.e(LOG_TAG, ste.toString());
                    }
                }
                try {
                    service.getIncomingCallIntent().send(this.mGoogleImsService.mContext, 101, fillIn);
                } catch (PendingIntent.CanceledException ce) {
                    StackTraceElement[] exMsg = ce.getStackTrace();
                    Log.e(LOG_TAG, ce.getMessage());
                    for (StackTraceElement ste: exMsg) {
                        Log.e(LOG_TAG, ste.toString());
                    }
                }
            }
        }
    }

    @Override
    public void onIncomingPreAlerting(ImsCallInfo callInfo, String remoteUri) {
        int callId = callInfo.getCallId();
        IImsCallSession secPendingSession = this.mGoogleImsService.mVolteServiceModule.getPendingSession(Integer.toString(callId));
        if (secPendingSession != null) {
            try {
                onIncomingCall(secPendingSession.getPhoneId(), callId);
            } catch (RemoteException e) {
                StackTraceElement[] exMsg = e.getStackTrace();
                Log.e(LOG_TAG, e.getMessage());
                for (StackTraceElement ste: exMsg) {
                    Log.e(LOG_TAG, ste.toString());
                }
            }
        }
    }

    @Override
    public void onCdpnInfo(int phoneId, String calledPartyNumber, int timeout) {
    }

    @Override
    public void onDialogEvent(DialogEvent de) {
        ImsMultiEndPointImpl currMultEndpt = GoogleImsService.mMultEndPoints.get(Integer.valueOf(de.getPhoneId()));
        if (currMultEndpt != null) {
            int deCmcType = this.mGoogleImsService.getCmcTypeFromRegHandle(de.getRegId());
            currMultEndpt.setDialogInfo(de, deCmcType);
            currMultEndpt.requestImsExternalCallStateInfo();
        }
    }

    public void notifyFeatureCapableChanged(int phoneId) {
        Log.i(LOG_TAG, "notifyFeatureCapableChanged, phoneId: " + phoneId);
        for (Integer key : GoogleImsService.mServiceList.keySet()) {
            ServiceProfile service = GoogleImsService.mServiceList.get(key);
            if (service != null && phoneId == service.getPhoneId()) {
                service.notifyRegistrationFeatureCapabilityChanged(service.getServiceClass(), this.mGoogleImsService.convertCapaToFeature(this.mGoogleImsService.mCapabilities[phoneId]), null);
            }
        }
    }

    @Override
    public void onP2pRegCompleteEvent() {
        Log.d(LOG_TAG, "onP2pRegCompleteEvent");
        if (this.mGoogleImsService.isEnabledWifiDirectFeature()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        ImsNotifier.this.mGoogleImsService.getCmcImsServiceUtil().createCmcCallSession();
                    } catch (RemoteException e) {
                    }
                }
            });
        }
    }

    @Override
    public void onP2pPushCallEvent(final DialogEvent de) {
        Log.d(LOG_TAG, "onP2pPushCallEvent");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    ImsNotifier.this.mGoogleImsService.preparePushCall(de);
                } catch (RemoteException e) {
                }
            }
        });
    }
}
