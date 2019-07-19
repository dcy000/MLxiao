package com.gcml.mod_doc_advisory.net;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserEntity;
import com.gcml.common.recommend.bean.get.RobotAmount;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.RxUtils;
import com.gcml.mod_doc_advisory.bean.ContractInfo;
import com.gcml.mod_doc_advisory.bean.DiseaseResult;
import com.gcml.mod_doc_advisory.bean.Docter;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.mod_doc_advisory.bean.DoctorInfoBean;
import com.gcml.mod_doc_advisory.bean.YzInfoBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2018/9/28.
 */

public class QianYueRepository {

    QianYueService qianYueService = RetrofitHelper.service(QianYueService.class);

    public Observable<DoctorInfoBean> getDoctorInfo(String doctorId) {
        return qianYueService.getDoctInfo(doctorId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> cancelContract(String bid) {
        return qianYueService.cancelContract(bid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Doctor> DoctorInfo(String userId) {
        return qianYueService.DoctorInfo(userId).compose(RxUtils.apiResultTransformer());
    }


    public Observable<String> getCallId(String doctorId) {
        return qianYueService.getCallId(doctorId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<YzInfoBean>> getYzList(String userId) {
        return qianYueService.getYzList(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<DiseaseResult> getJibing(String bname) {
        return qianYueService.getJibing(bname).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<Docter>> doctor_list(int start, int limit) {
        if (ChannelUtils.isJGYS()) {
            // 健管演示
            return qianYueService.doctorListOld(start, limit)
                    .compose(RxUtils.apiResultTransformer());
        }
        return qianYueService.doctor_list(start, limit).compose(RxUtils.apiResultTransformer());
    }

    public Observable<ArrayList<Docter>> onlinedoctor_list(int status, String doctorName, int page, int pageSize) {
        if (ChannelUtils.isJGYS()) {
            // 健管演示
            return qianYueService.onlineDoctorListOld(status, doctorName, page, pageSize)
                    .compose(RxUtils.apiResultTransformer());
        }

        return qianYueService.onlinedoctor_list(status, doctorName, page, pageSize)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<UserEntity> PersonInfo(String userId) {
        return qianYueService.PersonInfo(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<RobotAmount> Person_Amount(String eqid) {
        return qianYueService.Person_Amount(eqid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<ContractInfo> getContractInfo(String userId, String doctorId) {
        return qianYueService.getContractInfo(userId, doctorId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> bindDoctor(String userId, String doctorId) {
        return qianYueService.bindDoctor(userId, doctorId).compose(RxUtils.apiResultTransformer());
    }
}
