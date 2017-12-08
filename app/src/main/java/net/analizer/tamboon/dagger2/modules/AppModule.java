package net.analizer.tamboon.dagger2.modules;

import net.analizer.datalayer.api.ApiImp;
import net.analizer.domainlayer.api.ApiInterface;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppModule {

    @Singleton
    @Binds
    public abstract ApiInterface provideApiInterface(ApiImp apiImp);
}
