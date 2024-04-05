package com.sec.internal.google;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsCallSession;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.telephony.ims.stub.ImsUtImplBase;

import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSessionListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMMTelFeature;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsService;
import com.android.ims.internal.IImsUt;

public class ImsMmtelFeature extends android.telephony.ims.compat.feature.MMTelFeature
{
    private final int mPhoneId;
    private int mServiceId;
    GoogleImsService mMainSvc;
    public ImsMmtelFeature(GoogleImsService inMainSvc, int inPhoneId) {
        mPhoneId = inPhoneId;
        setSlotId(inPhoneId);
        mMainSvc = inMainSvc;
        mServiceId = -1;
        setFeatureState(2);
    }

    /**
     * Notifies the MMTel feature that you would like to start a session. This should always be
     * done before making/receiving IMS calls. The IMS service will register the device to the
     * operator's network with the credentials (from ISIM) periodically in order to receive calls
     * from the operator's network. When the IMS service receives a new call, it will send out an
     * intent with the provided action string. The intent contains a call ID extra
     * {@link IImsCallSession#getCallId} and it can be used to take a call.
     *
     * @param incomingCallIntent When an incoming call is received, the IMS service will call
     * {@link PendingIntent#send} to send back the intent to the caller with
     * ImsManager#INCOMING_CALL_RESULT_CODE as the result code and the intent to fill in the call
     * ID; It cannot be null.
     * @param listener To listen to IMS registration events; It cannot be null
     * @return an integer (greater than 0) representing the session id associated with the session
     * that has been started.
     */
    public int startSession(PendingIntent incomingCallIntent, IImsRegistrationListener listener) {
        if (mServiceId == -1) {
            mServiceId = mMainSvc.open(mPhoneId, 1, incomingCallIntent, listener);
        }
        return mServiceId;
    }

    /**
     * End a previously started session using the associated sessionId.
     * @param sessionId an integer (greater than 0) representing the ongoing session. See
     * {@link #startSession}.
     */
    public void endSession(int sessionId) {
        if (sessionId != mServiceId) {
            throw new RuntimeException("Provided session ID (" + String.valueOf(sessionId) +
                    ") does not match with the phone ID (" + String.valueOf(mPhoneId) +
                    ")'s session ID (" + String.valueOf(mServiceId) + ")");
        }
        if (mServiceId != -1) {
            mMainSvc.close(mServiceId);
            mServiceId = -1;
        }
    }

    /**
     * Checks if the IMS service has successfully registered to the IMS network with the specified
     * service & call type.
     *
     * @param callSessionType a service type that is specified in {@link ImsCallProfile}
     *        {@link ImsCallProfile#SERVICE_TYPE_NORMAL}
     *        {@link ImsCallProfile#SERVICE_TYPE_EMERGENCY}
     * @param callType a call type that is specified in {@link ImsCallProfile}
     *        {@link ImsCallProfile#CALL_TYPE_VOICE_N_VIDEO}
     *        {@link ImsCallProfile#CALL_TYPE_VOICE}
     *        {@link ImsCallProfile#CALL_TYPE_VT}
     *        {@link ImsCallProfile#CALL_TYPE_VS}
     * @return true if the specified service id is connected to the IMS network; false otherwise
     */
    public boolean isConnected(int callSessionType, int callType) {
        return mMainSvc.isConnected(mServiceId, callSessionType, callType);
    }

    /**
     * Checks if the specified IMS service is opened.
     *
     * @return true if the specified service id is opened; false otherwise
     */
    public boolean isOpened() {
        if (mServiceId != -1) {
            return mMainSvc.isOpened(mServiceId);
        }
        return false;
    }

    /**
     * Add a new registration listener for the client associated with the session Id.
     * @param listener An implementation of IImsRegistrationListener.
     */
    public void addRegistrationListener(IImsRegistrationListener listener) {
        if (mServiceId != -1) {
            mMainSvc.setRegistrationListener(mServiceId, listener);
        }
    }

    @Override
    public int getFeatureState() {
        return super.getFeatureState();
    }

    /**
     * Remove a previously registered listener using {@link #addRegistrationListener} for the client
     * associated with the session Id.
     * @param listener A previously registered IImsRegistrationListener
     */
    public void removeRegistrationListener(IImsRegistrationListener listener) {
        if (mServiceId != -1) {
            mMainSvc.removeRegistrationListener(mServiceId, listener);

        }
    }

    /**
     * Creates a {@link ImsCallProfile} from the service capabilities & IMS registration state.
     *
     * @param sessionId a session id which is obtained from {@link #startSession}
     * @param callSessionType a service type that is specified in {@link ImsCallProfile}
     *        {@link ImsCallProfile#SERVICE_TYPE_NONE}
     *        {@link ImsCallProfile#SERVICE_TYPE_NORMAL}
     *        {@link ImsCallProfile#SERVICE_TYPE_EMERGENCY}
     * @param callType a call type that is specified in {@link ImsCallProfile}
     *        {@link ImsCallProfile#CALL_TYPE_VOICE}
     *        {@link ImsCallProfile#CALL_TYPE_VT}
     *        {@link ImsCallProfile#CALL_TYPE_VT_TX}
     *        {@link ImsCallProfile#CALL_TYPE_VT_RX}
     *        {@link ImsCallProfile#CALL_TYPE_VT_NODIR}
     *        {@link ImsCallProfile#CALL_TYPE_VS}
     *        {@link ImsCallProfile#CALL_TYPE_VS_TX}
     *        {@link ImsCallProfile#CALL_TYPE_VS_RX}
     * @return a {@link ImsCallProfile} object
     */
    public ImsCallProfile createCallProfile(int sessionId, int callSessionType, int callType) {
        if (sessionId != mServiceId) {
            throw new RuntimeException("Provided session ID (" + String.valueOf(sessionId) +
                    ") does not match with the phone ID (" + String.valueOf(mPhoneId) +
                    ")'s session ID (" + String.valueOf(mServiceId) + ")");
        }
        if (mServiceId != -1) {
            return mMainSvc.createCallProfile(sessionId, callSessionType, callType);
        }
        return null;
    }

    /**
     * Creates an {@link ImsCallSession} with the specified call profile.
     * Use other methods, if applicable, instead of interacting with
     * {@link ImsCallSession} directly.
     *
     * @param sessionId a session id which is obtained from {@link #startSession}
     * @param profile a call profile to make the call
     */
    public IImsCallSession createCallSession(int sessionId, ImsCallProfile profile,
                                             IImsCallSessionListener listener) {
        if (sessionId != mServiceId) {
            throw new RuntimeException("Provided session ID (" + String.valueOf(sessionId) +
                    ") does not match with the phone ID (" + String.valueOf(mPhoneId) +
                    ")'s session ID (" + String.valueOf(mServiceId) + ")");
        }
        if (mServiceId != -1) {
            return mMainSvc.createCallSession(sessionId, profile, listener);
        }
        return null;
    }

    /**
     * Retrieves the call session associated with a pending call.
     *
     * @param sessionId a session id which is obtained from {@link #startSession}
     * @param callId a call id to make the call
     */
    public IImsCallSession getPendingCallSession(int sessionId, String callId) {
        if (sessionId != mServiceId) {
            throw new RuntimeException("Provided session ID (" + String.valueOf(sessionId) +
                    ") does not match with the phone ID (" + String.valueOf(mPhoneId) +
                    ")'s session ID (" + String.valueOf(mServiceId) + ")");
        }
        if (mServiceId != -1) {
            return mMainSvc.getPendingCallSession(sessionId, callId);
        } else {
            return null;
        }
    }

    /**
     * @return The Ut interface for the supplementary service configuration.
     */
    public ImsUtImplBase getUtInterface() {
        return mMainSvc.getUtInterface(mServiceId);
    }

    /**
     * @return The config interface for IMS Configuration
     */
    public IImsConfig getConfigInterface() {
        return mMainSvc.getConfigInterface(mPhoneId);
    }

    /**
     * Signal the MMTelFeature to turn on IMS when it has been turned off using {@link #turnOffIms}
     */
    public void turnOnIms() {
        mMainSvc.turnOnIms(mPhoneId);
    }

    /**
     * Signal the MMTelFeature to turn off IMS when it has been turned on using {@link #turnOnIms}
     */
    public void turnOffIms() {
        mMainSvc.turnOffIms(mPhoneId);
    }

    /**
     * @return The Emergency call-back mode interface for emergency VoLTE calls that support it.
     */
    public ImsEcbmImplBase getEcbmInterface() {
        return mMainSvc.getEcbmInterface(mServiceId);
    }

    /**
     * Sets the current UI TTY mode for the MMTelFeature.
     * @param uiTtyMode An integer containing the new UI TTY Mode.
     * @param onComplete A {@link Message} to be used when the mode has been set.
     */
    public void setUiTTYMode(int uiTtyMode, Message onComplete) {
        if (mServiceId != -1) {
            mMainSvc.setUiTTYMode(mServiceId, uiTtyMode, onComplete);
        }
    }

    /**
     * @return MultiEndpoint interface for DEP notifications
     */
    public ImsMultiEndpointImplBase getMultiEndpointInterface() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void onFeatureRemoved() {
        if (mServiceId != -1) {
            mMainSvc.close(mServiceId);
            mServiceId = -1;
        }
    }
}