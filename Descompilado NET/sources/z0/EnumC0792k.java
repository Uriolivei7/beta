package z0;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: z0.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0792k {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final EnumC0792k f11144b = new EnumC0792k("VITO_V2", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final EnumC0792k f11145c = new EnumC0792k("VITO_V1", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final EnumC0792k f11146d = new EnumC0792k("DRAWEE", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final EnumC0792k f11147e = new EnumC0792k("OTHER", 3);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumC0792k[] f11148f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f11149g;

    static {
        EnumC0792k[] enumC0792kArrA = a();
        f11148f = enumC0792kArrA;
        f11149g = AbstractC0764a.a(enumC0792kArrA);
    }

    private EnumC0792k(String str, int i3) {
    }

    private static final /* synthetic */ EnumC0792k[] a() {
        return new EnumC0792k[]{f11144b, f11145c, f11146d, f11147e};
    }

    public static EnumC0792k valueOf(String str) {
        return (EnumC0792k) Enum.valueOf(EnumC0792k.class, str);
    }

    public static EnumC0792k[] values() {
        return (EnumC0792k[]) f11148f.clone();
    }
}
