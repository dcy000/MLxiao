package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.XfGroupInfo;
import com.example.han.referralproject.facerecognition.FaceAuthenticationUtils;
import com.example.han.referralproject.facerecognition.IDeleteGroupListener;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.medlink.danbogh.utils.Handlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gzq on 2018/3/15.
 */

public class DeleteGroupActivity extends BaseActivity implements View.OnClickListener, IDeleteGroupListener {
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_delete2)
    Button btnDelete2;
    @BindView(R.id.btn_delete3)
    Button btnDelete47;
    private List<XfGroupInfo> lists;
    private int deleteSum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group);
        ButterKnife.bind(this);
        btnDelete.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnDelete2.setOnClickListener(this);
        btnDelete47.setOnClickListener(this);
        findViewById(R.id.btn_delete_ip).setOnClickListener(this);
        lists = new ArrayList<>();
    }

    int i = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_ip:
                String ip = ((EditText) findViewById(R.id.et_ip)).getText().toString().trim();
                NetworkApi.getXfGroupInfo(ip, "0", "0", new NetworkManager.SuccessCallback<ArrayList<XfGroupInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<XfGroupInfo> response) {
                        lists.addAll(response);
                        text.append("获取完成。总共需要检验数据：" + response.size());
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);

                    }
                });
                break;

            case R.id.btn_delete2:
                NetworkApi.getXfGroupInfo("116", "0", "0", new NetworkManager.SuccessCallback<ArrayList<XfGroupInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<XfGroupInfo> response) {
                        lists.addAll(response);
                        text.append("获取完成。总共需要检验数据：" + response.size());
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);

                    }
                });
                break;
            case R.id.btn_delete:
                NetworkApi.getXfGroupInfo("118", "0", "0", new NetworkManager.SuccessCallback<ArrayList<XfGroupInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<XfGroupInfo> response) {
                        lists.addAll(response);
                        text.append("获取完成。总共需要检验数据：" + response.size());
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);
                    }
                });

                break;
            case R.id.btn_delete3:
                NetworkApi.getXfGroupInfo("47", "0", "0", new NetworkManager.SuccessCallback<ArrayList<XfGroupInfo>>() {
                    @Override
                    public void onSuccess(ArrayList<XfGroupInfo> response) {
                        lists.addAll(response);
                        text.append("获取完成。总共需要检验数据：" + response.size());
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                        FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);
                    }
                });

                break;
            case R.id.btn_stop:
//                FaceAuthenticationUtils.getInstance(this).cancelIdentityVerifier();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory(), "faces.json");
                        lists.clear();
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                            StringBuilder builder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                builder.append(line);

//                                String[] tmp = line.split(" : ");
//                                String groupId = tmp[1];
//                                XfGroupInfo groupInfo = new XfGroupInfo();
//                                groupInfo.gid = groupId;
//                                lists.add(groupInfo);
                            }

                            String faces = builder.toString();
                            JSONArray jsonArray = new JSONArray(faces);
                            int length = jsonArray.length();
                            for (int j = 0; j < length; j++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    String groupId = jsonObject.optInt("groupid") + "";
                                    XfGroupInfo groupInfo = new XfGroupInfo();
                                    groupInfo.gid = groupId;
                                    lists.add(groupInfo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text.append("获取完成。总共需要检验数据：" + lists.size());
                                }
                            });
                            FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                            FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (reader != null) {
                                    reader.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
                break;
        }
    }

    @Override
    public void onResult(IdentityResult result, boolean islast) {
        text.append(result.getResultString() + "\n");
        i++;
        deleteSum++;
        Handlers.bg().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i < 1000) {
                    FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                    FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);
                } else {
                    Handlers.bg().removeCallbacksAndMessages(null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText("总共删除" + deleteSum);
                        }
                    });
                }

            }
        }, 1000);
    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

    }

    @Override
    public void onError(SpeechError error) {
        text.append(error.getPlainDescription(true) + "\n");
        i++;
        Handlers.bg().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i < 1000) {
                    FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).deleteGroup(lists.get(i).gid, lists.get(i).xfid);
                    FaceAuthenticationUtils.getInstance(DeleteGroupActivity.this).setOnDeleteGroupListener(DeleteGroupActivity.this);
                } else {
                    Handlers.bg().removeCallbacksAndMessages(null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText("总共删除" + deleteSum);
                        }
                    });
                }
            }
        }, 1000);

    }
}
