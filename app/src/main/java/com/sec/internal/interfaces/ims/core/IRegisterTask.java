package com.sec.internal.interfaces.ims.core;

import android.net.Network;
import android.os.Message;
import com.sec.ims.ImsRegistration;
import com.sec.ims.settings.ImsProfile;
import com.sec.ims.util.SipError;
import com.sec.internal.constants.Mno;
import com.sec.internal.constants.ims.DiagnosisConstants;
import com.sec.internal.constants.ims.core.RegistrationConstants;
import com.sec.internal.ims.core.RcsRegistration;
import java.util.List;

public interface IRegisterTask extends PdnEventListener {
    void clearNotAvailableReason();

    void clearSuspended();

    void clearSuspendedBySnapshot();

    void clearUpdateRegisteringFlag();

    void clearUserAgent();

    int getDeregiCause(SipError sipError);

    int getDeregiReason();

    int getDnsQueryRetryCount();

    List<String> getFilteredReason();

    IRegistrationGovernor getGovernor();

    ImsRegistration getImsRegistration();

    int getLastRegiFailReason();

    Mno getMno();

    Network getNetworkConnected();

    int getNotAvailableReason();

    String getPani();

    String getPcscfHostname();

    int getPdnType();

    int getPhoneId();

    ImsProfile getProfile();

    RcsRegistration.Builder getRcsRegistrationBuilder();

    String getReason();

    int getRegiFailReason();

    DiagnosisConstants.REGI_REQC getRegiRequestType();

    int getRegistrationRat();

    Message getResultMessage();

    RegistrationConstants.RegisterTaskState getState();

    IUserAgent getUserAgent();

    boolean hasForcedPendingUpdate();

    boolean hasPendingDeregister();

    boolean hasPendingEpdgHandover();

    boolean hasPendingUpdate();

    boolean isEpdgHandoverInProgress();

    boolean isImmediatePendingUpdate();

    boolean isKeepPdn();

    boolean isNeedOmadmConfig();

    boolean isOneOf(RegistrationConstants.RegisterTaskState... registerTaskStateArr);

    boolean isRcsOnly();

    boolean isRefreshReg();

    boolean isSuspended();

    boolean isUpdateRegistering();

    void keepEmergencyTask(boolean z);

    boolean needKeepEmergencyTask();

    void resetTaskOnPdnDisconnected();

    void setDeregiReason(int i);

    void setDnsQueryRetryCount(int i);

    void setEpdgHandoverInProgress(boolean z);

    void setHasForcedPendingUpdate(boolean z);

    void setHasPendingDeregister(boolean z);

    void setHasPendingEpdgHandover(boolean z);

    void setImmediatePendingUpdate(boolean z);

    void setImsRegistration(ImsRegistration imsRegistration);

    void setIsRefreshReg(boolean z);

    void setKeepPdn(boolean z);

    void setLastRegiFailReason(int i);

    void setNotAvailableReason(int i);

    void setPaniSet(String str, String str2);

    void setPcscfHostname(String str);

    void setPdnType(int i);

    void setPendingUpdate(boolean z);

    void setReason(String str);

    void setRecoveryReason(String str);

    void setRegiFailReason(int i);

    void setRegiRequestType(DiagnosisConstants.REGI_REQC regi_reqc);

    void setRegistrationRat(int i);

    void setResultMessage(Message message);

    void setState(RegistrationConstants.RegisterTaskState registerTaskState);

    void setUpdateRegistering(boolean z);

    void setUserAgent(IUserAgent iUserAgent);
}
