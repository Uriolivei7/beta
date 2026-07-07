package d1;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: d1.f, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0498f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final EnumC0498f f9205b = new EnumC0498f("JSC", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final EnumC0498f f9206c = new EnumC0498f("HERMES", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final /* synthetic */ EnumC0498f[] f9207d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f9208e;

    static {
        EnumC0498f[] enumC0498fArrA = a();
        f9207d = enumC0498fArrA;
        f9208e = AbstractC0764a.a(enumC0498fArrA);
    }

    private EnumC0498f(String str, int i3) {
    }

    private static final /* synthetic */ EnumC0498f[] a() {
        return new EnumC0498f[]{f9205b, f9206c};
    }

    public static EnumC0498f valueOf(String str) {
        return (EnumC0498f) Enum.valueOf(EnumC0498f.class, str);
    }

    public static EnumC0498f[] values() {
        return (EnumC0498f[]) f9207d.clone();
    }
}
