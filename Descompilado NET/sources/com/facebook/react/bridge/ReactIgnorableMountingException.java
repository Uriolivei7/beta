package com.facebook.react.bridge;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class ReactIgnorableMountingException extends RuntimeException {
    public static final Companion Companion = new Companion(null);

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean isIgnorable(Throwable th) {
            D2.h.f(th, "e");
            while (th != null) {
                if (th instanceof ReactIgnorableMountingException) {
                    return true;
                }
                th = th.getCause();
            }
            return false;
        }

        private Companion() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactIgnorableMountingException(String str) {
        super(str);
        D2.h.f(str, "m");
    }

    public static final boolean isIgnorable(Throwable th) {
        return Companion.isIgnorable(th);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactIgnorableMountingException(Throwable th) {
        super(th);
        D2.h.f(th, "e");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReactIgnorableMountingException(String str, Throwable th) {
        super(str, th);
        D2.h.f(str, "m");
        D2.h.f(th, "e");
    }
}
