package org.ms.matrix.app.ui.activity.login;

import android.util.Log;

import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.ms.matrix.app.MatrixClient;
import org.ms.matrix.app.db.User;
import org.ms.matrix.app.db.UserDbInjection;
import org.ms.module.base.module.BaseModel;
import org.ms.module.supper.client.Modules;

public class LoginActivityModel extends BaseModel<LoginActivityPresenter> implements ILoginActivity {


    private static final String TAG = "LoginActivityModel";

    public LoginActivityModel(LoginActivityPresenter presenter) {
        super(presenter);
    }

    @Override
    public void queryLatestUser() {


        Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
            @Override
            public void run() {
                User user = UserDbInjection.providerUserDataSource().queryLatestUser();
                queryLatestUserCallBack(user);
            }
        });

    }

    @Override
    public void queryLatestUserCallBack(User user) {

        presenter.queryLatestUserCallBack(user);
    }

    @Override
    public void login(String username, String password) {

        Log.e(TAG, "login: " + username);
        MatrixClient.getInstance().login(username, password, new ApiCallback<Credentials>() {
            @Override
            public void onNetworkError(Exception e) {

                e.printStackTrace();
            }

            @Override
            public void onMatrixError(MatrixError e) {


                Modules.getLogModule().e(TAG, e.getMessage());
            }

            @Override
            public void onUnexpectedError(Exception e) {


                e.printStackTrace();

            }

            @Override
            public void onSuccess(Credentials info) {

                String accessToken = info.getAccessToken();
                String deviceId = info.getDeviceId();
                String homeServer = info.getHomeServer();
                String userId = info.getUserId();


                Modules.getLogModule().e(TAG, "accessToken : " + accessToken);
                Modules.getLogModule().e(TAG, "deviceId : " + deviceId);
                Modules.getLogModule().e(TAG, "homeServer : " + homeServer);
                Modules.getLogModule().e(TAG, "userId : " + userId);


                Modules.getUtilsModule().getThreadPoolUtils().runSubThread(new Runnable() {
                    @Override
                    public void run() {

                        User user = new User();
                        user._access_token = accessToken;
                        user._device_id = deviceId;
                        user._home_server = homeServer;
                        user._password = password;
                        user._username = username;
                        user._timestamp = System.currentTimeMillis();

                        UserDbInjection.providerUserDataSource().insert(user);

                    }
                });


                onLoginCallBack();

            }
        });
    }

    @Override
    public void onLoginCallBack() {


        presenter.onLoginCallBack();

    }
}
