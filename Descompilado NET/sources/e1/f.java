package e1;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final f f9354b = new f("EXPERIMENTAL", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final f f9355c = new f("CANARY", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final f f9356d = new f("STABLE", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ f[] f9357e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f9358f;

    static {
        f[] fVarArrA = a();
        f9357e = fVarArrA;
        f9358f = AbstractC0764a.a(fVarArrA);
    }

    private f(String str, int i3) {
    }

    private static final /* synthetic */ f[] a() {
        return new f[]{f9354b, f9355c, f9356d};
    }

    public static f valueOf(String str) {
        return (f) Enum.valueOf(f.class, str);
    }

    public static f[] values() {
        return (f[]) f9357e.clone();
    }
}
