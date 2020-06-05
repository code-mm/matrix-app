package org.ms.matrix.app;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.MXFileStore;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.ms.matrix.app.ui.activity.login.LoginActivity;
import org.ms.module.supper.client.Modules;

import java.util.Collection;

public class MatrixClient {


    private static final String TAG = "MatrixClient";
    private static final MatrixClient instance = new MatrixClient();
    private HomeServerConnectionConfig homeServerConnectionConfig;
    private MXSession session;
    private LoginRestClient loginRestClient;
    private RoomsRestClient roomsRestClient;

    public static MatrixClient getInstance() {
        return instance;
    }


    public void init() {
        homeServerConnectionConfig = new HomeServerConnectionConfig.Builder()
                .withHomeServerUri(Uri.parse("https://matrix.mhw828.com"))
                .build();
    }


    public void login(String username, String password, ApiCallback<Credentials> callBack) {

        loginRestClient = new LoginRestClient(homeServerConnectionConfig);
        loginRestClient.loginWithUser(username, password, new ApiCallback<Credentials>() {
            @Override
            public void onNetworkError(Exception e) {

                if (callBack != null) {
                    callBack.onNetworkError(e);
                }
            }

            @Override
            public void onMatrixError(MatrixError e) {
                if (callBack != null) {
                    callBack.onMatrixError(e);
                }
            }

            @Override
            public void onUnexpectedError(Exception e) {
                if (callBack != null) {
                    callBack.onUnexpectedError(e);
                }
            }

            @Override
            public void onSuccess(Credentials info) {
                homeServerConnectionConfig.setCredentials(info);
                MXFileStore store = new MXFileStore(homeServerConnectionConfig, false, Modules.getDataModule().getAppData().getApplication());
                session = new MXSession.Builder(homeServerConnectionConfig, new MXDataHandler(store, info), Modules.getDataModule().getAppData().getApplication())
                        .build();

                if (callBack != null) {
                    callBack.onSuccess(info);
                }

            }
        });
    }


    public void getRooms() {
        Log.e(TAG, "getRooms: " );

        session.startEventStream(null);

        Collection<User> users = session.getDataHandler().getStore().getUsers();



        roomsRestClient = new RoomsRestClient(homeServerConnectionConfig);
        Collection<Room> rooms = session.getDataHandler().getStore().getRooms();

        for (Room itemRoom : rooms) {
            String roomId = itemRoom.getRoomId();

            Log.e(TAG, "getRooms roomId: "+roomId );
        }


    }

}
