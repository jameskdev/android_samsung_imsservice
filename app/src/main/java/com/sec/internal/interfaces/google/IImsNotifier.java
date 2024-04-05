package com.sec.internal.interfaces.google;

import com.sec.ims.DialogEvent;
import com.sec.ims.ImsRegistration;
import com.sec.ims.ImsRegistrationError;
import com.sec.ims.volte2.data.ImsCallInfo;

public interface IImsNotifier {
    void notifyImsRegistration(ImsRegistration reg, boolean registered, ImsRegistrationError error);
    void onIncomingCall(int phoneId, int callId);
    void onIncomingPreAlerting(ImsCallInfo callInfo, String remoteUri);
    void onCdpnInfo(int phoneId, String calledPartyNumber, int timeout);
    void onDialogEvent(DialogEvent de);
    void onP2pRegCompleteEvent();
    void onP2pPushCallEvent(final DialogEvent de);
}
