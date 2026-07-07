package com.facebook.react.bridge;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ReactSoftExceptionLogger {
    public static final ReactSoftExceptionLogger INSTANCE = new ReactSoftExceptionLogger();
    private static final List<ReactSoftExceptionListener> listeners = new CopyOnWriteArrayList();

    public static final class Categories {
        public static final Categories INSTANCE = new Categories();
        public static final String RVG_IS_VIEW_CLIPPED = "ReactViewGroup.isViewClipped";
        public static final String RVG_ON_VIEW_REMOVED = "ReactViewGroup.onViewRemoved";
        public static final String SOFT_ASSERTIONS = "SoftAssertions";
        public static final String SURFACE_MOUNTING_MANAGER_MISSING_VIEWSTATE = "SurfaceMountingManager:MissingViewState";

        private Categories() {
        }
    }

    public interface ReactSoftExceptionListener {
        void logSoftException(String str, Throwable th);
    }

    private ReactSoftExceptionLogger() {
    }

    public static final void addListener(ReactSoftExceptionListener reactSoftExceptionListener) {
        D2.h.f(reactSoftExceptionListener, "listener");
        List<ReactSoftExceptionListener> list = listeners;
        if (list.contains(reactSoftExceptionListener)) {
            return;
        }
        list.add(reactSoftExceptionListener);
    }

    public static final void clearListeners() {
        listeners.clear();
    }

    private static final void logNoThrowSoftExceptionWithMessage(String str, String str2) {
        logSoftException(str, new ReactNoCrashSoftException(str2));
    }

    public static final void logSoftException(String str, Throwable th) {
        D2.h.f(str, "category");
        D2.h.f(th, "cause");
        List<ReactSoftExceptionListener> list = listeners;
        if (list.isEmpty()) {
            Y.a.n(str, "Unhandled SoftException", th);
            return;
        }
        Iterator<ReactSoftExceptionListener> it = list.iterator();
        while (it.hasNext()) {
            it.next().logSoftException(str, th);
        }
    }

    public static final void logSoftExceptionVerbose(String str, Throwable th) {
        D2.h.f(str, "category");
        D2.h.f(th, "cause");
        logSoftException(str + "|" + th.getClass().getSimpleName() + ":" + th.getMessage(), th);
    }

    public static final void removeListener(ReactSoftExceptionListener reactSoftExceptionListener) {
        D2.h.f(reactSoftExceptionListener, "listener");
        listeners.remove(reactSoftExceptionListener);
    }
}
