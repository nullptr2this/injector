package io.github.nullifythis.android.app.injection;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import dagger.ObjectGraph;

public abstract class ComponentInjector implements Injector {

    public static <T extends Activity & InjectingComponent> ComponentInjector create(@NonNull T activity) {
        return new ActivityComponentInjector<T>(activity);
    }

    public static <T extends Fragment & InjectingComponent> ComponentInjector create(@NonNull T fragment) {
        return new FragmentComponentInjector<T>(fragment);
    }

    public static <T extends Service & InjectingComponent> ComponentInjector create(@NonNull T service) {
        return new ServiceComponentInjector<T>(service);
    }

    public void onCreate() {
        this.objectGraph = createObjectGraph();
        InjectingComponent component = getInjectingComponent();
        if(component.requiresInjection()) {
            inject(component);
        }
    }

    public void onDestroy() {
        this.objectGraph = null;
    }

    @NonNull
    public abstract InjectingComponent getInjectingComponent();

    @Override
    public void inject(@Nullable Object target) {
        throwIfObjectGraphIsUnavailable();
        if (null == target) {
            return;
        }
        objectGraph.inject(target);
    }

    @Override
    @NonNull
    public <T> T create(@NonNull Class<T> clazz) {
        throwIfObjectGraphIsUnavailable();
        return objectGraph.get(clazz);
    }

    @NonNull
    public ObjectGraph getObjectGraph() {
        throwIfObjectGraphIsUnavailable();
        return objectGraph;
    }

    @NonNull
    protected abstract ObjectGraph createObjectGraph();

    @NonNull
    private static <T extends Fragment & InjectingComponent> ObjectGraph createObjectGraph(@NonNull T fragment) {

        Fragment parent = fragment.getParentFragment();

        while(null != parent) {
            if (parent instanceof InjectingComponent) {
                return createObjectGraph((InjectingComponent) parent, fragment);
            }
            parent = parent.getParentFragment();
        }

        Activity activity = fragment.getActivity();

        if (activity instanceof InjectingComponent) {
            return createObjectGraph((InjectingComponent) activity, fragment);
        }

        Application application = (null != activity)
                ? activity.getApplication()
                : null;

        return createObjectGraph(convertToBaseApplicationOrThrow(application), fragment);
    }

    @NonNull
    private static ObjectGraph createObjectGraph(@NonNull InjectingComponent parent, @NonNull InjectingComponent child) {
        return createObjectGraph(parent.getComponentInjector().getObjectGraph(), child);
    }

    @NonNull
    private static <T extends Activity & InjectingComponent> ObjectGraph createObjectGraph(@NonNull T activity) {
        return createObjectGraph(convertToBaseApplicationOrThrow(activity.getApplication()), activity);
    }

    @NonNull
    private static <T extends Service & InjectingComponent> ObjectGraph createObjectGraph(@NonNull T service) {
        return createObjectGraph(convertToBaseApplicationOrThrow(service.getApplication()), service);
    }

    @NonNull
    private static ObjectGraph createObjectGraph(@NonNull BaseInjectingApplication application, @NonNull InjectingComponent component) {
        return createObjectGraph(application.getObjectGraph(), component);
    }

    @NonNull
    private static ObjectGraph createObjectGraph(@NonNull ObjectGraph objectGraph, @NonNull InjectingComponent child) {
        Object[] additionalModules = child.createComponentModules();
        return (null != additionalModules && 0 < additionalModules.length) ?
                objectGraph.plus(additionalModules) : objectGraph;
    }

    @NonNull
    private static BaseInjectingApplication convertToBaseApplicationOrThrow(@NonNull Context application) {
        if (!(application instanceof BaseInjectingApplication)) {
            throw new IllegalStateException("The hosting Application must be an BaseInjectingApplication instance.");
        }
        return (BaseInjectingApplication) application;
    }

    private void throwIfObjectGraphIsUnavailable() {
        if (null == objectGraph) {
            throw new IllegalStateException("The object graph has not yet been created or has been released.");
        }
    }

    private static class ActivityComponentInjector<T extends Activity & InjectingComponent> extends ComponentInjector {

        public ActivityComponentInjector(@NonNull T activity) {
            this.activity = activity;
        }

        @NonNull
        @Override
        public ObjectGraph createObjectGraph() {
            return ComponentInjector.createObjectGraph(activity);
        }

        @NonNull
        @Override
        public InjectingComponent getInjectingComponent() {
            return activity;
        }

        @NonNull
        private final T activity;
    }

    private static class FragmentComponentInjector<T extends Fragment & InjectingComponent> extends ComponentInjector {

        public FragmentComponentInjector(@NonNull T fragment) {
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public ObjectGraph createObjectGraph() {
            return ComponentInjector.createObjectGraph(fragment);
        }

        @NonNull
        @Override
        public InjectingComponent getInjectingComponent() {
            return fragment;
        }

        @NonNull
        private final T fragment;
    }

    private static class ServiceComponentInjector<T extends Service & InjectingComponent> extends ComponentInjector {

        public ServiceComponentInjector(@NonNull T service) {
            this.service = service;
        }

        @NonNull
        @Override
        public ObjectGraph createObjectGraph() {
            return ComponentInjector.createObjectGraph(service);
        }

        @NonNull
        @Override
        public InjectingComponent getInjectingComponent() {
            return service;
        }

        @NonNull
        private final T service;
    }

    private ComponentInjector() {}

    private ObjectGraph objectGraph;
}
