package r2;

import kotlin.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: r2.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0682e {

    /* JADX INFO: renamed from: r2.e$a */
    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f10564a;

        static {
            int[] iArr = new int[EnumC0684g.values().length];
            try {
                iArr[EnumC0684g.f10565b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[EnumC0684g.f10566c.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[EnumC0684g.f10567d.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f10564a = iArr;
        }
    }

    public static Lazy a(C2.a aVar) {
        D2.h.f(aVar, "initializer");
        DefaultConstructorMarker defaultConstructorMarker = null;
        return new m(aVar, defaultConstructorMarker, 2, defaultConstructorMarker);
    }

    public static Lazy b(EnumC0684g enumC0684g, C2.a aVar) {
        D2.h.f(enumC0684g, "mode");
        D2.h.f(aVar, "initializer");
        int i3 = a.f10564a[enumC0684g.ordinal()];
        int i4 = 2;
        if (i3 == 1) {
            DefaultConstructorMarker defaultConstructorMarker = null;
            return new m(aVar, defaultConstructorMarker, i4, defaultConstructorMarker);
        }
        if (i3 == 2) {
            return new l(aVar);
        }
        if (i3 == 3) {
            return new s(aVar);
        }
        throw new C0685h();
    }
}
