package io.github.nullifythis.android.app.injection;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ApplicationModule {

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Provides
    @Singleton
    @ApplicationScope
    public Context provideApplicationContext() {
        return application;
    }

    @NonNull
    private final Application application;
}