/**
 * BCPayPalSyncObserver.java
 * <p/>
 * Created by xuanzhui on 2015/10/28.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.beecloud.async;

public interface BCPayPalSyncObserver {
    void onSyncSucceed(String id);

    /**
     * executed when sync failed
     * @param billInfo  un-synced bill information which is a json string,
     *                  you can de-serialize it as a map,
     *                  all keys can be "billTitle", "billTotalFee"(cents),
     *                  "optional"(inner map), "billNum",
     *                  "channel" (indicate SANDBOX or LIVE),
     *                  "storeDate"(inner usage, you can ignore),
     *                  "currency"(like USD)
     * @param failInfo  sync failed reason
     * @return          if true is returned then sdk will consider that
     *                  you have successfully dealt with the bill
     *                  and so not store the un-synced bill info into cache,
     *                  else store
     */
    boolean onSyncFailed(String billInfo, String failInfo);
}
