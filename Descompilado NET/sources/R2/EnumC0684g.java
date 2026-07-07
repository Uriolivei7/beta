package r2;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: r2.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0684g {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final EnumC0684g f10565b = new EnumC0684g("SYNCHRONIZED", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final EnumC0684g f10566c = new EnumC0684g("PUBLICATION", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final EnumC0684g f10567d = new EnumC0684g("NONE", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ EnumC0684g[] f10568e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f10569f;

    static {
        EnumC0684g[] enumC0684gArrA = a();
        f10568e = enumC0684gArrA;
        f10569f = AbstractC0764a.a(enumC0684gArrA);
    }

    private EnumC0684g(String str, int i3) {
    }

    private static final /* synthetic */ EnumC0684g[] a() {
        return new EnumC0684g[]{f10565b, f10566c, f10567d};
    }

    public static EnumC0684g valueOf(String str) {
        return (EnumC0684g) Enum.valueOf(EnumC0684g.class, str);
    }

    public static EnumC0684g[] values() {
        return (EnumC0684g[]) f10568e.clone();
    }
}
