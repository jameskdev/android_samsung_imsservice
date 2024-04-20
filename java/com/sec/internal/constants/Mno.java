package com.sec.internal.constants;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import android.support.annotation.NonNull;

import com.samsung.android.feature.SemCscFeature;
import com.sec.internal.constants.ims.DiagnosisConstants;
import com.sec.internal.constants.ims.config.ConfigConstants;
import com.sec.internal.constants.ims.entitilement.NSDSNamespaces;
import com.sec.internal.constants.tapi.UserConsentProviderContract;
import java.util.HashSet;
import java.util.Set;

public class Mno {
    private Country mCountry;
    private String mName;
    private Region mRegion;
    private String mSalesCode;
    private static final String LOG_TAG = Mno.class.getSimpleName();
    public static char MVNO_DELIMITER = ':';
    public static char GC_DELIMITER = '@';
    public static String GC_BLOCK_EXTENSION = "@BLOCKGC";
    public static String MOCK_MNO_PROPERTY = "persist.ims.mock.mno";
    public static String MOCK_MNONAME_PROPERTY = "persist.ims.mock.mnoname";
    public static String GCF_OPERATOR_CODE = "00101";
    public static String GCF_OPERATOR_NAME = "GCF";
    protected static Set<Mno> sTable = new HashSet<>();
    public static Mno GOOGLEGC = new Mno("GoogleGC_ALL", "", Region.END_OF_REGION, Country.END_OF_COUNTRY);
    public static Mno MDMN = new Mno("MDMN");
    public static Mno DEFAULT = new Mno("DEFAULT", "DEFAULT", Region.END_OF_REGION, Country.END_OF_COUNTRY);
    public static Mno GENERIC = new Mno("GENERIC", "GENERIC", Region.END_OF_REGION, Country.END_OF_COUNTRY);
    public static Mno GCF = new Mno("GCF", "GCF,CPW", Region.GCF, Country.GCF);
    public static Mno SAMSUNG_RCS = new Mno("Samsung_OPEN", "SOR", Region.END_OF_REGION, Country.END_OF_COUNTRY);
    public static Mno CMCC = new Mno("CMCC_CN", "CHM,CBK,CHC", Region.EAST_ASIA, Country.CHINA);
    public static Mno CTC = new Mno("CTC_CN", "CTC", Region.EAST_ASIA, Country.CHINA);
    public static Mno CU = new Mno("CU_CN", "CHU", Region.EAST_ASIA, Country.CHINA);
    public static Mno CMHK = new Mno("CMHK_HK", "TGY", Region.EAST_ASIA, Country.HONGKONG);
    public static Mno CSL = new Mno("CSL_HK", "TGY", Region.EAST_ASIA, Country.HONGKONG);
    public static Mno HK3 = new Mno("3_HK", "TGY", Region.EAST_ASIA, Country.HONGKONG);
    public static Mno PCCW = new Mno("PCCW_HK", "TGY", Region.EAST_ASIA, Country.HONGKONG);
    public static Mno SMARTONE = new Mno("Smartone_HK", "TGY", Region.EAST_ASIA, Country.HONGKONG);
    public static Mno DOCOMO = new Mno("Docomo_JP", "DCM", Region.EAST_ASIA, Country.JAPAN);
    public static Mno KDDI = new Mno("KDDI_JP", "KDI,UQM,JCO,SJP", Region.EAST_ASIA, Country.JAPAN);
    public static Mno RAKUTEN_JAPAN = new Mno("Rakuten_JP", "RKT", Region.EAST_ASIA, Country.JAPAN);
    public static Mno SOFTBANK = new Mno("Softbank_JP", "SBM", Region.EAST_ASIA, Country.JAPAN);
    public static Mno KT = new Mno("KT_KR", "KTT,KTC,K06,K01", Region.EAST_ASIA, Country.KOREA);
    public static Mno LGU = new Mno("LGU+_KR", "LGT,LUC", Region.EAST_ASIA, Country.KOREA);
    public static Mno SAMSUNG = new Mno("SamsungTestbed_KR", ConfigConstants.PVALUE.CLIENT_VENDOR, Region.EAST_ASIA, Country.KOREA);
    public static Mno SKT = new Mno("SKT_KR", "SKT,SKC,KOO", Region.EAST_ASIA, Country.KOREA);
    public static Mno CTCMO = new Mno("CTC_MO", "TGY", Region.EAST_ASIA, Country.MACAU);
    public static Mno CTM = new Mno("CTM_MO", "TGY", Region.EAST_ASIA, Country.MACAU);
    public static Mno MACAU_THREE = new Mno("3_MO", "TGY", Region.EAST_ASIA, Country.MACAU);
    public static Mno MACAU_SMARTONE = new Mno("Smartone_MO", "TGY", Region.EAST_ASIA, Country.MACAU);
    public static Mno APT = new Mno("APT_TW", "BRI", Region.EAST_ASIA, Country.TAIWAN);
    public static Mno CHT = new Mno("CHT_TW", "BRI", Region.EAST_ASIA, Country.TAIWAN);
    public static Mno FET = new Mno("FET_TW", "BRI", Region.EAST_ASIA, Country.TAIWAN);
    public static Mno TSTAR = new Mno("TSTAR_TW", "BRI", Region.EAST_ASIA, Country.TAIWAN);
    public static Mno TWM = new Mno("TWM_TW", "BRI", Region.EAST_ASIA, Country.TAIWAN);
    public static Mno SEATEL_CAMBODIA = new Mno("Seatel_KH", "CAM", Region.SOUTH_EAST_ASIA, Country.CAMBODIA);
    public static Mno CELLCARD_CAMBODIA = new Mno("Cellcard_KH", "", Region.SOUTH_EAST_ASIA, Country.CAMBODIA);
    public static Mno SMART_CAMBODIA = new Mno("Smart_KH", "", Region.SOUTH_EAST_ASIA, Country.CAMBODIA);
    public static Mno SMARTFREN = new Mno("Smartfren_ID", "XID", Region.SOUTH_EAST_ASIA, Country.INDONESIA);
    public static Mno TELKOMSEL = new Mno("Telkomsel_ID", "", Region.SOUTH_EAST_ASIA, Country.INDONESIA);
    public static Mno INDOSAT_ID = new Mno("Indosat_ID", "XID", Region.SOUTH_EAST_ASIA, Country.INDONESIA);
    public static Mno XL_ID = new Mno("XL_ID", "", Region.SOUTH_EAST_ASIA, Country.INDONESIA);
    public static Mno CELCOM = new Mno("Celcom_MY", "XME", Region.SOUTH_EAST_ASIA, Country.MALAYSIA);
    public static Mno DIGI = new Mno("DIGI_MY", "", Region.SOUTH_EAST_ASIA, Country.MALAYSIA);
    public static Mno P1 = new Mno("P1_MY", "", Region.SOUTH_EAST_ASIA, Country.MALAYSIA);
    public static Mno UMOBILE = new Mno("UMobile_MY", "", Region.SOUTH_EAST_ASIA, Country.MALAYSIA);
    public static Mno YTL = new Mno("YTL_MY", "", Region.SOUTH_EAST_ASIA, Country.MALAYSIA);
    public static Mno MAXIS_MY = new Mno("Maxis_MY", "", Region.SOUTH_EAST_ASIA, Country.MALAYSIA);
    public static Mno GLOBE_PH = new Mno("Globe_PH", "GLB,XTC", Region.SOUTH_EAST_ASIA, Country.PHILIPPINES);
    public static Mno DITO = new Mno("DITO_PH", "XTC", Region.SOUTH_EAST_ASIA, Country.PHILIPPINES);
    public static Mno SMART_PH = new Mno("Smart_PH", "SMA", Region.SOUTH_EAST_ASIA, Country.PHILIPPINES);
    public static Mno MOBILEONE = new Mno("Mobileone_SG", "MM1", Region.SOUTH_EAST_ASIA, Country.SINGAPORE);
    public static Mno SINGTEL = new Mno("Singtel_SG", "SIN", Region.SOUTH_EAST_ASIA, Country.SINGAPORE);
    public static Mno STARHUB = new Mno("Starhub_SG", "STH", Region.SOUTH_EAST_ASIA, Country.SINGAPORE);
    public static Mno TPG_SG = new Mno("TPG_SG", "XSP", Region.SOUTH_EAST_ASIA, Country.SINGAPORE);
    public static Mno AIS = new Mno("AIS_TH", "THL", Region.SOUTH_EAST_ASIA, Country.THAILAND);
    public static Mno DTAC = new Mno("DTAC_TH", "", Region.SOUTH_EAST_ASIA, Country.THAILAND);
    public static Mno TRUEMOVE = new Mno("Truemove_TH", "", Region.SOUTH_EAST_ASIA, Country.THAILAND);
    public static Mno OOREDOO_MM = new Mno("Ooredoo_MM", "MYM", Region.SOUTH_EAST_ASIA, Country.MYANMAR);
    public static Mno TELENOR_MM = new Mno("Telenor_MM", "", Region.SOUTH_EAST_ASIA, Country.MYANMAR);
    public static Mno MPT_MM = new Mno("MPT_MM", "", Region.SOUTH_EAST_ASIA, Country.MYANMAR);
    public static Mno MYTEL = new Mno("Mytel_MM", "", Region.SOUTH_EAST_ASIA, Country.MYANMAR);
    public static Mno VINAPHONE = new Mno("Vinaphone_VN", "XXV", Region.SOUTH_EAST_ASIA, Country.VIETNAM);
    public static Mno VIETTEL = new Mno("Viettel_VN", "", Region.SOUTH_EAST_ASIA, Country.VIETNAM);
    public static Mno VIETNAMOBILE = new Mno("Vietnamobile_VN", "", Region.SOUTH_EAST_ASIA, Country.VIETNAM);
    public static Mno LAOTEL = new Mno("Laotel_LA", "LAO", Region.SOUTH_EAST_ASIA, Country.LAOS);
    public static Mno AIRTEL = new Mno("AIRTEL_IN", "", Region.SOUTH_WEST_ASIA, Country.INDIA);
    public static Mno BSNL = new Mno("BSNL_IN", "", Region.SOUTH_WEST_ASIA, Country.INDIA);
    public static Mno RJIL = new Mno("RJIL_IN", "INS,INU", Region.SOUTH_WEST_ASIA, Country.INDIA);
    public static Mno VODAFONE_INDIA = new Mno("Vodafone_IN", "", Region.SOUTH_WEST_ASIA, Country.INDIA);
    public static Mno IDEA_INDIA = new Mno("IDEA_IN", "", Region.SOUTH_WEST_ASIA, Country.INDIA);
    public static Mno ROBI = new Mno("ROBI_BD", "BKD,BNG", Region.SOUTH_WEST_ASIA, Country.BANGLADESH);
    public static Mno GRAMEENPHONE = new Mno("GRAMEENPHONE_BD", "", Region.SOUTH_WEST_ASIA, Country.BANGLADESH);
    public static Mno NAMASTE = new Mno("Namaste_NP", "NPL", Region.SOUTH_WEST_ASIA, Country.NEPAL);
    public static Mno TELENOR_PAK = new Mno("Telenor_PK", "PAK", Region.SOUTH_WEST_ASIA, Country.PAKISTAN);
    public static Mno JAZZ_PAK = new Mno("Jazz_PK", "", Region.SOUTH_WEST_ASIA, Country.PAKISTAN);
    public static Mno ZONG_PAK = new Mno("Zong_PK", "", Region.SOUTH_WEST_ASIA, Country.PAKISTAN);
    public static Mno DLOG = new Mno("Dialog_LK", "SLK,SLI", Region.SOUTH_WEST_ASIA, Country.SRILANKA);
    public static Mno MOBITEL_LK = new Mno("Mobitel_LK", "", Region.SOUTH_WEST_ASIA, Country.SRILANKA);
    public static Mno HUTCH_LK = new Mno("Hutch_LK", "", Region.SOUTH_WEST_ASIA, Country.SRILANKA);
    public static Mno AIRTEL_LK = new Mno("AIRTEL_LK", "", Region.SOUTH_WEST_ASIA, Country.SRILANKA);
    public static Mno OOREDOO_MV = new Mno("Ooredoo_MV", "", Region.SOUTH_WEST_ASIA, Country.MALDIVES);
    public static Mno PTR = new Mno("PTR_IL", "PTR", Region.MIDDLE_EAST, Country.ISRAEL);
    public static Mno CELLCOM = new Mno("Cellcom_IL", "CEL", Region.MIDDLE_EAST, Country.ISRAEL);
    public static Mno PCL = new Mno("PCL_IL", "PCL", Region.MIDDLE_EAST, Country.ISRAEL);
    public static Mno HOTMOBILE = new Mno("HotMobile_IL", "ILO", Region.MIDDLE_EAST, Country.ISRAEL);
    public static Mno ALFA = new Mno("Alfa_LB", "MID", Region.MIDDLE_EAST, Country.LEBANON);
    public static Mno TOUCH = new Mno("Touch_LB", "", Region.MIDDLE_EAST, Country.LEBANON);
    public static Mno VIVA_KUWAIT = new Mno("Viva_KW", "XSG", Region.MIDDLE_EAST, Country.KUWAIT);
    public static Mno ZAIN_KUWAIT = new Mno("Zain_KW", "", Region.MIDDLE_EAST, Country.KUWAIT);
    public static Mno OOREDOO_KUWAIT = new Mno("Ooredoo_KW", "", Region.MIDDLE_EAST, Country.KUWAIT);
    public static Mno MOI = new Mno("MOI_QA", "", Region.MIDDLE_EAST, Country.QATAR);
    public static Mno OOREDOO_QATAR = new Mno("Ooredoo_QA", "", Region.MIDDLE_EAST, Country.QATAR);
    public static Mno VODAFONE_QATAR = new Mno("Vodafone_QA", "", Region.MIDDLE_EAST, Country.QATAR);
    public static Mno AVEA_TURKEY = new Mno("AVEA_TR", "TUR", Region.MIDDLE_EAST, Country.TURKEY);
    public static Mno TURKCELL_TURKEY = new Mno("Turkcell_TR", "", Region.MIDDLE_EAST, Country.TURKEY);
    public static Mno VODAFONE_TURKEY = new Mno("Vodafone_TR", "", Region.MIDDLE_EAST, Country.TURKEY);
    public static Mno ETISALAT_UAE = new Mno("Etisalat_AE", "", Region.MIDDLE_EAST, Country.UAE);
    public static Mno DU_UAE = new Mno("DU_AE", "", Region.MIDDLE_EAST, Country.UAE);
    public static Mno ATHEER_UAE = new Mno("Atheer_AE", "", Region.MIDDLE_EAST, Country.UAE);
    public static Mno ETISALAT_EG = new Mno("Etisalat_EG", "EGY", Region.MIDDLE_EAST, Country.EGYPT);
    public static Mno VODAFONE_EG = new Mno("Vodafone_EG", "", Region.MIDDLE_EAST, Country.EGYPT);
    public static Mno WE_EG = new Mno("We_EG", "", Region.MIDDLE_EAST, Country.EGYPT);
    public static Mno ORANGE_EG = new Mno("Orange_EG", "", Region.MIDDLE_EAST, Country.EGYPT);
    public static Mno OMANTEL_OMAN = new Mno("Omantel_OM", "", Region.MIDDLE_EAST, Country.OMAN);
    public static Mno MCCI_IRAN = new Mno("Mcci_IR", "THR", Region.MIDDLE_EAST, Country.IRAN);
    public static Mno MTN_IRAN = new Mno("MTN_IR", "", Region.MIDDLE_EAST, Country.IRAN);
    public static Mno ZAIN_KSA = new Mno("Zain_SA", "KSA", Region.MIDDLE_EAST, Country.KSA);
    public static Mno STC_KSA = new Mno("STC_SA", "", Region.MIDDLE_EAST, Country.KSA);
    public static Mno MOBILY_KSA = new Mno("MOBILY_SA", "", Region.MIDDLE_EAST, Country.KSA);
    public static Mno VIVA_BAHRAIN = new Mno("Viva_BH", "", Region.MIDDLE_EAST, Country.BAHRAIN);
    public static Mno ZAIN_BAHRAIN = new Mno("Zain_BH", "", Region.MIDDLE_EAST, Country.BAHRAIN);
    public static Mno BATELCO_BAHRAIN = new Mno("Batelco_BH", "", Region.MIDDLE_EAST, Country.BAHRAIN);
    public static Mno ZAIN_JO = new Mno("Zain_JO", "", Region.MIDDLE_EAST, Country.JORDAN);
    public static Mno TELEKOM_ALBANIA = new Mno("Telekom_AL", "TAL", Region.EUROPE, Country.ALBANIA);
    public static Mno VODAFONE_ALBANIA = new Mno("Vodafone_AL", "AVF", Region.EUROPE, Country.ALBANIA);
    public static Mno MTS_ARMENIA = new Mno("MTS_AM", "", Region.EUROPE, Country.ARMENIA);
    public static Mno AUSTRIA_A1 = new Mno("A1_AT", "MOB", Region.EUROPE, Country.AUSTRIA);
    public static Mno BAKCELL_AZ = new Mno("Bakcell_AZ", "", Region.EUROPE, Country.AZERBAIJAN);
    public static Mno NAR_AZ = new Mno("Nar_AZ", "", Region.EUROPE, Country.AZERBAIJAN);
    public static Mno EPIC_CYPRUS = new Mno("Epic_CY", "", Region.EUROPE, Country.CYPRUS);
    public static Mno VODAFONE_CY = new Mno("Vodafone_CY", "", Region.EUROPE, Country.CYPRUS);
    public static Mno TELIA_EE = new Mno("Telia_EE", "SEB", Region.EUROPE, Country.ESTONIA);
    public static Mno ELISA_EE = new Mno("Elisa_EE", "", Region.EUROPE, Country.ESTONIA);
    public static Mno H3G_AT = new Mno("3_AT", "DRE,ATO", Region.EUROPE, Country.AUSTRIA);
    public static Mno TMOBILE_AUSTRIA = new Mno("TMobile_AT", "MAX,TRG", Region.EUROPE, Country.AUSTRIA);
    public static Mno VELCOM_BY = new Mno("Velcom_BY", "", Region.EUROPE, Country.BELARUS);
    public static Mno PROXIMUS = new Mno("Proximus_BE", "PRO,LUX", Region.EUROPE, Country.BELGIUM);
    public static Mno ORANGE_BELGIUM = new Mno("Orange_BE", "MST", Region.EUROPE, Country.BELGIUM);
    public static Mno TELENET_BELGIUM = new Mno("Telenet_BE", "", Region.EUROPE, Country.BELGIUM);
    public static Mno POST_LUXEMBOURG = new Mno("Post_LU", "", Region.EUROPE, Country.LUXEMBOURG);
    public static Mno TANGO_LUXEMBOURG = new Mno("Tango_LU", "", Region.EUROPE, Country.LUXEMBOURG);
    public static Mno ORANGE_LUXEMBOURG = new Mno("Orange_LU", "", Region.EUROPE, Country.LUXEMBOURG);
    public static Mno ORANGE_MOLDOVA = new Mno("Orange_MD", "", Region.EUROPE, Country.MOLDOVA);
    public static Mno TMOBILE_CZ = new Mno("TMobile_CZ", "TMZ,ETL,XEZ", Region.EUROPE, Country.CZECH);
    public static Mno TELEFONICA_CZ = new Mno("Telefonica_CZ", "O2C", Region.EUROPE, Country.CZECH);
    public static Mno VODAFONE_CZ = new Mno("Vodafone_CZ", "VDC", Region.EUROPE, Country.CZECH);
    public static Mno H3G_DK = new Mno("3_DK", "HTD", Region.EUROPE, Country.DENMARK);
    public static Mno TDC_DK = new Mno("TDC_DK", "", Region.EUROPE, Country.DENMARK);
    public static Mno TELENOR_DK = new Mno("Telenor_DK", "NEE", Region.EUROPE, Country.DENMARK);
    public static Mno TELIA_DK = new Mno("Telia_DK", "", Region.EUROPE, Country.DENMARK);
    public static Mno DNA_FINLAND = new Mno("DNA_FI", "", Region.EUROPE, Country.FINLAND);
    public static Mno ELISA_FINLAND = new Mno("Elisa_FI", "", Region.EUROPE, Country.FINLAND);
    public static Mno TELIA_FINLAND = new Mno("Telia_FI", "", Region.EUROPE, Country.FINLAND);
    public static Mno BOG = new Mno("Bouygues_FR", "BOG,XEF", Region.EUROPE, Country.FRANCE);
    public static Mno NRJ_FRANCE = new Mno("NRJ_FR", "", Region.EUROPE, Country.FRANCE);
    public static Mno ORANGE = new Mno("Orange_FR", "FTM", Region.EUROPE, Country.FRANCE);
    public static Mno SFR = new Mno("SFR_FR", "SFR", Region.EUROPE, Country.FRANCE);
    public static Mno EDF = new Mno("EDF_FR", "", Region.EUROPE, Country.FRANCE);
    public static Mno CORIOLIS = new Mno("Coriolis_FR", "", Region.EUROPE, Country.FRANCE);
    public static Mno FREE = new Mno("Free_FR", "", Region.EUROPE, Country.FRANCE);
    public static Mno MAGTICOM_GE = new Mno("Magticom_GE", "", Region.EUROPE, Country.GEORGIA);
    public static Mno TELEFONICA_GERMANY = new Mno("Telefonica_DE", "VIA", Region.EUROPE, Country.GERMANY);
    public static Mno TMOBILE = new Mno("TMobile_DE", "DTR,DTM,DBT,DCO,XEG", Region.EUROPE, Country.GERMANY);
    public static Mno VODAFONE = new Mno("Vodafone_DE", "VD2", Region.EUROPE, Country.GERMANY);
    public static Mno TMOBILE_GREECE = new Mno("TMobile_GR", "COS,EUR", Region.EUROPE, Country.GREECE);
    public static Mno VODAFONE_GREECE = new Mno("Vodafone_GR", "VGR", Region.EUROPE, Country.GREECE);
    public static Mno WIND_GREECE = new Mno("Wind_GR", "EUR", Region.EUROPE, Country.GREECE);
    public static Mno TELEPOST_GREENLAND = new Mno("Telepost_GL", "NEE", Region.EUROPE, Country.GREENLAND);
    public static Mno TMOBILE_HUNGARY = new Mno("TMobile_HU", "TMH,XEH", Region.EUROPE, Country.HUNGARY);
    public static Mno VODAFONE_HUNGARY = new Mno("Vodafone_HU", "VDH", Region.EUROPE, Country.HUNGARY);
    public static Mno TELENOR_HUNGARY = new Mno("Telenor_HU", "PAN", Region.EUROPE, Country.HUNGARY);
    public static Mno DIGI_HUNGARY = new Mno("DIGI_HU", "", Region.EUROPE, Country.HUNGARY);
    public static Mno TMOBILE_CROATIA = new Mno("TMobile_HR", "CRO,SEE,DHR", Region.EUROPE, Country.CROATIA);
    public static Mno VODAFONE_CROATIA = new Mno("Vodafone_HR", "VIP", Region.EUROPE, Country.CROATIA);
    public static Mno TELEKOM_SERBIA = new Mno("Telekom_RS", "", Region.EUROPE, Country.SERBIA);
    public static Mno TELENOR_SERBIA = new Mno("Telenor_RS", "MSR", Region.EUROPE, Country.SERBIA);
    public static Mno VIP_SERBIA = new Mno("Vipmobile_RS", "TOP", Region.EUROPE, Country.SERBIA);
    public static Mno METEOR_IRELAND = new Mno("Meteor_IE", "MET", Region.EUROPE, Country.IRELAND);
    public static Mno VODAFONE_IRELAND = new Mno("Vodafone_IE", "VDI", Region.EUROPE, Country.IRELAND);
    public static Mno TELECOM_ITALY = new Mno("Telecom_IT", "TIM", Region.EUROPE, Country.ITALY);
    public static Mno VODAFONE_ITALY = new Mno("Vodafone_IT", "OMN,ITV", Region.EUROPE, Country.ITALY);
    public static Mno WINDTRE = new Mno("Windtre_IT", "HUI", Region.EUROPE, Country.ITALY);
    public static Mno ALTEL_KAZAKHSTAN = new Mno("Altel_KZ", "SKZ", Region.EUROPE, Country.KAZAKHSTAN);
    public static Mno BEELINE_KAZAKHSTAN = new Mno("Beeline_KZ", "SKZ", Region.EUROPE, Country.KAZAKHSTAN);
    public static Mno TELE2_KAZAKHSTAN = new Mno("Tele2_KZ", "SKZ", Region.EUROPE, Country.KAZAKHSTAN);
    public static Mno LMT_LATVIA = new Mno("LMT_LV", "", Region.EUROPE, Country.LATVIA);
    public static Mno TELECOM_LI = new Mno("Telecom_LI", "", Region.EUROPE, Country.LIECHTENSTEIN);
    public static Mno ZEB = new Mno("ZEB_LT", "ZEB,SEB", Region.EUROPE, Country.LITHUANIA);
    public static Mno TMOBILE_MK = new Mno("TMobile_MK", "MBM", Region.EUROPE, Country.MACEDONIA);
    public static Mno VIP_MACEDONIA = new Mno("VIP_MK", "", Region.EUROPE, Country.MACEDONIA);
    public static Mno TMOBILE_ME = new Mno("TMOBILE_ME", "TMT", Region.EUROPE, Country.MONTENEGRO);
    public static Mno TELE2NL = new Mno("Tele2_NL", "PHN", Region.EUROPE, Country.NETHERLANDS);
    public static Mno TMOBILE_NED = new Mno("TMobile_NL", "TNL,DNL", Region.EUROPE, Country.NETHERLANDS);
    public static Mno VODAFONE_NETHERLAND = new Mno("Vodafone_NL", "VDF,VDP", Region.EUROPE, Country.NETHERLANDS);
    public static Mno KPN_NED = new Mno("KPN_NL", "", Region.EUROPE, Country.NETHERLANDS);
    public static Mno TELENOR_NORWAY = new Mno("Telenor_NO", "TEN", Region.EUROPE, Country.NORWAY);
    public static Mno TELIA_NORWAY = new Mno("Telia_NO", "", Region.EUROPE, Country.NORWAY);
    public static Mno ICENET_NORWAY = new Mno("IceNet_NO", "", Region.EUROPE, Country.NORWAY);
    public static Mno ORANGE_POLAND = new Mno("Orange_PL", "OPV,XEO,IDE", Region.EUROPE, Country.POLAND);
    public static Mno TMOBILE_PL = new Mno("TMobile_PL", "TPL,DPL", Region.EUROPE, Country.POLAND);
    public static Mno PLAY = new Mno("Play_PL", "PRT", Region.EUROPE, Country.POLAND);
    public static Mno PLUS_POLAND = new Mno("Plus_PL", "PLS", Region.EUROPE, Country.POLAND);
    public static Mno NOS_PORTUGAL = new Mno("NOS_PT", "OPT", Region.EUROPE, Country.PORTUGAL);
    public static Mno MEO_PORTUGAL = new Mno("MEO_PT", "MEO", Region.EUROPE, Country.PORTUGAL);
    public static Mno VODAFONE_PORTUGAL = new Mno("Vodafone_PT", "TCL,TPH", Region.EUROPE, Country.PORTUGAL);
    public static Mno VODAFONE_ROMANIA = new Mno("Vodafone_RO", "CNX,ROM", Region.EUROPE, Country.ROMANIA);
    public static Mno ORANGE_ROMANIA = new Mno("Orange_RO", "ORO", Region.EUROPE, Country.ROMANIA);
    public static Mno TMOBILE_ROMANIA = new Mno("TMobile_RO", "COA", Region.EUROPE, Country.ROMANIA);
    public static Mno RDS_ROMANIA = new Mno("RDS_RO", "", Region.EUROPE, Country.ROMANIA);
    public static Mno MTS_RUSSIA = new Mno("MTS_RU", "", Region.EUROPE, Country.RUSSIA);
    public static Mno MEGAFON_RUSSIA = new Mno("Megafon_RU", "", Region.EUROPE, Country.RUSSIA);
    public static Mno BEELINE_RUSSIA = new Mno("Beeline_RU", "", Region.EUROPE, Country.RUSSIA);
    public static Mno TELE2_RUSSIA = new Mno("TELE2_RU", "SER,CAU", Region.EUROPE, Country.RUSSIA);
    public static Mno SBERBANK_RUSSIA = new Mno("Sberbank_RU", "", Region.EUROPE, Country.RUSSIA);
    public static Mno TATTELECOM_RUSSIA = new Mno("Tattelecom_RU", "", Region.EUROPE, Country.RUSSIA);
    public static Mno MOTIV_RUSSIA = new Mno("Motiv_RU", "SER,CAU", Region.EUROPE, Country.RUSSIA);
    public static Mno ORANGE_SPAIN = new Mno("Orange_ES", "AMO,PHE", Region.EUROPE, Country.SPAIN);
    public static Mno VODAFONE_SPAIN = new Mno("Vodafone_ES", "ATL", Region.EUROPE, Country.SPAIN);
    public static Mno TELEFONICA_SPAIN = new Mno("Telefonica_ES", "XEC,PHE", Region.EUROPE, Country.SPAIN);
    public static Mno ORANGE_SLOVAKIA = new Mno("Orange_SK", "ORX,ORS", Region.EUROPE, Country.SLOVAKIA);
    public static Mno TMOBILE_SLOVAKIA = new Mno("TMobile_SK", "TMS", Region.EUROPE, Country.SLOVAKIA);
    public static Mno TELEFONICA_SLOVAKIA = new Mno("Telefonica_SK", "ORV", Region.EUROPE, Country.SLOVAKIA);
    public static Mno SWAN_SLOVAKIA = new Mno("4ka_SK", "", Region.EUROPE, Country.SLOVAKIA);
    public static Mno A1_SVN = new Mno("A1_SI", "SIM", Region.EUROPE, Country.SLOVENIA);
    public static Mno TELEKOM_SVN = new Mno("Telekom_SI", "MOT,SIO", Region.EUROPE, Country.SLOVENIA);
    public static Mno TELEMACH_SVN = new Mno("Telemach_SI", "SEE", Region.EUROPE, Country.SLOVENIA);
    public static Mno H3G_SE = new Mno("3_SE", "HTS", Region.EUROPE, Country.SWEDEN);
    public static Mno TELENOR_SWE = new Mno("Telenor_SE", "VDS", Region.EUROPE, Country.SWEDEN);
    public static Mno TELE2_SWE = new Mno("Tele2_SE", "", Region.EUROPE, Country.SWEDEN);
    public static Mno TELIA_SWE = new Mno("Telia_SE", "", Region.EUROPE, Country.SWEDEN);
    public static Mno ORANGE_SWITZERLAND = new Mno("Orange_CH", "", Region.EUROPE, Country.SWITZERLAND);
    public static Mno SWISSCOM = new Mno("Swisscom_CH", "SWC,AUT", Region.EUROPE, Country.SWITZERLAND);
    public static Mno SUNRISE_CH = new Mno("Sunrise_CH", "", Region.EUROPE, Country.SWITZERLAND);
    public static Mno UPC_CH = new Mno("UPC_CH", "", Region.EUROPE, Country.SWITZERLAND);
    public static Mno EE = new Mno("EE_GB", "EVR,BTB,BTE", Region.EUROPE, Country.UK);
    public static Mno EE_ESN = new Mno("EEESN_GB", "U06", Region.EUROPE, Country.UK);
    public static Mno VIRGIN = new Mno("Virgin_GB", "", Region.EUROPE, Country.UK);
    public static Mno GAMMA = new Mno("Gamma_GB", "", Region.EUROPE, Country.UK);
    public static Mno SMARTY = new Mno("SMARTY_GB", "", Region.EUROPE, Country.UK);
    public static Mno SUPERDRUG = new Mno("Superdrug_GB", "", Region.EUROPE, Country.UK);
    public static Mno BTOP = new Mno("BTOP_GB", "", Region.EUROPE, Country.UK);
    public static Mno H3G = new Mno("Hutchison_GB", "H3G", Region.EUROPE, Country.UK);
    public static Mno TELEFONICA_UK = new Mno("Telefonica_GB", "O2U,BTU,XEU", Region.EUROPE, Country.UK);
    public static Mno TELEFONICA_UK_LAB = new Mno("TelefonicaLAB_GB", "", Region.EUROPE, Country.UK);
    public static Mno VODAFONE_UK = new Mno("Vodafone_GB", "VOD", Region.EUROPE, Country.UK);
    public static Mno SKY = new Mno("Sky_GB", "", Region.EUROPE, Country.UK);
    public static Mno LYCA = new Mno("Lyca_GB", "", Region.EUROPE, Country.UK);
    public static Mno NOVA_IS = new Mno("Nova_IS", "", Region.EUROPE, Country.ICELAND);
    public static Mno TELENOR_BULGARIA = new Mno("Telenor_BG", "BGL", Region.EUROPE, Country.BULGARIA);
    public static Mno A1_BULGARIA = new Mno("A1_BG", "", Region.EUROPE, Country.BULGARIA);
    public static Mno VIVACOM_BULGARIA = new Mno("Vivacom_BG", "VVT", Region.EUROPE, Country.BULGARIA);
    public static Mno KYIVSTAR_UA = new Mno("Kyivstar_UA", "", Region.EUROPE, Country.UKRAINE);
    public static Mno LIFECELL_UA = new Mno("Lifecell_UA", "SEK", Region.EUROPE, Country.UKRAINE);
    public static Mno BELL = new Mno("Bell_CA", "BMC,BMR,VMC,XAC", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno ROGERS = new Mno("RWC_CA", "RWC,FMC,TBT", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno CHATR = new Mno("CHATR_CA", "CHR,RWC,FMC", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno ZTAR = new Mno("ZTAR_CA", "CHR,RWC,FMC", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno CTF = new Mno("CTF_CA", "CHR,RWC,FMC", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno MOBILICITY = new Mno("MOBILICITY_CA", "CHR,RWC,FMC", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno TELUS = new Mno("Telus_CA", "TLS,TLA", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno KOODO = new Mno("Koodo_CA", "KDO", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno VTR = new Mno("VideoTron_CA", "VTR", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno EASTLINK = new Mno("ESK_CA", "ESK", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno SASKTEL = new Mno("SaskTel_CA", "BWA", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno WIND = new Mno("Wind_CA", "GLW,SJR", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno XPLORE = new Mno("Xplore_CA", "XPL", Region.NORTH_AMERICA, Country.CANADA);
    public static Mno ATT = new Mno("ATT_US", "ATT,TFA,AIO,XAR,APP", Region.NORTH_AMERICA, Country.US);
    public static Mno TMOUS = new Mno("TMobile_US", "TMB,TMK,TFO,XAG,XAA,DSH", Region.NORTH_AMERICA, Country.US);
    public static Mno SPRINT = new Mno("Sprint_US", "SPR,BST,VMU,TFS,XAS", Region.NORTH_AMERICA, Country.US);
    public static Mno USCC = new Mno("USCC_US", "USC", Region.NORTH_AMERICA, Country.US);
    public static Mno VZW = new Mno("VZW_US", "VZW,CCT,LRA,TFV,TFN,CHA,FKR,VPP", Region.NORTH_AMERICA, Country.US);
    public static Mno ALTICE = new Mno("Altice_US", "ATC", Region.NORTH_AMERICA, Country.US);
    public static Mno GCI = new Mno("GCI_US", "", Region.NORTH_AMERICA, Country.US);
    public static Mno GEOVERSE = new Mno("Geoverse_US", "", Region.NORTH_AMERICA, Country.US);
    public static Mno GOOGLEGCUS = new Mno("GoogleGC_US", "", Region.NORTH_AMERICA, Country.US);
    public static Mno INTEROP = new Mno("Interop_US", "", Region.NORTH_AMERICA, Country.US);
    public static Mno GENERIC_IR92 = new Mno("GenericIR92_US", "ACG", Region.NORTH_AMERICA, Country.US);
    public static Mno DPAC = new Mno("DPAC_US", "", Region.NORTH_AMERICA, Country.US);
    public static Mno GTA = new Mno("GTA_US", "", Region.NORTH_AMERICA, Country.US);
    public static Mno CLARO_ARGENTINA = new Mno("Claro_AR", "CTI,ARO", Region.SOUTH_AMERICA, Country.ARGENTINA);
    public static Mno MOVISTAR_ARGENTINA = new Mno("Movistar_AR", "UFN,ARO", Region.SOUTH_AMERICA, Country.ARGENTINA);
    public static Mno PERSONAL_ARGENTINA = new Mno("Personal_AR", "PSN,ARO", Region.SOUTH_AMERICA, Country.ARGENTINA);
    public static Mno ALIV_BAHAMAS = new Mno("Aliv_BS", "BAA,TTT", Region.SOUTH_AMERICA, Country.BAHAMAS);
    public static Mno VIVO_BRAZIL = new Mno("Vivo_BR", "ZVV,ZTO", Region.SOUTH_AMERICA, Country.BRAZIL);
    public static Mno TIM_BRAZIL = new Mno("Tim_BR", "ZTM,ZTO", Region.SOUTH_AMERICA, Country.BRAZIL);
    public static Mno CLARO_BRAZIL = new Mno("Claro_BR", "ZTA,ZTO", Region.SOUTH_AMERICA, Country.BRAZIL);
    public static Mno OI_BRAZIL = new Mno("Oi_BR", "ZTR,ZTO", Region.SOUTH_AMERICA, Country.BRAZIL);
    public static Mno AVANTEL_COLOMBIA = new Mno("Avantel_CO", "COD,COO", Region.SOUTH_AMERICA, Country.COLOMBIA);
    public static Mno MOVISTAR_COLOMBIA = new Mno("Movistar_CO", "COB,COO", Region.SOUTH_AMERICA, Country.COLOMBIA);
    public static Mno CLARO_COLOMBIA = new Mno("Claro_CO", "COM,COO", Region.SOUTH_AMERICA, Country.COLOMBIA);
    public static Mno TIGO_COLOMBIA = new Mno("Tigo_CO", "COL,COO", Region.SOUTH_AMERICA, Country.COLOMBIA);
    public static Mno CLARO_COSTARICA = new Mno("Claro_CR", "CRC,GTO", Region.SOUTH_AMERICA, Country.COSTA_RICA);
    public static Mno CLARO_DOMINICAN = new Mno("Claro_DR", "CDR,DOO", Region.SOUTH_AMERICA, Country.DOMINICAN);
    public static Mno ORANGE_DOMINICANA = new Mno("Orange_DO", "DOR,DOO", Region.SOUTH_AMERICA, Country.DOMINICAN);
    public static Mno MOVISTAR_ECUADOR = new Mno("Movistar_EC", "EBE,EON", Region.SOUTH_AMERICA, Country.ECUADOR);
    public static Mno CLARO_ECUADOR = new Mno("Claro_EC", "ECO,EON", Region.SOUTH_AMERICA, Country.ECUADOR);
    public static Mno ALE_ECUADOR = new Mno("ALE_EC", "ALE,EON", Region.SOUTH_AMERICA, Country.ECUADOR);
    public static Mno CLARO_ELSALVADOR = new Mno("Claro_SV", "PGU,GTO", Region.SOUTH_AMERICA, Country.EL_SALVADOR);
    public static Mno TIGO_GUATEMALA = new Mno("Tigo_GT", "CGU,GTO,TPA", Region.SOUTH_AMERICA, Country.GUATEMALA);
    public static Mno CLARO_GUATEMALA = new Mno("Claro_GT", "PGU,GTO", Region.SOUTH_AMERICA, Country.GUATEMALA);
    public static Mno CLARO_HONDURAS = new Mno("Claro_HN", "PGU,GTO", Region.SOUTH_AMERICA, Country.HONDURAS);
    public static Mno TCE = new Mno("Telcel_MX", "TCE,MXO", Region.SOUTH_AMERICA, Country.MEXICO);
    public static Mno ATT_MEXICO = new Mno("Att_ius_MX", "IUS,MXO", Region.SOUTH_AMERICA, Country.MEXICO);
    public static Mno ALTAN_MEXICO = new Mno("Altan_MX", "MXO", Region.SOUTH_AMERICA, Country.MEXICO);
    public static Mno MOVISTAR_MEXICO = new Mno("Movistar_MX", "TMM,MXO", Region.SOUTH_AMERICA, Country.MEXICO);
    public static Mno AIRBUS_MEXICO = new Mno("Airbus_MX", "MXO", Region.SOUTH_AMERICA, Country.MEXICO);
    public static Mno CLARO_NICARAGUA = new Mno("Claro_NI", "PGU,GTO", Region.SOUTH_AMERICA, Country.NICARAGUA);
    public static Mno CABLE_PANAMA = new Mno("Cable_PA", "PCW,TPA,GTO", Region.SOUTH_AMERICA, Country.PANAMA);
    public static Mno MOVISTAR_PANAMA = new Mno("Movistar_PA", "PBS,GTO,TPA", Region.SOUTH_AMERICA, Country.PANAMA);
    public static Mno CLARO_PANAMA = new Mno("Claro_PA", "CPA,GTO", Region.SOUTH_AMERICA, Country.PANAMA);
    public static Mno CLARO_PARAGUAY = new Mno("Claro_PY", "CTP,UPO", Region.SOUTH_AMERICA, Country.PARAGUAY);
    public static Mno CLARO_PERU = new Mno("Claro_PE", "PET,PEO", Region.SOUTH_AMERICA, Country.PERU);
    public static Mno ENTEL_PERU = new Mno("Entel_PE", "PNT,PEO", Region.SOUTH_AMERICA, Country.PERU);
    public static Mno MOVISTAR_PERU = new Mno("Movistar_PE", "SAM,PEO", Region.SOUTH_AMERICA, Country.PERU);
    public static Mno CLARO_PUERTO = new Mno("Claro_PR", "PCT", Region.SOUTH_AMERICA, Country.PUERTO);
    public static Mno ENTEL_BOLIVIA = new Mno("Entel_BO", "BVO,BVE", Region.SOUTH_AMERICA, Country.BOLIVIA);
    public static Mno TIGO_BOLIVIA = new Mno("Tigo_BO", "BVT,BVO", Region.SOUTH_AMERICA, Country.BOLIVIA);
    public static Mno MOVISTAR_CHILE = new Mno("Movistar_CL", "CHT,CHO,CHH,CHP,CHQ,CHK", Region.SOUTH_AMERICA, Country.CHILE);
    public static Mno CLARO_CHILE = new Mno("Claro_CL", "CHL,CHO,CHH,CHP,CHQ,CHK", Region.SOUTH_AMERICA, Country.CHILE);
    public static Mno WOM_CHILE = new Mno("Wom_CL", "CHX,CHO,CHH,CHP,CHQ,CHK", Region.SOUTH_AMERICA, Country.CHILE);
    public static Mno ENTEL_CHILE = new Mno("Entel_CL", "CHE,CHO,CHH,CHP,CHQ,CHK", Region.SOUTH_AMERICA, Country.CHILE);
    public static Mno MOVISTAR_URUGUAY = new Mno("Movistar_UY", "UFU,UYO,UPO", Region.SOUTH_AMERICA, Country.URUGUAY);
    public static Mno CLARO_URUGUAY = new Mno("Claro_UY", "CTU,UYO,UPO", Region.SOUTH_AMERICA, Country.URUGUAY);
    public static Mno MAROC_MOROCCO = new Mno("Maroc_MA", "MAT", Region.AFRICA, Country.MOROCO);
    public static Mno INWI_MOROCCO = new Mno("INWI_MA", "MWD", Region.AFRICA, Country.MOROCO);
    public static Mno ORANGE_MOROCCO = new Mno("Orange_MA", "", Region.AFRICA, Country.MOROCO);
    public static Mno ORANGE_SENEGAL = new Mno("Orange_SN", "DKR", Region.AFRICA, Country.SENEGAL);
    public static Mno SMILE_NIGERIA = new Mno("Smile_NG", "ECT", Region.AFRICA, Country.NIGERIA);
    public static Mno NTEL_NIGERIA = new Mno("Ntel_NG", "", Region.AFRICA, Country.NIGERIA);
    public static Mno MTN_NIGERIA = new Mno("MTN_NG", "", Region.AFRICA, Country.NIGERIA);
    public static Mno OOREDOO_TUNISIA = new Mno("Ooredoo_TN", "TUN", Region.AFRICA, Country.TUNISIA);
    public static Mno CELLC_SOUTHAFRICA = new Mno("CellC_ZA", "XFA,XFV", Region.AFRICA, Country.SOUTHAFRICA);
    public static Mno MTN_SOUTHAFRICA = new Mno("MTN_ZA", "", Region.AFRICA, Country.SOUTHAFRICA);
    public static Mno VODACOM_SOUTHAFRICA = new Mno("Vodacom_ZA", "", Region.AFRICA, Country.SOUTHAFRICA);
    public static Mno TELKOM_SOUTHAFRICA = new Mno("Telkom_ZA", "", Region.AFRICA, Country.SOUTHAFRICA);
    public static Mno SMILE_TANZANIA = new Mno("Smile_TZ", "AFR", Region.AFRICA, Country.TANZANIA);
    public static Mno SMILE_UGANDA = new Mno("Smile_UG", "", Region.AFRICA, Country.UGANDA);
    public static Mno MTN_GHANA = new Mno("MTN_GH", "ACR", Region.AFRICA, Country.GHANA);
    public static Mno SAFARICOM_KENYA = new Mno("Safaricom_KE", "", Region.AFRICA, Country.KENYA);
    public static Mno JTL_KENYA = new Mno("JTL_KE", "", Region.AFRICA, Country.KENYA);
    public static Mno OPTUS = new Mno("Optus_AU", "OPS,OPP", Region.OCEANIA, Country.AUSTRALIA);
    public static Mno TELSTRA = new Mno("Telstra_AU", "TEL,XSA,TLP,ATS", Region.OCEANIA, Country.AUSTRALIA);
    public static Mno VODAFONE_AUSTRALIA = new Mno("Vodafone_AU", "VAU,VAP", Region.OCEANIA, Country.AUSTRALIA);
    public static Mno TWO_DEGREE = new Mno("TwoDegree_NZ", "NZC,2DX,XNZ", Region.OCEANIA, Country.NEWZEALAND);
    public static Mno VODAFONE_NEWZEALAND = new Mno("Vodafone_NZ", "VNZ,VNX", Region.OCEANIA, Country.NEWZEALAND);
    public static Mno SPARK = new Mno("Spark_NZ", "TNZ,TNX", Region.OCEANIA, Country.NEWZEALAND);
    public static Mno BLUESKY = new Mno("Bluesky_NZ", "", Region.OCEANIA, Country.NEWZEALAND);
    public static Mno ASTCA = new Mno("Astca_NZ", "", Region.OCEANIA, Country.NEWZEALAND);

    public enum Region {
        GCF,
        EAST_ASIA,
        SOUTH_EAST_ASIA,
        SOUTH_WEST_ASIA,
        MIDDLE_EAST,
        EUROPE,
        NORTH_AMERICA,
        SOUTH_AMERICA,
        AFRICA,
        OCEANIA,
        END_OF_REGION
    }

    public enum Country {
        GCF(Region.GCF, "GCF"),
        CHINA(Region.EAST_ASIA, "CN"),
        HONGKONG(Region.EAST_ASIA, "HK"),
        JAPAN(Region.EAST_ASIA, "JP"),
        KOREA(Region.EAST_ASIA, "KR"),
        MACAU(Region.EAST_ASIA, "MO"),
        TAIWAN(Region.EAST_ASIA, "TW"),
        CAMBODIA(Region.SOUTH_EAST_ASIA, "KH"),
        INDONESIA(Region.SOUTH_EAST_ASIA, UserConsentProviderContract.UserConsentList.ID),
        MALAYSIA(Region.SOUTH_EAST_ASIA, "MY"),
        PHILIPPINES(Region.SOUTH_EAST_ASIA, "PH"),
        SINGAPORE(Region.SOUTH_EAST_ASIA, "SG"),
        THAILAND(Region.SOUTH_EAST_ASIA, "TH"),
        VIETNAM(Region.SOUTH_EAST_ASIA, "VN"),
        MYANMAR(Region.SOUTH_EAST_ASIA, "MM"),
        LAOS(Region.SOUTH_EAST_ASIA, "LA"),
        INDIA(Region.SOUTH_WEST_ASIA, "IN"),
        NEPAL(Region.SOUTH_WEST_ASIA, "NP"),
        PAKISTAN(Region.SOUTH_WEST_ASIA, "PK"),
        SRILANKA(Region.SOUTH_WEST_ASIA, "LK"),
        MALDIVES(Region.SOUTH_WEST_ASIA, "MV"),
        BANGLADESH(Region.SOUTH_WEST_ASIA, "BD"),
        ISRAEL(Region.MIDDLE_EAST, "IL"),
        KUWAIT(Region.MIDDLE_EAST, "KW"),
        TURKEY(Region.MIDDLE_EAST, "TR"),
        QATAR(Region.MIDDLE_EAST, "QA"),
        UAE(Region.MIDDLE_EAST, "AE"),
        OMAN(Region.MIDDLE_EAST, "OM"),
        IRAN(Region.MIDDLE_EAST, "IR"),
        KSA(Region.MIDDLE_EAST, "SA"),
        BAHRAIN(Region.MIDDLE_EAST, "BH"),
        EGYPT(Region.MIDDLE_EAST, "EG"),
        JORDAN(Region.MIDDLE_EAST, "JO"),
        LEBANON(Region.MIDDLE_EAST, "LB"),
        ALBANIA(Region.EUROPE, "AL"),
        ARMENIA(Region.EUROPE, "AM"),
        AUSTRIA(Region.EUROPE, "AT"),
        AZERBAIJAN(Region.EUROPE, "AZ"),
        BELARUS(Region.EUROPE, "BY"),
        BELGIUM(Region.EUROPE, "BE"),
        BULGARIA(Region.EUROPE, "BG"),
        CROATIA(Region.EUROPE, "HR"),
        CYPRUS(Region.EUROPE, "CY"),
        CZECH(Region.EUROPE, "CZ"),
        DENMARK(Region.EUROPE, "DK"),
        ESTONIA(Region.EUROPE, "EE"),
        FINLAND(Region.EUROPE, "FI"),
        FRANCE(Region.EUROPE, "FR"),
        GEORGIA(Region.EUROPE, "GE"),
        GERMANY(Region.EUROPE, "DE"),
        GREECE(Region.EUROPE, "GR"),
        GREENLAND(Region.EUROPE, "GL"),
        HUNGARY(Region.EUROPE, "HU"),
        ICELAND(Region.EUROPE, "IS"),
        IRELAND(Region.EUROPE, "IE"),
        ITALY(Region.EUROPE, "IT"),
        KAZAKHSTAN(Region.EUROPE, "KZ"),
        LATVIA(Region.EUROPE, "LV"),
        LIECHTENSTEIN(Region.EUROPE, "LI"),
        LITHUANIA(Region.EUROPE, "LT"),
        LUXEMBOURG(Region.EUROPE, "LU"),
        MACEDONIA(Region.EUROPE, "MK"),
        MOLDOVA(Region.EUROPE, "MD"),
        MONTENEGRO(Region.EUROPE, "ME"),
        NETHERLANDS(Region.EUROPE, "NL"),
        NORWAY(Region.EUROPE, "NO"),
        POLAND(Region.EUROPE, "PL"),
        PORTUGAL(Region.EUROPE, "PT"),
        ROMANIA(Region.EUROPE, "RO"),
        RUSSIA(Region.EUROPE, "RU"),
        SERBIA(Region.EUROPE, "RS"),
        SLOVAKIA(Region.EUROPE, "SK"),
        SLOVENIA(Region.EUROPE, "SI"),
        SPAIN(Region.EUROPE, "ES"),
        SWEDEN(Region.EUROPE, "SE"),
        SWITZERLAND(Region.EUROPE, "CH"),
        UK(Region.EUROPE, "GB"),
        UKRAINE(Region.EUROPE, "UA"),
        CANADA(Region.NORTH_AMERICA, "CA"),
        US(Region.NORTH_AMERICA, "US"),
        ARGENTINA(Region.SOUTH_AMERICA, "AR"),
        BAHAMAS(Region.SOUTH_AMERICA, "BS"),
        BRAZIL(Region.SOUTH_AMERICA, "BR"),
        COLOMBIA(Region.SOUTH_AMERICA, "CO"),
        COSTA_RICA(Region.SOUTH_AMERICA, "CR"),
        ECUADOR(Region.SOUTH_AMERICA, DiagnosisConstants.RCSM_MTYP_EC),
        EL_SALVADOR(Region.SOUTH_AMERICA, "SV"),
        DOMINICAN(Region.SOUTH_AMERICA, "DR"),
        GUATEMALA(Region.SOUTH_AMERICA, "GT"),
        HONDURAS(Region.SOUTH_AMERICA, "HN"),
        MEXICO(Region.SOUTH_AMERICA, "MX"),
        NICARAGUA(Region.SOUTH_AMERICA, "NI"),
        PANAMA(Region.SOUTH_AMERICA, "PA"),
        PERU(Region.SOUTH_AMERICA, "PE"),
        CHILE(Region.SOUTH_AMERICA, "CL"),
        BOLIVIA(Region.SOUTH_AMERICA, "BO"),
        URUGUAY(Region.SOUTH_AMERICA, "UY"),
        PUERTO(Region.SOUTH_AMERICA, "PR"),
        PARAGUAY(Region.SOUTH_AMERICA, "PY"),
        MOROCO(Region.AFRICA, "MA"),
        NIGERIA(Region.AFRICA, "NG"),
        SOUTHAFRICA(Region.AFRICA, "ZA"),
        TANZANIA(Region.AFRICA, "TZ"),
        UGANDA(Region.AFRICA, "UG"),
        ZAMBIA(Region.AFRICA, "ZM"),
        GHANA(Region.AFRICA, "GH"),
        KENYA(Region.AFRICA, "KE"),
        SENEGAL(Region.AFRICA, "SN"),
        TUNISIA(Region.AFRICA, "TN"),
        AUSTRALIA(Region.OCEANIA, "AU"),
        NEWZEALAND(Region.OCEANIA, "NZ"),
        END_OF_COUNTRY(Region.END_OF_REGION, "END");

        private final String countryIso;
        private final Region region;

        Country(Region region, String countryIso) {
            this.region = region;
            this.countryIso = countryIso;
        }

        public Region getRegion() {
            return this.region;
        }

        public String getCountryIso() {
            return this.countryIso;
        }
    }

    public static Mno fromSalesCode(String operator) {
        if (getMockMno() != null) {
            return getMockMno();
        }
        if ("TFN".equals(operator)) {
            String baseSalesCode = SemCscFeature.getInstance().getString("CscFeature_Common_ConfigMvnoBaseNet", "VZW");
            if ("VZW".equalsIgnoreCase(baseSalesCode)) {
                return VZW;
            }
            if (NSDSNamespaces.NSDSSettings.CHANNEL_NAME_TMO.equalsIgnoreCase(baseSalesCode)) {
                return TMOUS;
            }
            if ("ATT".equalsIgnoreCase(baseSalesCode) || "APP".equalsIgnoreCase(baseSalesCode)) {
                return ATT;
            }
        } else {
            for (Mno mno : sTable) {
                String str = mno.mSalesCode;
                if (str != null) {
                    String[] salesCodes = str.split(",");
                    for (String s : salesCodes) {
                        if (s.equals(operator)) {
                            return mno;
                        }
                    }
                    continue;
                }
            }
        }
        return DEFAULT;
    }

    public static Mno fromName(String name) {
        String mnoName;
        String str = LOG_TAG;
        Log.d(str, "fromName: " + name);
        String mockMnoname = getMockMnoname();
        if (!TextUtils.isEmpty(mockMnoname)) {
            name = mockMnoname;
            String str2 = LOG_TAG;
            Log.d(str2, "fromName: use mockMnoname: " + mockMnoname);
        }
        int delimiterPos = name.indexOf(MVNO_DELIMITER);
        if (delimiterPos != -1) {
            mnoName = name.substring(0, delimiterPos);
        } else {
            mnoName = name;
        }
        for (Mno mno : sTable) {
            if (mno.getName().equalsIgnoreCase(mnoName)) {
                String str3 = LOG_TAG;
                Log.d(str3, "fromName: found mno : " + mno + ", " + mno.getCountryCode());
                return mno;
            }
        }
        Log.d(LOG_TAG, "fromName: not found mno");
        return DEFAULT;
    }

    public static String getMockOperatorCode() {
        return SystemProperties.get(MOCK_MNO_PROPERTY, "");
    }

    public static String getMockMnoname() {
        return SystemProperties.get(MOCK_MNONAME_PROPERTY, "");
    }

    public static Mno getMockMno() {
        String mockMnoname = getMockMnoname();
        if (TextUtils.isEmpty(mockMnoname)) {
            return null;
        }
        Mno mno = fromName(mockMnoname);
        if (mno != null) {
            String str = LOG_TAG;
            Log.d(str, "getMockMno: returning mock Mno " + mno);
        }
        return mno;
    }

    protected Mno() {
        this.mSalesCode = "";
        this.mRegion = Region.END_OF_REGION;
        this.mCountry = Country.END_OF_COUNTRY;
        this.mName = "DEFAULT";
    }

    private Mno(String name) {
        this.mSalesCode = "";
        this.mRegion = Region.END_OF_REGION;
        this.mCountry = Country.END_OF_COUNTRY;
        this.mName = name;
    }

    private Mno(String name, String salesCode, Region region, Country country) {
        this.mSalesCode = "";
        this.mRegion = Region.END_OF_REGION;
        this.mCountry = Country.END_OF_COUNTRY;
        this.mName = name;
        this.mSalesCode = salesCode;
        this.mRegion = region;
        this.mCountry = country;
        sTable.add(this);
    }

    public String getName() {
        return this.mName;
    }

    public String getMatchedSalesCode(String salesCode) {
        String[] salesCodes = getAllSalesCodes();
        for (String code : salesCodes) {
            if (code.equals(salesCode)) {
                return salesCode;
            }
        }
        return salesCodes[0];
    }

    public boolean equalsWithSalesCode(Mno mno, String salesCode) {
        return this == mno && getMatchedSalesCode(salesCode).equals(salesCode);
    }

    public String getMatchedNetworkCode(String networkCode) {
        String[] salesCodes = getAllSalesCodes();
        if (fromSalesCode(networkCode) == SPRINT) {
            return salesCodes[0];
        }
        return getMatchedSalesCode(networkCode);
    }

    public String[] getAllSalesCodes() {
        return this.mSalesCode.split(",");
    }

    public static void updateGenerictMno(String mnoname) {
        Country country = getCountryFromMnomap(mnoname);
        Mno mno = GENERIC;
        mno.mName = mnoname;
        mno.mCountry = country;
        mno.mRegion = country.getRegion();
        String str = LOG_TAG;
        Log.d(str, "updateGenerictMno: GENERIC.mName = " + GENERIC.mName + ", GENERIC.mCountry =" + GENERIC.mCountry + " GENERIC.mRegion = " + GENERIC.mRegion);
    }

    public static Country getCountryFromMnomap(String mnoname) {
        Country[] values;
        if (TextUtils.isEmpty(mnoname)) {
            return Country.END_OF_COUNTRY;
        }
        int beginIndex = mnoname.indexOf("_");
        if (beginIndex != -1) {
            int endIndex = mnoname.indexOf(MVNO_DELIMITER);
            if (endIndex == -1) {
                endIndex = mnoname.length();
            }
            String countryCode = mnoname.substring(beginIndex + 1, endIndex);
            Log.d(LOG_TAG, "getCountryFromMnomap: countryCode = " + countryCode);
            for (Country c : Country.values()) {
                if (TextUtils.equals(c.getCountryIso(), countryCode)) {
                    return c;
                }
            }
        }
        return Country.END_OF_COUNTRY;
    }

    public static Region getRegionOfDevice() {
        Country[] values;
        String deviceCountryCode = SystemProperties.get("ro.csc.countryiso_code", "KR");
        Log.d(LOG_TAG, "getRegionOfDevice: deviceCountryCode = " + deviceCountryCode);
        for (Country c : Country.values()) {
            if (TextUtils.equals(c.getCountryIso(), deviceCountryCode)) {
                return c.getRegion();
            }
        }
        return Region.END_OF_REGION;
    }

    public String getCountryCode() {
        return this.mCountry.getCountryIso();
    }

    public boolean isUSA() {
        return this.mCountry == Country.US;
    }

    public boolean isKor() {
        return this.mCountry == Country.KOREA && this != SAMSUNG;
    }

    public boolean isJpn() {
        return this.mCountry == Country.JAPAN;
    }

    public boolean isChn() {
        return this.mCountry == Country.CHINA || this == CTCMO;
    }

    public boolean isHk() {
        return this.mCountry == Country.HONGKONG;
    }

    public boolean isHkMo() {
        return this.mCountry == Country.HONGKONG || (this.mCountry == Country.MACAU && this != CTCMO);
    }

    public boolean isTw() {
        return this.mCountry == Country.TAIWAN;
    }

    public boolean isEur() {
        return this.mRegion == Region.EUROPE;
    }

    public boolean isAfrica() {
        return this.mRegion == Region.AFRICA;
    }

    public boolean isNordic() {
        return this.mCountry == Country.SWEDEN || this.mCountry == Country.NORWAY || this.mCountry == Country.FINLAND || this.mCountry == Country.DENMARK;
    }

    public boolean isSea() {
        return this.mRegion == Region.SOUTH_EAST_ASIA;
    }

    public boolean isSG() {
        return this.mCountry == Country.SINGAPORE;
    }

    public boolean isMea() {
        return this.mRegion == Region.MIDDLE_EAST || this.mRegion == Region.AFRICA;
    }

    public boolean isSwa() {
        return this.mRegion == Region.SOUTH_WEST_ASIA;
    }

    public boolean isRjil() {
        return this == RJIL;
    }

    public boolean isAus() {
        return this.mCountry == Country.AUSTRALIA;
    }

    public boolean isNZ() {
        return this.mCountry == Country.NEWZEALAND;
    }

    public boolean isOce() {
        return this.mRegion == Region.OCEANIA;
    }

    public boolean isTeliaCo() {
        return this == TELIA_DK || this == TELIA_FINLAND || this == TELIA_NORWAY || this == TELIA_SWE;
    }

    public boolean isCanada() {
        return this.mCountry == Country.CANADA;
    }

    public boolean isIndia() {
        return this.mCountry == Country.INDIA;
    }

    public boolean isLatin() {
        return !isATTMexico() && this.mRegion == Region.SOUTH_AMERICA;
    }

    public boolean isATTMexico() {
        return this == ATT_MEXICO;
    }

    public boolean isVodafone() {
        return this == VODAFONE_UK || this == VODAFONE || this == VODAFONE_SPAIN || this == VODAFONE_ITALY || this == VODAFONE_NETHERLAND || this == VODAFONE_HUNGARY || this == VODAFONE_IRELAND || this == VODACOM_SOUTHAFRICA || this == VODAFONE_GREECE || this == VODAFONE_ROMANIA || this == VODAFONE_PORTUGAL || this == VODAFONE_CROATIA || this == VODAFONE_TURKEY || this == VODAFONE_ALBANIA || this == VODAFONE_CZ || this == VODAFONE_CY;
    }

    public boolean isTmobile() {
        return this == TMOBILE || this == TMOBILE_CZ || this == TMOBILE_PL || this == TMOBILE_HUNGARY || this == TMOBILE_NED || this == TMOBILE_GREECE || this == TMOBILE_CROATIA || this == TMOBILE_SLOVAKIA || this == TMOBILE_AUSTRIA || this == TMOBILE_MK || this == TMOBILE_ME || this == TMOBILE_ROMANIA;
    }

    public boolean isOrange() {
        return this == ORANGE || this == ORANGE_BELGIUM || this == ORANGE_LUXEMBOURG || this == ORANGE_POLAND || this == ORANGE_ROMANIA || this == ORANGE_SLOVAKIA || this == ORANGE_MOROCCO || this == ORANGE_SENEGAL || this == ORANGE_SPAIN || this == ORANGE_MOLDOVA;
    }

    public boolean isSprint() {
        return this == SPRINT;
    }

    public boolean isAmerica() {
        return this.mRegion == Region.SOUTH_AMERICA || this.mRegion == Region.NORTH_AMERICA;
    }

    @NonNull
    public String toString() {
        return this.mName;
    }

    public boolean isEmeasewaoce() {
        return this.mRegion == Region.EUROPE || this.mRegion == Region.MIDDLE_EAST || this.mRegion == Region.AFRICA || this.mRegion == Region.SOUTH_EAST_ASIA || this.mRegion == Region.SOUTH_WEST_ASIA || this.mRegion == Region.OCEANIA;
    }

    public static String convertContryCode(String key) {
        if (TextUtils.isEmpty(key) || key.lastIndexOf("_") < 0) {
            return key;
        }
        int start = key.lastIndexOf("_");
        int end = key.length();
        if (key.lastIndexOf(":") > 0) {
            end = key.lastIndexOf(":");
            String countryCode = key.substring(start, end);
            return key.replaceFirst(countryCode + ":", countryCode.toUpperCase() + ":");
        } else {
            String countryCode = key.substring(start, end);
            return key.replaceFirst(countryCode + "$", countryCode.toUpperCase());
        }
    }

    public boolean isOneOf(Mno... mnos) {
        for (Mno mno : mnos) {
            if (this == mno) {
                return true;
            }
        }
        return false;
    }
}
