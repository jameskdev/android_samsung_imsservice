package com.sec.internal.google;

import android.os.RemoteException;
import android.telephony.ims.stub.ImsEcbmImplBase;

import com.android.ims.internal.IImsEcbmListener;

public class ImsEcbmImpl extends ImsEcbmImplBase {
    public void enterEmergencyCallbackMode() {
        enteredEcbm();
    }

    public void setListener(IImsEcbmListener listener) throws RemoteException {
        getImsEcbm().setListener(listener);
    }

    @Override
    public void exitEmergencyCallbackMode() {
        exitedEcbm();
    }
}
