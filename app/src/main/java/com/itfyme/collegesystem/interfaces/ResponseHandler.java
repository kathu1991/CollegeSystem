package com.itfyme.collegesystem.interfaces;

public interface ResponseHandler {
    public void onSuccess(Object data) ;
    public void onFail(Object data) ;
    public void onNoData(Object data) ;
}
