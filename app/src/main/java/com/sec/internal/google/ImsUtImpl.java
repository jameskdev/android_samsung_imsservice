package com.sec.internal.google;

import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsInfo;
import android.telephony.ims.ImsUtListener;
import android.telephony.ims.stub.ImsUtImplBase;
import android.text.TextUtils;
import com.sec.ims.ss.IImsUtEventListener;
import com.sec.internal.ims.registry.ImsRegistry;
import com.sec.internal.interfaces.ims.servicemodules.ss.IUtServiceModule;

public class ImsUtImpl extends ImsUtImplBase {
    private int mPhoneId;
    private IUtServiceModule mUtService;
    private ImsUtListener mListener = null;
    private IImsUtEventListener mUtEventListener = new IImsUtEventListener.Stub() { // from class: com.sec.internal.google.ImsUtImpl.1
        public void onUtConfigurationUpdateFailed(int reqId, Bundle data) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                int errorCode = data.getInt("errorCode", 0);
                String errorMsg = data.getString("errorMsg");
                ImsReasonInfo reasonInfo = new ImsReasonInfo(ImsUtImpl.this.convertErrorReasonToFw(errorCode), 0, errorMsg);
                ImsUtImpl.this.mListener.onUtConfigurationUpdateFailed(reqId, reasonInfo);
            }
        }

        public void onUtConfigurationQueryFailed(int reqId, Bundle data) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                int errorCode = data.getInt("errorCode", 0);
                String errorMsg = data.getString("errorMsg");
                ImsReasonInfo reasonInfo = new ImsReasonInfo(ImsUtImpl.this.convertErrorReasonToFw(errorCode), 0, errorMsg);
                ImsUtImpl.this.mListener.onUtConfigurationQueryFailed(reqId, reasonInfo);
            }
        }

        public void onUtConfigurationQueried(int reqId, Bundle data) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                ImsUtImpl.this.mListener.onUtConfigurationQueried(reqId, data);
            }
        }

        public void onUtConfigurationUpdated(int reqId) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                ImsUtImpl.this.mListener.onUtConfigurationUpdated(reqId);
            }
        }

        public void onUtConfigurationCallWaitingQueried(int reqId, boolean status) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                ImsSsInfo[] ssInfoList = (ImsSsInfo[]) ImsSsInfo.CREATOR.newArray(1);
                ImsSsInfo.Builder builder = new ImsSsInfo.Builder(status ? 1 : 0);
                ssInfoList[0] = builder.build();
                ImsUtImpl.this.mListener.onUtConfigurationCallWaitingQueried(reqId, ssInfoList);
            }
        }

        public void onUtConfigurationCallForwardQueried(int reqId, Bundle[] callForwardList) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                ImsCallForwardInfo[] cfInfoList = (ImsCallForwardInfo[]) ImsCallForwardInfo.CREATOR.newArray(callForwardList.length);
                for (int i = 0; i < callForwardList.length; i++) {
                    int status = callForwardList[i].getInt("status", 0);
                    int cfType = callForwardList[i].getInt("condition", 0);
                    int noReplyTimer = callForwardList[i].getInt("NoReplyTimer", 0);
                    int uriType = callForwardList[i].getInt("ToA", 0);
                    String cfUri = callForwardList[i].getString("number");
                    if (TextUtils.isEmpty(cfUri)) {
                        cfUri = "";
                    }
                    int ssClass = callForwardList[i].getInt("serviceClass", 1);
                    ImsCallForwardInfo cfInfo = new ImsCallForwardInfo(cfType, status, uriType, ssClass, cfUri, noReplyTimer);
                    cfInfoList[i] = cfInfo;
                }
                ImsUtImpl.this.mListener.onUtConfigurationCallForwardQueried(reqId, cfInfoList);
            }
        }

        public void onUtConfigurationCallBarringQueried(int reqId, Bundle[] callBarringList) throws RemoteException {
            if (ImsUtImpl.this.mListener != null) {
                ImsSsInfo[] ssInfoList = (ImsSsInfo[]) ImsSsInfo.CREATOR.newArray(callBarringList.length);
                for (int i = 0; i < callBarringList.length; i++) {
                    int status = callBarringList[i].getInt("status", 0);
                    int cbType = callBarringList[i].getInt("condition", 0);
                    if (cbType == 10) {
                        String number = callBarringList[i].getString("number");
                        ImsSsInfo.Builder builder = new ImsSsInfo.Builder(status).setIncomingCommunicationBarringNumber(number);
                        ssInfoList[i] = builder.build();
                    } else {
                        int serviceClass = callBarringList[i].getInt("serviceClass", 1);
                        ImsSsInfo.Builder builder2 = new ImsSsInfo.Builder(status);
                        ssInfoList[i] = builder2.build();
                    }
                }
                ImsUtImpl.this.mListener.onUtConfigurationCallBarringQueried(reqId, ssInfoList);
            }
        }
    };

    public ImsUtImpl(int phoneId) {
        this.mPhoneId = -1;
        this.mUtService = null;
        this.mPhoneId = phoneId;
        IUtServiceModule utServiceModule = ImsRegistry.getServiceModuleManager().getUtServiceModule();
        this.mUtService = utServiceModule;
        if (utServiceModule != null) {
            utServiceModule.registerForUtEvent(this.mPhoneId, this.mUtEventListener);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public int queryCallBarring(int cbType) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCallBarring(this.mPhoneId, cbType, 255);
    }

    @Override
    public int queryCallForward(int condition, String number) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCallForward(this.mPhoneId, condition, number);
    }

    @Override
    public int queryCallWaiting() {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCallWaiting(this.mPhoneId);
    }

    @Override
    public int queryCLIR() {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCLIR(this.mPhoneId);
    }

    @Override
    public int queryClir() {
        return queryCLIR();
    }

    @Override
    public int queryClip() {
        return queryCLIP();
    }

    @Override
    public int queryColr() {
        return queryCOLR();
    }

    @Override
    public int queryColp() {
        return queryCOLP();
    }

    @Override
    public int updateClir(int clirMode) {
        return updateCLIR(clirMode);
    }

    @Override
    public int updateClip(boolean enable) {
        return updateCLIP(enable);
    }

    @Override
    public int updateColr(int presentation) {
        return updateCOLR(presentation);
    }

    @Override
    public int updateColp(boolean enable) {
        return updateCOLP(enable);
    }

    @Override
    public int queryCLIP() {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCLIP(this.mPhoneId);
    }

    @Override
    public int queryCOLR() {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCOLR(this.mPhoneId);
    }

    @Override
    public int queryCOLP() {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCOLP(this.mPhoneId);
    }

    @Override
    public int transact(Bundle ssInfo) {
        return -1;
    }

    @Override
    public int updateCallBarring(int cbType, int action, String[] barrList) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCallBarring(this.mPhoneId, cbType, action, 255, null, barrList);
    }

    @Override
    public int updateCallBarringWithPassword(int cbType, int action, String[] barrList,
                                             int serviceClass, String password) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCallBarring(this.mPhoneId, cbType, action,
                serviceClass, password, barrList);
    }

    @Override
    public int updateCallForward(int action, int condition, String number,
                                 int serviceClass, int timeSeconds) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCallForward(this.mPhoneId, action, condition, number, serviceClass, timeSeconds);
    }

    @Override
    public int updateCallWaiting(boolean enable, int serviceClass) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCallWaiting(this.mPhoneId, enable, serviceClass);
    }

    @Override
    public int updateCLIR(int clirMode) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCLIR(this.mPhoneId, clirMode);
    }

    @Override
    public int updateCLIP(boolean enable) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCLIP(this.mPhoneId, enable);
    }

    @Override
    public int updateCOLR(int presentation) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCOLR(this.mPhoneId, presentation);
    }

    @Override
    public int updateCOLP(boolean enable) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCOLP(this.mPhoneId, enable);
    }

    public void setListener(ImsUtListener listener) {
        this.mListener = listener;
    }

    @Override
    public int queryCallBarringForServiceClass(int cbType, int serviceClass) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.queryCallBarring(this.mPhoneId, cbType, serviceClass);
    }

    @Override
    public int updateCallBarringForServiceClass(int cbType, int action,
                                                String[] barrList, int serviceClass) {
        IUtServiceModule iUtServiceModule = this.mUtService;
        if (iUtServiceModule == null) {
            return -1;
        }
        return iUtServiceModule.updateCallBarring(this.mPhoneId, cbType, action,
                serviceClass, null, barrList);
    }

    public boolean isUssdEnabled() throws RemoteException {
        return this.mUtService.isUssdEnabled(this.mPhoneId);
    }

    private int convertErrorReasonToFw(int error) {
        if (error != 403) {
            if (error != 404) {
                if (error != 408) {
                    if (error != 5001) {
                        return 0;
                    }
                    return 805;
                }
                return 804;
            }
            return 801;
        }
        return 803;
    }
}