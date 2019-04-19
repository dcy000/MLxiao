package com.gcml.auth.face2.model;

import android.text.TextUtils;


import com.gcml.auth.face2.model.entity.FaceBdAngle;
import com.gcml.auth.face2.model.entity.FaceBdFaceInfo;
import com.gcml.auth.face2.model.entity.FaceBdSearch;
import com.gcml.auth.face2.model.entity.FaceBdUser;
import com.gcml.auth.face2.model.entity.FaceBdVerify;
import com.gcml.auth.face2.model.exception.FaceBdError;
import com.gcml.common.repository.http.ApiException;


import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

public class FaceBdErrorUtils {
    /**
     * 通过率   faceLiveness
     * <p>
     * 99.9%   0.022403
     * 99.5%   0.393241（推荐）
     * 99%	    0.649192
     * 98%	    0.933801
     * 97%	    0.973637
     * 96%	    0.988479
     * 95%	    0.994058
     */
    public static final double THRESHOLDS_FACE_LIVENESS = 0.40;
    public static final double THRESHOLDS_FACE_ANGLE = 20;
    public static final double THRESHOLDS_FACE_PROBABILITY = 0.6;

    public static final double SCORE_SEARCH_NORMAL = 80;
    public static final double SCORE_SEARCH_HIGH = 90;

    public static final int ERROR_UNKNOWN = -1000;
    public static final int ERROR_FACE_LIVELESS = -1001;
    public static final int ERROR_USER_SCORE_LOW = -1002;
    public static final int ERROR_INVALID_TOKEN = 110;
    public static final int ERROR_NO_FACE = 222202;
    public static final int ERROR_USER_NOT_EXIST = 223103;
    public static final int ERROR_USER_NOT_FOUND = 222207;

    public static String getMsg(int code, String otherMsg) {
        String msg = TextUtils.isEmpty(otherMsg) ? "把人脸放在框内": otherMsg;
        switch (code) {
            case ERROR_UNKNOWN:
                msg = "把人脸放在框内";
                break;
            case ERROR_NO_FACE:
                msg = "把人脸放在框内";
                break;
            case ERROR_FACE_LIVELESS:
                msg = "调整一下姿势";
                break;
            case ERROR_USER_SCORE_LOW:
                msg = "把人脸放在框内";
                break;
            case ERROR_USER_NOT_FOUND:
                msg = "把人脸放在框内";
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

        if (throwable instanceof ApiException) {
            ApiException e = ((ApiException) throwable);
            return new FaceBdError(e.code(), e.getMessage());
        }

        return new FaceBdError(throwable);
    }

    static volatile String accessToken = "";

    public static void clearTokenIfNeeded(int code) {
        boolean b = code == ERROR_INVALID_TOKEN;
        if (b) {
            accessToken = "";
        }
    }

    /**
     * 活体检测控制
     *
     * @param verify 接口返回的活体检测信息
     * @return "${imageType},${image}"
     */
    public static String liveFace(FaceBdVerify verify) {
        String image = "";
        double faceLiveness = verify.getFaceLiveness();
        double faceProbability = 0;
        List<FaceBdFaceInfo> faceList = verify.getFaceList();
        for (FaceBdFaceInfo info : faceList) {
            if (Math.abs(faceLiveness - info.getLiveness().getLivemapscore()) < 0.001
                    && checkFaceAngle(info.getAngle())) {
                image = "FACE_TOKEN," + info.getFaceToken();
                faceProbability = info.getFaceProbability();
            }
        }

        if (faceProbability >= THRESHOLDS_FACE_PROBABILITY
                && faceLiveness >= FaceBdErrorUtils.THRESHOLDS_FACE_LIVENESS
                && !TextUtils.isEmpty(image)) {
            return image;
        }
        return "";
    }

    private static boolean checkFaceAngle(FaceBdAngle angle) {
        if (angle == null) {
            return false;
        }
        return (angle.getPitch() < THRESHOLDS_FACE_ANGLE
                || angle.getPitch() > -THRESHOLDS_FACE_ANGLE
                || angle.getPitch() < THRESHOLDS_FACE_ANGLE - 180)
                && (angle.getRoll() < THRESHOLDS_FACE_ANGLE
                || angle.getRoll() > -THRESHOLDS_FACE_ANGLE
                || angle.getPitch() < THRESHOLDS_FACE_ANGLE - 180)
                && (angle.getYaw() < THRESHOLDS_FACE_ANGLE
                || angle.getYaw() > -THRESHOLDS_FACE_ANGLE
                || angle.getPitch() < THRESHOLDS_FACE_ANGLE - 180);

    }

    public static FaceBdUser checkSearch(FaceBdSearch search) {
        FaceBdUser faceBdUser = search.getUserList().get(0);
        if (faceBdUser.getScore() < FaceBdErrorUtils.SCORE_SEARCH_NORMAL) {
            throw new FaceBdError(FaceBdErrorUtils.ERROR_USER_SCORE_LOW, "");

        }
        return faceBdUser;
    }
}
