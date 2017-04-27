package io.github.nullifythis.android.app.injection;

import android.support.annotation.NonNull;

public interface InjectingComponent extends Injector {
    @NonNull
    public Object[] createComponentModules();
    @NonNull
    public ComponentInjector getComponentInjector();
    public boolean requiresInjection();
}
