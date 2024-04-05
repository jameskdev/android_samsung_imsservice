package com.sec.internal.interfaces.ims.core;

import android.net.Network;
import android.telephony.CellLocation;
import com.sec.internal.constants.ims.os.EmcBsIndication;
import com.sec.internal.constants.ims.os.VoPsIndication;
import com.sec.internal.helper.os.LinkPropertiesWrapper;
import java.util.List;

public interface IPdnController extends ISequentialInitializable {
    public static final int APN_REQUEST_STARTED = 1;
    public static final int APN_TYPE_NOT_AVAILABLE = 2;

    CellLocation getCellLocation(int i, boolean z);

    List<String> getDnsServers(PdnEventListener pdnEventListener);

    EmcBsIndication getEmcBsIndication(int i);

    String getInterfaceName(PdnEventListener pdnEventListener);

    String getIntfNameByNetType(Network network);

    LinkPropertiesWrapper getLinkProperties(PdnEventListener pdnEventListener);

    int getMobileDataRegState(int i);

    int getVoiceRegState(int i);

    VoPsIndication getVopsIndication(int i);

    boolean isConnected(int i, PdnEventListener pdnEventListener);

    boolean isEmergencyEpdgConnected(int i);

    boolean isEpdgConnected(int i);

    boolean isEpsOnlyReg(int i);

    boolean isNetworkRequested(PdnEventListener pdnEventListener);

    boolean isPendedEPDGWeakSignal(int i);

    boolean isVoiceRoaming(int i);

    boolean isWifiConnected();

    void registerForNetworkState(NetworkStateListener networkStateListener);

    boolean removeRouteToHostAddress(int i, String str);

    boolean requestRouteToHostAddress(int i, String str);

    void setPendedEPDGWeakSignal(int i, boolean z);

    int startPdnConnectivity(int i, PdnEventListener pdnEventListener, int i2);

    int stopPdnConnectivity(int i, int i2, PdnEventListener pdnEventListener);

    void unregisterForNetworkState(NetworkStateListener networkStateListener);
}
