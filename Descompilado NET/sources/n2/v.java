package n2;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class v {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final v f10099b = new v("NONE", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final v f10100c = new v("BOX_NONE", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final v f10101d = new v("BOX_ONLY", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final v f10102e = new v("AUTO", 3);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final /* synthetic */ v[] f10103f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f10104g;

    static {
        v[] vVarArrA = a();
        f10103f = vVarArrA;
        f10104g = AbstractC0764a.a(vVarArrA);
    }

    private v(String str, int i3) {
    }

    private static final /* synthetic */ v[] a() {
        return new v[]{f10099b, f10100c, f10101d, f10102e};
    }

    public static v valueOf(String str) {
        return (v) Enum.valueOf(v.class, str);
    }

    public static v[] values() {
        return (v[]) f10103f.clone();
    }
}
