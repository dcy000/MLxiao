package com.example.han.referralproject.bodytest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import java.util.ArrayList;

public class MonitorResultActivity extends BaseActivity {

    public static final String[] results =
            {"阳虚体(怕冷派)-冬天穿棉衣，夏天穿短袖，这些对普通人来说是再正常不过的事情，但是却有一个人群，即使在最热的时候也要穿长袖衬衫，更有甚者夏天穿毛衣、棉衣，三伏天，别人开着空调才觉得舒服，他却要\"全副武装\"，孤独的承受着冷的感觉。因为他是阳虚体质，体内阳气不足，身体就像冬天少了火炉的房间，从里到外的冷。"
                    , "阴虚体(缺水派)-与之相反，另外一个群体的奇怪现象是，喜欢冬天吃雪糕，因为有时候会觉得心里燥热，整体来说，喜欢那种在凛冽的寒风中吃着雪糕，里外都透心凉的感觉。晚上睡觉的时候手心、脚心发热，恨不得在脚底下放上冰块儿。所有人都认为这是年轻，火力旺，且不知，这样的人可能是阴虚体质。"
                    , "痰湿体(痰派)-除了这些比较特殊的情况，一些普遍的现象也都在透露与体质相关的信息。比如前面说过的那位糖尿病朋友，只不过痰多点，肥胖点，脸色差了点，眼泡爱肿点……我们每天上班、下班的路上，地铁里，商场里，学校里，像这样的人还真不少，而肚子大、脸色发暗的人儿可能就是痰湿体质。"
                    , "湿热体(长痘派)-还有和\"痰派\"有点相似的湿热体质，这种体质的人在外观上应该最好辨认，一张冒油的脸和满面痘痘是明显标志。有的人认为长痘不一定是坏事儿，说明还年轻，还有长\"青春痘\"的本事，其实是体内的湿热过重，里面又不\"通风\"，它们只好变成痘子往外挤。千万不要以为用香皂洗脸，脸上不擦任何东西就能这些\"油痘\"消下去，因为这些痘子的根在体内，这种体质的人却偏爱吃辣，越吃热就越重，痘子就越多。"
                    , "气虚体(气短派)-现在，减肥成了女孩子们的必修课，不管真胖假胖，也一定要减肥。但是你发现了吗？减肥后的女孩子，特别是\"饥饿疗法\"减肥后的女孩子，脸色柔白，说话的声音变轻了，行动起来羸弱了，看上去轻飘飘的。楚楚动人却成了气虚体质。气不足，自然没有办法对抗来自外界的病邪，于是感冒变成了家常便饭，感冒了还不爱好，柔弱的美人，变得更加柔弱。"
                    , "血瘀体(长斑派)-与长雀斑，蝴蝶般，老年斑有关的还有血瘀体质，如果有人经常为痛经而烦恼，就更需要了解一下这种体质，所谓不通则痛，而瘀则不痛，如果把血液比作身体里的河，痛经就是因为河道里有淤塞的地方，所以止疼片不管用，热红糖水不管用。哦，对了，如果你的身体经常出现莫名的瘀青，就更要留意自己的血瘀体质，它可能跟很多种疾病有关。"
                    , "所郁体(郁闷派)-近年来演艺圈艺人频发抑郁引发自杀事件。我们这一生当中，或多或少都有抑郁的时候，跟领导吵架了，跟妻子或丈夫窝气，跟同学、老师闹别扭，事业出现了阻滞，理想被现实打击……这么多的原因能够让我们郁闷，但是为什么，不是每个人都得抑郁症？每个人都有抛弃生命的想法？大多数时候，我们能够从抑郁的情绪里走出来，因为没走出来的人儿多数是气郁体质。"
                    , "特禀体(过敏派)-然后，还有发作起来让人生不如死的过敏，同在一间屋子，窗外偷偷飘进一片柳絮，悄悄地来又悄悄地走，别人只是揉揉鼻子就过去了，你可惨了，不停的打喷嚏，鼻涕、眼泪一起流，最后还流进了医院里，医生说：过敏了。你可曾想过，为什么一屋子的人唯有你过敏呢？还有海鲜过敏、鸡蛋过敏、牛奶过敏……无所不在的过敏原你怎么才能躲得开？远离过敏原只是一种不得已的办法，想要治本，还得调整你的特禀体质。"
                    , "平和体( 健康派)-最重要一种--人人渴望的平和体质。平和体质的人发黑如墨，脸色红润，身材匀称而充满活力，他们的性情开朗、阳光，绝对不会为小事斤斤计较，也不会轻易郁闷或动怒。\n" +
                    "平和质，不只是一种体质，而是一种生活状态，一份对健康的美好愿望，一个和谐生命的范本。"};
    private static final String TAGS = "tags";
    private String[] tags;
    private ViewPager vpResult;
    private ArrayList<PagerFragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_result);
        initData();
        initView();
    }

    private void initData() {
        tags = getIntent().getStringArrayExtra(TAGS);

    }

    public static void starMe(Context context, String[] tags) {
        Intent intent = new Intent(context, MonitorResultActivity.class);
        intent.putExtra(TAGS, tags);
        context.startActivity(intent);
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        mTitleText.setText("中医体质检测报告");
        vpResult = ((ViewPager) findViewById(R.id.vp_results));
        if (tags != null && tags.length != 0) {
            for (int i = 0; i < tags.length && i < 1; i++) {
                mFragments.add(PagerFragment.newInstance(i, tags));
            }
        }
        vpResult.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        for (PagerFragment fragment : mFragments) {
            vpResult.addOnPageChangeListener(fragment);
            fragment.setViewPager(vpResult);
        }
    }

}
