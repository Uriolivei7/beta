package com.facebook.react.views.text;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class u {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f8053b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final u f8054c = new u("NONE", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final u f8055d = new u("UPPERCASE", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final u f8056e = new u("LOWERCASE", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final u f8057f = new u("CAPITALIZE", 3);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final u f8058g = new u("UNSET", 4);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ u[] f8059h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f8060i;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(String str, u uVar) {
            if (str != null) {
                return v.a(str, uVar);
            }
            return null;
        }

        private a() {
        }
    }

    static {
        u[] uVarArrA = a();
        f8059h = uVarArrA;
        f8060i = AbstractC0764a.a(uVarArrA);
        f8053b = new a(null);
    }

    private u(String str, int i3) {
    }

    private static final /* synthetic */ u[] a() {
        return new u[]{f8054c, f8055d, f8056e, f8057f, f8058g};
    }

    public static final String b(String str, u uVar) {
        return f8053b.a(str, uVar);
    }

    public static u valueOf(String str) {
        return (u) Enum.valueOf(u.class, str);
    }

    public static u[] values() {
        return (u[]) f8059h.clone();
    }
}
