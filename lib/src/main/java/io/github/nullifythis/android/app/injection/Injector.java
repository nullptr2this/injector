package io.github.nullifythis.android.app.injection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Injector {
    public void inject(@Nullable Object target);
    @NonNull
    public <T> T create(@NonNull Class<T> clazz);
}