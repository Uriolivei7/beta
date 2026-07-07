package com.facebook.react.views.scroll;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class l {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f7887b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final l f7888c = new l("BEGIN_DRAG", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final l f7889d = new l("END_DRAG", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final l f7890e = new l("SCROLL", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final l f7891f = new l("MOMENTUM_BEGIN", 3);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final l f7892g = new l("MOMENTUM_END", 4);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ l[] f7893h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f7894i;

    public static final class a {

        /* JADX INFO: renamed from: com.facebook.react.views.scroll.l$a$a, reason: collision with other inner class name */
        public /* synthetic */ class C0116a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f7895a;

            static {
                int[] iArr = new int[l.values().length];
                try {
                    iArr[l.f7888c.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[l.f7889d.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[l.f7890e.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[l.f7891f.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    iArr[l.f7892g.ordinal()] = 5;
                } catch (NoSuchFieldError unused5) {
                }
                f7895a = iArr;
            }
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(l lVar) {
            D2.h.f(lVar, "type");
            int i3 = C0116a.f7895a[lVar.ordinal()];
            if (i3 == 1) {
                return "topScrollBeginDrag";
            }
            if (i3 == 2) {
                return "topScrollEndDrag";
            }
            if (i3 == 3) {
                return "topScroll";
            }
            if (i3 == 4) {
                return "topMomentumScrollBegin";
            }
            if (i3 == 5) {
                return "topMomentumScrollEnd";
            }
            throw new C0685h();
        }

        private a() {
        }
    }

    static {
        l[] lVarArrA = a();
        f7893h = lVarArrA;
        f7894i = AbstractC0764a.a(lVarArrA);
        f7887b = new a(null);
    }

    private l(String str, int i3) {
    }

    private static final /* synthetic */ l[] a() {
        return new l[]{f7888c, f7889d, f7890e, f7891f, f7892g};
    }

    public static final String b(l lVar) {
        return f7887b.a(lVar);
    }

    public static l valueOf(String str) {
        return (l) Enum.valueOf(l.class, str);
    }

    public static l[] values() {
        return (l[]) f7893h.clone();
    }
}
