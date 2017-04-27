package io.github.nullifythis.android.app.injection;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import dagger.ObjectGraph;

public abstract class BaseInjectingApplication extends Application implements Injector {

    @Override
    public final void inject(@Nullable Object target) {
        if (null != target) {
            throwIfObjectGraphNotConfigured();
            objectGraph.inject(target);
        }
    }

    @Override
    @NonNull
    public final <T> T create(@NonNull Class<T> clazz) {
        throwIfObjectGraphNotConfigured();
        return objectGraph.get(clazz);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configureObjectGraph();
        if (requiresInjection()) {
            inject(this);
        }
    }

    @NonNull
    protected abstract Object createApplicationModule();
    protected abstract boolean requiresInjection();

    @NonNull
    ObjectGraph createObjectGraph(Object applicationModule) {
        return ObjectGraph.create(new Object[]{ applicationModule });
    }

    @NonNull
    final ObjectGraph getObjectGraph() {
        return objectGraph;
    }

    private void configureObjectGraph() {
        if (null == objectGraph) {
            objectGraph = createObjectGraph(createApplicationModule());
        }
    }

    private void throwIfObjectGraphNotConfigured() {
        if (null == objectGraph) {
            throw new IllegalStateException("ObjectGraph has not been created.");
        }
    }

    private ObjectGraph objectGraph;
}