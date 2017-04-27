package io.github.nullifythis.android.app.injection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class BaseInjectingFragment extends Fragment implements InjectingComponent {

    public BaseInjectingFragment() {
        componentInjector = ComponentInjector.create(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        componentInjector.onCreate();
    }

    @Override
    public void onDestroy() {
        componentInjector.onDestroy();
        super.onDestroy();
    }

    @Override
    public final void inject(@Nullable Object target) {
        componentInjector.inject(target);
    }

    @Override
    @NonNull
    public final <T> T create(@NonNull Class<T> clazz) {
        return componentInjector.create(clazz);
    }

    @NonNull
    @Override
    public Object[] createComponentModules() {
        return new Object[0];
    }

    @NonNull
    @Override
    public ComponentInjector getComponentInjector() {
        return componentInjector;
    }

    private final ComponentInjector componentInjector;
}