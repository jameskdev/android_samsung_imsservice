package com.sec.internal.interfaces.ims.core;

import android.net.Uri;
import com.sec.ims.settings.ImsProfile;
import com.sec.ims.util.ImsUri;
import com.sec.ims.util.SipError;
import com.sec.internal.constants.ims.core.RegistrationConstants;
import com.sec.internal.constants.ims.gls.LocationInfo;
import com.sec.internal.ims.core.RegistrationGovernor;
import java.util.List;
import java.util.Set;

public interface IRegistrationGovernor {
    public static final int EVENT_PRESENCE = 1;
    public static final int EVENT_REGISTRATION = 0;

    public enum CallEvent {
        EVENT_CALL_UNKNOWN,
        EVENT_CALL_ESTABLISHED,
        EVENT_CALL_LAST_CALL_END,
        EVENT_CALL_ALT_SERVICE_INITIAL_REGI,
        EVENT_CALL_ALT_SERVICE_EMERGENCY_REGI,
        EVENT_CALL_ALT_SERVICE_EMERGENCY
    }

    public enum ThrottleState {
        IDLE,
        TEMPORARY_THROTTLED,
        PERMANENTLY_STOPPED
    }

    void addDelay(int i);

    boolean allowRoaming();

    void checkAcsPcscfListChange();

    boolean checkEmergencyInProgress();

    void checkProfileUpdateFromDM(boolean z);

    List<String> checkValidPcscfIp(List<String> list);

    boolean determineDeRegistration(int i, int i2);

    void enableRcsOverIms(ImsProfile imsProfile);

    Set<String> filterService(Set<String> set, int i);

    void finishOmadmProvisioningUpdate();

    String getCurrentPcscfIp();

    int getFailureCount();

    int getFailureType();

    String getMatchedPdnFailReason(String[] strArr, String str);

    int getNextImpuType();

    long getNextRetryMillis();

    int getP2pListSize(int i);

    RegistrationGovernor.PcoType getPcoType();

    int getPcscfOrdinal();

    String[] getPdnFailureReasons();

    long getRetryTimeOnPdnFail();

    RegistrationConstants.RegisterTaskState getState();

    ThrottleState getThrottleState();

    int getWFCSubscribeForbiddenCounter();

    boolean hasEmergencyTaskInPriority(List<? extends IRegisterTask> list);

    boolean hasNetworkFailure();

    boolean hasPdnFailure();

    boolean hasValidPcscfIpList();

    void increasePcscfIdx();

    boolean isDelayedDeregisterTimerRunning();

    boolean isDeregisterOnLocationUpdate();

    boolean isExistRetryTimer();

    boolean isIPSecAllow();

    boolean isLocationInfoLoaded(int i);

    boolean isMatchedPdnFailReason(String[] strArr, String str);

    boolean isMobilePreferredForRcs();

    boolean isNeedDelayedDeregister();

    boolean isNeedToPendingUpdateRegistration(int i, boolean z, boolean z2, boolean z3);

    boolean isNoNextPcscf();

    boolean isNonVoLteSimByPdnFail();

    boolean isOmadmConfigAvailable();

    boolean isPse911Prohibited();

    boolean isReadyToDualRegister(boolean z);

    boolean isReadyToGetReattach();

    boolean isReadyToRegister(int i);

    boolean isSrvccCase();

    boolean isThrottled();

    void makeThrottle();

    boolean needImsNotAvailable();

    boolean needPendingPdnConnected();

    void notifyLocationTimeout();

    void notifyReattachToRil();

    void notifyVoLteOnOffToRil(boolean z);

    void onCallStatus(CallEvent callEvent, SipError sipError, int i);

    void onConfigUpdated();

    void onContactActivated();

    void onDelayedDeregister();

    void onDeregistrationDone(boolean z);

    void onLocationCacheExpiry();

    void onLteDataNetworkModeSettingChanged(boolean z);

    IRegisterTask onManualDeregister(boolean z);

    void onPackageDataCleared(Uri uri);

    void onPdnConnected();

    void onPdnRequestFailed(String str);

    void onPublishError(SipError sipError);

    void onRegEventContactUriNotification(List<ImsUri> list, int i, String str, String str2);

    void onRegistrationDone();

    void onRegistrationError(SipError sipError, int i, boolean z);

    void onRoamingDataChanged(boolean z);

    void onRoamingLteChanged(boolean z);

    void onServiceStateDataChanged(boolean z, boolean z2);

    SipError onSipError(String str, SipError sipError);

    void onSubscribeError(int i, SipError sipError);

    void onTelephonyCallStatusChanged(int i);

    void onTimsTimerExpired();

    boolean onUpdateGeolocation(LocationInfo locationInfo);

    boolean onUpdatedPcoInfo(String str, int i);

    void onVolteRoamingSettingChanged(boolean z);

    void onVolteSettingChanged();

    void onWfcProfileChanged(byte[] bArr);

    void releaseThrottle(int i);

    void requestLocation(int i);

    void resetAllPcscfChecked();

    void resetAllRetryFlow();

    void resetIPSecAllow();

    void resetPcoType();

    void resetPcscfList();

    void resetPcscfPreference();

    void resetPdnFailureInfo();

    void resetPermanentFailure();

    void resetRetry();

    void retryDNSQuery();

    void setNeedDelayedDeregister(boolean z);

    void setPcoType(RegistrationGovernor.PcoType pcoType);

    void setRetryTimeOnPdnFail(long j);

    void startOmadmProvisioningUpdate();

    void startTimsTimer(String str);

    void stopTimsTimer(String str);

    void unRegisterIntentReceiver();

    void updatePcscfIpList(List<String> list);
}
