package com.sec.internal.interfaces.ims.core.iil;

import com.sec.ims.ImsRegistration;
import com.sec.ims.ImsRegistrationError;

public interface IIilManager {
    void notifyImsReady(boolean z);

    void notifyImsRegistration(ImsRegistration imsRegistration, boolean z, ImsRegistrationError imsRegistrationError);
}
