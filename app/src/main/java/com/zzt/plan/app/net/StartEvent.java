package com.zzt.plan.app.net;

/**
 * Created by zzt on 15-6-16.
 */
public class StartEvent {
    public StartEvent() {
    }

    public interface SuccessCallback {

    }

    public interface FailCallback {
        void onFail();

        void onFail(int code);
    }
}
