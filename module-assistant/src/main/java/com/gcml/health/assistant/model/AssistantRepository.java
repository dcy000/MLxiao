package com.gcml.health.assistant.model;

import com.gcml.health.assistant.model.entity.AbnormalEntity;
import com.gcml.health.assistant.model.entity.RecommendEntity;

import java.util.List;

import io.reactivex.Observable;

public class AssistantRepository {

    public Observable<List<AbnormalEntity>> abnormals() {
        return Observable.empty();
    }

    public Observable<List<RecommendEntity>> recommends() {
        return Observable.empty();
    }
}
