/**
 * BCBill.java
 *
 * Created by xuanzhui on 2015/7/29.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.beecloud.entity;

/**
 *  支付订单信息
 */
public class BCBillOrder extends BCOrder{
    /**
     * 以下命名需要和restful API匹配
     * 以便于Gson反序列化
     * 请忽略命名规则
     */

    //渠道返回的交易号，未支付成功时，是不含该参数的
    private String trade_no;

    //订单是否成功
    private Boolean spay_result;

    //订单是否被撤销
    private Boolean revert_result;

    //订单是否已经退款
    private Boolean refund_result;

    /**
     * @return  渠道返回的交易号，未支付成功时，无效
     */
    public String getTradeNum() {
        return trade_no;
    }

    /**
     * @return  true表示订单支付成功, false表示尚未支付
     */
    public Boolean getPayResult() {
        return spay_result;
    }

    /**
     * 用于线下产品,订单是否被撤销
     * @return  true表示订单已经被撤销
     */
    public Boolean getRevertResult() {
        return revert_result;
    }

    /**
     * @return  订单创建时间, 毫秒时间戳, 13位
     */
    public Long getCreatedTime() {
        return create_time;
    }

    /**
     * @return  true表示该支付订单已经退款成功
     */
    public Boolean getRefundResult() {
        return refund_result;
    }
}
