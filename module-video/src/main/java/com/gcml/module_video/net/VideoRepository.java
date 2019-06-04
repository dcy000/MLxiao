package com.gcml.module_video.net;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_video.video.VideoEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/10/8 10:05
 * created by:gzq
 * description:TODO
 */
public class VideoRepository {


    private static VideoService videoService = RetrofitHelper.service(VideoService.class);

   public Observable<List<VideoEntity>> getVideoList(int tag1,String tag2,String flag,int page,int pagesize){
       return videoService.getVideoList(tag1,tag2,flag,page,pagesize).compose(RxUtils.apiResultTransformer());
   }

}