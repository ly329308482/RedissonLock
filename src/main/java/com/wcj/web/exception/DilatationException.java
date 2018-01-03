package com.wcj.web.exception;

/**
 * Created by chengjie.wang on 2018/1/2.
 */
public class DilatationException extends Exception {

    public DilatationException(){
        super();
    }

    public DilatationException(String message){
        super(message);
    }

    public DilatationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
