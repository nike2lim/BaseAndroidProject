package com.example.shlim.baseandroidproject.database.listener;

public interface DataOperationListener {
    public void done();
    public void error(Exception exception);
}