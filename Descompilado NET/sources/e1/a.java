package E1;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f196b = new a("DEFAULT", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f197c = new a("RELOAD", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f198d = new a("FORCE_CACHE", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f199e = new a("ONLY_IF_CACHED", 3);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ a[] f200f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f201g;

    static {
        a[] aVarArrA = a();
        f200f = aVarArrA;
        f201g = AbstractC0764a.a(aVarArrA);
    }

    private a(String str, int i3) {
    }

    private static final /* synthetic */ a[] a() {
        return new a[]{f196b, f197c, f198d, f199e};
    }

    public static a valueOf(String str) {
        return (a) Enum.valueOf(a.class, str);
    }

    public static a[] values() {
        return (a[]) f200f.clone();
    }
}
