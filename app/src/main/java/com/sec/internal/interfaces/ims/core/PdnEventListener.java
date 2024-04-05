package com.sec.internal.interfaces.ims.core;

import android.net.Network;
import java.util.List;

public interface PdnEventListener {
    default void onConnected(int networkType, Network network) {
    }

    default void onDisconnected(int networkType, boolean isPdnUp) {
    }

    default void onSuspended(int networkType) {
    }

    default void onLocalIpChanged(int networkType, boolean isStackedIpChanged) {
    }

    default void onPcscfAddressChanged(int networkType, List<String> pcscf) {
    }

    default void onResumed(int networkType) {
    }

    default void onSuspendedBySnapshot(int networkType) {
    }

    default void onResumedBySnapshot(int networkType) {
    }

    default void onNetworkRequestFail() {
    }
}
