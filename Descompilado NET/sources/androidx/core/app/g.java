package androidx.core.app;

import android.content.res.Configuration;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f4398a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Configuration f4399b;

    public g(boolean z3) {
        this.f4398a = z3;
    }

    public final boolean a() {
        return this.f4398a;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public g(boolean z3, Configuration configuration) {
        this(z3);
        D2.h.f(configuration, "newConfig");
        this.f4399b = configuration;
    }
}
