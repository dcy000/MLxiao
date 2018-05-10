package com.medlink.danbogh.cache.exception;

/**
 * Created by lenovo on 2018/5/9.
 */

public class UserExistException extends RuntimeException {
    public UserExistException(String idCard) {
        super(String.format("User %s Exist", idCard));
    }
}
