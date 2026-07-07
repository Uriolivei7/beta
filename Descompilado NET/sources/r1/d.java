package R1;

import kotlin.enums.EnumEntries;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final d f1999b = new d("BORDER_RADIUS", 0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final d f2000c = new d("BORDER_TOP_LEFT_RADIUS", 1);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final d f2001d = new d("BORDER_TOP_RIGHT_RADIUS", 2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final d f2002e = new d("BORDER_BOTTOM_RIGHT_RADIUS", 3);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final d f2003f = new d("BORDER_BOTTOM_LEFT_RADIUS", 4);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final d f2004g = new d("BORDER_TOP_START_RADIUS", 5);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final d f2005h = new d("BORDER_TOP_END_RADIUS", 6);

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final d f2006i = new d("BORDER_BOTTOM_START_RADIUS", 7);

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final d f2007j = new d("BORDER_BOTTOM_END_RADIUS", 8);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final d f2008k = new d("BORDER_END_END_RADIUS", 9);

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final d f2009l = new d("BORDER_END_START_RADIUS", 10);

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final d f2010m = new d("BORDER_START_END_RADIUS", 11);

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final d f2011n = new d("BORDER_START_START_RADIUS", 12);

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final /* synthetic */ d[] f2012o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f2013p;

    static {
        d[] dVarArrA = a();
        f2012o = dVarArrA;
        f2013p = AbstractC0764a.a(dVarArrA);
    }

    private d(String str, int i3) {
    }

    private static final /* synthetic */ d[] a() {
        return new d[]{f1999b, f2000c, f2001d, f2002e, f2003f, f2004g, f2005h, f2006i, f2007j, f2008k, f2009l, f2010m, f2011n};
    }

    public static d valueOf(String str) {
        return (d) Enum.valueOf(d.class, str);
    }

    public static d[] values() {
        return (d[]) f2012o.clone();
    }
}
