
package org.ms.matrix.app;


import android.app.Application;

import org.ms.module.supper.client.Modules;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MatrixClient.getInstance().init();

        Modules.getControlSwitch().setLogOut(true);
        Modules.getControlSwitch().setRequestLog(true);
        Modules.getControlSwitch().setPrintStackTrace(true);
        Modules.getLogModule().setAliyunSend(false);
    }
}