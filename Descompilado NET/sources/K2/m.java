package K2;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class m implements f {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final m f843d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final m f844e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final m f845f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final m f846g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final m f847h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final m f848i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final m f849j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final /* synthetic */ m[] f850k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f851l;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f852b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f853c;

    static {
        int i3 = 2;
        f843d = new m("IGNORE_CASE", 0, i3, 0, 2, null);
        int i4 = 2;
        DefaultConstructorMarker defaultConstructorMarker = null;
        int i5 = 0;
        f844e = new m("MULTILINE", 1, 8, i5, i4, defaultConstructorMarker);
        int i6 = 2;
        DefaultConstructorMarker defaultConstructorMarker2 = null;
        int i7 = 0;
        f845f = new m("LITERAL", i3, 16, i7, i6, defaultConstructorMarker2);
        f846g = new m("UNIX_LINES", 3, 1, i5, i4, defaultConstructorMarker);
        f847h = new m("COMMENTS", 4, 4, i7, i6, defaultConstructorMarker2);
        f848i = new m("DOT_MATCHES_ALL", 5, 32, i5, i4, defaultConstructorMarker);
        f849j = new m("CANON_EQ", 6, 128, i7, i6, defaultConstructorMarker2);
        m[] mVarArrA = a();
        f850k = mVarArrA;
        f851l = AbstractC0764a.a(mVarArrA);
    }

    private m(String str, int i3, int i4, int i5) {
        this.f852b = i4;
        this.f853c = i5;
    }

    private static final /* synthetic */ m[] a() {
        return new m[]{f843d, f844e, f845f, f846g, f847h, f848i, f849j};
    }

    public static m valueOf(String str) {
        return (m) Enum.valueOf(m.class, str);
    }

    public static m[] values() {
        return (m[]) f850k.clone();
    }

    @Override // K2.f
    public int getValue() {
        return this.f852b;
    }

    /* synthetic */ m(String str, int i3, int i4, int i5, int i6, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i3, i4, (i6 & 2) != 0 ? i4 : i5);
    }
}
