package com.ml.edu.data;

/**
 * Created by afirez on 18-2-6.
 */

public class ApiException extends RuntimeException {
    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }
}
