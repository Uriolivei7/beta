package z0;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: renamed from: z0.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class EnumC0786e {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f11052c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final EnumC0786e[] f11053d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final EnumC0786e f11054e = new EnumC0786e("UNKNOWN", 0, -1);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final EnumC0786e f11055f = new EnumC0786e("REQUESTED", 1, 0);

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final EnumC0786e f11056g = new EnumC0786e("INTERMEDIATE_AVAILABLE", 2, 2);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final EnumC0786e f11057h = new EnumC0786e("SUCCESS", 3, 3);

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final EnumC0786e f11058i = new EnumC0786e("ERROR", 4, 5);

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final EnumC0786e f11059j = new EnumC0786e("EMPTY_EVENT", 5, 7);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final EnumC0786e f11060k = new EnumC0786e("RELEASED", 6, 8);

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final /* synthetic */ EnumC0786e[] f11061l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f11062m;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f11063b;

    /* JADX INFO: renamed from: z0.e$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: renamed from: z0.e$b */
    public /* synthetic */ class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f11064a;

        static {
            int[] iArr = new int[EnumC0786e.values().length];
            try {
                iArr[EnumC0786e.f11055f.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[EnumC0786e.f11057h.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[EnumC0786e.f11056g.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[EnumC0786e.f11058i.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[EnumC0786e.f11060k.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            f11064a = iArr;
        }
    }

    static {
        EnumC0786e[] enumC0786eArrA = a();
        f11061l = enumC0786eArrA;
        f11062m = AbstractC0764a.a(enumC0786eArrA);
        f11052c = new a(null);
        f11053d = values();
    }

    private EnumC0786e(String str, int i3, int i4) {
        this.f11063b = i4;
    }

    private static final /* synthetic */ EnumC0786e[] a() {
        return new EnumC0786e[]{f11054e, f11055f, f11056g, f11057h, f11058i, f11059j, f11060k};
    }

    public static EnumC0786e valueOf(String str) {
        return (EnumC0786e) Enum.valueOf(EnumC0786e.class, str);
    }

    public static EnumC0786e[] values() {
        return (EnumC0786e[]) f11061l.clone();
    }

    @Override // java.lang.Enum
    public String toString() {
        int i3 = b.f11064a[ordinal()];
        return i3 != 1 ? i3 != 2 ? i3 != 3 ? i3 != 4 ? i3 != 5 ? "unknown" : "released" : "error" : "intermediate_available" : "success" : "requested";
    }
}
