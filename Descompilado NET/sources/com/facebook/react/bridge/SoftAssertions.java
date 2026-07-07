package com.facebook.react.bridge;

import com.facebook.react.bridge.ReactSoftExceptionLogger;

/* JADX INFO: loaded from: classes.dex */
public final class SoftAssertions {
    public static final SoftAssertions INSTANCE = new SoftAssertions();

    private SoftAssertions() {
    }

    public static final void assertCondition(boolean z3, String str) {
        D2.h.f(str, "message");
        if (z3) {
            return;
        }
        ReactSoftExceptionLogger.logSoftException(ReactSoftExceptionLogger.Categories.SOFT_ASSERTIONS, new AssertionException(str));
    }

    public static final <T> T assertNotNull(T t3) {
        if (t3 == null) {
            ReactSoftExceptionLogger.logSoftException(ReactSoftExceptionLogger.Categories.SOFT_ASSERTIONS, new AssertionException("Expected object to not be null!"));
        }
        return t3;
    }

    public static final void assertUnreachable(String str) {
        D2.h.f(str, "message");
        ReactSoftExceptionLogger.logSoftException(ReactSoftExceptionLogger.Categories.SOFT_ASSERTIONS, new AssertionException(str));
    }
}
