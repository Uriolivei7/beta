package com.facebook.react.uimanager;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.EditText;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.uimanager.events.EventDispatcher;

/* JADX INFO: loaded from: classes.dex */
public class H0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f7254a = "com.facebook.react.uimanager.H0";

    public static float[] a(Context context) {
        EditText editText = new EditText(context);
        return new float[]{C0429f0.f(androidx.core.view.Z.w(editText)), C0429f0.f(androidx.core.view.Z.v(editText)), C0429f0.f(editText.getPaddingTop()), C0429f0.f(editText.getPaddingBottom())};
    }

    public static EventDispatcher b(ReactContext reactContext, int i3) {
        if (reactContext.isBridgeless()) {
            boolean z3 = reactContext instanceof B0;
            Object objB = reactContext;
            if (z3) {
                objB = ((B0) reactContext).b();
            }
            return ((P1.h) objB).getEventDispatcher();
        }
        UIManager uIManagerH = h(reactContext, i3, false);
        if (uIManagerH == null) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new ReactNoCrashSoftException("Unable to find UIManager for UIManagerType " + i3));
            return null;
        }
        EventDispatcher eventDispatcher = uIManagerH.getEventDispatcher();
        if (eventDispatcher == null) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new IllegalStateException("Cannot get EventDispatcher for UIManagerType " + i3));
        }
        return eventDispatcher;
    }

    public static EventDispatcher c(ReactContext reactContext, int i3) {
        EventDispatcher eventDispatcherB = b(reactContext, M1.a.a(i3));
        if (eventDispatcherB == null) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new IllegalStateException("Cannot get EventDispatcher for reactTag " + i3));
        }
        return eventDispatcherB;
    }

    public static ReactContext d(View view) {
        Context context = view.getContext();
        if (!(context instanceof ReactContext) && (context instanceof ContextWrapper)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (ReactContext) context;
    }

    public static int e(Context context) {
        if (context instanceof B0) {
            return ((B0) context).c();
        }
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int f(View view) {
        if (view instanceof InterfaceC0447o0) {
            InterfaceC0447o0 interfaceC0447o0 = (InterfaceC0447o0) view;
            if (interfaceC0447o0.getUIManagerType() == 2) {
                return interfaceC0447o0.getRootViewTag();
            }
            return -1;
        }
        int id = view.getId();
        if (M1.a.a(id) == 1) {
            return -1;
        }
        Context context = view.getContext();
        if (!(context instanceof B0) && (context instanceof ContextWrapper)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        int iE = e(context);
        if (iE == -1) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new IllegalStateException("Fabric View [" + id + "] does not have SurfaceId associated with it"));
        }
        return iE;
    }

    public static UIManager g(ReactContext reactContext, int i3) {
        return h(reactContext, i3, true);
    }

    private static UIManager h(ReactContext reactContext, int i3, boolean z3) {
        if (reactContext.isBridgeless()) {
            UIManager fabricUIManager = reactContext.getFabricUIManager();
            if (fabricUIManager != null) {
                return fabricUIManager;
            }
            ReactSoftExceptionLogger.logSoftException(f7254a, new ReactNoCrashSoftException("Cannot get UIManager because the instance hasn't been initialized yet."));
            return null;
        }
        if (!reactContext.hasCatalystInstance()) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new ReactNoCrashSoftException("Cannot get UIManager because the context doesn't contain a CatalystInstance."));
            return null;
        }
        if (!reactContext.hasActiveReactInstance()) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new ReactNoCrashSoftException("Cannot get UIManager because the context doesn't contain an active CatalystInstance."));
            if (z3) {
                return null;
            }
        }
        CatalystInstance catalystInstance = reactContext.getCatalystInstance();
        try {
            return i3 == 2 ? reactContext.getFabricUIManager() : (UIManager) catalystInstance.getNativeModule(UIManagerModule.class);
        } catch (IllegalArgumentException unused) {
            ReactSoftExceptionLogger.logSoftException(f7254a, new ReactNoCrashSoftException("Cannot get UIManager for UIManagerType: " + i3));
            return (UIManager) catalystInstance.getNativeModule(UIManagerModule.class);
        }
    }

    public static UIManager i(ReactContext reactContext, int i3) {
        return g(reactContext, M1.a.a(i3));
    }
}
