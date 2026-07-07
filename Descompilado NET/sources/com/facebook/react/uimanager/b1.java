package com.facebook.react.uimanager;

import e1.C0525b;
import kotlin.Lazy;
import r2.AbstractC0681d;
import r2.EnumC0684g;

/* JADX INFO: loaded from: classes.dex */
public final class b1 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b1 f7464a = new b1();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Lazy f7465b = AbstractC0681d.b(EnumC0684g.f10565b, new C2.a() { // from class: com.facebook.react.uimanager.a1
        @Override // C2.a
        public final Object a() {
            return b1.d();
        }
    });

    private b1() {
    }

    public static final C0525b b() {
        return f7464a.c();
    }

    private final C0525b c() {
        return (C0525b) f7465b.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final C0525b d() {
        return new C0525b(1024);
    }
}
