package com.kayali_developer.bakingapp.api;

public interface ApiCallback<T> {
    void onResponse(T result);

    void onCancel();
}

