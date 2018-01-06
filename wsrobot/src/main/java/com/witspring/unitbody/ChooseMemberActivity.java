package com.witspring.unitbody;

import android.app.Application;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsbodyActivityChooseMemberBinding;
import com.witspring.model.entity.Member;
import com.witspring.util.HttpUtil;
import com.witspring.util.StringUtil;

import java.util.List;

public class ChooseMemberActivity extends BaseActivity {

    private WsbodyActivityChooseMemberBinding binding;
    public static final int REQUEST_ADD_MEMBER = 1;

    public static Application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.wsbody_activity_choose_member);
        setToolbar(binding.toolbar, "选择查诊人");
        app = getApplication();
        Fresco.initialize(app);
        HttpUtil.init(app);
        String currentUserStr = getIntent().getStringExtra("currentUser");
        String membersStr = getIntent().getStringExtra("members");
        if (StringUtil.isTrimBlank(membersStr) && StringUtil.isTrimBlank(currentUserStr) ) {
            membersStr = "[{\"name\":\"高新亮\",\"sex\":1,\"age\":280,photo:\"http://www.witspring.com/logo1.png\"},{\"name\":\"吴忠富\",\"sex\":2,\"age\":280,photo:\"http://www.witspring.com/logo2.png\"},{\"name\":\"付聪\",\"sex\":1,\"age\":280,photo:\"http://www.witspring.com/logo3.png\"}]";
        }
        List<Member> members = Member.parseMemberList(membersStr);
        Member currmember = Member.parseMember(currentUserStr);
        if (currmember != null) {
            members.add(0, currmember);
        }
        LayoutInflater inflater = getLayoutInflater();
        for (final Member member : members) {
            View view = inflater.inflate(R.layout.wsbody_item_member_avator, null);
            SimpleDraweeView ivAvatar = view.findViewById(R.id.ivAvatar);
            final TextView tvShort = view.findViewById(R.id.tvShort);
            TextView tvName = view.findViewById(R.id.tvName);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(member.getPhoto()))
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            tvShort.setVisibility(View.VISIBLE);
                            tvShort.setText(member.getName().substring(member.getName().length() - 1));
                        }
                    })
                    // other setters
                    .build();
            ivAvatar.setController(controller);
            tvName.setText(member.getName());
            view.setTag(member);
            view.setOnClickListener(clickListener);
            binding.flowlayout.addView(view);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Member member = (Member) view.getTag();
            Intent intent = new Intent(getContext(), BodyActivity.class);
            intent.putExtra("member", member);
            startActivity(intent);
        }
    };

    public void llOther(View view) {
        startActivityForResult(new Intent(this, AddMemberActivity.class), REQUEST_ADD_MEMBER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Member member = (Member) data.getSerializableExtra("member");
            Intent intent = new Intent(getContext(), BodyActivity.class);
            intent.putExtra("member", member);
            startActivity(intent);
        }
    }
}
