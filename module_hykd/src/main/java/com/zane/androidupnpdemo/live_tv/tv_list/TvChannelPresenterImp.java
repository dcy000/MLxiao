package com.zane.androidupnpdemo.live_tv.tv_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenenyu.router.RouteCallback;
import com.chenenyu.router.RouteResult;
import com.chenenyu.router.Router;
import com.gzq.administrator.lib_common.utils.ThreadPoolTool;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.iflytek.wake.MLVoiceWake;
import com.iflytek.wake.MLWakeuperListener;
import com.zane.androidupnpdemo.live_tv.LiveBean;
import com.zane.androidupnpdemo.live_tv.tv_play.TvPlayActivity;
import com.zane.androidupnpdemo.utils.PinyinHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvChannelPresenterImp implements ITvChannelInteractor.OnParseExcelListener, ITvChannelPresenter {
    private TvChannelActivity tvChannelActivity;
    private ITvChannelInteractor tvChannelInteractor;
    private List<LiveBean> mData;
    private static int currentPlayPotion = 0;
    private static ThreadPoolTool threadPool;
    private static boolean isOnPause=false;
    private ImpMLWakeuperListener impMLWakeuperListener;
    private ImpSynthesizerListener impSynthesizerListener;
    private static ImpMLRecognizerListener impMLRecognizerListener;
    public TvChannelPresenterImp(ITvList tvChannelActivity, ITvChannelInteractor tvChannelInteractor) {
        this.tvChannelActivity = (TvChannelActivity) tvChannelActivity;
        this.tvChannelInteractor = tvChannelInteractor;
        MLVoiceWake.initGlobalContext((TvChannelActivity)tvChannelActivity);
        impMLWakeuperListener=new ImpMLWakeuperListener(this.tvChannelActivity);
        impSynthesizerListener=new ImpSynthesizerListener(this.tvChannelActivity);
        impMLRecognizerListener=new ImpMLRecognizerListener(this.tvChannelActivity);
    }

    private void speakAndListen() {
        MLVoiceWake.startWakeUp(impMLWakeuperListener);
        MLVoiceSynthetize.startSynthesize(tvChannelActivity, "主人，您想看哪个电视节目？", impSynthesizerListener, false);
    }
    private static class ImpMLWakeuperListener extends MLWakeuperListener {
        private WeakReference<TvChannelActivity> weakActivity;
        public ImpMLWakeuperListener(TvChannelActivity activity) {
            weakActivity=new WeakReference<TvChannelActivity>(activity);
        }

        @Override
        public void onMLError(int errorCode) {

        }

        @Override
        public void onMLResult() {
            Router.build("app/speech").go(weakActivity.get(), new RouteCallback() {
                    @Override
                    public void callback(RouteResult routeResult, Uri uri, String s) {
                        Log.e("跳转说话页面的结果", "callback: "+routeResult.toString()+"----"+uri+"++"+s );
                    }
                });
        }
    }
    private static class ImpSynthesizerListener implements SynthesizerListener{
        private WeakReference<TvChannelActivity> weakActivity;
        public ImpSynthesizerListener(TvChannelActivity activity) {
            weakActivity=new WeakReference<TvChannelActivity>(activity);
        }

        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            threadPool = new ThreadPoolTool(ThreadPoolTool.Type.SingleThread, 1);
            Runnable command = new Runnable() {

                @Override
                public void run() {
                    if (!isOnPause) {
                        MLVoiceRecognize.startRecognize(weakActivity.get(),impMLRecognizerListener);
                    }
                }
            };
            if (threadPool!=null) {
                threadPool.scheduleWithFixedDelay(command, 0,2000, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }
    private class ImpMLRecognizerListener extends MLRecognizerListener{
        private WeakReference<TvChannelActivity> weakActivity;
        public ImpMLRecognizerListener(TvChannelActivity activity) {
            weakActivity=new WeakReference<TvChannelActivity>(activity);
        }

        @Override
        public void onMLVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onMLBeginOfSpeech() {

        }

        @Override
        public void onMLEndOfSpeech() {

        }

        @Override
        public void onMLResult(String result) {
            ToastTool.showShort(result);
            String inSpell = PinyinHelper.getPinYin(result);
            if (inSpell.matches("(.*)fanhui(.*)")) {
                weakActivity.get().closeActivity();
                return;
            }
            if (inSpell.matches("(.*)yitai|cctvyi(.*)")) {//CCTV1
                currentPlayPotion = 0;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }

            if (inSpell.matches("(.*)ertai|cctver|cctvcai(jin|jing)|cai(jin|jing)(.*)")) {//CCTV2
                currentPlayPotion = 1;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(san|shan)tai|cctv(san|shan)(.*)")) {//CCTV3
                currentPlayPotion = 2;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }

            if (inSpell.matches("(.*)sitai|cctvsi(.*)")) {//CCTV4
                currentPlayPotion = 3;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }

            if (inSpell.matches("(.*)qitai|cctvqi(.*)")) {//CCTV7
                currentPlayPotion = 4;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)jiu|cctvjiu(.*)")) {//CCTV9
                currentPlayPotion = 5;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)shitai|cctvshi(.*)")) {//CCTV10
                currentPlayPotion = 6;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(si|shi)yitai|cctv(si|shi)yi(.*)")) {//CCTV11
                currentPlayPotion = 7;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(si|shi)ertai|cctv(si|shi)er(.*)")) {//CCTV12
                currentPlayPotion = 8;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)wenhua(jin|jing)(pin|ping)(.*)")) {//CCTV-文化精品
                currentPlayPotion = 10;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(shao|sao)er(.*)")) {//CCTV-少儿
                currentPlayPotion = 11;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(si|shi)wutai|cctv(si|shi)wu(.*)")) {//CCTV15
                currentPlayPotion = 9;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(zu|zhu)qiu(.*)")) {//CCTV-风云足球
                currentPlayPotion = 12;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            //====================================
            if (inSpell.matches("(.*)di(li|ni)(.*)")) {//CCTV-世界地理
                currentPlayPotion = 13;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)jun(si|shi)(.*)")) {//CCTV-国防军事
                currentPlayPotion = 14;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)wutai|tiyue|cctvwu(.*)")) {//CCTV-体育
                currentPlayPotion = 15;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)fengyunju(chang|cang)(.*)")) {//CCTV-风云剧场
                currentPlayPotion = 16;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(yin|ying)yue(.*)")) {//CCTV-风云音乐
                currentPlayPotion = 17;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)huaijiu(.*)")) {//CCTV-怀旧剧场
                currentPlayPotion = 18;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(lao|nao)gu(si|shi)(.*)")) {//CCTV-老故事
                currentPlayPotion = 19;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)gaoerfu|wangqiu(.*)")) {//CCTV-高尔夫·网球
                currentPlayPotion = 20;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(fu|hu)(nan|lan)(.*)")) {//湖南
                currentPlayPotion = 21;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)jiang(su|shu)(.*)")) {//江苏
                currentPlayPotion = 22;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(zhe|ze)jiang(.*)")) {//浙江
                currentPlayPotion = 23;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(shang|sang)hai|dongfang(.*)")) {//上海
                currentPlayPotion = 24;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)yun(nan|lan)(.*)")) {//云南
                currentPlayPotion = 25;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)dong(nan|lan)|(fu|hu)jian(.*)")) {//东南
                currentPlayPotion = 26;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(liao|niao)(ning|ling|nin|lin)(.*)")) {//辽宁
                currentPlayPotion = 27;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(chong|cong)(qin|qing)(.*)")) {//重庆
                currentPlayPotion = 28;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)hei(long|nong)jiang(.*)")) {//黑龙江
                currentPlayPotion = 29;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)gui(zhou|zou)(.*)")) {//贵州
                currentPlayPotion = 30;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)jiangxi(.*)")) {//江西
                currentPlayPotion = 31;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)guangxi(.*)")) {//广西
                currentPlayPotion = 32;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)guangdong(.*)")) {//广东
                currentPlayPotion = 33;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)ji(lin|ling)(.*)")) {//吉林
                currentPlayPotion = 34;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(shan|san)dong(.*)")) {//山东
                currentPlayPotion = 35;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(si|shi)(chuan|cuan)(.*)")) {//四川
                currentPlayPotion = 36;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)xi(zang|zhang)(.*)")) {//西藏
                currentPlayPotion = 37;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(nei|lei)(men|meng)gu(.*)")) {//内蒙古
                currentPlayPotion = 38;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(xin|xing)jiang(.*)")) {//新疆
                currentPlayPotion = 39;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(shen|sen|seng|sheng)(zhen|zheng|zen|zeng)(.*)")) {//深圳
                currentPlayPotion = 40;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)bei(jin|jing)(.*)")) {//北京纪实
                currentPlayPotion = 41;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)yidong(.*)")) {//重庆移动
                currentPlayPotion = 42;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(long|nong)cun(.*)")) {//重庆公共农村
                currentPlayPotion = 43;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)dao(si|shi)(.*)")) {//重庆导视
                currentPlayPotion = 44;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)dong(zuo|zhuo)(.*)")) {//CHC-动作电影
                currentPlayPotion = 45;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)huanxiao(.*)")) {//欢笑剧场
                currentPlayPotion = 46;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }

            if (inSpell.matches("(.*)(shi|si)(shang|sang)(.*)")) {//生活时尚
                currentPlayPotion = 47;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)jiating(yin|ying)yuan(.*)")) {//CHC-家庭影院
                currentPlayPotion = 48;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)gouwu(.*)")) {//家有购物
                currentPlayPotion = 49;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)rexiao|(jin|jing)(pin|ping)(.*)")) {//热销精选
                currentPlayPotion = 50;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(shou|sou)cang(.*)")) {//天下收藏
                currentPlayPotion = 51;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)jia(zhen|zheng|zen|zeng)(.*)")) {//家政
                currentPlayPotion = 52;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)gaoerfu(.*)")) {//高尔夫
                currentPlayPotion = 53;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)wanluo|qipai(.*)")) {//网络棋牌
                currentPlayPotion = 54;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)katong|(jin|jing)(yin|ying)(.*)")) {//金鹰卡通
                currentPlayPotion = 55;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)dongman(.*)")) {//新动漫
                currentPlayPotion = 56;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(li|ni)cai(.*)")) {//家庭理财
                currentPlayPotion = 57;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
            if (inSpell.matches("(.*)(cai|chai)fu(.*)")) {//财富天下
                currentPlayPotion = 58;
                tvListItemClick(null, null, currentPlayPotion);
                return;
            }
        }

        @Override
        public void onMLError(SpeechError error) {

        }
    }
    @Override
    public void onStart() {
        if (tvChannelActivity != null)
            tvChannelActivity.showDialog();
    }

    @Override
    public void onFinished(List<LiveBean> channels) {
        this.mData = channels;
        if (tvChannelActivity != null) {
            tvChannelActivity.hideDialog();
            tvChannelActivity.fillData(channels);
        }
    }

    @Override
    public void onError(String error) {
        if (tvChannelActivity != null) {
            tvChannelActivity.hideDialog();
            tvChannelActivity.onError(error);
        }
    }

    @Override
    public void getChannels() {
        tvChannelInteractor.getChannels(this, tvChannelActivity);
    }

    @Override
    public void tvListItemClick(BaseQuickAdapter adapter, View view, int position) {
        TvPlayActivity.startTvPlayActivity((Context) tvChannelActivity,
                new Intent((Context) tvChannelActivity, TvPlayActivity.class)
                        .putParcelableArrayListExtra("tvs", (ArrayList<? extends Parcelable>) mData)
                        .putExtra("position", position));
    }

    @Override
    public void onResume() {
        isOnPause=false;
        speakAndListen();
    }

    @Override
    public void onPause() {
        isOnPause=true;
        MLVoiceWake.stopWakeUp();
    }

    @Override
    public void onDestroy() {
        tvChannelActivity = null;
        if (threadPool!=null) {
            impMLWakeuperListener=null;
            impSynthesizerListener=null;
            impMLRecognizerListener=null;
            MLVoiceRecognize.destroy();
            MLVoiceSynthetize.destory();
            threadPool.shutDownNow();
            threadPool=null;
        }
    }

}
