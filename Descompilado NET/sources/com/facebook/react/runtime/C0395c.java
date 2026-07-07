package com.facebook.react.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.facebook.react.runtime.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0395c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final a f7167c = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f7168a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f7169b = Collections.synchronizedList(new ArrayList());

    /* JADX INFO: renamed from: com.facebook.react.runtime.c$a */
    private static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public C0395c(boolean z3) {
        this.f7168a = z3;
    }

    protected final void a(String str) {
        D2.h.f(str, "state");
        Y.a.I("BridgelessReact", str);
        if (this.f7168a) {
            this.f7169b.add(str);
        }
    }
}
