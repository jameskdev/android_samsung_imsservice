package com.sec.internal.google;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.aidl.IImsSmsListener;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsUtImplBase;
import android.text.TextUtils;
import android.util.Log;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSessionListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.internal.telephony.PublishDialog;
import com.samsung.android.cmcnsd.CmcNsdManager;
import com.sec.ims.DialogEvent;
import com.sec.ims.ImsRegistration;
import com.sec.ims.extensions.Extensions;
import com.sec.ims.extensions.TelephonyManagerExt;
import com.sec.ims.util.ImsUri;
import com.sec.ims.util.NameAddr;
import com.sec.ims.volte2.data.CallProfile;
import com.sec.internal.constants.Mno;
import com.sec.internal.constants.ims.ImsConstants;
import com.sec.internal.constants.ims.entitilement.NSDSNamespaces;
import com.sec.internal.constants.ims.servicemodules.volte2.CallConstants;
import com.sec.internal.google.cmc.CmcCallSessionManager;
import com.sec.internal.google.cmc.CmcConnectivityController;
import com.sec.internal.google.cmc.CmcImsCallSessionImpl;
import com.sec.internal.google.cmc.CmcImsServiceUtil;
import com.sec.internal.helper.CollectionUtils;
import com.sec.internal.helper.ImsCallUtil;
import com.sec.internal.helper.SimUtil;
import com.sec.internal.helper.UriUtil;
import com.sec.internal.helper.os.TelephonyManagerWrapper;
import com.sec.internal.ims.registry.ImsRegistry;
import com.sec.internal.ims.util.ImsUtil;
import com.sec.internal.interfaces.google.IGoogleImsService;
import com.sec.internal.interfaces.ims.servicemodules.IServiceModuleManager;
import com.sec.internal.interfaces.ims.servicemodules.volte2.IVolteServiceModule;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class GoogleImsService implements IGoogleImsService {
    private static final String IMS_CALL_PERMISSION = "android.permission.ACCESS_IMS_CALL_SERVICE";
    private static final String LOG_TAG = "GoogleImsService";
    ImsFeature.Capabilities[] mCapabilities;
    Context mContext;
    int mPhoneCount;
    IServiceModuleManager mServiceModuleManager;
    IVolteServiceModule mVolteServiceModule;
    static int mServiceIdRef = 0;
    static Map<Integer, ServiceProfile> mServiceList = new ConcurrentHashMap();
    static Map<Integer, ImsMultiEndPointImpl> mMultEndPoints = new ConcurrentHashMap();
    static GoogleImsService mInstance = null;
    static Map<Integer, IImsSmsListener> mSmsListenerList = new ConcurrentHashMap();
    static ImsSmsImpl[] mSmsImpl = {null, null};
    Map<Integer, ImsUtImplBase> mUtList = new ConcurrentHashMap();
    Map<Integer, ImsEcbmImpl> mImsEcbmList = new ConcurrentHashMap();
    Map<Integer, IImsConfig> mConfigs = new ConcurrentHashMap();
    Map<Integer, ImsCallSessionImpl> mCallSessionList = new ConcurrentHashMap();
    String mServiceUrn = "";
    Map<Integer, Uri[]> mOwnUris = new ConcurrentHashMap();
    IImsSmsListener mSmsListener = null;
    private ImsCallSessionImpl mConferenceHost = null;
    private boolean mIsInitialMerge = false;
    private Map<Integer, Bundle> mImsConferenceState = new HashMap();
    private ImsNotifier mImsNotifier = new ImsNotifier(this);
    private CmcImsServiceUtil mCmcImsServiceUtil = null;
    private CmcConnectivityController mConnectivityController = null;

    public static GoogleImsService getInstanceIfReady() {
        if (mInstance != null) {
            return mInstance;
        }
        return null;
    }

    public static boolean isReady() {
        if (mInstance == null) {
            return false;
        }
        return true;
    }

    public void setConnectivityController(CmcConnectivityController cController) {
        this.mConnectivityController = cController;
        this.mCmcImsServiceUtil = new CmcImsServiceUtil(this.mContext, this, this.mConnectivityController, this.mVolteServiceModule);
        this.mConnectivityController.setPhoneId(SimUtil.getDefaultPhoneId());
    }

    public boolean isEnabledWifiDirectFeature() {
        return this.mConnectivityController.isEnabledWifiDirectFeature();
    }

    public CmcConnectivityController.DeviceType getDeviceType() {
        return this.mConnectivityController.getDeviceType();
    }

    public CmcNsdManager getNsdManager() {
        return this.mConnectivityController.getNsdManager();
    }

    public CmcImsServiceUtil getCmcImsServiceUtil() {
        return this.mCmcImsServiceUtil;
    }

    public void preparePushCall(DialogEvent de) throws RemoteException {
        Log.i(LOG_TAG, "preparePushCall(), size: " + this.mCallSessionList.size());
        if (de == null && this.mCallSessionList.size() > 0) {
            Log.i(LOG_TAG, "Push for [PD]");
            for (Map.Entry<Integer, ImsCallSessionImpl> e : this.mCallSessionList.entrySet()) {
                ImsCallSessionImpl sessionImpl = e.getValue();
                if (sessionImpl.mSession != null && sessionImpl.isP2pPrimaryType(sessionImpl.mSession.getCmcType())) {
                    ImsReasonInfo reasonInfo = new ImsReasonInfo(6007, 6007);
                    sessionImpl.mListener.callSessionResumeFailed(reasonInfo);
                    return;
                }
            }
        } else if (de != null) {
            Log.i(LOG_TAG, "Push for [SD]");
            int deCmcType = getCmcTypeFromRegHandle(de.getRegId());
            mMultEndPoints.get(Integer.valueOf(de.getPhoneId())).setP2pPushDialogInfo(de, deCmcType);
        }
    }

    private GoogleImsService(Context context, IServiceModuleManager smm) {
        this.mContext = context;
        this.mServiceModuleManager = smm;
        this.mVolteServiceModule = smm.getVolteServiceModule();
        int phoneCount = TelephonyManagerWrapper.getInstance(this.mContext).getPhoneCount();
        this.mPhoneCount = phoneCount;
        this.mCapabilities = new ImsFeature.Capabilities[phoneCount];
        for (int i = 0; i < this.mPhoneCount; i++) {
            this.mCapabilities[i] = new ImsFeature.Capabilities();
            this.mOwnUris.put(Integer.valueOf(i), new Uri[0]);
        }
    }

    public static synchronized GoogleImsService getInstance(Context context, IServiceModuleManager smm) {
        GoogleImsService googleImsService;
        synchronized (GoogleImsService.class) {
            if (mInstance == null) {
                mInstance = new GoogleImsService(context, smm);
            }
            googleImsService = mInstance;
        }
        return googleImsService;
    }

    public static int getRegistrationTech(int networkType) {
        if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {
            return ServiceState.RIL_RADIO_TECHNOLOGY_LTE;
        }
        return networkType;
    }

    private int getIncreasedServiceId() {
        int i = mServiceIdRef + 1;
        mServiceIdRef = i;
        if (i >= 254) {
            mServiceIdRef = 0;
        }
        return mServiceIdRef;
    }

    public int open(final int phoneId, final int serviceClass, PendingIntent incomingCallIntent, IImsRegistrationListener listener) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "open");
        if (incomingCallIntent == null || listener == null) {
            throw null;
        }
        if (this.mVolteServiceModule == null) {
            IVolteServiceModule volteServiceModule = this.mServiceModuleManager.getVolteServiceModule();
            this.mVolteServiceModule = volteServiceModule;
            if (volteServiceModule == null) {
                throw new RemoteException("Not ready to open");
            }
        }
        int serviceId = ((Integer) mServiceList.entrySet().stream().
            filter((o) -> {
                Map.Entry value_o = (Map.Entry) o; 
                return value_o.getValue() != null && 
                    ((ServiceProfile) value_o.getValue()).getServiceClass() == serviceClass && 
                    ((ServiceProfile) value_o.getValue()).getPhoneId() == phoneId; }).
            findFirst().map((o) -> { return (Integer) ((Map.Entry) o).getKey(); }).
            orElse(Integer.valueOf(getIncreasedServiceId()))).intValue();
        ServiceProfile newServiceProfile = new ServiceProfile(phoneId, serviceClass, listener, incomingCallIntent);
        mServiceList.put(Integer.valueOf(serviceId), newServiceProfile);
        listener.registrationDisconnected(DataTypeConvertor.convertToGoogleImsReason(1000));
        if (serviceClass == 1) {
            listener.registrationFeatureCapabilityChanged(serviceClass, convertCapaToFeature(this.mCapabilities[phoneId]), (int[]) null);
            ImsRegistration[] registrationList = ImsRegistry.getRegistrationManager().getRegistrationInfo();
            if (!CollectionUtils.isNullOrEmpty(registrationList)) {
                for (ImsRegistration reg : registrationList) {
                    if (reg.getPhoneId() == phoneId) {
                        Log.i("notifyRegistrationListenerRegistered with rat " + reg.getCurrentRat(), "ServiceProfile");
                        listener.registrationConnectedWithRadioTech(getRegistrationTech(reg.getCurrentRat()));
                        if (isOwnUrisChanged(phoneId, reg)) {
                            listener.registrationAssociatedUriChanged(this.mOwnUris.get(Integer.valueOf(phoneId)));
                        }
                    }
                }
            }
        }
        return serviceId;
    }

    public void close(int serviceId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "close");
        mServiceList.remove(Integer.valueOf(serviceId));
    }

    public boolean isConnected(int serviceId, int serviceType, int callType) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "isConnected");
        if (mServiceList.containsKey(Integer.valueOf(serviceId))) {
            return true;
        }
        throw new RemoteException();
    }

    public boolean isOpened(int serviceId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "isOpened");
        return mServiceList.containsKey(Integer.valueOf(serviceId));
    }

    public void removeRegistrationListener(int serviceId, IImsRegistrationListener listener) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "removeRegistrationListener");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (service != null) {
            service.removeRegistrationListener(listener);
            mServiceList.put(Integer.valueOf(serviceId), service);
        }
    }

    public void setRegistrationListener(int serviceId, IImsRegistrationListener listener) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "setRegistrationListener");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (service != null) {
            service.setRegistrationListener(listener);
            mServiceList.put(Integer.valueOf(serviceId), service);
        }
    }

    public void addRegistrationListener(int phoneId, int serviceClass, IImsRegistrationListener listener) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "addRegistrationListener");
    }

    public ImsCallProfile createCallProfile(int serviceId, int serviceType, int callType) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "createCallProfile");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (isConnected(serviceId, serviceType, callType) && service != null) {
            ImsCallProfile profile = new ImsCallProfile(serviceType, callType);
            ImsRegistration[] registrations = ImsRegistry.getRegistrationInfoByPhoneId(service.getPhoneId());
            if (registrations != null) {
                int length = registrations.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    ImsRegistration regi = registrations[i];
                    if (regi != null && regi.getImsProfile() != null && regi.hasVolteService() && (serviceType == 2 || !regi.getImsProfile().hasEmergencySupport())) {
                        Mno mno = Mno.fromName(regi.getImsProfile().getMnoName());
                        boolean supportHeldHostMerge = mno == Mno.VZW || mno == Mno.USCC;
                        profile.setCallExtraBoolean("SupportHeldHostMerge", supportHeldHostMerge);
                        if (serviceType != 2 && regi.getImsProfile().getCmcType() == 0) {
                            profile.setCallExtra("CallRadioTech", String.valueOf(ServiceState.networkTypeToRilRadioTechnology(regi.getCurrentRat())));
                            break;
                        }
                    }
                    i++;
                }
            }
            return profile;
        }
        throw new RemoteException();
    }

    public IImsCallSession createCallSession(int serviceId, ImsCallProfile profile, IImsCallSessionListener listener) throws RemoteException, UnsupportedOperationException {
        IVolteServiceModule iVolteServiceModule;
        ImsUri normalizedUri;
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "createCallSession");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (!isOpened(serviceId) || (iVolteServiceModule = this.mVolteServiceModule) == null || service == null) {
            throw new RemoteException();
        }
        try {
            boolean isTtyMode = iVolteServiceModule.getTtyMode(service.getPhoneId()) != Extensions.TelecomManager.TTY_MODE_OFF;
            CallProfile callProfile = DataTypeConvertor.convertToSecCallProfile(service.getPhoneId(), profile, isTtyMode);
            int cmcType = 0;
            this.mCmcImsServiceUtil.setServiceProfile(service);
            Bundle oemExtras = profile.mCallExtras.getBundle("android.telephony.ims.extra.OEM_EXTRAS");
            if (oemExtras != null) {
                String janskyMsisdn = oemExtras.getString("com.samsung.telephony.extra.CALL_START_WITH_JANSKY_MSISDN");
                if (!TextUtils.isEmpty(janskyMsisdn) && (normalizedUri = this.mVolteServiceModule.getNormalizedUri(service.getPhoneId(), janskyMsisdn)) != null) {
                    callProfile.setLineMsisdn(UriUtil.getMsisdnNumber(normalizedUri));
                    callProfile.setOriginatingUri(normalizedUri);
                }
                if (oemExtras.containsKey("com.samsung.telephony.extra.CMC_TYPE")) {
                    cmcType = oemExtras.getInt("com.samsung.telephony.extra.CMC_TYPE");
                }
            }
            int cmcType2 = this.mCmcImsServiceUtil.prepareCallSession(cmcType, profile, callProfile, service);
            if (cmcType2 > 0) {
                return this.mCmcImsServiceUtil.createCallSession(cmcType2, serviceId, profile, callProfile, service);
            }
            ImsRegistration[] registrations = ImsRegistry.getRegistrationInfoByPhoneId(service.getPhoneId());
            if (registrations != null) {
                for (ImsRegistration regi : registrations) {
                    if (regi != null && regi.getImsProfile() != null && regi.hasVolteService() && cmcType2 == regi.getImsProfile().getCmcType() && callProfile.getUrn() == "urn:service:unspecified") {
                        if (this.mServiceUrn.isEmpty()) {
                            callProfile.setUrn(ImsCallUtil.ECC_SERVICE_URN_DEFAULT);
                        } else {
                            callProfile.setUrn(this.mServiceUrn);
                            this.mServiceUrn = "";
                        }
                    }
                }
            }
            callProfile.setCmcType(cmcType2);
            if (SimUtil.isSoftphoneEnabled() && callProfile.getCallType() == 7) {
                callProfile.setCallType(13);
                callProfile.setUrn((String) null);
            }
            int volteRegHandle = getVolteRegHandle(service);
            if (ImsRegistry.getCmcAccountManager().isSecondaryDevice() && volteRegHandle == -1 && TelephonyManagerWrapper.getInstance(this.mContext).isVoiceCapable() && cmcType2 == 0 && !ImsUtil.isCdmalessEnabled(service.getPhoneId()) && !ImsCallUtil.isE911Call(callProfile.getCallType())) {
                callProfile.setForceCSFB(true);
            }
            com.sec.ims.volte2.IImsCallSession secCallSession = this.mVolteServiceModule.createSession(callProfile);
            secCallSession.setEpdgState(TextUtils.equals(profile.getCallExtra("CallRadioTech"), String.valueOf(18)));
            ImsCallSessionImpl session = new ImsCallSessionImpl(profile, secCallSession, null, this);
            this.mCallSessionList.put(Integer.valueOf(session.getCallIdInt()), session);
            if (isEnabledWifiDirectFeature()) {
                this.mCmcImsServiceUtil.acquireP2pNetwork();
            }
            return session;
        } catch (RemoteException e) {
            throw new UnsupportedOperationException();
        }
    }

    public IImsCallSession getPendingCallSession(int serviceId, String callId) throws RemoteException {
        int oir;
        ImsCallSessionImpl imsCallSessionImpl;
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getPendingCallSession");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (!isOpened(serviceId) || this.mVolteServiceModule == null || service == null) {
            throw new RemoteException();
        }
        int phoneId = service.getPhoneId();
        com.sec.ims.volte2.IImsCallSession session = this.mVolteServiceModule.getPendingSession(callId);
        CallConstants.STATE sessionState = CallConstants.STATE.values()[session.getCallStateOrdinal()];
        if (sessionState == CallConstants.STATE.EndingCall || sessionState == CallConstants.STATE.EndedCall) {
            throw new RemoteException();
        }
        CallProfile cp = session.getCallProfile();
        ImsCallProfile profile = new ImsCallProfile(1, DataTypeConvertor.convertToGoogleCallType(cp.getCallType()), prepareComposerDataBundle(cp.getComposerData()), new ImsStreamMediaProfile());
        ImsRegistration registration = session.getRegistration();
        Mno mno = null;
        if (registration != null) {
            int currentRat = registration.getCurrentRat();
            if (cp.getRadioTech() != 0) {
                currentRat = cp.getRadioTech();
            }
            if (this.mCmcImsServiceUtil.isCmcSecondaryType(session.getCmcType())) {
                profile.setCallExtra("CallRadioTech", String.valueOf(14));
            } else {
                profile.setCallExtra("CallRadioTech", String.valueOf(currentRat));
            }
            session.setEpdgStateNoNotify(currentRat == 18);
            mno = Mno.fromName(registration.getImsProfile().getMnoName());
        }
        profile.setCallExtra("oi", cp.getDialingNumber());
        profile.mMediaProfile = DataTypeConvertor.convertToGoogleMediaProfile(cp.getMediaProfile());
        if (mno == Mno.DOCOMO) {
            oir = getOirExtraFromDialingNumberForDcm(cp.getLetteringText());
        } else {
            String number = cp.getDialingNumber();
            if (TextUtils.isEmpty(number)) {
                number = NSDSNamespaces.NSDSSimAuthType.UNKNOWN;
            }
            oir = getOirExtraFromDialingNumber(number);
        }
        profile.setCallExtraInt("oir", oir);
        profile.setCallExtraInt("cnap", oir);
        profile.setCallExtra("cna", cp.getLetteringText());
        profile.setCallExtra("com.samsung.telephony.extra.PHOTO_RING_AVAILABLE", cp.getPhotoRing());
        profile.setCallExtraBoolean("com.samsung.telephony.extra.IS_TWO_PHONE_MODE", !TextUtils.isEmpty(cp.getNumberPlus()));
        profile.setCallExtraBoolean("com.samsung.telephony.extra.MT_CONFERENCE", "1".equals(cp.getIsFocus()));
        profile.setCallExtra("com.samsung.telephony.extra.DUAL_NUMBER", cp.getNumberPlus());
        profile.setCallExtra("com.samsung.telephony.extra.ALERT_INFO", cp.getAlertInfo());
        profile.setCallExtra("com.samsung.telephony.extra.LINE_MSISDN", cp.getLineMsisdn());
        profile.mMediaProfile.setRttMode(cp.getMediaProfile().getRttMode());
        if (cp.getHistoryInfo() != null) {
            profile.setCallExtra("com.samsung.telephony.extra.CALL_FORWARDING_REDIRECT_NUMBER", cp.getHistoryInfo());
            if ("anonymous".equalsIgnoreCase(cp.getHistoryInfo())) {
                profile.setCallExtra("com.samsung.telephony.extra.CALL_FORWARDING_PRESENTATION", "1");
            } else {
                profile.setCallExtra("com.samsung.telephony.extra.CALL_FORWARDING_PRESENTATION", "0");
            }
        }
        if (cp.getVerstat() != null) {
            profile.setCallExtra("com.samsung.telephony.extra.ims.VERSTAT", cp.getVerstat());
            if (cp.getVerstat().equals("TN-Validation-Passed")) {
                profile.setCallerNumberVerificationStatus(1);
            } else if (cp.getVerstat().equals("TN-Validation-Failed")) {
                profile.setCallerNumberVerificationStatus(2);
            } else {
                profile.setCallerNumberVerificationStatus(0);
            }
        } else {
            profile.setCallerNumberVerificationStatus(0);
        }
        if (cp.getHDIcon() == 1) {
            profile.mRestrictCause = 0;
        } else {
            profile.mRestrictCause = 3;
        }
        this.mCmcImsServiceUtil.getPendingCallSession(phoneId, profile, session);
        if (session.getCmcType() <= 0) {
            Log.d(LOG_TAG, "getPendingCallSession, create imsCallSessionImpl for [NORMAL volte call]");
            imsCallSessionImpl = new ImsCallSessionImpl(profile, session, null, this);
        } else {
            Log.d(LOG_TAG, "getPendingCallSession, create imsCallSessionImpl for [CMC+D2D volte call]");
            CmcCallSessionManager p2pSessionManager = new CmcCallSessionManager(session, this.mVolteServiceModule, getNsdManager(), isEnabledWifiDirectFeature());
            imsCallSessionImpl = new CmcImsCallSessionImpl(profile, p2pSessionManager, null, this);
        }
        this.mCallSessionList.put(Integer.valueOf(imsCallSessionImpl.getCallIdInt()), imsCallSessionImpl);
        return imsCallSessionImpl;
    }

    public ImsUtImplBase getUtInterface(int serviceId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getUtInterface");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (service == null) {
            return null;
        }
        int phoneId = service.getPhoneId();
        if (this.mUtList.containsKey(Integer.valueOf(phoneId))) {
            return this.mUtList.get(Integer.valueOf(phoneId));
        }
        ImsUtImplBase ut = new ImsUtImpl(phoneId);
        this.mUtList.put(Integer.valueOf(phoneId), ut);
        return ut;
    }

    public IImsConfig getConfigInterface(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getConfigInterface");
        if (this.mConfigs.containsKey(Integer.valueOf(phoneId))) {
            return this.mConfigs.get(Integer.valueOf(phoneId));
        }
        IImsConfig config = new ImsConfigImpl(phoneId);
        this.mConfigs.put(Integer.valueOf(phoneId), config);
        return config;
    }

    public void turnOnIms(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "turnOnIms");
    }

    public void turnOffIms(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "turnOffIms");
    }

    public ImsEcbmImpl getEcbmInterface(int serviceId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getEcbmInterface");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (service == null) {
            return null;
        }
        return getEcbmInterfaceForPhoneId(service.getPhoneId());
    }

    private ImsEcbmImpl getEcbmInterfaceForPhoneId(int phoneId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getEcbmInterfaceForPhoneId");
        if (this.mImsEcbmList.containsKey(Integer.valueOf(phoneId))) {
            return this.mImsEcbmList.get(Integer.valueOf(phoneId));
        }
        ImsEcbmImpl ecbm = new ImsEcbmImpl();
        this.mImsEcbmList.put(Integer.valueOf(phoneId), ecbm);
        return ecbm;
    }

    public void setUiTTYMode(int serviceId, int uiTtyMode, Message onComplete) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "setUiTTYMode");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (service == null) {
            return;
        }
        int phoneId = service.getPhoneId();
        IVolteServiceModule iVolteServiceModule = this.mVolteServiceModule;
        if (iVolteServiceModule == null) {
            throw new RemoteException();
        }
        iVolteServiceModule.setUiTTYMode(phoneId, uiTtyMode, onComplete);
    }

    public void setTtyMode(int phoneId, int mode) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "setTtyMode");
        IVolteServiceModule iVolteServiceModule = this.mVolteServiceModule;
        if (iVolteServiceModule == null) {
            throw new RemoteException();
        }
        iVolteServiceModule.setTtyMode(phoneId, mode);
    }

    public IImsMultiEndpoint getMultiEndpointInterface(int serviceId) {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getMultiEndpointInterface");
        ServiceProfile service = mServiceList.get(Integer.valueOf(serviceId));
        if (service == null) {
            return null;
        }
        int phoneId = service.getPhoneId();
        if (mMultEndPoints.containsKey(Integer.valueOf(phoneId))) {
            return mMultEndPoints.get(Integer.valueOf(phoneId));
        }
        ImsMultiEndPointImpl multiEndpoint = new ImsMultiEndPointImpl(service.getPhoneId());
        mMultEndPoints.put(Integer.valueOf(phoneId), multiEndpoint);
        return multiEndpoint;
    }

    public void changeAudioPath(int phoneId, int direction) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "changeAudioPath");
        IVolteServiceModule iVolteServiceModule = this.mVolteServiceModule;
        if (iVolteServiceModule == null) {
            throw new RemoteException();
        }
        iVolteServiceModule.updateAudioInterface(phoneId, direction);
    }

    public int startLocalRingBackTone(int streamType, int volume, int toneType) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "startLocalRingBackTone");
        IVolteServiceModule iVolteServiceModule = this.mVolteServiceModule;
        if (iVolteServiceModule == null) {
            throw new RemoteException();
        }
        return iVolteServiceModule.startLocalRingBackTone(streamType, volume, toneType);
    }

    public int stopLocalRingBackTone() throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "stopLocalRingBackTone");
        IVolteServiceModule iVolteServiceModule = this.mVolteServiceModule;
        if (iVolteServiceModule == null) {
            throw new RemoteException();
        }
        return iVolteServiceModule.stopLocalRingBackTone();
    }

    public String getTrn(String srcMsisdn, String dstMsisdn) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getTrn");
        IVolteServiceModule iVolteServiceModule = this.mVolteServiceModule;
        if (iVolteServiceModule == null) {
            throw new RemoteException();
        }
        return iVolteServiceModule.getTrn(srcMsisdn, dstMsisdn);
    }

    public void sendPublishDialog(int phoneId, PublishDialog publishDialog) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "sendPublishDialog");
        if (this.mVolteServiceModule == null) {
            throw new RemoteException();
        }
        this.mCmcImsServiceUtil.sendPublishDialog(phoneId, publishDialog);
    }

    public boolean isCmcEmergencyCallSupported(int phoneId) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "isCmcEmergencyCallSupported");
        if (ImsRegistry.getCmcAccountManager() == null) {
            throw new RemoteException();
        }
        return ImsRegistry.getCmcAccountManager().isEmergencyCallSupported();
    }

    public void triggerAutoConfigurationForApp(int phoneId) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "triggerAutoConfigurationForApp");
        ImsRegistry.triggerAutoConfigurationForApp(phoneId);
    }

    public void sendSms(int phoneId, int token, int messageRef, String format, String smsc, boolean isRetry, byte[] pdu) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "sendSms");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        imsSmsImplArr[phoneId].sendSms(phoneId, token, messageRef, format, smsc, pdu);
    }

    public void setRetryCount(int phoneId, int token, int retryCount) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "sendSms");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        imsSmsImplArr[phoneId].setRetryCount(phoneId, token, retryCount);
    }

    public void acknowledgeSms(int phoneId, int token, int messageRef, int result) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "acknowledgeSms");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        imsSmsImplArr[phoneId].acknowledgeSms(phoneId, token, messageRef, result);
    }

    public void acknowledgeSmsReport(int phoneId, int token, int messageRef, int result) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "acknowledgeSmsReport");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        imsSmsImplArr[phoneId].acknowledgeSmsReport(phoneId, token, messageRef, result);
    }

    public void acknowledgeSmsWithPdu(int phoneId, int token, int msgRef, byte[] data) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "acknowledgeSmsWithPdu");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        imsSmsImplArr[phoneId].acknowledgeSmsWithPdu(phoneId, token, msgRef, data);
    }

    public void setSmsListener(int phoneId, IImsSmsListener listener) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "setSmsListener");
        if (mSmsListenerList.containsKey(Integer.valueOf(phoneId))) {
            if (mSmsListenerList.get(Integer.valueOf(phoneId)) != listener) {
                mSmsListenerList.replace(Integer.valueOf(phoneId), listener);
            }
            this.mSmsListener = listener;
            return;
        }
        this.mSmsListener = listener;
        mSmsListenerList.put(Integer.valueOf(phoneId), this.mSmsListener);
    }

    public void onSmsReady(int phoneId) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "onSmsReady");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            imsSmsImplArr[phoneId] = new ImsSmsImpl(this.mContext, phoneId, this.mSmsListener);
        } else {
            imsSmsImplArr[phoneId].setSmsListener(this.mSmsListener);
        }
        mSmsImpl[phoneId].updateTPMR(phoneId);
    }

    public void sendRpSmma(int phoneId, String smsc) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "sendRpSmma");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        imsSmsImplArr[phoneId].sendRpSmma(phoneId, smsc);
    }

    public String getSmsFormat(int phoneId) throws RemoteException {
        this.mContext.enforceCallingOrSelfPermission(IMS_CALL_PERMISSION, "getSmsFormat");
        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
        if (imsSmsImplArr[phoneId] == null) {
            throw new RemoteException();
        }
        return imsSmsImplArr[phoneId].getSmsFormat(phoneId);
    }

    public ImsCallSessionImpl getConferenceHost() {
        return this.mConferenceHost;
    }

    public void setConferenceHost(ImsCallSessionImpl hostSession) {
        this.mConferenceHost = hostSession;
        if (hostSession == null) {
            this.mImsConferenceState.clear();
        }
    }

    public boolean hasConferenceHost() {
        return this.mConferenceHost != null;
    }

    public boolean isInitialMerge() {
        return this.mIsInitialMerge;
    }

    public void setInitialMerge(boolean initialMerge) {
        this.mIsInitialMerge = initialMerge;
    }

    public ImsCallSessionImpl getCallSession(int callId) {
        return this.mCallSessionList.get(Integer.valueOf(callId));
    }

    public void setCallSession(int callId, ImsCallSessionImpl sessionImpl) {
        this.mCallSessionList.put(Integer.valueOf(callId), sessionImpl);
    }

    public void setServiceUrn(String mServiceUrn) {
        this.mServiceUrn = mServiceUrn;
    }

    public void putConferenceState(int callId, String user, String endPoint, String status, ImsCallProfile callprofile) {
        Bundle confState = new Bundle();
        confState.putString("user", user);
        confState.putString("endpoint", endPoint);
        confState.putString("status", status);
        confState.putInt("callId", callId);
        confState.putString("cna", callprofile.getCallExtra("cna"));
        confState.putInt("cnap", callprofile.getCallExtraInt("cnap"));
        confState.putInt("oir", callprofile.getCallExtraInt("oir"));
        confState.putInt("audioQuality", callprofile.getMediaProfile().getAudioQuality());
        confState.putBoolean("com.samsung.telephony.extra.MT_CONFERENCE", callprofile.getCallExtraBoolean("com.samsung.telephony.extra.MT_CONFERENCE"));
        confState.putString("com.samsung.telephony.extra.ims.VERSTAT", callprofile.getCallExtra("com.samsung.telephony.extra.ims.VERSTAT"));
        this.mImsConferenceState.put(Integer.valueOf(callId), confState);
    }

    public void putConferenceStateList(int userId, int callId, String user, String endPoint, String status, int sipError, ImsCallProfile callprofile) {
        Bundle confState = new Bundle();
        confState.putString("user", user);
        confState.putString("endpoint", endPoint);
        confState.putString("status", status);
        confState.putInt("callId", callId);
        confState.putInt("sipError", sipError);
        confState.putString("uriType", "tel");
        confState.putString("cna", callprofile.getCallExtra("cna"));
        confState.putInt("cnap", callprofile.getCallExtraInt("cnap"));
        confState.putInt("oir", callprofile.getCallExtraInt("oir"));
        confState.putInt("audioQuality", callprofile.getMediaProfile().getAudioQuality());
        confState.putBoolean("com.samsung.telephony.extra.MT_CONFERENCE", callprofile.getCallExtraBoolean("com.samsung.telephony.extra.MT_CONFERENCE"));
        confState.putString("com.samsung.telephony.extra.ims.VERSTAT", callprofile.getCallExtra("com.samsung.telephony.extra.ims.VERSTAT"));
        if ("disconnected".equals(status)) {
            confState.putInt("disconnectCause", 2);
        }
        this.mImsConferenceState.put(Integer.valueOf(userId), confState);
    }

    public void removeConferenceState(int callId) {
        this.mImsConferenceState.remove(Integer.valueOf(callId));
    }

    public ImsConferenceState getImsConferenceState() {
        ImsConferenceState imsConfState = new ImsConferenceState();
        for (Map.Entry<Integer, Bundle> conference : this.mImsConferenceState.entrySet()) {
            imsConfState.mParticipants.put(conference.getKey().toString(), conference.getValue());
        }
        return imsConfState;
    }

    public void updateSecConferenceInfo(ImsCallProfile callProfile) {
        Bundle newSecConferenceInfo = new Bundle();
        Bundle oldSecConferenceInfo = callProfile.mCallExtras.getBundle("secConferenceInfo");
        for (Map.Entry<Integer, Bundle> conference : this.mImsConferenceState.entrySet()) {
            Integer callId = conference.getKey();
            Bundle newValue = conference.getValue();
            Bundle oldValue = null;
            if (oldSecConferenceInfo != null) {
                Iterator<String> it = oldSecConferenceInfo.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String oldCallId = it.next();
                    if (TextUtils.equals(oldCallId, callId.toString())) {
                        oldValue = oldSecConferenceInfo.getBundle(oldCallId);
                        break;
                    }
                }
            }
            if (oldValue != null) {
                oldValue.putAll(newValue);
                newSecConferenceInfo.putBundle(callId.toString(), oldValue);
            } else {
                ImsCallSessionImpl session = getCallSession(callId.intValue());
                int origCallId = newValue.getInt("callId");
                if (origCallId > 0) {
                    session = getCallSession(origCallId);
                }
                if (session != null) {
                    ImsCallProfile profile = session.getCallProfile();
                    newValue.putString("cna", profile.getCallExtra("cna"));
                    CallProfile cp = null;
                    try {
                        com.sec.ims.volte2.IImsCallSession s = this.mVolteServiceModule.getSessionByCallId(callId.intValue());
                        if (s != null) {
                            cp = s.getCallProfile();
                        }
                    } catch (RemoteException e) {
                    }
                    if (cp != null && !TextUtils.isEmpty(cp.getVerstat())) {
                        newValue.putString("com.samsung.telephony.extra.ims.VERSTAT", cp.getVerstat());
                    }
                    newSecConferenceInfo.putBundle(callId.toString(), newValue);
                }
            }
        }
        callProfile.mCallExtras.putBundle("secConferenceInfo", newSecConferenceInfo);
    }

    public int getParticipantId(String user) {
        try {
            int callId = Integer.parseInt(user);
            if (this.mImsConferenceState.containsKey(Integer.valueOf(callId))) {
                return callId;
            }
        } catch (IllegalArgumentException e) {
        }
        for (Map.Entry<Integer, Bundle> participant : this.mImsConferenceState.entrySet()) {
            if (user.equals(participant.getValue().getString("user"))) {
                return participant.getKey().intValue();
            }
        }
        return -1;
    }

    public void updateParticipant(int callId, String status) {
        updateParticipant(callId, null, null, status, -1);
    }

    public void updateParticipant(int callId, String status, int disconnectCause) {
        updateParticipant(callId, null, null, status, disconnectCause);
    }

    public void updateParticipant(int callId, String user, String endPoint, String status, int disconnectCause) {
        Bundle confState = this.mImsConferenceState.get(Integer.valueOf(callId));
        if (confState != null) {
            if (!TextUtils.isEmpty(user)) {
                confState.putString("user", user);
            }
            if (!TextUtils.isEmpty(endPoint)) {
                confState.putString("endpoint", endPoint);
            }
            if (!TextUtils.isEmpty(status)) {
                confState.putString("status", status);
            }
            if (disconnectCause != -1) {
                confState.putInt("android.telephony.ims.extra.CALL_DISCONNECT_CAUSE", disconnectCause);
            }
            this.mImsConferenceState.replace(Integer.valueOf(callId), confState);
        }
    }

    public void enterEmergencyCallbackMode(int phoneId) throws RemoteException {
        ImsEcbmImpl ecbm = getEcbmInterfaceForPhoneId(phoneId);
        ecbm.enterEmergencyCallbackMode();
    }

    public int getOirExtraFromDialingNumber(String number) {
        if (NSDSNamespaces.NSDSSimAuthType.UNKNOWN.equalsIgnoreCase(number)) {
            return 3;
        }
        if ("RESTRICTED".equalsIgnoreCase(number) || number.toLowerCase(Locale.US).contains("anonymous")) {
            return 1;
        }
        if ("Coin line/payphone".equalsIgnoreCase(number)) {
            return 4;
        }
        return ("Interaction with other service".equalsIgnoreCase(number) || "Unavailable".equalsIgnoreCase(number)) ? 3 : 2;
    }

    public int getOirExtraFromDialingNumberForDcm(String Pletteting) {
        if (!TextUtils.isEmpty(Pletteting) && Pletteting.startsWith("Anonymous")) {
            return 1;
        }
        if (!TextUtils.isEmpty(Pletteting) && Pletteting.startsWith("Coin line/payphone")) {
            return 4;
        }
        if (!TextUtils.isEmpty(Pletteting) && Pletteting.length() > 0) {
            return 3;
        }
        return 2;
    }

    @Override
    public void updateCapabilities(int phoneId, ImsFeature.Capabilities capabilities) {
        if (!this.mCapabilities[phoneId].equals(capabilities)) {
            this.mCapabilities[phoneId] = capabilities.copy();
            this.mImsNotifier.notifyFeatureCapableChanged(phoneId);
        }
    }

    @Override
    public void updateCapabilities(int phoneId, int[] capabilities, boolean[] capables) {
        Log.i(LOG_TAG, "updateCapabilities, phoneId: " + phoneId);
        boolean capabilityUpdated = false;
        for (int capability = 0; capability < capabilities.length; capability++) {
            if (capables[capability] != this.mCapabilities[phoneId].isCapable(capabilities[capability])) {
                if (capables[capability]) {
                    this.mCapabilities[phoneId].addCapabilities(capabilities[capability]);
                    if (capabilities[capability] == 8) {
                        ImsSmsImpl[] imsSmsImplArr = mSmsImpl;
                        if (imsSmsImplArr[phoneId] != null) {
                            imsSmsImplArr[phoneId].updateTPMR(phoneId);
                        }
                    }
                } else {
                    this.mCapabilities[phoneId].removeCapabilities(capabilities[capability]);
                }
                capabilityUpdated = true;
            }
        }
        if (capabilityUpdated) {
            this.mImsNotifier.notifyFeatureCapableChanged(phoneId);
        }
    }

    public void onCallClosed(int callIdInt) {
        this.mCallSessionList.remove(Integer.valueOf(callIdInt));
    }

    int[] convertCapaToFeature(ImsFeature.Capabilities capabilities) {
        int[] features = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
        if (capabilities.isCapable(1)) {
            features[0] = 0;
            features[2] = 2;
        }
        if (capabilities.isCapable(2)) {
            features[1] = 1;
            features[3] = 3;
        }
        if (capabilities.isCapable(4)) {
            features[4] = 4;
            features[5] = 5;
        }
        if (capabilities.isCapable(8)) {
            features[6] = 6;
            features[7] = 7;
        }
        return features;
    }

    public int getVolteRegHandle(ServiceProfile service) {
        if (service.getServiceClass() == 1) {
            ImsRegistration[] registrationList = ImsRegistry.getRegistrationManager().getRegistrationInfo();
            for (ImsRegistration reg : registrationList) {
                if (reg != null && reg.getPhoneId() == service.getPhoneId() && reg.hasVolteService() && reg.getImsProfile() != null && reg.getImsProfile().getCmcType() == 0) {
                    return reg.getHandle();
                }
            }
            return -1;
        }
        return -1;
    }

    int getCmcTypeFromRegHandle(int regHandle) {
        ImsRegistration[] registrationList = ImsRegistry.getRegistrationManager().getRegistrationInfo();
        for (ImsRegistration reg : registrationList) {
            if (reg != null && regHandle == reg.getHandle() && reg.getImsProfile() != null) {
                return reg.getImsProfile().getCmcType();
            }
        }
        return -1;
    }

    private Uri[] extractOwnUrisFromReg(ImsRegistration reg) {
        if (reg == null || CollectionUtils.isNullOrEmpty(reg.getImpuList())) {
            return new Uri[0];
        }
        return (Uri[]) reg.getImpuList().stream().
                filter((o) -> { return UriUtil.hasMsisdnNumber(((NameAddr) o).getUri()); }).
                map((o) -> { return Uri.parse("tel:" + UriUtil.getMsisdnNumber(((NameAddr) o).getUri())); }).
                toArray((i) -> { return new Uri[i]; });
    }

    boolean isOwnUrisChanged(int phoneId, ImsRegistration reg) {
        Uri[] latestUris = extractOwnUrisFromReg(reg);
        if (Arrays.deepEquals(this.mOwnUris.get(Integer.valueOf(phoneId)), latestUris)) {
            return false;
        }
        this.mOwnUris.put(Integer.valueOf(phoneId), latestUris);
        return true;
    }

    public ImsNotifier getImsNotifier() {
        return this.mImsNotifier;
    }

    public Bundle prepareComposerDataBundle(Bundle cBundle) {
        Bundle callExtras = new Bundle();
        if (cBundle != null && !cBundle.isEmpty()) {
            if (cBundle.containsKey(CallConstants.ComposerData.IMPORTANCE)) {
                callExtras.putBoolean(ImsConstants.Intents.EXTRA_CALL_IMPORTANCE, cBundle.getBoolean(CallConstants.ComposerData.IMPORTANCE));
            }
            if (cBundle.containsKey(CallConstants.ComposerData.IMAGE)) {
                callExtras.putString(ImsConstants.Intents.EXTRA_CALL_IMAGE, cBundle.getString(CallConstants.ComposerData.IMAGE));
            }
            if (cBundle.containsKey("subject")) {
                callExtras.putString(ImsConstants.Intents.EXTRA_CALL_SUBJECT, cBundle.getString("subject"));
            }
            if (cBundle.containsKey(CallConstants.ComposerData.LONGITUDE)) {
                callExtras.putString(ImsConstants.Intents.EXTRA_CALL_LONGITUDE, cBundle.getString(CallConstants.ComposerData.LONGITUDE));
            }
            if (cBundle.containsKey(CallConstants.ComposerData.LATITUDE)) {
                callExtras.putString(ImsConstants.Intents.EXTRA_CALL_LATITUDE, cBundle.getString(CallConstants.ComposerData.LATITUDE));
            }
            if (cBundle.containsKey(CallConstants.ComposerData.RADIUS)) {
                callExtras.putString(ImsConstants.Intents.EXTRA_CALL_RADIUS, cBundle.getString(CallConstants.ComposerData.RADIUS));
            }
        }
        return callExtras;
    }
}
