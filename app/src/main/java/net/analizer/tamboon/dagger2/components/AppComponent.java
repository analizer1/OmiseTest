package net.analizer.tamboon.dagger2.components;

import android.content.Context;

import net.analizer.domainlayer.api.ApiInterface;
import net.analizer.tamboon.CharityActivity;
import net.analizer.tamboon.TamboonApplication;
import net.analizer.tamboon.dagger2.modules.AppModule;
import net.analizer.tamboon.dagger2.modules.NetModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {

    void inject(CharityActivity charityActivity);

    ApiInterface apiInterface();

    @Component.Builder
    interface Builder {

        AppComponent build();

        @BindsInstance
        Builder application(TamboonApplication application);

        @BindsInstance
        Builder context(Context context);

        Builder netModule(NetModule netModule);
    }
}
