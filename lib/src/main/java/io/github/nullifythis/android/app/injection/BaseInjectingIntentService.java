package io.github.nullifythis.android.app.injection;

import android.app.IntentService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BaseInjectingIntentService extends IntentService
        implements InjectingComponent {

    public BaseInjectingIntentService(String name) {
        super(name);
        componentInjector = ComponentInjector.create(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    @Override
    @NonNull
    public Object[] createComponentModules() {
        return new Object[]{ new ServiceModule(this) };
    }

    @NonNull
    @Override
    public ComponentInjector getComponentInjector() {
        return componentInjector;
    }

    private final ComponentInjector componentInjector;
}
