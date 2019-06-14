package com.example.module_control_volume.wakeup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.han.referralproject.new_music.HttpCallback;
import com.example.han.referralproject.new_music.HttpClient;
import com.example.han.referralproject.new_music.MusicPlayActivity;
import com.example.han.referralproject.new_music.PlaySearchedMusic;
import com.example.han.referralproject.new_music.SearchMusic;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.lenovo.rto.unit.Unit;
import com.example.lenovo.rto.unit.UnitModel;
import com.example.module_control_volume.R;
import com.gcml.common.AppDelegate;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.face2.VertifyFace2ProviderImp;
import com.gcml.common.recommend.bean.get.KeyWordDefinevBean;
import com.gcml.common.recommend.bean.get.Music;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.IHuiQuanBodyTestProvider;
import com.gcml.common.service.ITaskProvider;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.PinYinUtils;
import com.gcml.common.utils.SharedPreferencesUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.iflytek.utils.QaApi;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Context.AUDIO_SERVICE;
import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
import static com.example.lenovo.rto.Constans.SCENE_Id;

/**
 * 雄安版本的页面跳转
 */

public class XADataDealHelper {
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
            String hourOfDay = matcherWhenAlarm.group(2);
            String minute = matcherWhenAlarm.group(3);
            String tip = String.format(Locale.CHINA, "小易将在%s:%s提醒您吃药", hourOfDay, minute);
            speak(tip);
            return;
        }

        //检查更新
        if (inSpell.matches(".*(woyaogengxin|gengxinxitong|xinbanben).*")) {
            Routerfit.register(AppRouter.class).getAppUpdateProvider().checkAppVersion(context, true);
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //关于我们
        if (inSpell.matches(".*(banbenhao|nilaizinali|guanyuwomen).*")) {
            Routerfit.register(AppRouter.class).skipAboutActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //恢复出厂设置
        if (inSpell.matches(".*(banbenhao|nilaizinali|guanyuwomen).*")) {
            //恢复出厂设置
            UserSpHelper.clear(AppDelegate.INSTANCE.app());
            Routerfit.register(AppRouter.class).skipWelcomeActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        // 健康检测跳转
        if (inSpell.matches(".*(zuogejiancha|jianchashenti|zuotijian|zuotijian).*")) {
            //恢复出厂设置
            jiance();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*yinyue.*")) {
            Routerfit.register(AppRouter.class).skipTheOldMusicActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        //退出账号
        if (inSpell.matches(".*(woyaotuichu|tuichuzhanghao)")) {
            exit();
            return;
        }

        //儿歌
        if (inSpell.matches(".*(erge|ertonggequ).*")) {
            Routerfit.register(AppRouter.class).skipChildEduSheetDetailsActivity("儿童歌曲");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //健康检测
        if (inSpell.matches(".*(jiankangjiance|zuogejiancha|jianchashenti|zuotijian).*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //个人信息
        if (inSpell.matches(".*(geren|xiugai)xinxi.*")
                || inSpell.matches(".*huantouxiang.*")) {
            Routerfit.register(AppRouter.class).skipUserInfoActivit();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //健康管理
        if (inSpell.matches(".*(jiankangguanli|gaoxueyaguanli|gaoxueyafangan|" +
                "gaoxueyazhiliao|gaoxueyacaipu|jiankangfangan|jiankangbaogao).*")) {
            Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //我的设备
        if (inSpell.matches(".*(shouhuanxinxi|chakanshouhuan).*")) {
            toShouHuan();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }


        //健康数据
        if (inSpell.matches(".*(danganxiazai|lishishuju|lishijilu|jiancejieguo|celiangshuju|jiankangshuju|jiankangdangan|jianchajieguo|jiancejilu).*")) {
            vertifyFaceThenHealthRecordActivity();
            if (listener != null) {
                listener.onEnd();
            }

            return;
        }


        //设置界面
        if (inSpell.matches(".*(shezhi|sezhi|shezi|sezi).*")) {
            Routerfit.register(AppRouter.class).skipSettingActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //家庭医生
        if (inSpell.matches(".*(gaoxueyasuifang|tangniaobingsuifang|suifang|jiankangtijian|zhongyitizhibianshi|jiandang).*")) {
            Routerfit.register(AppRouter.class).skipHealthProfileActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //症状自查
        if (inSpell.matches(".*(shentibushufu|nanshou|duziteng|ganmaole|zhengzhuangzicha).*")) {
            IHuiQuanBodyTestProvider bodyTestProvider = Routerfit.register(AppRouter.class).getBodyTestProvider();
            if (bodyTestProvider != null) {
                bodyTestProvider.gotoPage(context);
            }
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //症状自查
        if (inSpell.matches(".*(shentibushufu|nanshou|duziteng|ganmaole|zhengzhuangzicha).*")) {
            IHuiQuanBodyTestProvider bodyTestProvider = Routerfit.register(AppRouter.class).getBodyTestProvider();
            if (bodyTestProvider != null) {
                bodyTestProvider.gotoPage(context);
            }
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //健康管理
        if (inSpell.matches(".*(jiankangguanli).*")) {
            toJianKangGuanli();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //风险评估
        if (inSpell.matches(".*(fengxianpinggu|pinggu|shenti).*")) {
            toFengxianPingu();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //健康方案
        if (inSpell.matches(".*(jiankangfangan|gaoxueyafangan).*")) {
            Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //每日任务
        if (inSpell.matches(".*(jiankangrenwu|zuorenwu|chakanrenwu|jintianderenwu|jintianzuoshenme).*")) {
            toDailyTask();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //wifi连接
        if (inSpell.matches(".*(lianjiewifi|lianjiewangluo|duanwangle|duanwangla|meiwangla|meiwangle|wifi).*")) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(yulezhongxin).*")) {
            Routerfit.register(AppRouter.class).skipRecreationEntranceActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(laorenyule).*")) {
            Routerfit.register(AppRouter.class).skipTheOldHomeActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }


        if (inSpell.matches(".*(youjiao|youjiaowenyu|ertongyoujiao|jiaoxiaohai|ertongyule).*")) {
            Routerfit.register(AppRouter.class).skipChildEduHomeActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(yaolanqu).*")) {
            Routerfit.register(AppRouter.class).skipChildEduSheetDetailsActivity("摇篮曲");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(taijiaoyinyue|taijiaoyinle|taijiao|tingyinle).*")) {
            Routerfit.register(AppRouter.class).skipChildEduSheetDetailsActivity("胎教音乐");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(tingyinyue|tingge|fangge|yinyueguan|yinleguan).*")) {
            Routerfit.register(AppRouter.class).skipTheOldMusicActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(gushi|tangshisongci|songci|tangshi).*") || result.matches(".*古诗.*")) {
            Routerfit.register(AppRouter.class).skipChildEduPoemListActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(jianggexiaohua|xiaohua|youqudehua).*")) {
            Routerfit.register(AppRouter.class).skipChildEduJokesActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (result.matches(".*听故事|故事.*")) {
            Routerfit.register(AppRouter.class).skipChildEduPoemListActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(guangbo|diantai|shouyinji|zhisheng|diantai).*")) {
            Routerfit.register(AppRouter.class).skipRadioActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }


        if (inSpell.matches(".*(zhougongjiemeng|jiemeng|jiegemeng|zuolemeng).*")) {
            Routerfit.register(AppRouter.class).skipJieMengActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(lishijintian|lishishangdejintian|lishishangjintiandeshijian).*")) {
            Routerfit.register(AppRouter.class).skipHistoryTodayActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(riqichaxun|jidianle|chaxunriqi|jintianxingqiji|jidianle|jintianshenmerizi).*")) {
            Routerfit.register(AppRouter.class).skipDateInquireActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(caipu|shaocai|chishenme|chishengme|zuocai|tuijiancai).*")) {
//            CC.obtainBuilder("app.component.recreation.tool").setActionName("cookBook").build().call();
            Routerfit.register(AppRouter.class).skipCookBookActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(baike).*")) {
//            CC.obtainBuilder("app.component.recreation.tool").setActionName("baike").build().call();
            Routerfit.register(AppRouter.class).skipBaikeActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }


        if (inSpell.matches(".*(jisuanqi|zuosuanshu).*")) {
//            CC.obtainBuilder("app.component.recreation.tool").setActionName("calculate").build().call();
            Routerfit.register(AppRouter.class).skipCalculationActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //个人中心
        if (inSpell.matches(".*gerenzhongxin.*")) {
            gotoPersonCenter();
            return;
        }
        if (inSpell.matches(".*zhujiemian|zujiemian|jujiemian|zhuye.*")
                || inSpell.matches(".*zhujiemian|shuijiao|xiuxi|guanbi.*")) {
            gotoHomePage();
            return;
        }
        if (inSpell.matches(REGEX_SEE_DOCTOR)) {
            Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(context);
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(woyaokanshipin|jiankangxuanjiao).*")) {
            Routerfit.register(AppRouter.class).skipVideoListActivity(0);
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        /*if (inSpell.matches(".*(jinju|jingju|yueju|xiju).*")) {
            startActivity(VideoListActivity.class, "position", 1);
            return;
        }*/
        if (inSpell.matches(".*(shenghuozhushou).*")) {
            Routerfit.register(AppRouter.class).skipVideoListActivity(2);
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
       /* if (inSpell.matches(".*(donghuapian|dongman).*")) {
            startActivity(VideoListActivity.class, "position", 3);
            return;
        }*/
        if (inSpell.matches(".*(qianyueyisheng|zhuanshuyisheng|sirenyisheng).*")) {
            gotoQianyueYiSheng();
            return;
        }
        if (inSpell.matches(".*(zaixiangu(wen|weng)).*")) {
            Routerfit.register(AppRouter.class).skipOnlineDoctorListActivity("");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(zi|zhi)xun.*")
                || inSpell.matches(".*(gu(wen|weng)|shipin)((zi|zhi)xun).*")) {
            Routerfit.register(AppRouter.class).skipDoctorAskGuideActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        if (inSpell.matches(".*(guanxin(bin|bing)).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("冠心病");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(zhiqiguanxiaochuan).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("支气管哮喘");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(gan(yin|ying)hua).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("肝硬化");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
//        if (inSpell.matches(".*(tang(niao|liao)(bin|bing)).*")) {
//            startActivity(DiseaseDetailsActivity.class, "type", "糖尿病");
//            return;
//        }
        if (inSpell.matches(".*(tongfeng).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("痛风");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(changweiyan).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("肠胃炎");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(ji(xin|xing)(sang|shang)huxidaoganran).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("急性上呼吸道感染");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(xinbaoyan).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("心包炎");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*((pin|ping)(xie|xue)).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("贫血");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(feiyan).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("肺炎");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(di(xie|xue)tang).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("低血糖");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*((nao|lao)chu(xie|xue)).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("脑出血");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(fei(suan|shuan)sai).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("肺栓塞");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        if (inSpell.matches(".*(dianxian).*")) {
            Routerfit.register(AppRouter.class).skipDiseaseDetailsActivity("癫痫");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        //血压
        if (inSpell.matches(".*(celiangxueya|liangxueya|cexueya|xueyajiance|xueyayi).*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }

            return;

        }
        //血氧
        if (inSpell.matches(".*cexueyang.*")
                || inSpell.matches(".*celiangxueyang.*")
                || inSpell.matches(".*liangxueyang.*")
                || inSpell.matches(".*ce.*baohedu.*")
                || inSpell.matches(".*xueyangjiance.*")
                || inSpell.matches(".*xueyangyi*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }
            return;

        }
        //血糖
        if (result.matches(".*测.*血糖.*")
                || inSpell.matches(".*liang.*xuetang.*")
                || inSpell.matches(".*xuetangyi.*")
                || inSpell.matches(".*xuetangjiance.*")
        ) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }

            return;
        }
        //体温
        if (result.matches(".*测.*体温.*")
                || result.matches(".*测.*温度.*")
                || inSpell.matches(".*liang.*tiwen.*")
                || inSpell.matches(".*liang.*wendu.*")
                || inSpell.matches(".*(tiwenjiance|erwenqiang).*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }

            return;

        }
        //心电
        if (inSpell.matches(".*ce.*xindian.*")
                || inSpell.matches(".*xindian(celiang|ceshi|jiance|tu|yi).*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        //胆固醇尿酸
        if (inSpell.matches(".*ce.*(niaosuan|xuezhi|danguchun|xueniaosuan).*")
                || inSpell.matches(".*danguchunjiance.*")
                || inSpell.matches(".*xueniaosuanjiance.*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        //体重
        if (inSpell.matches(".*ce.*tizhong.*")
                || inSpell.matches(".*liangtizhong.*")
                || inSpell.matches(".*tizhongjiance.*")
                || inSpell.matches(".*tizhongceng.*")
                || inSpell.matches(".*tizhongcheng.*")) {
            jiance();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //签约医生
        if (inSpell.matches("qianyueyisheng|qianyue")) {
            gotoQianyueYiSheng();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }

        //在线医生
        if (inSpell.matches("zaixianyisheng")) {
            Routerfit.register(AppRouter.class).skipOnlineDoctorListActivity("");
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        //视频咨询

        if (inSpell.matches(".*zixunyisheng|zhaoyisheng|geiyishengdadianhua|shipinzixun|wenyisheng*")) {
            Routerfit.register(AppRouter.class).skipDoctorAskGuideActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        }
        //音量设置
        if (inSpell.matches(".*(tiaodashengyin|tiaoxiaoshengyin|yinliang|shengyintaiqing|shengyintaixiang).*")) {
            Routerfit.register(AppRouter.class).skipVoiceSettingActivity();
            if (listener != null) {
                listener.onEnd();
            }
            return;
        } else {
            new SpeechTask().execute();
        }
    }

    private void toFengxianPingu() {
        Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.sex) || TextUtils.isEmpty(user.birthday) ||
                                TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                            ToastUtils.showShort("请确保年龄性别身高体重信息已完善");
                            MLVoiceSynthetize.startSynthesize(
                                    context,
                                    "请确保年龄性别身高体重信息已完善");
                        } else {
                            Routerfit.register(AppRouter.class).skipHealthInquiryActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void toDailyTask() {
        Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                            ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                            MLVoiceSynthetize.startSynthesize(
                                    context,
                                    "请先去个人中心完善体重和身高信息");
                        } else {
                            ITaskProvider taskProvider = Routerfit.register(AppRouter.class).getTaskProvider();
                            if (taskProvider != null) {
                                taskProvider
                                        .isTaskHealth()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new io.reactivex.observers.DefaultObserver<Object>() {
                                            @Override
                                            public void onNext(Object o) {
                                                Routerfit.register(AppRouter.class).skipTaskActivity("MLSpeech");
                                            }

                                            @Override
                                            public void onError(Throwable throwable) {
                                                if (throwable instanceof NullPointerException) {
                                                    Routerfit.register(AppRouter.class).skipTaskActivity("MLSpeech");
                                                } else {
                                                    Routerfit.register(AppRouter.class).skipTaskActivity("MLSpeech");
                                                }
                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void toJianKangGuanli() {
        Routerfit.register(AppRouter.class).skipHealthMannageActivity();
    }

    private void jiance() {
        Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
       /* Routerfit.register(AppRouter.class)
                .getVertifyFaceProvider3()
                .checkUserEntityAndVertifyFace(true, true, true, new VertifyFace2ProviderImp.VertifyFaceResult() {
                    @Override
                    public void success() {
                        Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });*/
    }


    private void gotoQianyueYiSheng() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.doctorId) || user.doctorId.equals("0")) {
                            Routerfit.register(AppRouter.class).skipBindDoctorActivity("contractOnly");
                        } else {
                            if ("0".equals(user.state)) {
                                // 签约待审核
                                Routerfit.register(AppRouter.class).skipBindDoctorActivity("contractOnly");
                            } else {
                                // 已签约
                                Routerfit.register(AppRouter.class).skipCheckContractActivity();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void gotoHomePage() {
        Routerfit.register(AppRouter.class).skipMainActivity();
        if (listener != null) {
            listener.onEnd();
        }
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
        Routerfit.register(AppRouter.class).skipUserCenterActivity();
        if (listener != null) {
            listener.onEnd();
        }
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

    private void startActivityWithOutCallback(Class<?> cls/*, String key, String value*/) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    private void exit() {
        MobclickAgent.onProfileSignOff();
        Routerfit.register(AppRouter.class).getCallProvider().logout();
        UserSpHelper.setToken("");
        UserSpHelper.setEqId("");
        UserSpHelper.setUserId("");
        Routerfit.register(AppRouter.class).skipAuthActivity();
    }

    private void vertifyFaceThenHealthRecordActivity() {
        Routerfit.register(AppRouter.class)
                .getVertifyFaceProvider3()
                .checkUserEntityAndVertifyFace(true, true, true, new VertifyFace2ProviderImp.VertifyFaceResult() {
                    @Override
                    public void success() {
                        Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
    }

    private void toShouHuan() {
        Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (!TextUtils.isEmpty(user.watchCode)) {
                            Routerfit.register(AppRouter.class).skipBraceletActivtity();
                        } else {
                            Routerfit.register(AppRouter.class).skipHealthManageTipActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
