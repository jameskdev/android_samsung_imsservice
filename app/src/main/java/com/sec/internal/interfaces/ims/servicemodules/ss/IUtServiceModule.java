package com.sec.internal.interfaces.ims.servicemodules.ss;

import com.sec.ims.ss.IImsUtEventListener;

public interface IUtServiceModule {
    public int queryCallForward(int mPhoneId, int condition, String number);

    public int queryCallWaiting(int mPhoneId);

    public int queryCLIR(int mPhoneId);

    public int queryCLIP(int mPhoneId);

    public int queryCOLR(int mPhoneId);

    public int queryCOLP(int mPhoneId);

    public int queryCallBarring(int mPhoneId, int cbType, int i);

    void registerForUtEvent(int mPhoneId, IImsUtEventListener mUtEventListener);

    int updateCallBarring(int mPhoneId, int cbType, int action, int i, Object o, String[] barrList);

    int updateCallForward(int mPhoneId, int action, int condition, String number, int serviceClass, int timeSeconds);

    int updateCallWaiting(int mPhoneId, boolean enable, int serviceClass);

    int updateCLIR(int mPhoneId, int clirMode);

    int updateCLIP(int mPhoneId, boolean enable);

    int updateCOLP(int mPhoneId, boolean enable);

    int updateCOLR(int mPhoneId, int presentation);

    boolean isUssdEnabled(int mPhoneId);
}
