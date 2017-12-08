package net.analizer.tamboon;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import net.analizer.tamboon.dagger2.HasComponent;
import net.analizer.tamboon.dagger2.components.AppComponent;
import net.analizer.tamboon.dagger2.components.DaggerAppComponent;
import net.analizer.tamboon.dagger2.modules.NetModule;

public class TamboonApplication extends Application
        implements HasComponent<AppComponent> {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();
        initLeakCanary();
    }

    @Override
    public AppComponent getComponent() {
        return mAppComponent;
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .application(this)
                .context(getApplicationContext())
                .netModule(new NetModule(AppConfig.BASE_API))
                .build();
    }

    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }
}
