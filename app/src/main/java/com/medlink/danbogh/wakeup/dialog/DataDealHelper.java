package com.medlink.danbogh.wakeup.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.DiseaseDetailsActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.children.entertainment.ChildEduJokesActivity;
import com.example.han.referralproject.children.entertainment.ChildEduSheetDetailsActivity;
import com.example.han.referralproject.children.study.ChildEduPoemListActivity;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.HttpCallback;
import com.example.han.referralproject.new_music.HttpClient;
import com.example.han.referralproject.new_music.Music;
import com.example.han.referralproject.new_music.MusicPlayActivity;
import com.example.han.referralproject.new_music.PlaySearchedMusic;
import com.example.han.referralproject.new_music.SearchMusic;
import com.example.han.referralproject.radio.RadioActivity;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.settting.SharedPreferencesUtils;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.settting.bean.KeyWordDefinevBean;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.speechsynthesis.QaApi;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.tcm.SymptomCheckActivity;
import com.example.han.referralproject.tcm.activity.OlderHealthManagementSerciveActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.lenovo.rto.unit.Unit;
import com.example.lenovo.rto.unit.UnitModel;
import com.gcml.common.data.UserSpHelper;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_health_record.HealthRecordActivity;
import com.gcml.old.auth.personal.PersonDetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.call2.NimCallActivity;
import com.ml.edu.OldRouter;
import com.ml.edu.old.TheOldHomeActivity;
import com.ml.edu.old.music.TheOldMusicActivity;
import com.witspring.unitbody.ChooseMemberActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

import static android.content.Context.AUDIO_SERVICE;
import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
import static com.example.lenovo.rto.Constans.SCENE_Id;

/**
 * Created by lenovo on 2018/8/30.
 */

public class DataDealHelper {
    public static final String REGEX_SET_ALARM = ".*((ding|she|shezhi|)naozhong|tixing|chiyao|fuyao|chiyaotixing|dingshi).*";
    public static final String REGEX_SET_ALARM_WHEN = ".*tixing.*(shangwu|xiawu).*(\\d{1,2}):(\\d{1,2}).*yao.*";
    public static final String REGEX_SEE_DOCTOR = ".*(bushufu|touteng|fa(sao|shao)|duziteng|nanshou).*";

    private Context context;
    private String result;
    static Boolean whine = false;
    private Gson gson;

    private void speak(String text, boolean whine) {
        MLVoiceSynthetize.startSynthesize(context, text, new SynthesizerListener() {
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
                if (listener != null) {
                    listener.onEnd();
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        }, whine);
    }

    private void speak(String text) {
        speak(text, whine);
    }

    private void startActivity(Class<?> cls) {
        startActivity(cls, null, null);
    }

    private void startActivity(Class<?> cls, String key, String value) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(key, value);
        context.startActivity(intent);

        if (listener != null) {
            listener.onEnd();
        }

    }

    private void startActivity(Class<?> cls, String key, int value) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(key, value);
        context.startActivity(intent);


        if (listener != null) {
            listener.onEnd();
        }
    }

    private void startActivity(Class<?> cls, String key, Object value) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(key, (Serializable) value);
        context.startActivity(intent);


        if (listener != null) {
            listener.onEnd();
        }
    }

    interface OnEndListener {
        void onEnd();
    }

    public void setListener(OnEndListener listener) {
        this.listener = listener;
    }

    OnEndListener listener;


    public void onDataAction(Context context, String result) {
        this.result = result;
        this.context = context;
        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(".*(biansheng|biansheng|bianyin).*")) {
            whine = true;
        }
        if (inSpell.matches(".*(huifuzhengchang|xiaoyiyuansheng).*")) {
            whine = false;
        }
        Pattern patternWhenAlarm = Pattern.compile(REGEX_SET_ALARM_WHEN);
        Matcher matcherWhenAlarm = patternWhenAlarm.matcher(inSpell);
        if (matcherWhenAlarm.find()) {
            String am = matcherWhenAlarm.group(1);
            String hourOfDay = matcherWhenAlarm.group(2);
            String minute = matcherWhenAlarm.group(3);
            AlarmHelper.setupAlarm(context,
                    am.equals("shangwu") ? Integer.valueOf(hourOfDay) : Integer.valueOf(hourOfDay) + 12,
                    Integer.valueOf(minute));
            String tip = String.format(Locale.CHINA,
                    "您好，小易将在%s:%s提醒您吃药", hourOfDay, minute);
            speak(tip);
            return;
        }

        if (inSpell.matches(".*gengxin.*")) {
            NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
                @Override
                public void onSuccess(VersionInfoBean response) {
                    try {
                        if (response != null && response.vid > context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode) {
                            new UpdateAppManager(context).showNoticeDialog(response.url);
                        } else {
                            speak("当前已经是最新版本了");
                            Toast.makeText(context, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {
                    speak("当前已经是最新版本了");
                    Toast.makeText(context, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        if (inSpell.matches(".*(hujiaojiaren|jiaren.*dianhua*)")) {
            NimCallActivity.launchNoCheck(context, UserSpHelper.getEqId());

            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*yinyue.*")) {
            OldRouter.routeToOldMusicActivity(context);

            if (listener != null) {
                listener.onEnd();
            }
            return;
        }


        if (inSpell.matches(".*(erge|ertonggequ).*")) {
            startActivity(ChildEduSheetDetailsActivity.class, "sheetCategory", "儿童歌曲");
            return;
        }
        /*******************************************************/
        if (inSpell.matches(".*((meiri|zuo|zhuo|chakan|cakan|jintiande)renwu).*") || inSpell.matches(".*(jintianzhuoshenme|jintianzuoshenme).*")) {
            CC.obtainBuilder("app.component.task").addParam("startType", "MLSpeech").build().callAsync();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(zuogejiancha|jianchashenti|zuotijian).*")) {

            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });

            if (listener != null) {
                listener.onEnd();
            }

//                CC.obtainBuilder("health_measure")
//                        .setActionName("SingleMeasure").build()
//                        .call();
            return;
        }


        if (inSpell.matches(".*(geren|xiugai)xinxi.*")
                || inSpell.matches(".*huantouxiang.*")) {
            CC.obtainBuilder("com.gcml.auth.profileInfo").build().callAsync();
            return;
        }

        if (inSpell.matches(".*(jiankangguanli|gaoxueyaguanli|gaoxueyafangan|" +
                "gaoxueyazhiliao|gaoxueyacaipu|jiankangfangan|jiankangbaogao).*")) {
            startActivity(SlowDiseaseManagementActivity.class);
            return;
        }

        if (inSpell.matches(".*(danganxiazai|lishishuju|lishijilu|jiancejieguo|celiangshuju|jiankangshuju|jiankangdangan|jianchajieguo).*")) {
            startActivity(HealthRecordActivity.class);
            return;
        }

        if (inSpell.matches(".*(fengxian|fengxianpinggu|fengxianpanduan" +
                "|huanbingfenxiang|debingfengxian|jiankangyuce|jiankangyuche|pinggu).*")) {
            CC.obtainBuilder("health_measure")
                    .setActionName("To_HealthInquiryActivity")
                    .build()
                    .call();

            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(qiehuan|qiehuanzhanghao|chongxindenglu|zhongxindenglu|tianjiazhanghao).*")) {
            startActivity(PersonDetailActivity.class);
            return;
        }


        if (inSpell.matches(".*(shezhi|jiqirenshezhi|wifilianjie|diaojieyinliang|tiaojieyinliang|yiliangdaxiao).*")) {
            startActivity(SettingActivity.class);
            return;
        }

        if (inSpell.matches(".*(yulezhongxin).*")) {
            CC.obtainBuilder("app.component.recreation").build().callAsync();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(laorenyule).*")) {
            startActivity(TheOldHomeActivity.class);
            return;
        }


        if (inSpell.matches(".*(youjiao|youjiaowenyu|ertongyoujiao|jiaoxiaohai|ertongyule).*")) {
            startActivity(ChildEduHomeActivity.class);
            return;
        }

        if (inSpell.matches(".*(xiaogongju).*")) {
            CC.obtainBuilder("app.component.recreation.tools").build().call();

            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(yaolanqu).*")) {
            startActivity(ChildEduSheetDetailsActivity.class, "sheetCategory", "摇篮曲");
            return;
        }

        if (inSpell.matches(".*(taijiaoyinyue|taijiaoyinle|taijiao|tingyinle).*")) {
            startActivity(ChildEduSheetDetailsActivity.class, "sheetCategory", "胎教音乐");
            return;
        }

        if (inSpell.matches(".*(zhougongjiemeng|jiemeng|jiegemeng).*")) {
            CC.obtainBuilder("app.component.recreation.tool").setActionName("oneiromancy").build().call();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(lishijintian|lishishangdejintian|lishishangjintiandeshijian).*")) {
            CC.obtainBuilder("app.component.recreation.tool").setActionName("historyToday").build().call();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(riqichaxun|jidianle|chaxunriqi|jintianxingqiji|jidianle|jintianshenmerizi).*")) {
            CC.obtainBuilder("app.component.recreation.tool").setActionName("dateInquiry").build().call();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(caipu|shaocai|chishenme|chishengme|zuocai|tuijiancai).*")) {
            CC.obtainBuilder("app.component.recreation.tool").setActionName("cookBook").build().call();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(baike).*")) {
            CC.obtainBuilder("app.component.recreation.tool").setActionName("baike").build().call();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }


        if (inSpell.matches(".*(jisuanqi|zuosuanshu).*")) {
            CC.obtainBuilder("app.component.recreation.tool").setActionName("calculate").build().call();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(zaima|jiqirenduihua|liaotian|shuohua).*")) {
            startActivity(SpeechSynthesisActivity.class);
            return;
        }

        if (inSpell.matches(".*(zhengzhuangzicha).*")) {
            startActivity(SymptomCheckActivity.class);
            return;
        }

        if (inSpell.matches(".*(zhongyitizhi).*")) {
            startActivity(OlderHealthManagementSerciveActivity.class);
            return;
        }

        if (inSpell.matches(".*(tingyinyue|tingge|fangge|yinyueguan|yinleguan).*")) {
            startActivity(TheOldMusicActivity.class);
            return;
        }

        if (inSpell.matches(".*(gushi|tangshisongci|songci|tangshi).*") || result.matches(".*古诗.*")) {
            startActivity(ChildEduPoemListActivity.class);
            return;
        }

        if (inSpell.matches(".*(jianggexiaohua|xiaohua|youqudehua).*")) {
            startActivity(ChildEduJokesActivity.class);
            return;
        }


        if (result.matches(".*听故事|故事.*")) {
            startActivity(ChildEduPoemListActivity.class);
            return;
        }

        /*******************************************************/

//            if (inSpell.matches(".*jian(ce|che|ca|cha).*")
//                    ||inSpell.matches(".*(ce|che)(shi|si).*")) {
//                Intent intent = new Intent(SpeechSynthesisActivity.this, FaceRecognitionActivity.class);
//                intent.putExtra("from", "Test");
//                startActivityForResult(intent);
//                return;
//            }

        if (inSpell.matches(".*xiaoxi.*")) {
            startActivity(MessageActivity.class);
            return;
        }

        if (inSpell.matches(".*(guangbo|diantai|shouyinji|zhisheng|diantai).*")) {
            startActivity(RadioActivity.class);
            return;
        }

        if (inSpell.matches(".*gerenzhongxin.*")
                || inSpell.matches(".*gerenshezhi.*")) {
            gotoPersonCenter();
            return;
        }
        if (inSpell.matches(".*zhujiemian|zujiemian|jujiemian|zhuye.*")
                || inSpell.matches(".*zhujiemian|shuijiao|xiuxi|guanbi.*")) {
            gotoHomePage();
            return;
        }
        if (inSpell.matches("yishengjianyi|chakanxiaoxi")) {
            startActivity(MessageActivity.class);
            return;
        }

        if (inSpell.matches(REGEX_SET_ALARM)) {
            startActivity(AlarmList2Activity.class);
            return;
        }
        if (inSpell.matches(REGEX_SEE_DOCTOR)) {
            DiseaseUser diseaseUser = new DiseaseUser(
                    LocalShared.getInstance(context).getUserName(),
                    LocalShared.getInstance(context).getSex().equals("男") ? 1 : 2,
                    Integer.parseInt(LocalShared.getInstance(context).getUserAge()) * 12,
                    LocalShared.getInstance(context).getUserPhoto()
            );
            String currentUser = new Gson().toJson(diseaseUser);
            startActivity(ChooseMemberActivity.class, "currentUser", currentUser);
            return;
        }

        if (inSpell.matches(".*(jiankangzhishi|jiankangketang|jiangkangxuanchuan" +
                "|tingke|xuexi|yiqishiyong|shebeishiyong|shiyongjiaocheng|shiyongfangfa" +
                "|shebeijianjie|yiqijieshao|jiankangjiaoyu|jiankangxuanjiao).*")) {
            startActivity(VideoListActivity.class, "position", 0);
            return;
        }
        if (inSpell.matches(".*(jinju|jingju|yueju|xiju).*")) {
            startActivity(VideoListActivity.class, "position", 1);
            return;
        }
        if (inSpell.matches(".*(shenghuozhushou).*")) {
            startActivity(VideoListActivity.class, "position", 2);
            return;
        }
        if (inSpell.matches(".*(donghuapian|dongman).*")) {
            startActivity(VideoListActivity.class, "position", 3);
            return;
        }
        if (inSpell.matches(".*(qianyueyisheng).*")) {
            gotoQianyueYiSheng();
            return;
        }
        if (inSpell.matches(".*(zaixianyi(shen|sheng|seng)).*")) {
            startActivity(OnlineDoctorListActivity.class);
            return;
        }
        if (inSpell.matches(".*(yi(shen|sheng|seng)|dadianhua|(zi|zhi)xun).*")) {
            startActivity(DoctorAskGuideActivity.class);
            return;
        }

        if (inSpell.matches(".*(guanxin(bin|bing)).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "冠心病");
            return;
        }
        if (inSpell.matches(".*(zhiqiguanxiaochuan).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "支气管哮喘");
            return;
        }
        if (inSpell.matches(".*(gan(yin|ying)hua).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "肝硬化");
            return;
        }
//        if (inSpell.matches(".*(tang(niao|liao)(bin|bing)).*")) {
//            startActivity(DiseaseDetailsActivity.class, "type", "糖尿病");
//            return;
//        }
        if (inSpell.matches(".*(tongfeng).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "痛风");
            return;
        }
        if (inSpell.matches(".*(changweiyan).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "肠胃炎");
            return;
        }
        if (inSpell.matches(".*(ji(xin|xing)(sang|shang)huxidaoganran).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "急性上呼吸道感染");
            return;
        }
        if (inSpell.matches(".*(xinbaoyan).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "心包炎");
            return;
        }
        if (inSpell.matches(".*((pin|ping)(xie|xue)).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "贫血");
            return;
        }
        if (inSpell.matches(".*(feiyan).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "肺炎");
            return;
        }
        if (inSpell.matches(".*(di(xie|xue)tang).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "低血糖");
            return;
        }
        if (inSpell.matches(".*((nao|lao)chu(xie|xue)).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "脑出血");
            return;
        }
        if (inSpell.matches(".*(fei(suan|shuan)sai).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "肺栓塞");
            return;
        }
        if (inSpell.matches(".*(dianxian).*")) {
            startActivity(DiseaseDetailsActivity.class, "type", "癫痫");
            return;
        }
        boolean dealKeyWord = keyWordDeal(inSpell);
        if (dealKeyWord) {
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(liangxueya|cexueya|xueyajiance).*")) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }

        } else if (inSpell.matches(".*ce.*xueyang.*")
                || inSpell.matches(".*xueyang.*")
                || inSpell.matches(".*liang.*xueyang.*")
                || inSpell.matches(".*ce.*baohedu.*")) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }


        } else if (result.matches(".*测.*血糖.*")
                || inSpell.matches(".*liang.*xuetang.*")
                || inSpell.matches(".*xuetangyi.*")
                ) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }


        } else if (result.matches(".*测.*体温.*") || result.matches(".*测.*温度.*") || inSpell.matches(".*liang.*tiwen.*") || inSpell.matches(".*liang.*wendu.*")) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }

        } else if (inSpell.matches(".*ce.*xindian.*")
                || inSpell.matches(".*xindian(celiang|ceshi|jiance).*")) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }


        } else if (inSpell.matches(".*ce.*(niaosuan|xuezhi|danguchun).*")) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }

        } else if (inSpell.matches(".*ce.*tizhong.*")) {
            CC.obtainBuilder("com.gcml.auth.face.signin")
                    .addParam("skip", true)
                    .addParam("currentUser", false)
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            boolean skip = "skip".equals(result.getErrorMessage());
                            if (result.isSuccess() || skip) {
                                if (skip) {
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                    return;
                                }
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                            } else {
                                ToastUtils.showShort(result.getErrorMessage());
                            }
                        }
                    });
            if (listener != null) {
                listener.onEnd();
            }

        } else if (result.matches(".*视频.*") || inSpell.matches(".*jiankang.*jiangtan.*")) {
            startActivity(VideoListActivity.class);


        } else if (inSpell.matches(".*yisheng.*zixun.*") || inSpell.matches("wenyisheng|yishengzixun|jiatingyisheng|yuyue")) {

            if ("".equals(context.getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE).getString("name", ""))) {
                ToastUtils.showShort("请先查看是否与签约医生签约成功");
            } else {
                startActivity(DoctorappoActivity.class);
            }


        } else if (inSpell.matches(".*dashengyin.*")
                || inSpell.matches(".*dayinliang.*")
                || inSpell.matches(".*dashengdian.*")
                || inSpell.matches(".*dadiansheng.*")
                || inSpell.matches(".*yinliang.*da.*")
                || inSpell.matches(".*shengyin.*da.*")
                || inSpell.matches(".*tigao.*shengyin.*")
                || inSpell.matches(".*shengyin.*tigao.*")
                || inSpell.matches(".*yinliang.*shenggao.*")
                || inSpell.matches(".*shenggao.*yinliang.*")
                || inSpell.matches(".*shengyin.*xiangyidian.*")
                || inSpell.matches(".*shengyin.*zhongyidian.*")

                ) {
            addVoice();
        } else if (inSpell.matches(".*xiaoshengyin.*")
                || inSpell.matches(".*xiaoyinliang.*")
                || inSpell.matches(".*xiaoshengdian.*")
                || inSpell.matches(".*xiaodiansheng.*")
                || inSpell.matches(".*shengyin.*xiao.*")
                || inSpell.matches(".*yinliang.*xiao.*")
                || inSpell.matches(".*yinliang.*jiangdi.*")
                || inSpell.matches(".*jiangdi.*yinliang.*")
                || inSpell.matches(".*jiangdi.*shengyin.*")
                || inSpell.matches(".*shengyin.*jiangdi.*")
                || inSpell.matches(".*shengyin.*qingyidian.*")

                ) {

            deleteVoice();


        } else if (inSpell.matches(".*bu.*liao.*") || result.contains("退出")
                || result.contains("返回") || result.contains("再见")
                || result.contains("闭嘴") || inSpell.matches(".*baibai.*")) {
        } else if (inSpell.matches(".*((bin|bing)(zheng|zhen|zen|zeng)|(zi|zhi)(ca|cha)).*")) {
            DiseaseUser diseaseUser = new DiseaseUser(
                    LocalShared.getInstance(context).getUserName(),
                    LocalShared.getInstance(context).getSex().equals("男") ? 1 : 2,
                    Integer.parseInt(LocalShared.getInstance(context).getUserAge()) * 12,
                    LocalShared.getInstance(context).getUserPhoto()
            );
            String currentUser = new Gson().toJson(diseaseUser);
            startActivity(com.witspring.unitbody.ChooseMemberActivity.class, "currentUser", currentUser);
        } else if (inSpell.matches(".*chongqian|qianbugou|meiqian.*") || inSpell.matches(".*chongzhi.*") || result.contains("钱不够") || result.contains("没钱")) {
            startActivity(PayActivity.class);
        } else if (inSpell.matches(".*maidongxi")
                || inSpell.matches(".*mai.*shizhi")
                || inSpell.matches(".*mai.*xueyaji")
                || inSpell.matches(".*mai.*xuetangyi")
                || inSpell.matches(".*mai.*erwenqiang")
                || inSpell.matches(".*mai.*xueyangyi")
                || inSpell.matches(".*mai.*xindianyi")
                || inSpell.matches(".*shizhiyongwan.*")
                || inSpell.matches(".*shizhi.*meiyou")
                || inSpell.matches(".*shangcheng")) {
            startActivity(MarketActivity.class);


        } else if (inSpell.matches(".*dingdan|wodedingdan|chakandingdan|dingdanxiangqing|gouwuqingdan")) {

            startActivity(OrderListActivity.class);
        } else if (inSpell.matches(".*((bin|bing)(zheng|zhen|zen|zeng)|(zi|zhi)(ca|cha)|(lan|nan)(shou|sou)).*")) {//症状自查
            DiseaseUser diseaseUser = new DiseaseUser(LocalShared.getInstance(context).getUserName(),
                    LocalShared.getInstance(context).getSex().equals("男") ? 1 : 2,
                    Integer.parseInt(LocalShared.getInstance(context).getUserAge()) * 12,
                    LocalShared.getInstance(context).getUserPhoto()
            );
            String currentUser = new Gson().toJson(diseaseUser);
            startActivity(com.witspring.unitbody.ChooseMemberActivity.class, "currentUser", currentUser);
        } else if (inSpell.matches(".*(dangan).*")) {
            CC.obtainBuilder("com.gcml.auth.profileInfo").build().callAsync();
            if (listener != null) {
                listener.onEnd();
            }
        } else {
            new SpeechTask().execute();
        }
    }


    private void gotoQianyueYiSheng() {
        NetworkApi.PersonInfo(UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if ("1".equals(response.getState())) {
                    //已签约
                    startActivity(DoctorappoActivity.class);
                } else if ("0".equals(response.getState())
                        && (TextUtils.isEmpty(response.getDoctername()))) {
                    startActivity(OnlineDoctorListActivity.class, "flag", "contract");
                } else {
                    startActivity(CheckContractActivity.class);
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);

                if (listener != null) {
                    listener.onEnd();
                }
            }
        });
    }

    private void gotoHomePage() {
        startActivity(MainActivity.class);
    }

    class SpeechTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                post(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private HashMap<String, String> results;
    String str1;
    private volatile String mAudioPath;

    private void post(String str) {
        if (str.matches("(.*)是谁")) {
            str = "百科" + str.substring(0, str.length() - 2);
        }
        results = QaApi.getQaFromXf(str);
        Timber.i("QaApi %s", results);
        String audiopath = results.get("audiopath");
        String text = results.get("text");
        boolean empty = TextUtils.isEmpty(text);
        if (!TextUtils.isEmpty(audiopath)) {
            mAudioPath = audiopath;
            if (!empty) {
                speak(text);
            } else {
                onActivitySpeakFinish();
            }
            return;
        }

        if ("musicX".equals(results.get("service")) && TextUtils.isEmpty(audiopath)) {
            mAudioPath = audiopath;
            int index = text.indexOf("的歌曲");
            if (index == -1) {
                index = text.indexOf("的");
                index += 1;
            } else {
                index += 3;
            }
            String music = "";
            if (index != -1) {
                music = text.substring(index);
                searchMusic(music);
            }

            return;
        }

        if (!TextUtils.isEmpty(text)) {
            speak(text);
            return;
        }

        if (!empty) {
            speak(text);
            return;
        }
        str1 = empty ? "我真的不知道了" : text;
        try {
            str1 = sendMessage(str);
        } catch (Exception e) {
            defaultToke();
        }
    }

    private void onActivitySpeakFinish() {
        if (!TextUtils.isEmpty(mAudioPath)) {
            String service = results.get("service");
            if ("storyTelling".equals(service)) {
            }
            onPlayAudio(mAudioPath);
            mAudioPath = null;
            return;
        }
    }

    private void onPlayAudio(String audioPath) {
        Music music = new Music(audioPath);
        startActivity(MusicPlayActivity.class, "music", music);
    }

    private AccessToken data;
    private String sessionId = "";
    private StringBuilder sb;

    private String sendMessage(final String request) {
        if (TextUtils.isEmpty(request)) {
            defaultToke();
            return str1;
        }
        data = EHSharedPreferences.ReadAccessToken(ACCESSTOKEN_KEY);
        if (data == null) {
            return str1;
        }
        UnitModel model = new UnitModel();
        model.getUnit(data.getAccessToken(), SCENE_Id, request, sessionId, new HttpListener<Unit>() {

            @Override
            public void onSuccess(Unit data) {
                if (data != null) {
                    sessionId = data.getSession_id();
                }
                List<Unit.ActionListBean> list = data.getAction_list();
                if (list != null && list.size() != 0) {
                    if (list.size() >= 10) {
                        str1 = list.get(new Random().nextInt(10)).getSay().replace("<USER-NAME>", "");
                    } else {
                        str1 = list.get(0).getSay().replace("<USER-NAME>", "");
                    }
                }
                defaultToke();
            }

            @Override
            public void onError() {
                defaultToke();
            }

            @Override
            public void onComplete() {

            }
        });
        return str1;
    }

    private void defaultToke() {
        if (str1 != null) {

            if (context.getString(R.string.speak_null).equals(str1)) {
                int randNum = new Random().nextInt(30) + 1;

                switch (randNum) {

                    case 1:
                        speak(context.getString(R.string.speak_1));
                        break;
                    case 2:
                        speak(context.getString(R.string.speak_2));
                        break;
                    case 3:
                        speak(context.getString(R.string.speak_3));

                        break;
                    case 4:
                        speak(context.getString(R.string.speak_4));

                        break;
                    case 5:
                        speak(context.getString(R.string.speak_5));

                        break;
                    case 6:
                        speak(context.getString(R.string.speak_6));

                        break;
                    case 7:
                        speak(context.getString(R.string.speak_7));

                        break;
                    case 8:
                        speak(context.getString(R.string.speak_8));

                        break;
                    case 9:
                        speak(context.getString(R.string.speak_9));

                        break;
                    case 10:
                        speak(context.getString(R.string.speak_10));
                        break;
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                        speak(result);
                        break;
                    default:
                        speak(result);
                        break;
                }

            } else {
                speak(str1);
            }
        }
    }

    private List<SearchMusic.Song> mSearchMusicList = new ArrayList<>();

    private void searchMusic(String keyword) {

        HttpClient.searchMusic(keyword, new HttpCallback<SearchMusic>() {

            @Override
            public void onSuccess(SearchMusic response) {
                if (response == null || response.getSong() == null) {
                    speak("抱歉，没找到这首歌");
                    return;
                }
                mSearchMusicList.clear();
                mSearchMusicList.addAll(response.getSong());


                new PlaySearchedMusic(context, mSearchMusicList.get(0)) {
                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onExecuteSuccess(Music music) {
                        //跳转到音乐播放界面去
                        startActivity(MusicPlayActivity.class, "music", music);
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        ToastUtils.showShort(R.string.unable_to_play);
                    }
                }.execute();

            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    private void gotoPersonCenter() {
        startActivity(PersonDetailActivity.class);
    }

    private void addVoice() {
        AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume += 3;
        if (volume < maxVolume) {
            manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
            speak("调大声音");
        } else {
            speak("当前已经是最大声音了");
        }
    }


    private void deleteVoice() {
        AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        int volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume -= 3;
        if (volume > 3) {
            manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
            speak("调小声音");
        } else {
            speak("当前已经是最小声音了");
        }

    }

    private List<KeyWordDefinevBean> getDefineData(String keyWord) {
        String xueya = (String) SharedPreferencesUtils.getParam(context, keyWord, "");
        if (gson == null) {
            gson = new Gson();
        }
        List<KeyWordDefinevBean> list = gson.fromJson(xueya, new TypeToken<List<KeyWordDefinevBean>>() {
        }.getType());
        if (list != null) {
            return list;
        }

        return new ArrayList<KeyWordDefinevBean>();
    }

    private boolean keyWordDeal(String yuyin) {
        if (TextUtils.isEmpty(yuyin)) {
            return false;
        }
        //血压
//        jiance.addAll(getDefineData("xueyang"));
//        jiance.addAll(getDefineData("tiwen"));
//        jiance.addAll(getDefineData("xuetang"));
//        jiance.addAll(getDefineData("xindian"));
//        jiance.addAll(getDefineData("tizhong"));
//        jiance.addAll(getDefineData("sanheyi"));
        List<KeyWordDefinevBean> jiance = getDefineData("xueya");
        String pinyin;
        for (int i = 0; i < jiance.size(); i++) {
            pinyin = jiance.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                return true;
            }
        }

        //血氧
        List<KeyWordDefinevBean> xueyang = getDefineData("xueyang");
        for (int i = 0; i < xueyang.size(); i++) {
            pinyin = xueyang.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });

                return true;
            }
        }
        //体温

        List<KeyWordDefinevBean> tiwen = getDefineData("tiwen");
        for (int i = 0; i < tiwen.size(); i++) {
            pinyin = tiwen.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                return true;
            }
        }

        //血糖

        List<KeyWordDefinevBean> xuetang = getDefineData("xuetang");
        for (int i = 0; i < xuetang.size(); i++) {
            pinyin = xuetang.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                return true;
            }
        }

        //心电
        List<KeyWordDefinevBean> xindian = getDefineData("xindian");
        for (int i = 0; i < xindian.size(); i++) {
            pinyin = xindian.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                return true;
            }
        }

        //体重
        List<KeyWordDefinevBean> tizhong = getDefineData("tizhong");
        for (int i = 0; i < tizhong.size(); i++) {
            pinyin = tizhong.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                return true;
            }
        }


        //三合一
        List<KeyWordDefinevBean> sanheyi = getDefineData("sanheyi");
        for (int i = 0; i < sanheyi.size(); i++) {
            pinyin = sanheyi.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                return true;
            }
        }


        //调大声音
        List<KeyWordDefinevBean> addVoice = getDefineData("tiaodashengyin");
        for (int i = 0; i < addVoice.size(); i++) {
            pinyin = addVoice.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(addVoice.get(i).pinyin)) {
                addVoice();
                return true;
            }
        }

        //调小声音
        List<KeyWordDefinevBean> deleteVoice = getDefineData("tiaoxiaoshengyin");
        for (int i = 0; i < deleteVoice.size(); i++) {
            pinyin = deleteVoice.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                addVoice();
                return true;
            }
        }

        //回到主界面
        List<KeyWordDefinevBean> home = getDefineData("huidaozhujiemian");
        for (int i = 0; i < home.size(); i++) {
            pinyin = home.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                startActivityWithOutCallback(MainActivity.class);
                return true;
            }
        }

        //个人中心
        List<KeyWordDefinevBean> personCenter = getDefineData("gerenzhongxin");
        for (int i = 0; i < personCenter.size(); i++) {
            pinyin = personCenter.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                startActivityWithOutCallback(PersonDetailActivity.class);
                return true;
            }
        }

        //症状自查
        List<KeyWordDefinevBean> check = getDefineData("zhengzhuangzicha");
        for (int i = 0; i < check.size(); i++) {
            pinyin = check.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                startActivityWithOutCallback(SymptomCheckActivity.class);
                return true;
            }
        }

        //测量历史
        List<KeyWordDefinevBean> celianglishi = getDefineData("celianglishi");
        for (int i = 0; i < celianglishi.size(); i++) {
            pinyin = celianglishi.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                startActivityWithOutCallback(HealthRecordActivity.class);
                return true;
            }
        }

        //医生建议
        List<KeyWordDefinevBean> doctorJianyi = getDefineData("yishengjianyi");
        for (int i = 0; i < doctorJianyi.size(); i++) {
            pinyin = doctorJianyi.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                startActivityWithOutCallback(MessageActivity.class);
                return true;
            }
        }

        //吃药提醒
        List<KeyWordDefinevBean> chiyaoTixing = getDefineData("chiyaotixing");
        for (int i = 0; i < chiyaoTixing.size(); i++) {
            pinyin = chiyaoTixing.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(chiyaoTixing.get(i).pinyin)) {
                startActivityWithOutCallback(AlarmList2Activity.class);
                return true;
            }
        }

        //账户充值
        List<KeyWordDefinevBean> zhanghuchongzhi = getDefineData("zhanghuchongzhi");
        for (int i = 0; i < zhanghuchongzhi.size(); i++) {
            if (yuyin.contains(zhanghuchongzhi.get(i).pinyin)) {
                startActivityWithOutCallback(PayActivity.class);
                return true;
            }
        }

        //我的订单
        List<KeyWordDefinevBean> dingdan = getDefineData("wodedingdan");
        for (int i = 0; i < dingdan.size(); i++) {
            if (yuyin.contains(dingdan.get(i).pinyin)) {
                startActivityWithOutCallback(OrderListActivity.class);
                return true;
            }
        }

        //健康课堂
        List<KeyWordDefinevBean> jiankangketang = getDefineData("jiankangketang");
        for (int i = 0; i < jiankangketang.size(); i++) {
            if (yuyin.contains(jiankangketang.get(i).pinyin)) {
                startActivityWithOutCallback(VideoListActivity.class);
                return true;
            }
        }


        //娱乐
        List<KeyWordDefinevBean> yule = getDefineData("yule");
        for (int i = 0; i < yule.size(); i++) {
            if (yuyin.contains(yule.get(i).pinyin)) {
                //老人娱乐
                startActivityWithOutCallback(TheOldHomeActivity.class);
                return true;
            }
        }


        //收音机
        List<KeyWordDefinevBean> shouyinji = getDefineData("shouyinji");
        for (int i = 0; i < shouyinji.size(); i++) {
            if (yuyin.contains(shouyinji.get(i).pinyin)) {
                startActivityWithOutCallback(RadioActivity.class);
                return true;
            }
        }

        //音乐
        List<KeyWordDefinevBean> yinyue = getDefineData("yinyue");
        for (int i = 0; i < yinyue.size(); i++) {
            if (yuyin.contains(yinyue.get(i).pinyin)) {
                startActivityWithOutCallback(TheOldMusicActivity.class);
                return true;
            }
        }


        //医生咨询
        List<KeyWordDefinevBean> zixunyisheng = getDefineData("yishengzixun");
        for (int i = 0; i < zixunyisheng.size(); i++) {
            if (yuyin.contains(zixunyisheng.get(i).pinyin)) {
                startActivityWithOutCallback(DoctorAskGuideActivity.class);
                return true;
            }
        }

        //在线医生
        List<KeyWordDefinevBean> zaixianyisheng = getDefineData("zaixianyisheng");
        for (int i = 0; i < zaixianyisheng.size(); i++) {
            if (yuyin.contains(zaixianyisheng.get(i).pinyin)) {
                startActivityWithOutCallback(OnlineDoctorListActivity.class);
                return true;
            }
        }

        //签约医生
        List<KeyWordDefinevBean> qianyueyisheng = getDefineData("qianyueyisheng");
        for (int i = 0; i < qianyueyisheng.size(); i++) {
            if (yuyin.contains(qianyueyisheng.get(i).pinyin)) {
                gotoQianyueYiSheng();
                return true;
            }
        }


        //健康商城
        List<KeyWordDefinevBean> jiankang = getDefineData("jiankangshangcheng");
        for (int i = 0; i < jiankang.size(); i++) {
            if (yuyin.contains(jiankang.get(i).pinyin)) {
                startActivityWithOutCallback(MarketActivity.class);
                return true;
            }
        }

        return false;

    }

    private void startActivityWithOutCallback(Class<?> cls/*, String key, String value*/) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        intent.putExtra(key, value);
        context.startActivity(intent);
    }

}
