package com.medlink.danbogh.cache.exception;

/**
 * Created by lenovo on 2018/5/9.
 */

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String idCard) {
        super(String.format("User %s not exist", idCard));
    }
}
