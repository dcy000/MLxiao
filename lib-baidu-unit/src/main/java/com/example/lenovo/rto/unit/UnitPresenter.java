//package com.example.lenovo.rto.unit;
//
//
//import com.example.lenovo.rto.http.HttpListener;
//
///**
// * Created by huyin on 2017/9/12.
// */
//
//public class UnitPresenter implements HttpListener<Unit> {
//
//     UnitView unitView;
//     UnitModel unitModel;
//
//     public UnitPresenter(UnitView unitView) {
//          this.unitView = unitView;
//          unitModel = new UnitModel();
//     }
//
//     public void getUnit(String accessToken, int sceneId, String query, String sessionId) {
//          unitModel.getUnit(accessToken, sceneId, query, sessionId, this);
//     }
//
//     @Override
//     public void onSuccess(Unit data) {
//          if (unitView != null) {
//               if (unitModel != null) {
//                    unitView.setUnit(data);
//               }
//          }
//     }
//
//     @Override
//     public void onError() {
//          if (unitView != null) {
//               unitView.showOther();
//          }
//     }
//}
