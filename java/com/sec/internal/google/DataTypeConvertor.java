package com.sec.internal.google;

import android.os.Bundle;
import android.os.SystemProperties;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.text.TextUtils;
import com.sec.ims.volte2.data.CallProfile;
import com.sec.ims.volte2.data.MediaProfile;
import com.sec.ims.volte2.data.VolteConstants;
import com.sec.internal.constants.Mno;
import com.sec.internal.constants.ims.ImsConstants;
import com.sec.internal.constants.ims.entitilement.NSDSNamespaces;
import com.sec.internal.constants.ims.servicemodules.volte2.CallConstants;
import com.sec.internal.helper.ImsCallUtil;
import com.sec.internal.helper.OmcCode;
import com.sec.internal.helper.SimUtil;

public class DataTypeConvertor {
    public static ImsReasonInfo convertToGoogleImsReason(int errorCode) {
        return new ImsReasonInfo(ImsReasonInfo.CODE_REGISTRATION_ERROR, 1, "");
    }

    public static int convertUrnToEccCat(String urn) {
        if (TextUtils.isEmpty(urn)) {
            return 0;
        }
        if ("urn:service:unspecified".equalsIgnoreCase(urn)) {
            return 254;
        }
        if ("urn:service:sos.mountain".equalsIgnoreCase(urn)) {
            return 16;
        }
        if ("urn:service:sos.marine".equalsIgnoreCase(urn)) {
            return 8;
        }
        if ("urn:service:sos.fire".equalsIgnoreCase(urn)) {
            return 4;
        }
        if ("urn:service:sos.ambulance".equalsIgnoreCase(urn)) {
            return 2;
        }
        if ("urn:service:sos.police".equalsIgnoreCase(urn)) {
            return 1;
        }
        if ("urn:service:sos.traffic".equalsIgnoreCase(urn)) {
            return 20;
        }
        return ImsCallUtil.ECC_SERVICE_URN_DEFAULT.equalsIgnoreCase(urn) ? 0 : 254;
    }

    public static String convertEccCatToURN(String eccCatStr) {
        int phoneId = SimUtil.getDefaultPhoneId();
        Mno mno = SimUtil.getSimMno(phoneId);
        if (TextUtils.isEmpty(eccCatStr)) {
            return ImsCallUtil.ECC_SERVICE_URN_DEFAULT;
        }
        int eccCat = Integer.parseInt(eccCatStr);
        if (eccCat == 254) {
            return "urn:service:unspecified";
        }
        if (eccCat == 16) {
            return "urn:service:sos.mountain";
        }
        if (eccCat == 8) {
            return "urn:service:sos.marine";
        }
        if (eccCat == 4) {
            return "urn:service:sos.fire";
        }
        if (eccCat == 2) {
            return "urn:service:sos.ambulance";
        }
        if (eccCat == 1) {
            return "urn:service:sos.police";
        }
        if (eccCat == 20) {
            return "urn:service:sos.traffic";
        }
        return (mno.isJpn() && eccCat == 6) ? "urn:service:sos.fire" : ImsCallUtil.ECC_SERVICE_URN_DEFAULT;
    }

    public static String convertEccCatToURNSpecificKor(String eccCatStr) {
        if (TextUtils.isEmpty(eccCatStr)) {
            return ImsCallUtil.ECC_SERVICE_URN_DEFAULT;
        }
        int eccCat = Integer.parseInt(eccCatStr);
        if (eccCat == 254) {
            return "urn:service:unspecified";
        }
        if (eccCat == 8) {
            return "urn:service:sos.marine";
        }
        if (eccCat == 4) {
            return "urn:service:sos.fire";
        }
        if (eccCat == 1) {
            return "urn:service:sos.police";
        }
        if (eccCat == 6 || eccCat == 7) {
            return "urn:service:sos.country-specific.kr.111";
        }
        if (eccCat == 3) {
            return "urn:service:sos.country-specific.kr.113";
        }
        if (eccCat == 18) {
            return "urn:service:sos.country-specific.kr.117";
        }
        if (eccCat == 19) {
            return "urn:service:sos.country-specific.kr.118";
        }
        return eccCat == 9 ? "urn:service:sos.country-specific.kr.125" : ImsCallUtil.ECC_SERVICE_URN_DEFAULT;
    }

    public static String convertToClirPrefix(int clirMode) {
        if (clirMode != 1) {
            if (clirMode != 2) {
                if (clirMode != 3) {
                    return null;
                }
                return NSDSNamespaces.NSDSSimAuthType.UNKNOWN;
            }
            return "*31#";
        }
        return "#31#";
    }

    public static int convertToSecCallType(int callType) {
        return convertToSecCallType(0, callType, false, false);
    }

    public static int convertToSecCallType(int serviceType, int callType, boolean isTtyMode, boolean isGroupCall) {
        switch (callType) {
            case 2:
                if (serviceType == 2) {
                    return 7;
                }
                if (isTtyMode) {
                    return 9;
                }
                if (isGroupCall) {
                    return 5;
                }
                return 1;
            case 3:
            default:
                return 0;
            case 4:
            case 8:
                if (serviceType == 2) {
                    return 8;
                }
                if (isGroupCall) {
                    return 6;
                }
                return 2;
            case 5:
            case 9:
                return 3;
            case 6:
            case 10:
                return 4;
            case 7:
                if (serviceType == 2) {
                    return 7;
                }
                return 1;
        }
    }

    public static int convertToGoogleCallType(int type) {
        switch (type) {
            case 1:
            case 5:
                return 2;
            case 2:
            case 6:
            case 8:
                return 4;
            case 3:
                return 5;
            case 4:
                return 6;
            case 7:
            default:
                return 2;
        }
    }

    public static MediaProfile convertToSecMediaProfile(ImsStreamMediaProfile profile) {
        MediaProfile convertedProfile = new MediaProfile();
        int videoQuality = -1;
        int videoOrientation = 0;
        int i = profile.mVideoQuality;
        if (i == 0) {
            videoQuality = 0;
        } else if (i == 1) {
            videoQuality = 12;
        } else if (i == 2) {
            videoQuality = 13;
            videoOrientation = 1;
        } else if (i == 4) {
            videoQuality = 13;
        } else if (i == 8) {
            videoQuality = 15;
            videoOrientation = 1;
        } else if (i == 16) {
            videoQuality = 15;
        }
        VolteConstants.AudioCodecType audioCodec = VolteConstants.AudioCodecType.AUDIO_CODEC_NONE;
        int i2 = profile.mAudioQuality;
        if (i2 == 1) {
            audioCodec = VolteConstants.AudioCodecType.AUDIO_CODEC_AMRNB;
        } else if (i2 == 2) {
            audioCodec = VolteConstants.AudioCodecType.AUDIO_CODEC_AMRWB;
        }
        convertedProfile.setVideoQuality(videoQuality);
        convertedProfile.setVideoOrientation(videoOrientation);
        convertedProfile.setAudioCodec(audioCodec);
        convertedProfile.setRttMode(profile.getRttMode());
        return convertedProfile;
    }

    public static ImsStreamMediaProfile convertToGoogleMediaProfile(MediaProfile profile) {
        ImsStreamMediaProfile convertedProfile = new ImsStreamMediaProfile();
        int videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_NONE;
        int profileVideoQuality = profile.getVideoQuality();
        if (profileVideoQuality == 0) {
            videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_NONE;
        } else if (profileVideoQuality != 15) {
            if (profileVideoQuality == 12) {
                videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_QCIF;
            } else if (profileVideoQuality == 13) {
                if (profile.getVideoOrientation() == 1) {
                    videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_QVGA_LANDSCAPE;
                } else {
                    videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_QVGA_PORTRAIT;
                }
            }
        } else if (profile.getVideoOrientation() == 1) {
            videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_VGA_LANDSCAPE;
        } else {
            videoQuality = ImsStreamMediaProfile.VIDEO_QUALITY_VGA_PORTRAIT;
        }
        int audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_AMR;
        switch (profile.getAudioCodec()) {
            case AUDIO_CODEC_NONE:
            case AUDIO_CODEC_AMRNB:
                audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_AMR;
                break;
            case AUDIO_CODEC_AMRWB:
                audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_AMR_WB;
                break;
            case AUDIO_CODEC_EVS:
            case AUDIO_CODEC_EVSNB:
                audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_EVS_NB;
                break;
            case AUDIO_CODEC_EVSWB:
                audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_EVS_WB;
                break;
            case AUDIO_CODEC_EVSSWB:
                audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_EVS_SWB;
                break;
            case AUDIO_CODEC_EVSFB:
                audioQuality = ImsStreamMediaProfile.AUDIO_QUALITY_EVS_FB;
                break;
        }
        convertedProfile.mAudioQuality = audioQuality;
        convertedProfile.mVideoQuality = videoQuality;
        convertedProfile.mAudioDirection = 3;
        if (profile.getVideoPause()) {
            convertedProfile.mVideoDirection = 0;
        } else {
            convertedProfile.mVideoDirection = 3;
        }
        convertedProfile.mRttMode = profile.getRttMode();
        return convertedProfile;
    }

    public static CallProfile convertToSecCallProfile(int phoneId, ImsCallProfile profile, boolean isTtyMode) {
        CallProfile convertedProfile = new CallProfile();
        convertedProfile.setPhoneId(phoneId);
        Bundle oemExtras = profile.mCallExtras.getBundle("android.telephony.ims.extra.OEM_EXTRAS");
        String eccCatStr = "";
        String emergencyRat = "";
        boolean isGroupCall = false;
        if (oemExtras != null) {
            eccCatStr = oemExtras.getString("EccCat");
            emergencyRat = oemExtras.getString("imsEmergencyRat");
            String letteringText = oemExtras.getString("DisplayText");
            String alertInfo = oemExtras.getString("com.samsung.telephony.extra.ALERT_INFO");
            if (!TextUtils.isEmpty(letteringText)) {
                convertedProfile.setLetteringText(letteringText);
            }
            if (!TextUtils.isEmpty(alertInfo)) {
                convertedProfile.setAlertInfo(alertInfo);
            }
            isGroupCall = oemExtras.getBoolean("com.samsung.telephony.extra.DIAL_CONFERENCE_CALL");
        }
        if (profile.getCallExtraBoolean("e_call", false) || profile.mServiceType == 2) {
            int dialingString = profile.mCallType;
            convertedProfile.setCallType(convertToSecCallType(2, dialingString, isTtyMode, isGroupCall));
            convertedProfile.setEmergencyRat(emergencyRat);
            if (TextUtils.equals(emergencyRat, "VoWIFI")) {
                profile.setCallExtra("CallRadioTech", String.valueOf(18));
            } else {
                profile.setCallExtra("CallRadioTech", String.valueOf(14));
            }
            Mno mno = SimUtil.getSimMno(phoneId);
            String salesCode = SystemProperties.get(OmcCode.PERSIST_OMC_CODE_PROPERTY, "");
            if (TextUtils.isEmpty(salesCode)) {
                salesCode = SystemProperties.get(OmcCode.OMC_CODE_PROPERTY);
            }
            if (mno.isKor() || (mno == Mno.DEFAULT && TextUtils.equals(salesCode, "KTC"))) {
                convertedProfile.setUrn(convertEccCatToURNSpecificKor(eccCatStr));
            } else {
                convertedProfile.setUrn(convertEccCatToURN(eccCatStr));
            }
        } else {
            int dialingString2 = profile.getCallExtraInt("dialstring", 0);
            if (dialingString2 == 2) {
                convertedProfile.setCallType(12);
            } else {
                convertedProfile.setCallType(convertToSecCallType(profile.mServiceType, profile.mCallType, isTtyMode, isGroupCall));
                convertedProfile.setCLI(convertToClirPrefix(profile.getCallExtraInt("oir", 0)));
            }
        }
        if (isGroupCall) {
            convertedProfile.setConferenceCall(2);
        }
        convertedProfile.setMediaProfile(convertToSecMediaProfile(profile.mMediaProfile));
        convertedProfile.setComposerData(processCallComposerInfo(profile));
        return convertedProfile;
    }

    private static Bundle processCallComposerInfo(ImsCallProfile imsprofile) {
        Bundle cBundle = new Bundle();
        if (imsprofile != null) {
            Bundle callMainExtras = imsprofile.getCallExtras();
            Bundle callExtras = callMainExtras != null ? callMainExtras.getBundle("android.telephony.ims.extra.OEM_EXTRAS") : null;
            if (callExtras != null && !callExtras.isEmpty()) {
                if (callExtras.containsKey(ImsConstants.Intents.EXTRA_CALL_IMPORTANCE)) {
                    cBundle.putBoolean(CallConstants.ComposerData.IMPORTANCE, callExtras.getBoolean(ImsConstants.Intents.EXTRA_CALL_IMPORTANCE));
                }
                if (!TextUtils.isEmpty(callExtras.getString(ImsConstants.Intents.EXTRA_CALL_SUBJECT))) {
                    cBundle.putString("subject", callExtras.getString(ImsConstants.Intents.EXTRA_CALL_SUBJECT));
                }
                if (!TextUtils.isEmpty(callExtras.getString(ImsConstants.Intents.EXTRA_CALL_IMAGE))) {
                    cBundle.putString(CallConstants.ComposerData.IMAGE, callExtras.getString(ImsConstants.Intents.EXTRA_CALL_IMAGE));
                }
                if (!TextUtils.isEmpty(callExtras.getString(ImsConstants.Intents.EXTRA_CALL_LATITUDE)) && !TextUtils.isEmpty(callExtras.getString(ImsConstants.Intents.EXTRA_CALL_LONGITUDE))) {
                    cBundle.putString(CallConstants.ComposerData.LONGITUDE, callExtras.getString(ImsConstants.Intents.EXTRA_CALL_LONGITUDE));
                    cBundle.putString(CallConstants.ComposerData.LATITUDE, callExtras.getString(ImsConstants.Intents.EXTRA_CALL_LATITUDE));
                    if (!TextUtils.isEmpty(callExtras.getString(ImsConstants.Intents.EXTRA_CALL_RADIUS))) {
                        cBundle.putString(CallConstants.ComposerData.RADIUS, callExtras.getString(ImsConstants.Intents.EXTRA_CALL_RADIUS));
                    }
                }
            }
        }
        return cBundle;
    }
}