package com.sec.internal.interfaces.ims.core;

import android.net.Uri;
import android.os.Bundle;
import com.sec.ims.ImsRegistration;
import java.security.cert.X509Certificate;
import java.util.List;

public interface IRegistrationHandlerNotifiable {
    void notifyAirplaneModeChanged(int i);

    void notifyChatbotAgreementChanged(int i);

    void notifyConfigChanged(Uri uri, int i);

    void notifyContactActivated(int i, int i2);

    void notifyDeRegistered(Bundle bundle);

    void notifyDnsResponse(List<String> list, int i, int i2);

    void notifyEmergencyReady(int i);

    void notifyImsSettingChanged(Uri uri, int i);

    void notifyImsSettingUpdated(int i);

    void notifyLocationModeChanged();

    void notifyLteDataNetworkModeSettingChanged(boolean z, int i);

    void notifyMnoMapUpdated(Uri uri, int i);

    void notifyMobileDataPressedSettingeChanged(int i, int i2);

    void notifyMobileDataSettingeChanged(int i, int i2);

    void notifyRcsUserSettingChanged(int i, int i2);

    void notifyRefreshRegNotification(int i);

    void notifyRegEventContactUriNotification(Bundle bundle);

    void notifyRegistered(int i, int i2, ImsRegistration imsRegistration);

    void notifyRegistrationError(Bundle bundle);

    void notifyRoamingDataSettigChanged(int i, int i2);

    void notifyRoamingLteSettigChanged(boolean z);

    void notifySetupWizardCompleted();

    void notifySubscribeError(Bundle bundle);

    void notifyTriggeringRecoveryAction(IRegisterTask iRegisterTask, long j);

    void notifyVolteRoamingSettingChanged(boolean z, int i);

    void notifyVolteSettingChanged(boolean z, boolean z2, int i);

    void notifyVowifiSettingChanged(int i, long j);

    void notifyX509CertVerificationRequested(X509Certificate[] x509CertificateArr);

    void removeRecoveryAction();

    void sendTryRegister(int i);
}
