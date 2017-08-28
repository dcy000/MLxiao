package com.megvii.faceppidcardui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.megvii.faceppidcardui.util.ConstantData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;
    public EditText mEditText1;
    public EditText mEditText2;
    public EditText mEditText3;
    public ImageView mImageView;
    public String doctors;
    public String phones;
    public String addres;
    public byte[] imageData;
    public String mAuthid;
    public String names;
    public String id_card_number;
    public String genders;
    public int age;
    public ImageView mImageView1;
    int real;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    SharedPreferences.Editor editor;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showNormal("审核成功，恭喜你完成注册！");
                    Log.e("sharedPreferences1", sharedPreferences1.getString("mAuthid", ""));
                    Log.e("sharedPreferences1", sharedPreferences2.getString("mAuthid", ""));

                    if (sharedPreferences1.getString("mAuthid", "").equals("")) {
                        editor = sharedPreferences1.edit();
                        update();
                        Log.e("==============", "sharedPreferences1");
                        editor = sharedPreferences4.edit();
                        update();
                        Log.e("==============", "sharedPreferences4");

                    } else if (sharedPreferences2.getString("mAuthid", "").equals("")) {
                        editor = sharedPreferences2.edit();
                        update();
                        Log.e("==============", "sharedPreferences2");

                    } else {
                        editor = sharedPreferences3.edit();
                        update();
                    }


                    break;
                case 1:
                    showNormal("审核失败");
                    break;
            }
            super.handleMessage(msg);
        }

        public void update() {
            editor.putString("doctor", doctors);
            editor.putString("phone", phones);
            editor.putString("address", addres);
            editor.putString("mAuthid", sharedPreferences.getString("mAuthid", ""));
            editor.putString("imageData", sharedPreferences.getString("imageData", ""));
            editor.putString("name", sharedPreferences.getString("name", ""));
            editor.putString("id_card_number", sharedPreferences.getString("id_card_number", ""));
            editor.putString("gender", sharedPreferences.getString("gender", ""));
            editor.putString("age", real + "");
            editor.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTextView1 = (TextView) findViewById(R.id.name);
        mTextView2 = (TextView) findViewById(R.id.gender);
        mTextView3 = (TextView) findViewById(R.id.age);
        mEditText1 = (EditText) findViewById(R.id.doctor_id);
        mEditText2 = (EditText) findViewById(R.id.phone);
        mEditText3 = (EditText) findViewById(R.id.address);
        mImageView = (ImageView) findViewById(R.id.submit);
        mImageView1 = (ImageView) findViewById(R.id.register_image);

        mImageView.setEnabled(false);
        sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(ConstantData.SHARED_FILE_NAME1, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(ConstantData.SHARED_FILE_NAME2, Context.MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences(ConstantData.SHARED_FILE_NAME3, Context.MODE_PRIVATE);
        sharedPreferences4 = getSharedPreferences(ConstantData.SHARED_FILE_NAME4, Context.MODE_PRIVATE);

       /* String imageData1 = sharedPreferences.getString("imageData", "");
        imageData = imageData1.getBytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
*/
        String imageData1 = sharedPreferences.getString("imageData", "");
        byte[] bytes = Base64.decode(imageData1.getBytes(), 1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        mAuthid = sharedPreferences.getString("mAuthid", "");
        names = sharedPreferences.getString("name", "");
        id_card_number = sharedPreferences.getString("id_card_number", "");
        genders = sharedPreferences.getString("gender", "");
        String birthday = sharedPreferences.getString("birthday", "");
        String str[] = birthday.split("-");
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy");
        age = Integer.parseInt(str[0]);
        int year = Integer.parseInt(simple.format(date));
        real = year - age;
        mTextView1.setText("姓名：" + names);
        mTextView2.setText("性别：" + genders);
        mTextView3.setText("年龄：" + real);

        mImageView1.setImageBitmap(bitmap);

        mEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doctors = mEditText1.getText().toString().replaceAll(" ", "");
                phones = mEditText2.getText().toString().replaceAll(" ", "");
                addres = mEditText3.getText().toString().replaceAll(" ", "");

                if (!TextUtils.isEmpty(doctors) && !TextUtils.isEmpty(phones) && !TextUtils.isEmpty(addres)) {
                    mImageView.setEnabled(true);
                } else {
                    mImageView.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        mEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                doctors = mEditText1.getText().toString().replaceAll(" ", "");
                phones = mEditText2.getText().toString().replaceAll(" ", "");
                addres = mEditText3.getText().toString().replaceAll(" ", "");

                if (!TextUtils.isEmpty(doctors) && !TextUtils.isEmpty(phones) && !TextUtils.isEmpty(addres)) {
                    mImageView.setEnabled(true);
                } else {
                    mImageView.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        mEditText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                doctors = mEditText1.getText().toString().replaceAll(" ", "");
                phones = mEditText2.getText().toString().replaceAll(" ", "");
                addres = mEditText3.getText().toString().replaceAll(" ", "");

                if (!TextUtils.isEmpty(doctors) && !TextUtils.isEmpty(phones) && !TextUtils.isEmpty(addres)) {
                    mImageView.setEnabled(true);
                } else {
                    mImageView.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormal("审核中，请稍后.....");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            post();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                //    showNormal();
            }
        });


    }

    private void post() throws Exception {
        // 创建URL对象
        URL url = new URL(ConstantData.BASE_URL + "/referralProject/RegisterServlet");
        // 获取该URL与服务器的连接对象
        URLConnection conn = url.openConnection();
        // 设置头信息，请求头信息了解
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");

        // 设置可以操作连接的输入输出流
        conn.setDoOutput(true);// 默认为false，允许使用输出流
        conn.setDoInput(true);// 默认为true，允许使用输入流


        // 传参数
        PrintWriter pw = new PrintWriter(conn.getOutputStream());
        pw.print("datas=" + mAuthid + "," + names + "," + genders + "," + real + "," + id_card_number + "," + phones + "," + addres + "," + doctors);
        pw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String lineContent = null;
        String content = null;

        while ((lineContent = br.readLine()) != null) {
            content = lineContent;
            Log.e("=========", content);
        }

        if (content.equals("0")) {
            mHandler.sendEmptyMessage(0);

        } else {
            mHandler.sendEmptyMessage(1);


        }
        pw.close();
        br.close();

    }

    NDialog dialog = new NDialog(this);

    public void showNormal(String message) {

        dialog.setMessageCenter(true)
                .setMessage(message)
                .setMessageSize(25)
                .setButtonCenter(false)
                .setButtonSize(25)
                .setPositiveTextColor(Color.parseColor("#0000FF"))
                .setCancleable(false)
                .setOnConfirmListener(new NDialog.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        dialog.create(NDialog.CONFIRM).dismiss();
                        finish();
                    }
                }).create(NDialog.CONFIRM).show();


        /*final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("审核成功，恭喜你完成注册！");

        builder.setPositiveButton("确定", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.cancel();
                    }
                }
        );
        builder.show();*/


    }

}
