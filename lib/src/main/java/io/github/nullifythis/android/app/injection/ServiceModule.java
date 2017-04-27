package io.github.nullifythis.android.app.injection;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ServiceModule {

    public ServiceModule(@NonNull Service service) {
        this.service = service;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Provides
    @Singleton
    @ComponentScope
    public Context provideComponentContext() {
        return service;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Provides
    @Singleton
    @ServiceScope
    public Context provideServiceContext() {
        return service;
    }

    @NonNull
    private final Service service;
}
