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

/**
 * A callback interface to notify user that the task's status.
 */
public interface Callback {

    /**
     * This method will be invoked when thread has been occurs an error.
     * @param threadName The running thread name
     * @param t The exception
     */
    void onError(String threadName, Throwable t);

    /**
     * notify user to know that it completed.
     * @param threadName The running thread name
     */
    void onCompleted(String threadName);

    /**
     * notify user that task start running
     * @param threadName The running thread name
     */
    void onStart(String threadName);
}
