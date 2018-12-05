package ru.bashmag.khakimulin.reportmonitor.core;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import ru.bashmag.khakimulin.reportmonitor.core.di.HasComponent;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

/**
 * Created by Mark Khakimulin on 17.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public abstract class BaseFragment extends Fragment {

    private boolean mIsInjected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        try {
            mIsInjected = onInjectView();
        } catch (IllegalStateException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
            mIsInjected = false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsInjected) onViewInjected(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsInjected) {
            mIsInjected = onInjectView();
            if (mIsInjected) onViewInjected(savedInstanceState);
        }
    }

    /**
     * Gets a component for dependency injection by its type.
     *
     * @throws IllegalStateException if component has not been initialized yet.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) throws IllegalStateException {
        C component = componentType.cast(((HasComponent<C>) getActivity()).getComponent());
        if (component == null) {
            throw new IllegalStateException(componentType.getSimpleName() + " has not been initialized yet.");
        }
        return component;
    }

    /**
     * Called to do an optional injection. This will be called on {@link #onCreate(Bundle)} and if
     * an exception is thrown or false returned, on {@link #onActivityCreated(Bundle)} again.
     * Within this method get the injection component and inject the view. Based on returned value
     * {@link #onViewInjected(Bundle)} will be called. Check {@link #onViewInjected(Bundle)}
     * documentation for more info.
     *
     * @return True, if injection was successful, false otherwise. Returns false by default.
     * @throws IllegalStateException If there is a failure in getting injection component or
     *                               injection process itself. This can occur if activity holding
     *                               component instance has been killed by the system and has not
     *                               been initialized yet.
     */
    protected boolean onInjectView() throws IllegalStateException {
        // Return false by default.
        return false;
    }

    /**
     * Called when the fragment has been injected and the field injected can be initialized. This
     * will be called on {@link #onViewCreated(View, Bundle)} if {@link #onInjectView()} returned
     * true when executed on {@link #onCreate(Bundle)}, otherwise it will be called on
     * {@link #onActivityCreated(Bundle)} if {@link #onInjectView()} returned true right before.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @CallSuper
    protected void onViewInjected(Bundle savedInstanceState) {
        // Intentionally left empty.
    }


    public int getColorWrapper(Context context, int id) {
        return Utils.getColorWrapper(context,id);
    }
}