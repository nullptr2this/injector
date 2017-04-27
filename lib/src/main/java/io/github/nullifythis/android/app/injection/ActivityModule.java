package io.github.nullifythis.android.app.injection;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class ActivityModule {

    public ActivityModule(@NonNull Activity activity) {
        this.activity = activity;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Provides
    @Singleton
    @ComponentScope
    public Context provideComponentContext() {
        return activity;
    }

    @SuppressWarnings("unused")
    @NonNull
    @Provides
    @Singleton
    @ActivityScope
    public Context provideActivityContext() {
        return activity;
    }

    @NonNull
    private final Activity activity;
}