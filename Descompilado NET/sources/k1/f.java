package k1;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final f f9588c = new f("JS", 0, "JS");

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final f f9589d = new f("NATIVE", 1, "Native");

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ f[] f9590e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f9591f;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f9592b;

    static {
        f[] fVarArrA = a();
        f9590e = fVarArrA;
        f9591f = AbstractC0764a.a(fVarArrA);
    }

    private f(String str, int i3, String str2) {
        this.f9592b = str2;
    }

    private static final /* synthetic */ f[] a() {
        return new f[]{f9588c, f9589d};
    }

    public static f valueOf(String str) {
        return (f) Enum.valueOf(f.class, str);
    }

    public static f[] values() {
        return (f[]) f9590e.clone();
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f9592b;
    }
}
