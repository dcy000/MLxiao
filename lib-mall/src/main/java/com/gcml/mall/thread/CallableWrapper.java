/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gcml.mall.thread;

import java.util.concurrent.Callable;

final class CallableWrapper<T> implements Callable<T> {
    private String name;
    private Callback callback;
    private Callable<T> proxy;

    CallableWrapper(Configs configs, Callable<T> proxy) {
        this.name = configs.name;
        this.proxy = proxy;
        this.callback = new CallbackDelegate(configs.callback, configs.deliver, configs.asyncCallback);
    }

    @Override
    public T call() throws Exception {
        Tools.resetThread(Thread.currentThread(),name,callback);
        if (callback != null) {
            callback.onStart(name);
        }

        // avoid NullPointException
        T t = proxy == null ? null : proxy.call();
        if (callback != null)  {
            callback.onCompleted(name);
        }
        return t;
    }
}
