package z0;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f11151c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final n[] f11152d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final n f11153e = new n("UNKNOWN", 0, -1);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final n f11154f = new n("VISIBLE", 1, 1);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final n f11155g = new n("INVISIBLE", 2, 2);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final /* synthetic */ n[] f11156h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f11157i;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f11158b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    static {
        n[] nVarArrA = a();
        f11156h = nVarArrA;
        f11157i = AbstractC0764a.a(nVarArrA);
        f11151c = new a(null);
        f11152d = values();
    }

    private n(String str, int i3, int i4) {
        this.f11158b = i4;
    }

    private static final /* synthetic */ n[] a() {
        return new n[]{f11153e, f11154f, f11155g};
    }

    public static n valueOf(String str) {
        return (n) Enum.valueOf(n.class, str);
    }

    public static n[] values() {
        return (n[]) f11156h.clone();
    }
}
