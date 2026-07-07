package com.facebook.react.uimanager;

import kotlin.Lazy;
import r2.AbstractC0681d;
import r2.EnumC0684g;

/* JADX INFO: renamed from: com.facebook.react.uimanager.u0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0458u0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0458u0 f7631a = new C0458u0();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Lazy f7632b = AbstractC0681d.b(EnumC0684g.f10567d, new C2.a() { // from class: com.facebook.react.uimanager.t0
        @Override // C2.a
        public final Object a() {
            return C0458u0.c();
        }
    });

    private C0458u0() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final com.facebook.yoga.c c() {
        com.facebook.yoga.c cVarA = com.facebook.yoga.d.a();
        cVarA.b(0.0f);
        cVarA.a(com.facebook.yoga.k.ALL);
        return cVarA;
    }

    public final com.facebook.yoga.c b() {
        Object value = f7632b.getValue();
        D2.h.e(value, "getValue(...)");
        return (com.facebook.yoga.c) value;
    }
}
