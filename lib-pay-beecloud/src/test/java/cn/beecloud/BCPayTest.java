package cn.beecloud;

import android.app.Activity;
import android.content.Intent;

import com.alipay.sdk.app.PayTask;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCQRCodeResult;
import cn.beecloud.entity.BCReqParams;
import cn.beecloud.entity.BCRestfulCommonResult;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BCHttpClientUtil.class, PayTask.class, BCCache.class})
public class BCPayTest {
    Activity activity;
    BCPay pay;
    CountDownLatch latch;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BeeCloud.setAppIdAndSecret("c5d1cba1-5e3f-4ba0-941d-9b0a371fe719",
                "39a7a518-9ac8-4a9e-87bc-7885f33cf18c");

        BCPay.initPayPal("paypal-id",
                "paypal-secret",
                BCPay.PAYPAL_PAY_TYPE.SANDBOX,
                Boolean.FALSE);
    }

    @Before
    public void setUp() throws Exception {
        activity = Mockito.mock(Activity.class);
        pay = BCPay.getInstance(activity);
        latch = new CountDownLatch(1);
    }

    /**
     * 测试微信初始化，由于测试环境没有微信app，所以预期的结果应该都是失败
     * @throws Exception
     */
    @Test
    public void testInitWechatPay() throws Exception {
        Object[][] test = new Object[][] {
                new Object[]{null, null},
                new Object[]{activity, null}
        };

        for (Object[] objects : test) {
            Assert.assertTrue(
                    BCPay.initWechatPay(
                            (Activity) objects[0],
                            (String) objects[1])
                            .startsWith("Error"));
        }

        //clear to make sure it will not affect later wechat related test case
        BCPay.detachWechat();
    }

    /**
     * 测试清理
     * @throws Exception
     */
    @Test
    public void testClear() throws Exception {
        BCPay.clear();

        Assert.assertEquals(null, BCPay.mContextActivity);
        Assert.assertEquals(null, BCPay.payCallback);
        Assert.assertEquals(null, BCPay.payPalSyncObserver);
    }

    /**
     * #1
     * 测试参数错误
     * @throws Exception
     */
    @Test
    public void testReqPaymentAsyncParamsInvalid() throws Exception {
        Object[][] test = new Object[][]{
                //依次对应必填参数 渠道类型 订单标题 订单金额 订单流水号
                new Object[]{BCReqParams.BCChannelTypes.JD_WAP, null, null, null},
                new Object[]{BCReqParams.BCChannelTypes.WX_APP, null, null, null},
                new Object[]{BCReqParams.BCChannelTypes.ALI_APP, "试一个很长的title--参数公共返回参数取值及含义参见支付公共返回参数部分", null, null},
                new Object[]{BCReqParams.BCChannelTypes.UN_APP, "正常title", null, null},
                new Object[]{BCReqParams.BCChannelTypes.BD_APP, "正常title", -1, null},
                new Object[]{BCReqParams.BCChannelTypes.PAYPAL_LIVE, "正常title", 3, null},
                new Object[]{BCReqParams.BCChannelTypes.PAYPAL_SANDBOX, "正常title", 3, "123"},
        };

        for (Object[] objects : test) {
            BCPay.PayParams payParams = new BCPay.PayParams();
            payParams.channelType = (BCReqParams.BCChannelTypes)objects[0];
            payParams.billTitle = (String)objects[1];
            payParams.billTotalFee = (Integer)objects[2];
            payParams.billNum = (String)objects[3];

            final CountDownLatch localLatch = new CountDownLatch(1);

            pay.reqPaymentAsync(payParams,
                    new BCCallback() {
                        @Override
                        public void done(BCResult result) {
                            Assert.assertTrue(result instanceof BCPayResult);

                            BCPayResult payResult = (BCPayResult)result;

                            Assert.assertEquals(BCPayResult.RESULT_FAIL, payResult.getResult());

                            //渠道错误由BCException抛出，其他正常参数异常错误
                            Assert.assertTrue(payResult.getErrCode() == BCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE ||
                                    payResult.getErrCode() == BCPayResult.APP_INTERNAL_PARAMS_ERR_CODE);

                            Assert.assertTrue(payResult.getErrMsg().equals(BCPayResult.FAIL_EXCEPTION) ||
                                    payResult.getErrMsg().equals(BCPayResult.FAIL_INVALID_PARAMS));

                            //System.out.println(payResult.getDetailInfo());

                            localLatch.countDown();
                        }
                    });

            localLatch.await(2000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * #2
     * 模拟网络400等异常
     * @throws Exception
     */
    @Test
    public void testReqPaymentAsyncNetworkError() throws Exception {
        final BCHttpClientUtil.Response response = new BCHttpClientUtil.Response();
        response.code = 400;
        response.content = "mocked";

        //mock network
        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "httpPost",
                String.class, Map.class)).toReturn(response);

        //prepare params
        final BCPay.PayParams payParams = new BCPay.PayParams();
        payParams.channelType = BCReqParams.BCChannelTypes.WX_APP;
        payParams.billTitle = "正常title";
        payParams.billTotalFee = 3;
        payParams.billNum = "123456789ABCDE";

        pay.reqPaymentAsync(payParams,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        Assert.assertTrue(result instanceof BCPayResult);

                        BCPayResult payResult = (BCPayResult) result;

                        Assert.assertEquals(BCPayResult.RESULT_FAIL, payResult.getResult());
                        Assert.assertEquals((Integer)BCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE,
                                payResult.getErrCode());
                        Assert.assertEquals(BCPayResult.FAIL_EXCEPTION,
                                payResult.getErrMsg());
                        //System.out.println(payResult.getDetailInfo());

                        latch.countDown();
                    }
                });

        latch.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * #3
     * 模拟网络200 但是存在后台参数配置问题导致的错误
     * @throws Exception
     */
    @Test
    public void testReqPaymentAsyncErrorFromServer() throws Exception {
        final BCHttpClientUtil.Response response = new BCHttpClientUtil.Response();
        response.code = 200;
        response.content = "{\"result_msg\":\"PAY_FACTOR_NOT_SET\",\"err_detail\":\"支付宝参数: partner, seller_id, RSA密钥未配置\",\"result_code\":2}";

        //mock network
        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "httpPost",
                String.class, Map.class)).toReturn(response);

        //prepare params
        final BCPay.PayParams payParams = new BCPay.PayParams();
        payParams.channelType = BCReqParams.BCChannelTypes.WX_APP;
        payParams.billTitle = "正常title";
        payParams.billTotalFee = 3;
        payParams.billNum = "123456789ABCDE";
        Map<String, String> analysis = new HashMap<String, String>();
        analysis.put("category","en");
        payParams.analysis = analysis;

        pay.reqPaymentAsync(payParams,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        Assert.assertTrue(result instanceof BCPayResult);

                        BCPayResult payResult = (BCPayResult) result;

                        Assert.assertEquals(BCPayResult.RESULT_FAIL, payResult.getResult());

                        //回调的错误信息与服务端保持一致
                        Assert.assertEquals((Integer) 2, payResult.getErrCode());
                        Assert.assertEquals("PAY_FACTOR_NOT_SET",
                                payResult.getErrMsg());
                        Assert.assertEquals("支付宝参数: partner, seller_id, RSA密钥未配置",
                                payResult.getDetailInfo());

                        latch.countDown();
                    }
                });

        latch.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * 模拟微信发起支付
     * @throws Exception
     */
    @Test
    public void testReqWXPaymentAsync() throws Exception {
        final BCHttpClientUtil.Response response = new BCHttpClientUtil.Response();
        response.code = 200;
        response.content = "{\"nonce_str\":\"20151119114011235\",\"package\":\"Sign=WXPay\",\"resultCode\":0,\"errMsg\":\"OK:\",\"prepay_id\":\"wx20151119114008765ce0ad440384384408\",\"result_msg\":\"OK\",\"partner_id\":\"1240421502\",\"err_detail\":\"\",\"result_code\":0,\"id\":\"ff5fef0c-93bd-46d2-8500-5ed53502d344\",\"app_id\":\"wxf1aa465362b4c8f1\",\"pay_sign\":\"D0F8E79A14731B59CE04B634FD588C35\",\"timestamp\":\"1447904408\"}";

        //mock network
        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "httpPost",
                String.class, Map.class)).toReturn(response);

        Map<String,String> optional = new HashMap<String, String>();
        optional.put("aya", "啊呀");

        pay.reqWXPaymentAsync("正常title",
                22,
                "123456789ABCDE",
                optional,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        Assert.assertTrue(result instanceof BCPayResult);

                        BCPayResult payResult = (BCPayResult) result;

                        //对于微信的支付由于无法注册，预期为失败
                        Assert.assertEquals(BCPayResult.RESULT_FAIL, payResult.getResult());

                        Assert.assertEquals((Integer) BCPayResult.APP_INTERNAL_EXCEPTION_ERR_CODE,
                                payResult.getErrCode());
                        Assert.assertEquals(BCPayResult.FAIL_EXCEPTION,
                                payResult.getErrMsg());
                        Assert.assertTrue(payResult.getDetailInfo().startsWith("Error: 微信API为空"));
                        System.out.println(payResult.getDetailInfo());

                        Assert.assertEquals("ff5fef0c-93bd-46d2-8500-5ed53502d344",
                                BCCache.getInstance().billID);

                        latch.countDown();
                    }
                });

        latch.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * 模拟银联发起支付
     * @throws Exception
     */
    @Test
    public void testReqUnionPaymentAsync() throws Exception {
        final BCHttpClientUtil.Response response = new BCHttpClientUtil.Response();
        response.code = 200;
        response.content = "{\"result_msg\":\"OK\",\"err_detail\":\"\",\"result_code\":0,\"tn\":\"201511191446391024558\",\"id\":\"b1841106-639f-48d2-bfbe-5086b5dddc31\"}";

        //mock network
        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "httpPost",
                String.class, Map.class)).toReturn(response);

        pay.reqUnionPaymentAsync("正常title",
                1,
                "123456789ABCDE",
                null,
                Mockito.mock(BCCallback.class));

        //has to wait until child thread exits before verification as no callback is returned in this case
        Thread.sleep(1000);

        //startActivity should be called in reqUnionPaymentViaAPP
        Mockito.verify(activity, Mockito.times(1)).startActivity(Mockito.any(Intent.class));

        Assert.assertEquals("b1841106-639f-48d2-bfbe-5086b5dddc31",
                BCCache.getInstance().billID);
    }

    /**
     * 模拟paypal支付
     * @throws Exception
     */
    @Test
    public void testReqPayPalPaymentAsync() throws Exception {
        pay.reqPayPalPaymentAsync("billnum",
                111,
                "USD",
                null,
                Mockito.mock(BCCallback.class));

        //startActivity should be called in reqPayPalPaymentAsync
        Mockito.verify(activity, Mockito.times(1)).startActivity(Mockito.any(Intent.class));
    }

    /**
     * 模拟paypal支付Test Mode
     * @throws Exception
     */
    @Test
    public void testReqPayPalPaymentAsyncTestMode() throws Exception {
        BeeCloud.setSandbox(true);

        pay.reqPayPalPaymentAsync("billnum",
                111,
                "USD",
                null,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        Assert.assertTrue(result instanceof BCPayResult);
                        BCPayResult payResult = (BCPayResult) result;

                        Assert.assertEquals(BCPayResult.RESULT_FAIL, payResult.getResult());
                        Assert.assertEquals((Integer) BCPayResult.APP_INTERNAL_PARAMS_ERR_CODE,
                                payResult.getErrCode());
                        Assert.assertTrue(payResult.getDetailInfo()
                                .startsWith("PayPal支付暂不支持通过BeeCloud.setSandbox设置测试模式"));

                        BeeCloud.setSandbox(false);
                    }
                });
    }

    /**
     * 模拟paypal客户端支付完成后向服务端发送支付结果
     * @throws Exception
     */
    @Test
    public void testSyncPayPalPaymentWithParams() throws Exception {
        final BCHttpClientUtil.Response tokenResp = new BCHttpClientUtil.Response();
        tokenResp.code = 200;
        tokenResp.content = "{\"access_token\":\"mocked-token\"}";

        //mock paypal token
        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "getPayPalAccessToken"))
                .toReturn(tokenResp);

        //mock sync
        final BCHttpClientUtil.Response syncResp = new BCHttpClientUtil.Response();
        syncResp.code = 200;
        syncResp.content = "{\"result_msg\":\"OK\",\"err_detail\":\"\",\"result_code\":0,\"id\":\"0b1c193c-7aef-484d-985a-e3c7bcdb86b3\"}";

        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "httpPost",
                String.class, Map.class)).toReturn(syncResp);

        BCPayResult res = pay.syncPayPalPayment("billTitle",
                1111,
                "billNum123456",
                "USD",
                null);

        //System.out.println(res[1]);
        Assert.assertEquals(BCPayResult.RESULT_SUCCESS, res.getResult());
        Assert.assertEquals("0b1c193c-7aef-484d-985a-e3c7bcdb86b3", res.getId());
    }

    /**
     * 模拟paypal客户端支付完成后向服务端发送支付结果
     * @throws Exception
     */
    @Test
    public void testSyncPayPalPaymentWithJsonStr() throws Exception {
        final BCHttpClientUtil.Response tokenResp = new BCHttpClientUtil.Response();
        tokenResp.code = 200;
        tokenResp.content = "{\"access_token\":\"mocked-token\"}";

        //mock paypal token
        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "getPayPalAccessToken"))
                .toReturn(tokenResp);

        //mock sync
        final BCHttpClientUtil.Response syncResp = new BCHttpClientUtil.Response();
        syncResp.code = 200;
        syncResp.content = "{\"result_msg\":\"OK\",\"err_detail\":\"\",\"result_code\":0,\"id\":\"0b1c193c-7aef-484d-985a-e3c7bcdb86b3\"}";

        PowerMockito.stub(PowerMockito.method(BCHttpClientUtil.class, "httpPost",
                String.class, Map.class)).toReturn(syncResp);

        PowerMockito.stub(PowerMockito.method(BCCache.class, "removeSyncedPalPalRecords")).toReturn(null);

        String jsonStr = "{\"storeDate\":\"2015-11-20 11:07:35\",\"billTitle\":\"PayPal payment test\",\"billNum\":\"1TF328031A3869241KZHI42I\",\"channel\":\"SANDBOX\",\"currency\":\"USD\",\"optional\":\"{\\\"PayPal key2\\\":\\\"PayPal value2\\\",\\\"PayPal key1\\\":\\\"PayPal value1\\\"}\",\"billTotalFee\":\"1\"}";

        BCPayResult res = pay.syncPayPalPayment(jsonStr);

        Assert.assertEquals(BCPayResult.RESULT_SUCCESS, res.getResult());
        Assert.assertEquals("0b1c193c-7aef-484d-985a-e3c7bcdb86b3", res.getId());
    }
}
