package com.sec.internal.google;

import android.content.Intent;
import android.os.IBinder;
import android.telephony.ims.compat.ImsService;
import android.telephony.ims.compat.feature.ImsFeature;
import android.telephony.ims.compat.feature.MMTelFeature;
import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.ConcurrentHashMap;

public class GoogleImsServiceAdapter extends ImsService {
    private final String LOG_TAG = "GoogleImsServiceAdapter";
    private final ConcurrentHashMap<Integer, ImsMmtelFeature> mFeatureList;
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "onBind:");
        return super.onBind(intent);
    }

    public GoogleImsServiceAdapter() {
        mFeatureList = new ConcurrentHashMap<>();
    }

    @Override
    public ImsMmtelFeature onCreateMMTelImsFeature(int slotId) {
        if (GoogleImsService.isReady()) {
            if (mFeatureList.containsKey(slotId)) {
                return mFeatureList.get(slotId);
            } else {
                ImsMmtelFeature m = new ImsMmtelFeature(GoogleImsService.getInstanceIfReady(), slotId);
                mFeatureList.put(slotId, m);
                return m;
            }
        } else {
            Log.e(LOG_TAG, "Service not available yet!");
            return null;
        }
    }

    @Override
    public MMTelFeature onCreateEmergencyMMTelImsFeature(int slotId) {
        if (GoogleImsService.isReady() && mFeatureList.containsKey(slotId)) {
            return mFeatureList.get(slotId);
        } else {
            Log.e(LOG_TAG, "Service not available yet!");
            return null;
        }
    }
}
