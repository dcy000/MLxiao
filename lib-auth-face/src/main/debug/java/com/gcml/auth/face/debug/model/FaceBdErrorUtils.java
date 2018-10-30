package com.gcml.auth.face.debug.model;

import com.gcml.auth.face.debug.model.exception.FaceBdError;

public class FaceBdErrorUtils {

    public static final int ERROR_UNKNOWN = -1;

    public static final int ERROR_NO_FACE = 222202;
    public static final int ERROR_FACE_LIVELESS = -1000;
    public static final int ERROR_USER_SCORE_LOW = -1001;
    public static final int ERROR_USER_NOT_EXIST = 223103;
    public static final int ERROR_USER_NOT_FOUND = 222207;

    public static String getMsg(int code) {
        String msg = "把人脸放在框内";
        switch (code) {
            case ERROR_UNKNOWN:
                msg = "把人脸放在框内";
                break;
            case ERROR_NO_FACE:
                msg = "我没看到你啊";
                break;
            case ERROR_FACE_LIVELESS:
                msg = "想看更帅的你";
                break;
            case ERROR_USER_NOT_FOUND:
                msg = "想看更帅的你";
                break;
            default:
                break;
        }
        return msg;
    }

    public static FaceBdError wrap(Throwable throwable) {
        if (throwable instanceof FaceBdError) {
            return (FaceBdError) throwable;
        }
        return new FaceBdError(throwable);
    }
}
