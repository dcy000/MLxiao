package com.gcml.module_face_recognition.network;

import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_face_recognition.bean.UserInfoBean;
import com.gcml.module_face_recognition.bean.XfGroupInfo;
import com.gcml.module_face_recognition.manifests.FaceRecognitionSPManifest;

import java.util.List;

import io.reactivex.Observable;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 11:53
 * created by:gzq
 * description:TODO
 */
public class FaceRepository {
    private static IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    private static FaceRecognitionService faceRecognitionService = mRepositoryHelper.retrofitService(FaceRecognitionService.class);

    /**
     * 记录创建的人脸识别组id到服务器
     *
     * @param groupId
     * @param xfid
     * @return
     */
    public static Observable<List<XfGroupInfo>> recordXfGroupInfo(String groupId, String xfid) {
        return faceRecognitionService.recordXfGroupInformation(FaceRecognitionSPManifest.getUserId(), groupId, xfid)
                .compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取七牛云token
     *
     * @return
     */
    public static Observable<String> getQiniuToken() {
        return faceRecognitionService.getQiniuToken()
                .compose(RxUtils.apiResultTransformer());
    }

    /**
     * 同步七牛云返回的头像地址到我们自己的服务器
     *
     * @param userPhoto
     * @param xfid
     * @return
     */
    public static Observable<Object> syncRegistHeadUrl(String userPhoto, String xfid) {
        return faceRecognitionService.syncRegistHeadUrl(userPhoto, FaceRecognitionSPManifest.getUserId(), xfid)
                .compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取所有用户的信息
     *
     * @param usersIds
     * @return
     */
    public static Observable<List<UserInfoBean>> getAllUsersInformation(String usersIds) {
        return faceRecognitionService.getAllUsersInformation(usersIds)
                .compose(RxUtils.apiResultTransformer());
    }

    /**
     * 支付成功之后，同步订单信息
     *
     * @param userid
     * @param eqid
     * @param orderid
     * @return
     */
    public static Observable<String> syncPayOrderId(String userid, String eqid, String orderid) {
        return faceRecognitionService.syncPayOrderId(userid, eqid, orderid)
                .compose(RxUtils.apiResultTransformer());
    }

    /**
     * 取消支付失败的订单
     * @param pay_state
     * @param delivery_state
     * @param display_state
     * @param orderid
     * @return
     */
    public static Observable<String> cancelPayOrderId(String pay_state, String delivery_state, String display_state, String orderid) {
        return faceRecognitionService.cancelPayOrderId(pay_state, delivery_state, display_state, orderid)
                .compose(RxUtils.apiResultTransformer());
    }
}
