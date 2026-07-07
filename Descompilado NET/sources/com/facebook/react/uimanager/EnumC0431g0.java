package com.facebook.react.uimanager;

import java.util.Locale;
import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: com.facebook.react.uimanager.g0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0431g0 {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f7478b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final EnumC0431g0 f7479c = new EnumC0431g0("NONE", 0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final EnumC0431g0 f7480d = new EnumC0431g0("BOX_NONE", 1);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final EnumC0431g0 f7481e = new EnumC0431g0("BOX_ONLY", 2);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final EnumC0431g0 f7482f = new EnumC0431g0("AUTO", 3);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumC0431g0[] f7483g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f7484h;

    /* JADX INFO: renamed from: com.facebook.react.uimanager.g0$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean a(EnumC0431g0 enumC0431g0) {
            D2.h.f(enumC0431g0, "pointerEvents");
            return enumC0431g0 == EnumC0431g0.f7482f || enumC0431g0 == EnumC0431g0.f7481e;
        }

        public final boolean b(EnumC0431g0 enumC0431g0) {
            D2.h.f(enumC0431g0, "pointerEvents");
            return enumC0431g0 == EnumC0431g0.f7482f || enumC0431g0 == EnumC0431g0.f7480d;
        }

        public final EnumC0431g0 c(String str) {
            if (str == null) {
                return EnumC0431g0.f7482f;
            }
            Locale locale = Locale.US;
            D2.h.e(locale, "US");
            String upperCase = str.toUpperCase(locale);
            D2.h.e(upperCase, "toUpperCase(...)");
            return EnumC0431g0.valueOf(K2.o.v(upperCase, "-", "_", false, 4, null));
        }

        private a() {
        }
    }

    static {
        EnumC0431g0[] enumC0431g0ArrA = a();
        f7483g = enumC0431g0ArrA;
        f7484h = AbstractC0764a.a(enumC0431g0ArrA);
        f7478b = new a(null);
    }

    private EnumC0431g0(String str, int i3) {
    }

    private static final /* synthetic */ EnumC0431g0[] a() {
        return new EnumC0431g0[]{f7479c, f7480d, f7481e, f7482f};
    }

    public static final boolean b(EnumC0431g0 enumC0431g0) {
        return f7478b.a(enumC0431g0);
    }

    public static final boolean c(EnumC0431g0 enumC0431g0) {
        return f7478b.b(enumC0431g0);
    }

    public static final EnumC0431g0 d(String str) {
        return f7478b.c(str);
    }

    public static EnumC0431g0 valueOf(String str) {
        return (EnumC0431g0) Enum.valueOf(EnumC0431g0.class, str);
    }

    public static EnumC0431g0[] values() {
        return (EnumC0431g0[]) f7483g.clone();
    }
}
