package com.gcml.testpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextClock;
import android.widget.TextView;

import com.zzhoujay.richtext.RichText;

public class MainActivity extends AppCompatActivity {
    private final String htmltext = "<form name=\"punchout_form\" method=\"post\" action=\"https://openapi.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=dJKcb2u9ABooIsiDZ0z7yZ0cDrAoKZGcpaJq%2BFW5sdHeZrAeovpNQzSze%2FcuGfLmuVBq7eAegNrJ%2F5dRJ2qayh53rCTbGIeBwUD62HPMDl7XgzWXi37ilgGj8vcGQsZyEsIiYZfwTMXTssO8m%2FF%2FaGwW6JAJsiE54IMQyhulqxxyCpFstSSu%2BgxvTSVXoSs4tQp6H84izwbK7RA0m1tyaKIGYtqDIbtB89UfvnQJMMS4X3Ll8fX9DiKnl9uYyeCPiMbWFQfKkGS5tWqc6EWFOWUP9skXQCRkzPh5ihlUDtwTP5g9q7QwvRK4nrS7gczFfev27ryObf2%2FuulWPKq%2BFw%3D%3D&return_url=http%3A%2F%2Fw5a3dz.natappfree.cc%2Fsuccess.html&notify_url=http%3A%2F%2Fw5a3dz.natappfree.cc%2Forder%2Fsuccess&version=1.0&app_id=2016092700607153&sign_type=RSA2&timestamp=2019-04-22+14%3A34%3A29&alipay_sdk=alipay-sdk-java-3.1.0&format=json\">\n" +
            "<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;body&quot;:&quot;努比亚出品游戏手机&quot;,&quot;out_trade_no&quot;:&quot;dzcp5646165&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;,&quot;subject&quot;:&quot;红魔手机&quot;,&quot;timeout_express&quot;:&quot;10m&quot;,&quot;total_amount&quot;:&quot;10000&quot;}\">\n" +
            "<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n" +
            "</form>\n" +
            "<script>document.forms[0].submit();</script>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView viewById = (TextView) findViewById(R.id.tv_text);
        RichText.from(htmltext).into(viewById);

    }
}
