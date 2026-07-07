package E0;

import D2.h;
import H0.n;
import J0.InterfaceC0182p;
import V.d;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final b f194a = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f195b;

    private b() {
    }

    public static final a a(G0.b bVar, InterfaceC0182p interfaceC0182p, n nVar, boolean z3, boolean z4, int i3, int i4, ExecutorService executorService) {
        if (f195b) {
            return null;
        }
        try {
            Class<?> cls = Class.forName("com.facebook.fresco.animation.factory.AnimatedFactoryV2Impl");
            Class cls2 = Boolean.TYPE;
            Class cls3 = Integer.TYPE;
            Object objNewInstance = cls.getConstructor(G0.b.class, InterfaceC0182p.class, n.class, cls2, cls2, cls3, cls3, d.class).newInstance(bVar, interfaceC0182p, nVar, Boolean.valueOf(z3), Boolean.valueOf(z4), Integer.valueOf(i3), Integer.valueOf(i4), executorService);
            h.d(objNewInstance, "null cannot be cast to non-null type com.facebook.imagepipeline.animated.factory.AnimatedFactory");
            androidx.activity.result.d.a(objNewInstance);
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }
}
