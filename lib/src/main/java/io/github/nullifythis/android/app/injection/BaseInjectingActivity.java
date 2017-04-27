package io.github.nullifythis.android.app.injection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;

public abstract class BaseInjectingActivity extends ActionBarActivity implements InjectingComponent {

    public BaseInjectingActivity() {
        componentInjector = ComponentInjector.create(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        componentInjector.onCreate();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        componentInjector.onDestroy();
        super.onDestroy();
    }

    @Override
    public final void inject(@Nullable Object target) {
        componentInjector.inject(target);
    }

    @Override
    public final <T> T create(@NonNull Class<T> clazz) {
        return componentInjector.create(clazz);
    }

    @Override
    @NonNull
    public Object[] createComponentModules() {
        return new Object[]{ new ActivityModule(this) };
    }

    @NonNull
    @Override
    public ComponentInjector getComponentInjector() {
        return componentInjector;
    }

    private final ComponentInjector componentInjector;
}