package com.sec.internal.interfaces.ims.core;

import android.os.Message;
import android.util.Log;
import com.sec.ims.IImsRegistrationListener;
import com.sec.ims.ImsRegistration;
import com.sec.ims.options.Capabilities;
import com.sec.ims.settings.ImsProfile;
import com.sec.internal.constants.ims.os.NetworkEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRegistrationManager extends ISequentialInitializable {
    public static final int ID_SIM2_OFFSET = 1000;
    public static final String LOG_TAG = "RegiMgr";

    void addPendingUpdateRegistration(IRegisterTask iRegisterTask, int i);

    void blockVoWifiRegisterOnRoaminByCsfbError(int i, int i2);

    void bootCompleted();

    void deregister(IRegisterTask iRegisterTask, boolean z, boolean z2, int i, String str);

    void deregister(IRegisterTask iRegisterTask, boolean z, boolean z2, String str);

    void deregisterProfile(int i, int i2);

    void deregisterProfile(int i, int i2, boolean z);

    void deregisterProfile(List<Integer> list, boolean z, int i);

    void doPendingUpdateRegistration();

    int findBestNetwork(int i, ImsProfile imsProfile, IRegistrationGovernor iRegistrationGovernor);

    void forceNotifyToApp(int i);

    int getCmcLineSlotIndex();

    boolean getCsfbSupported(int i);

    int getCurrentNetwork(int i);

    int getCurrentNetworkByPhoneId(int i);

    String[] getCurrentPcscf(int i);

    IRegistrationGovernor getEmergencyGovernor(int i);

    String getImpi(ImsProfile imsProfile, int i);

    ImsProfile getImsProfile(int i, ImsProfile.PROFILE_TYPE profile_type);

    String getImsiByUserAgent(IUserAgent iUserAgent);

    String getImsiByUserAgentHandle(int i);

    NetworkEvent getNetworkEvent(int i);

    List<IRegisterTask> getPendingRegistration(int i);

    ImsProfile[] getProfileList(int i);

    IRegistrationGovernor getRegistrationGovernor(int i);

    IRegistrationGovernor getRegistrationGovernorByProfileId(int i, int i2);

    ImsRegistration getRegistrationInfo(int i);

    ImsRegistration[] getRegistrationInfo();

    ImsRegistration[] getRegistrationInfoByPhoneId(int i);

    Map<Integer, ImsRegistration> getRegistrationList();

    Set<String> getServiceForNetwork(ImsProfile imsProfile, int i, boolean z, int i2);

    int getTelephonyCallStatus(int i);

    IUserAgent getUserAgent(int i);

    IUserAgent getUserAgent(String str);

    IUserAgent getUserAgent(String str, int i);

    IUserAgent getUserAgentByImsi(String str, String str2);

    IUserAgent[] getUserAgentByPhoneId(int i, String str);

    IUserAgent getUserAgentByRegId(int i);

    IUserAgent getUserAgentOnPdn(int i, int i2);

    boolean hasOmaDmFinished();

    int isCmcRegistered(int i);

    boolean isEmergencyCallProhibited(int i);

    boolean isEpdnRequestPending(int i);

    boolean isInvite403DisabledService(int i);

    boolean isPdnConnected(ImsProfile imsProfile, int i);

    boolean isRcsRegistered(int i);

    boolean isSelfActivationRequired(int i);

    boolean isSuspended(int i);

    boolean isVoWiFiSupported(int i);

    void moveNextPcscf(int i, Message message);

    void notifyRCSAllowedChangedbyMDM();

    void notifyRomaingSettingsChanged(int i, int i2);

    void onDmConfigurationComplete();

    void refreshAuEmergencyProfile(int i);

    void registerCmcRegiListener(IImsRegistrationListener iImsRegistrationListener, int i);

    void registerListener(IImsRegistrationListener iImsRegistrationListener, int i);

    void registerListener(IImsRegistrationListener iImsRegistrationListener, boolean z, int i);

    void registerP2pListener(IImsRegistrationListener iImsRegistrationListener);

    int registerProfile(ImsProfile imsProfile, int i);

    void releaseThrottleByAcs(int i);

    void releaseThrottleByCmc(IRegisterTask iRegisterTask);

    void requestTryRegister(int i);

    void requestTryRegsiter(int i, long j);

    void sendDeregister(int i);

    void sendDeregister(int i, int i2);

    void sendDeregister(IRegisterTask iRegisterTask, long j);

    void sendDummyDnsQuery();

    void sendReRegister(int i, int i2);

    void setInvite403DisableService(boolean z, int i);

    void setOwnCapabilities(int i, Capabilities capabilities);

    void setRttMode(int i, boolean z);

    void setSSACPolicy(int i, boolean z);

    void setSilentLogEnabled(boolean z);

    void setThirdPartyFeatureTags(String[] strArr);

    void setTtyMode(int i, int i2);

    void startEmergencyRegistration(int i, Message message);

    void stopEmergencyPdnOnly(int i);

    void stopEmergencyRegistration(int i);

    void stopPdnConnectivity(int i, IRegisterTask iRegisterTask);

    void suspendRegister(boolean z, int i);

    void suspended(IRegisterTask iRegisterTask, boolean z);

    void unregisterCmcRegiListener(IImsRegistrationListener iImsRegistrationListener, int i);

    void unregisterListener(IImsRegistrationListener iImsRegistrationListener, int i);

    void updateChatService(int i);

    void updatePcoInfo(int i, String str, int i2);

    void updateRegistrationBySSAC(int i, boolean z);

    void updateTelephonyCallStatus(int i, int i2);

    static int getRegistrationInfoId(int profileId, int phoneId) {
        return (phoneId * 1000) + profileId;
    }

    static String getFormattedDeviceId(String deviceId) {
        String str;
        if (deviceId != null && deviceId.length() >= 14) {
            return deviceId.substring(0, 8) + "-" + deviceId.substring(8, 14) + "-0";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getFormattedDeviceId: ");
        if (deviceId == null) {
            str = "null!";
        } else {
            str = "length = " + deviceId.length();
        }
        sb.append(str);
        Log.d(LOG_TAG, sb.toString());
        return deviceId;
    }

    static int getDeregistrationTimeout(ImsProfile profile, int rat) {
        int deregistrationTimer = profile.getDeregTimeout(rat);
        if (deregistrationTimer < 1000) {
            Log.e(LOG_TAG, "Under 1000 Deregi Timer : " + deregistrationTimer);
            return 4000;
        }
        return deregistrationTimer;
    }
}
