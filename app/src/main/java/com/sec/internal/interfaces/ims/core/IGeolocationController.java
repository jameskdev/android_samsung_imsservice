package com.sec.internal.interfaces.ims.core;

import com.sec.internal.constants.ims.gls.LocationInfo;
import com.sec.internal.helper.os.ServiceStateWrapper;

public interface IGeolocationController extends ISequentialInitializable {
    LocationInfo getGeolocation();

    boolean isCountryCodeLoaded(int i);

    boolean isLocationServiceEnabled();

    void notifyEpdgAvailable(int i, int i2);

    void notifyServiceStateChanged(int i, ServiceStateWrapper serviceStateWrapper);

    boolean startGeolocationUpdate(int i, boolean z);

    boolean startGeolocationUpdate(int i, boolean z, int i2);

    void stopGeolocationUpdate();

    void stopPeriodicLocationUpdate(int i);

    boolean updateGeolocationFromLastKnown(int i);
}
