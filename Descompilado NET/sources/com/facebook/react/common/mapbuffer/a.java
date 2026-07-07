package com.facebook.react.common.mapbuffer;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX INFO: loaded from: classes.dex */
public interface a extends Iterable, E2.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0098a f6541a = C0098a.f6542a;

    /* JADX INFO: renamed from: com.facebook.react.common.mapbuffer.a$a, reason: collision with other inner class name */
    public static final class C0098a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ C0098a f6542a = new C0098a();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private static final H2.c f6543b = new H2.c(0, 65535);

        private C0098a() {
        }

        public final H2.c a() {
            return f6543b;
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    public static final class b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final b f6544b = new b("BOOL", 0);

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final b f6545c = new b("INT", 1);

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final b f6546d = new b("DOUBLE", 2);

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public static final b f6547e = new b("STRING", 3);

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public static final b f6548f = new b("MAP", 4);

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public static final b f6549g = new b("LONG", 5);

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private static final /* synthetic */ b[] f6550h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private static final /* synthetic */ EnumEntries f6551i;

        static {
            b[] bVarArrA = a();
            f6550h = bVarArrA;
            f6551i = AbstractC0764a.a(bVarArrA);
        }

        private b(String str, int i3) {
        }

        private static final /* synthetic */ b[] a() {
            return new b[]{f6544b, f6545c, f6546d, f6547e, f6548f, f6549g};
        }

        public static b valueOf(String str) {
            return (b) Enum.valueOf(b.class, str);
        }

        public static b[] values() {
            return (b[]) f6550h.clone();
        }
    }

    public interface c {
        long a();

        String b();

        int c();

        a d();

        double e();

        boolean f();

        int getKey();

        b getType();
    }

    a d(int i3);

    boolean g(int i3);

    boolean getBoolean(int i3);

    int getCount();

    double getDouble(int i3);

    int getInt(int i3);

    String getString(int i3);
}
