package com.sec.internal.constants.ims;

public class DiagnosisConstants {
    public enum REGI_REQC {
        UNKNOWN(0),
        INITIAL(1),
        REFRESH(2),
        HAND_OVER(3),
        RE_REGI(4),
        DE_REGI(9);

        private final int mCode;

        REGI_REQC(int code) {
            this.mCode = code;
        }

        public int getCode() {
            return this.mCode;
        }
    }
}
