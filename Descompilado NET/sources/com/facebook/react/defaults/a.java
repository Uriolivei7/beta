package com.facebook.react.defaults;

import r1.C0670b;
import r1.C0674f;
import r1.C0675g;
import r1.C0676h;
import r2.C0685h;
import r2.C0686i;
import r2.n;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f6561a = new a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static e1.f f6562b = e1.f.f9356d;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static boolean f6563c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static boolean f6564d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static boolean f6565e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static boolean f6566f;

    /* JADX INFO: renamed from: com.facebook.react.defaults.a$a, reason: collision with other inner class name */
    public /* synthetic */ class C0099a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f6567a;

        static {
            int[] iArr = new int[e1.f.values().length];
            try {
                iArr[e1.f.f9354b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[e1.f.f9355c.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[e1.f.f9356d.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f6567a = iArr;
        }
    }

    private a() {
    }

    public static final void b(boolean z3, boolean z4, boolean z5) {
        C0686i c0686iA = f6561a.a(z3, z4, z5);
        boolean zBooleanValue = ((Boolean) c0686iA.a()).booleanValue();
        String str = (String) c0686iA.b();
        if (!zBooleanValue) {
            throw new IllegalStateException(str.toString());
        }
        int i3 = C0099a.f6567a[f6562b.ordinal()];
        if (i3 == 1) {
            C0670b.n(new C0675g());
        } else if (i3 == 2) {
            C0670b.n(new C0674f());
        } else {
            if (i3 != 3) {
                throw new C0685h();
            }
            C0670b.n(new C0676h(z4, z5, z3));
        }
        f6563c = z4;
        f6564d = z3;
        f6565e = z4;
        f6566f = z5;
        h.f6573a.a();
    }

    public static /* synthetic */ void c(boolean z3, boolean z4, boolean z5, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            z3 = true;
        }
        if ((i3 & 2) != 0) {
            z4 = true;
        }
        if ((i3 & 4) != 0) {
            z5 = true;
        }
        b(z3, z4, z5);
    }

    public final C0686i a(boolean z3, boolean z4, boolean z5) {
        return (!z4 || z3) ? (!z5 || (z3 && z4)) ? n.a(Boolean.TRUE, "") : n.a(Boolean.FALSE, "bridgelessEnabled=true requires (turboModulesEnabled=true AND fabricEnabled=true) - Please update your DefaultNewArchitectureEntryPoint.load() parameters.") : n.a(Boolean.FALSE, "fabricEnabled=true requires turboModulesEnabled=true (is now false) - Please update your DefaultNewArchitectureEntryPoint.load() parameters.");
    }
}
