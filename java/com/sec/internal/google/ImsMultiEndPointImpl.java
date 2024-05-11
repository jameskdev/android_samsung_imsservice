package com.sec.internal.google;

import android.net.Uri;
import android.os.RemoteException;
import android.telephony.ims.ImsExternalCallState;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.text.TextUtils;
import com.sec.ims.Dialog;
import com.sec.ims.DialogEvent;
import com.sec.internal.constants.Mno;
import com.sec.internal.constants.ims.entitilement.NSDSNamespaces;
import com.sec.internal.helper.ImsCallUtil;
import com.sec.internal.helper.SimUtil;
import com.sec.internal.imscr.LogClass;
import com.sec.internal.log.IMSLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImsMultiEndPointImpl extends ImsMultiEndpointImplBase {
    private int mPhoneId;
    private List<ImsExternalCallState> mDialogList = new ArrayList();

    public ImsMultiEndPointImpl(int phoneId) {
        mPhoneId = phoneId;
    }

    /**
     * Framework will trigger this to get the latest Dialog Event Package information.
     */
    @Override
    public void requestImsExternalCallStateInfo() {
        onImsExternalCallStateUpdate(mDialogList);
    }

    public void setDialogInfo(DialogEvent de, int cmcType) {
        mDialogList.clear();
        if (de.getDialogList().size() == 0) {
            mDialogList.add(new ImsExternalCallState(-1, Uri.parse(""), false, 2, 0, false));
            return;
        }
        for (Dialog info : de.getDialogList()) {
            int dialogId = -1;
            boolean addDispName = true;
            if (info != null) {
                Mno mno = SimUtil.getSimMno(mPhoneId);
                if (mno == Mno.VZW) {
                    dialogId = ImsCallUtil.getIdForString(info.getSipCallId());
                } else {
                    try {
                        dialogId = Integer.parseInt(info.getDialogId());
                    } catch (NumberFormatException e) {
                    }
                }
                String remoteUri = info.getRemoteUri();
                if (!TextUtils.isEmpty(remoteUri) && remoteUri.contains(":")) {
                    if (remoteUri.startsWith("tel:")) {
                        remoteUri = remoteUri.replace("tel:", "sip:");
                    }
                    if ((cmcType == 2 || cmcType == 4 || cmcType == 8) && !TextUtils.isEmpty(info.getRemoteDispName()) && remoteUri.contains(info.getRemoteDispName())) {
                        addDispName = false;
                    }
                    if (!TextUtils.isEmpty(info.getRemoteDispName()) && addDispName) {
                        remoteUri = remoteUri + ";displayName=" + info.getRemoteDispName();
                    }
                    if (cmcType == 2 || cmcType == 4 || cmcType == 8) {
                        String tmpRemoteUri = remoteUri.substring(remoteUri.indexOf(":") + 1);
                        if (!TextUtils.isEmpty(tmpRemoteUri)) {
                            String oir = String.valueOf(getOirExtraFromDialingNumber(tmpRemoteUri));
                            remoteUri = remoteUri + ";oir=" + oir;
                            if (tmpRemoteUri.contains("Conference call") || info.getCallType() == 5 || info.getCallType() == 6) {
                                remoteUri = remoteUri + ";cmc_pd_state=1";
                            } else if (info.getCallType() == 7 || info.getCallType() == 8) {
                                remoteUri = remoteUri + ";cmc_pd_state=2";
                            }
                        }
                        remoteUri = remoteUri + ";cmc_type=2";
                    }
                    Uri address = Uri.parse(remoteUri);
                    int callType = DataTypeConvertor.convertToGoogleCallType(info.getCallType());
                    mDialogList.add(new ImsExternalCallState(dialogId, address, info.isPullAvailable(), info.getState(), callType, info.isHeld()));
                }
            }
        }
        StringBuilder crLogBuf = new StringBuilder("DE=");
        for (ImsExternalCallState iecs : mDialogList) {
            crLogBuf.append("[");
            crLogBuf.append(iecs.getCallId() % 100000);
            crLogBuf.append(",");
            crLogBuf.append(iecs.getCallState() == 1 ? "C" : "T");
            crLogBuf.append(",");
            crLogBuf.append(iecs.isCallHeld() ? "H" : "A");
            crLogBuf.append(",");
            if (iecs.isCallPullable()) {
                crLogBuf.append("T");
            } else {
                crLogBuf.append("F");
            }
            crLogBuf.append("]");
        }
        String crLog = crLogBuf.toString();
        IMSLog.c(LogClass.VOLTE_DIALOG_EVENT, crLog);
    }

    public List<ImsExternalCallState> getExternalCallStateList() {
        return mDialogList;
    }

    private int getOirExtraFromDialingNumber(String number) {
        if (NSDSNamespaces.NSDSSimAuthType.UNKNOWN.equalsIgnoreCase(number)) {
            return 3;
        }
        if (number.equalsIgnoreCase("RESTRICTED") || number.toLowerCase(Locale.US).contains("anonymous")) {
            return 1;
        }
        if (number.equalsIgnoreCase("Coin line/payphone")) {
            return 4;
        }
        return 2;
    }

    public void setP2pPushDialogInfo(DialogEvent de, int cmcType) throws RemoteException {
        mDialogList.clear();
        for (Dialog info : de.getDialogList()) {
            if (info != null) {
                try {
                    int dialogId = Integer.parseInt(info.getDialogId());
                    Uri address = Uri.parse("sip:D2D@samsungims.com;d2d.push");
                    int callType = DataTypeConvertor.convertToGoogleCallType(info.getCallType());
                    mDialogList.add(new ImsExternalCallState(dialogId, address, info.isPullAvailable(), info.getState(), callType, info.isHeld()));
                } catch (NumberFormatException e) {
                }
            }
        }
        requestImsExternalCallStateInfo();
    }
}
