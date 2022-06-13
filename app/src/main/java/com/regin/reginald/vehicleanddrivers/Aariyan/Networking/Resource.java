package com.regin.reginald.vehicleanddrivers.Aariyan.Networking;

import androidx.annotation.NonNull;

public class Resource<T> {
    public final Status status;
    public final T response;
    public final String errorMessage;

    public Resource(Status status, T response, String errorMessage) {
        this.status = status;
        this.response = response;
        this.errorMessage = errorMessage;
    }

    //Handle the success
    public static <T> Resource<T> success(@NonNull T response) {
        return new Resource<>(Status.SUCCESS, response, null);
    }

    //It will handle the error
    public static <T> Resource<T> error(String message, @NonNull T response) {
        return new Resource<>(Status.ERROR, response, message);
    }

    //Handle the loading
    public static <T> Resource<T> loading(@NonNull T response) {
        return new Resource<>(Status.LOADING, response, null);
    }

    //Creating Enum:
    public enum Status {SUCCESS, ERROR, LOADING}
}
