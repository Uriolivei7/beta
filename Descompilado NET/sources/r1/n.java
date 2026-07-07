package R1;

import kotlin.enums.EnumEntries;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;
import w2.AbstractC0764a;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* JADX INFO: loaded from: classes.dex */
public abstract class n {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final f f2074b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final n f2075c = new n("ALL", 0) { // from class: R1.n.a
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 8;
        }
    };

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final n f2076d = new n("LEFT", 1) { // from class: R1.n.i
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 0;
        }
    };

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final n f2077e = new n("RIGHT", 2) { // from class: R1.n.j
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 2;
        }
    };

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static final n f2078f = new n("TOP", 3) { // from class: R1.n.l
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 1;
        }
    };

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final n f2079g = new n("BOTTOM", 4) { // from class: R1.n.e
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 3;
        }
    };

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final n f2080h = new n("START", 5) { // from class: R1.n.k
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 4;
        }
    };

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final n f2081i = new n("END", 6) { // from class: R1.n.g
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 5;
        }
    };

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final n f2082j = new n("HORIZONTAL", 7) { // from class: R1.n.h
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 6;
        }
    };

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static final n f2083k = new n("VERTICAL", 8) { // from class: R1.n.m
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 7;
        }
    };

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final n f2084l = new n("BLOCK_START", 9) { // from class: R1.n.d
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 11;
        }
    };

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    public static final n f2085m = new n("BLOCK_END", 10) { // from class: R1.n.c
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 10;
        }
    };

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    public static final n f2086n = new n("BLOCK", 11) { // from class: R1.n.b
        {
            DefaultConstructorMarker defaultConstructorMarker = null;
        }

        @Override // R1.n
        public int b() {
            return 9;
        }
    };

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final /* synthetic */ n[] f2087o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final /* synthetic */ EnumEntries f2088p;

    public static final class f {
        public /* synthetic */ f(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final n a(int i3) {
            switch (i3) {
                case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                    return n.f2076d;
                case 1:
                    return n.f2078f;
                case 2:
                    return n.f2077e;
                case 3:
                    return n.f2079g;
                case 4:
                    return n.f2080h;
                case 5:
                    return n.f2081i;
                case 6:
                    return n.f2082j;
                case 7:
                    return n.f2083k;
                case 8:
                    return n.f2075c;
                case 9:
                    return n.f2086n;
                case 10:
                    return n.f2085m;
                case 11:
                    return n.f2084l;
                default:
                    throw new IllegalArgumentException("Unknown spacing type: " + i3);
            }
        }

        private f() {
        }
    }

    static {
        n[] nVarArrA = a();
        f2087o = nVarArrA;
        f2088p = AbstractC0764a.a(nVarArrA);
        f2074b = new f(null);
    }

    public /* synthetic */ n(String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i3);
    }

    private static final /* synthetic */ n[] a() {
        return new n[]{f2075c, f2076d, f2077e, f2078f, f2079g, f2080h, f2081i, f2082j, f2083k, f2084l, f2085m, f2086n};
    }

    public static n valueOf(String str) {
        return (n) Enum.valueOf(n.class, str);
    }

    public static n[] values() {
        return (n[]) f2087o.clone();
    }

    public abstract int b();

    private n(String str, int i3) {
    }
}
