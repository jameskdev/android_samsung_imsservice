package com.sec.internal.interfaces.ims.core;

import android.telephony.CellLocation;
import android.telephony.PreciseDataConnectionState;

public interface NetworkStateListener {
    void onCellLocationChanged(CellLocation cellLocation, int i);

    void onDataConnectionStateChanged(int i, boolean z, int i2);

    void onDefaultNetworkStateChanged(int i);

    void onEpdgConnected(int i);

    void onEpdgDeregisterRequested(int i);

    void onEpdgDisconnected(int i);

    void onEpdgIpsecDisconnected(int i);

    void onEpdgRegisterRequested(int i, boolean z);

    void onIKEAuthFAilure(int i);

    void onPreciseDataConnectionStateChanged(int i, PreciseDataConnectionState preciseDataConnectionState);
}
