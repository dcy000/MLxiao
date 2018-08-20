package com.gcml.common;

import android.app.Activity;
import com.gcml.common.repository.RepositoryActivity;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/15 15:26
 * created by:gzq
 * description:操作module-face_recognition模块的actions
 * 源模块维护CCActions，目标模块维护CCResultActions
 * 一个模块维护一个类，不可不同模块交叉使用
 */
public class CCFaceRecognitionActions {
    /**
     * 操作RegistrHead2XunfeiActivity的actions
     */
    public static class CCRegistrHead2XunfeiActivityActions {
        public static void jump2RegisterHead2XunfeiActivity(Activity activity) {
            if (activity instanceof RepositoryActivity) {

            }
        }
    }

    /**
     * 操作FaceRecognitionActivity的actions
     */
    public static class CCFaceRecognitionActivityActions {
        public static void jump2FaceRecognitionActivity(Activity activity) {
            if (activity instanceof RepositoryActivity) {

            }
        }
    }
}
