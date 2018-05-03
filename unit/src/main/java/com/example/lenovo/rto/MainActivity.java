package com.example.lenovo.rto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.lenovo.rto.unit.Unit;
import com.example.lenovo.rto.unit.UnitModel;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
import static com.example.lenovo.rto.Constans.SCENE_Id;


public class MainActivity extends AppCompatActivity implements HttpListener<AccessToken> {

    private AccessToken data = new AccessToken();
    private EditText request;
    private String sessionId = "";
    private TextView result;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAToken();
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        request = findViewById(R.id.request);
        result = findViewById(R.id.result);
    }


    private void initAToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(this);
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {

    }


    private void sendMessage() {
        data = EHSharedPreferences.ReadAccessToken(ACCESSTOKEN_KEY);
        UnitModel model = new UnitModel();
        model.getUnit(data.getAccessToken(), SCENE_Id, request.getText().toString(), sessionId, new HttpListener<Unit>() {

            @Override
            public void onSuccess(Unit data) {
                if (data != null)
                    sessionId = data.getSession_id();
                for (Unit.ActionListBean action : data.getAction_list()) {

                    if (!TextUtils.isEmpty(action.getSay())) {
                        sb = new StringBuilder();
                        sb.append(action.getSay());
                    }

                }
                result.setText(sb.toString());

            }

            @Override
            public void onError() {

            }
        });
    }
}
