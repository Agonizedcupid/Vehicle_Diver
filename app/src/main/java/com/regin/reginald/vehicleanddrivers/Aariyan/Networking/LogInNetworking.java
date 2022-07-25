package com.regin.reginald.vehicleanddrivers.Aariyan.Networking;

import android.util.Log;

import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.ApiClient;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.LogInInterface;
import com.regin.reginald.vehicleanddrivers.Aariyan.Interface.RestApi;
import com.regin.reginald.vehicleanddrivers.Aariyan.Model.LogInModel;
import com.regin.reginald.vehicleanddrivers.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LogInNetworking {

    private CompositeDisposable logInDisposable = new CompositeDisposable();
    RestApi api = ApiClient.getLogInClient().create(RestApi.class);

    List<LogInModel> list = new ArrayList<>();

    public void logIn(LogInInterface logInInterface, String userEmail, String userPassword, DatabaseAdapter databaseAdapter) {
        logInDisposable.add(api.getLogInData(userEmail, userPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray root = new JSONArray(responseBody.string());
                        if (root.length() > 0) {
                            list.clear();
                            JSONObject single = root.getJSONObject(0);
                            int id = single.getInt("ID");
                            String companyName = single.getString("CompanyName");
                            String tabletId = single.getString("TabletRegID");
                            String driverName = single.getString("DriverName");
                            String driverEmail = single.getString("DriverLoginName");
                            String driverPassword = single.getString("DriverPassword");
                            int groupId = single.getInt("GroupID");
                            String ip = single.getString("IP");
                            LogInModel model = new LogInModel(id, companyName, tabletId, driverName, driverEmail, driverPassword, groupId, ip);
                            logInInterface.loggedIn(model);
                            list.add(model);
                            insertIntoLocal(list, databaseAdapter);
                        } else {
                            logInInterface.error("Invalid Credential!");
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        logInInterface.error(throwable.getMessage());
                    }
                }));
    }

    private void insertIntoLocal(List<LogInModel> list, DatabaseAdapter databaseAdapter) {
        databaseAdapter.dropLogInTable();
        Observable observable = Observable.fromIterable(list).subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                LogInModel model = (LogInModel) o;
                databaseAdapter.insertLogIn(model);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("INSERT_LOGIN", "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d("INSERT_LOGIN", "onComplete: ");
            }
        };

        observable.subscribe(observer);
    }

}
