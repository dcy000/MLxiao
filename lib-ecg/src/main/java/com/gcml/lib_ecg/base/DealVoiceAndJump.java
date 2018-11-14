package com.gcml.lib_ecg.base;

public interface DealVoiceAndJump {
    void updateVoice(String voice);
    void jump2HealthHistory(int measureType);
    void jump2DemoVideo(int measureType);
}
