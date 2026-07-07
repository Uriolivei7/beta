package R1;

import android.content.Context;
import com.facebook.react.uimanager.W;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;
import r2.C0686i;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private W f2014a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private W f2015b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private W f2016c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private W f2017d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private W f2018e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private W f2019f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private W f2020g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private W f2021h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private W f2022i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private W f2023j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private W f2024k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private W f2025l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private W f2026m;

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f2027a;

        static {
            int[] iArr = new int[d.values().length];
            try {
                iArr[d.f1999b.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[d.f2000c.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[d.f2001d.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[d.f2003f.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[d.f2002e.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[d.f2004g.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[d.f2005h.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                iArr[d.f2006i.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                iArr[d.f2007j.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                iArr[d.f2011n.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                iArr[d.f2010m.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                iArr[d.f2009l.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                iArr[d.f2008k.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            f2027a = iArr;
        }
    }

    public e() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, 8191, null);
    }

    private final j a(k kVar, k kVar2, k kVar3, k kVar4, float f3, float f4) {
        float fB = kVar.b() + kVar3.b();
        float fA = kVar.a() + kVar2.a();
        float fB2 = kVar2.b() + kVar4.b();
        float fA2 = kVar3.a() + kVar4.a();
        float fMin = fB > 0.0f ? Math.min(f4 / fB, 1.0f) : 0.0f;
        float fMin2 = fA > 0.0f ? Math.min(f3 / fA, 1.0f) : 0.0f;
        float fMin3 = fB2 > 0.0f ? Math.min(f4 / fB2, 1.0f) : 0.0f;
        float fMin4 = fA2 > 0.0f ? Math.min(f3 / fA2, 1.0f) : 0.0f;
        return new j(new k(kVar.a() * Math.min(fMin2, fMin), kVar.b() * Math.min(fMin2, fMin)), new k(kVar2.a() * Math.min(fMin3, fMin2), kVar2.b() * Math.min(fMin3, fMin2)), new k(kVar3.a() * Math.min(fMin4, fMin), kVar3.b() * Math.min(fMin4, fMin)), new k(kVar4.a() * Math.min(fMin4, fMin3), kVar4.b() * Math.min(fMin4, fMin3)));
    }

    public final W b(d dVar) {
        D2.h.f(dVar, "property");
        switch (a.f2027a[dVar.ordinal()]) {
            case 1:
                return this.f2014a;
            case 2:
                return this.f2015b;
            case 3:
                return this.f2016c;
            case 4:
                return this.f2017d;
            case 5:
                return this.f2018e;
            case 6:
                return this.f2019f;
            case 7:
                return this.f2020g;
            case 8:
                return this.f2021h;
            case 9:
                return this.f2022i;
            case 10:
                return this.f2023j;
            case 11:
                return this.f2024k;
            case 12:
                return this.f2025l;
            case 13:
                return this.f2026m;
            default:
                throw new C0685h();
        }
    }

    public final boolean c() {
        return (this.f2014a == null && this.f2015b == null && this.f2016c == null && this.f2017d == null && this.f2018e == null && this.f2019f == null && this.f2020g == null && this.f2021h == null && this.f2022i == null && this.f2023j == null && this.f2024k == null && this.f2025l == null && this.f2026m == null) ? false : true;
    }

    public final j d(int i3, Context context, float f3, float f4) {
        k kVarC;
        k kVarC2;
        k kVarC3;
        k kVarC4;
        k kVarC5;
        k kVarC6;
        k kVarC7;
        k kVarC8;
        k kVarC9;
        k kVarC10;
        k kVarC11;
        k kVarC12;
        D2.h.f(context, "context");
        k kVar = new k(0.0f, 0.0f);
        if (i3 == 0) {
            W w3 = this.f2023j;
            if (w3 == null && (w3 = this.f2019f) == null && (w3 = this.f2015b) == null) {
                w3 = this.f2014a;
            }
            k kVar2 = (w3 == null || (kVarC4 = w3.c(f3, f4)) == null) ? kVar : kVarC4;
            W w4 = this.f2025l;
            if (w4 == null && (w4 = this.f2020g) == null && (w4 = this.f2016c) == null) {
                w4 = this.f2014a;
            }
            k kVar3 = (w4 == null || (kVarC3 = w4.c(f3, f4)) == null) ? kVar : kVarC3;
            W w5 = this.f2024k;
            if (w5 == null && (w5 = this.f2021h) == null && (w5 = this.f2017d) == null) {
                w5 = this.f2014a;
            }
            k kVar4 = (w5 == null || (kVarC2 = w5.c(f3, f4)) == null) ? kVar : kVarC2;
            W w6 = this.f2026m;
            if (w6 == null && (w6 = this.f2022i) == null && (w6 = this.f2018e) == null) {
                w6 = this.f2014a;
            }
            return a(kVar2, kVar3, kVar4, (w6 == null || (kVarC = w6.c(f3, f4)) == null) ? kVar : kVarC, f3, f4);
        }
        if (i3 != 1) {
            throw new IllegalArgumentException("Expected?.resolved layout direction");
        }
        if (com.facebook.react.modules.i18nmanager.a.f6977a.a().d(context)) {
            W w7 = this.f2025l;
            if (w7 == null && (w7 = this.f2020g) == null && (w7 = this.f2016c) == null) {
                w7 = this.f2014a;
            }
            k kVar5 = (w7 == null || (kVarC12 = w7.c(f3, f4)) == null) ? kVar : kVarC12;
            W w8 = this.f2023j;
            if (w8 == null && (w8 = this.f2019f) == null && (w8 = this.f2015b) == null) {
                w8 = this.f2014a;
            }
            k kVar6 = (w8 == null || (kVarC11 = w8.c(f3, f4)) == null) ? kVar : kVarC11;
            W w9 = this.f2026m;
            if (w9 == null && (w9 = this.f2021h) == null && (w9 = this.f2018e) == null) {
                w9 = this.f2014a;
            }
            k kVar7 = (w9 == null || (kVarC10 = w9.c(f3, f4)) == null) ? kVar : kVarC10;
            W w10 = this.f2024k;
            if (w10 == null && (w10 = this.f2022i) == null && (w10 = this.f2017d) == null) {
                w10 = this.f2014a;
            }
            return a(kVar5, kVar6, kVar7, (w10 == null || (kVarC9 = w10.c(f3, f4)) == null) ? kVar : kVarC9, f3, f4);
        }
        W w11 = this.f2025l;
        if (w11 == null && (w11 = this.f2020g) == null && (w11 = this.f2015b) == null) {
            w11 = this.f2014a;
        }
        k kVar8 = (w11 == null || (kVarC8 = w11.c(f3, f4)) == null) ? kVar : kVarC8;
        W w12 = this.f2023j;
        if (w12 == null && (w12 = this.f2019f) == null && (w12 = this.f2016c) == null) {
            w12 = this.f2014a;
        }
        k kVar9 = (w12 == null || (kVarC7 = w12.c(f3, f4)) == null) ? kVar : kVarC7;
        W w13 = this.f2026m;
        if (w13 == null && (w13 = this.f2021h) == null && (w13 = this.f2017d) == null) {
            w13 = this.f2014a;
        }
        k kVar10 = (w13 == null || (kVarC6 = w13.c(f3, f4)) == null) ? kVar : kVarC6;
        W w14 = this.f2024k;
        if (w14 == null && (w14 = this.f2022i) == null && (w14 = this.f2018e) == null) {
            w14 = this.f2014a;
        }
        return a(kVar8, kVar9, kVar10, (w14 == null || (kVarC5 = w14.c(f3, f4)) == null) ? kVar : kVarC5, f3, f4);
    }

    public final void e(d dVar, W w3) {
        D2.h.f(dVar, "property");
        switch (a.f2027a[dVar.ordinal()]) {
            case 1:
                this.f2014a = w3;
                return;
            case 2:
                this.f2015b = w3;
                return;
            case 3:
                this.f2016c = w3;
                return;
            case 4:
                this.f2017d = w3;
                return;
            case 5:
                this.f2018e = w3;
                return;
            case 6:
                this.f2019f = w3;
                return;
            case 7:
                this.f2020g = w3;
                return;
            case 8:
                this.f2021h = w3;
                return;
            case 9:
                this.f2022i = w3;
                return;
            case 10:
                this.f2023j = w3;
                return;
            case 11:
                this.f2024k = w3;
                return;
            case 12:
                this.f2025l = w3;
                return;
            case 13:
                this.f2026m = w3;
                return;
            default:
                throw new C0685h();
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof e)) {
            return false;
        }
        e eVar = (e) obj;
        return D2.h.b(this.f2014a, eVar.f2014a) && D2.h.b(this.f2015b, eVar.f2015b) && D2.h.b(this.f2016c, eVar.f2016c) && D2.h.b(this.f2017d, eVar.f2017d) && D2.h.b(this.f2018e, eVar.f2018e) && D2.h.b(this.f2019f, eVar.f2019f) && D2.h.b(this.f2020g, eVar.f2020g) && D2.h.b(this.f2021h, eVar.f2021h) && D2.h.b(this.f2022i, eVar.f2022i) && D2.h.b(this.f2023j, eVar.f2023j) && D2.h.b(this.f2024k, eVar.f2024k) && D2.h.b(this.f2025l, eVar.f2025l) && D2.h.b(this.f2026m, eVar.f2026m);
    }

    public int hashCode() {
        W w3 = this.f2014a;
        int iHashCode = (w3 == null ? 0 : w3.hashCode()) * 31;
        W w4 = this.f2015b;
        int iHashCode2 = (iHashCode + (w4 == null ? 0 : w4.hashCode())) * 31;
        W w5 = this.f2016c;
        int iHashCode3 = (iHashCode2 + (w5 == null ? 0 : w5.hashCode())) * 31;
        W w6 = this.f2017d;
        int iHashCode4 = (iHashCode3 + (w6 == null ? 0 : w6.hashCode())) * 31;
        W w7 = this.f2018e;
        int iHashCode5 = (iHashCode4 + (w7 == null ? 0 : w7.hashCode())) * 31;
        W w8 = this.f2019f;
        int iHashCode6 = (iHashCode5 + (w8 == null ? 0 : w8.hashCode())) * 31;
        W w9 = this.f2020g;
        int iHashCode7 = (iHashCode6 + (w9 == null ? 0 : w9.hashCode())) * 31;
        W w10 = this.f2021h;
        int iHashCode8 = (iHashCode7 + (w10 == null ? 0 : w10.hashCode())) * 31;
        W w11 = this.f2022i;
        int iHashCode9 = (iHashCode8 + (w11 == null ? 0 : w11.hashCode())) * 31;
        W w12 = this.f2023j;
        int iHashCode10 = (iHashCode9 + (w12 == null ? 0 : w12.hashCode())) * 31;
        W w13 = this.f2024k;
        int iHashCode11 = (iHashCode10 + (w13 == null ? 0 : w13.hashCode())) * 31;
        W w14 = this.f2025l;
        int iHashCode12 = (iHashCode11 + (w14 == null ? 0 : w14.hashCode())) * 31;
        W w15 = this.f2026m;
        return iHashCode12 + (w15 != null ? w15.hashCode() : 0);
    }

    public String toString() {
        return "BorderRadiusStyle(uniform=" + this.f2014a + ", topLeft=" + this.f2015b + ", topRight=" + this.f2016c + ", bottomLeft=" + this.f2017d + ", bottomRight=" + this.f2018e + ", topStart=" + this.f2019f + ", topEnd=" + this.f2020g + ", bottomStart=" + this.f2021h + ", bottomEnd=" + this.f2022i + ", startStart=" + this.f2023j + ", startEnd=" + this.f2024k + ", endStart=" + this.f2025l + ", endEnd=" + this.f2026m + ")";
    }

    public e(W w3, W w4, W w5, W w6, W w7, W w8, W w9, W w10, W w11, W w12, W w13, W w14, W w15) {
        this.f2014a = w3;
        this.f2015b = w4;
        this.f2016c = w5;
        this.f2017d = w6;
        this.f2018e = w7;
        this.f2019f = w8;
        this.f2020g = w9;
        this.f2021h = w10;
        this.f2022i = w11;
        this.f2023j = w12;
        this.f2024k = w13;
        this.f2025l = w14;
        this.f2026m = w15;
    }

    public /* synthetic */ e(W w3, W w4, W w5, W w6, W w7, W w8, W w9, W w10, W w11, W w12, W w13, W w14, W w15, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? null : w3, (i3 & 2) != 0 ? null : w4, (i3 & 4) != 0 ? null : w5, (i3 & 8) != 0 ? null : w6, (i3 & 16) != 0 ? null : w7, (i3 & 32) != 0 ? null : w8, (i3 & 64) != 0 ? null : w9, (i3 & 128) != 0 ? null : w10, (i3 & 256) != 0 ? null : w11, (i3 & 512) != 0 ? null : w12, (i3 & 1024) != 0 ? null : w13, (i3 & 2048) != 0 ? null : w14, (i3 & 4096) == 0 ? w15 : null);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public e(List<? extends C0686i> list) {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, 8191, null);
        D2.h.f(list, "properties");
        for (C0686i c0686i : list) {
            e((d) c0686i.a(), (W) c0686i.b());
        }
    }
}
