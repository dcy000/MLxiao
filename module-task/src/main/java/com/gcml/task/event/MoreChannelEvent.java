package com.gcml.task.event;

import com.gcml.task.bean.ChannelModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * desc:
 * author: wecent .
 * date: 2017/9/10 .
 */
public class MoreChannelEvent {
    public List<ChannelModel> selectedDatas;

    public List<ChannelModel> unSelectedDatas;

    public List<ChannelModel> allChannels;

    /**
     * 添加的第一个频道名称
     */
    public String firstChannelName;

    public MoreChannelEvent(List<ChannelModel> allChannels, String firstChannelName) {
        if (allChannels == null) return;
        this.allChannels = allChannels;
        this.firstChannelName = firstChannelName;

        selectedDatas = new ArrayList<>();
        unSelectedDatas = new ArrayList<>();

        Iterator iterator = allChannels.iterator();
        while (iterator.hasNext()) {
            ChannelModel channel = (ChannelModel) iterator.next();
            if (channel.getItemType() == ChannelModel.TYPE_MY || channel.getItemType() == ChannelModel.TYPE_OTHER) {
                iterator.remove();
            } else if (channel.getItemType() == ChannelModel.TYPE_MY_CHANNEL) {
                selectedDatas.add(channel);
            } else {
                unSelectedDatas.add(channel);
            }
        }
    }
}
