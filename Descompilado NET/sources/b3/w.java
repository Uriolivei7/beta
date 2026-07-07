package b3;

import java.util.List;
import java.util.RandomAccess;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0705b;

/* JADX INFO: loaded from: classes.dex */
public final class w extends AbstractC0705b implements RandomAccess {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f5661e = new a(null);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final l[] f5662c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int[] f5663d;

    public static final class a {
        private a() {
        }

        private final void a(long j3, i iVar, int i3, List list, int i4, int i5, List list2) {
            int i6;
            int i7;
            int i8;
            int i9;
            i iVar2;
            int i10 = i3;
            if (!(i4 < i5)) {
                throw new IllegalArgumentException("Failed requirement.");
            }
            for (int i11 = i4; i11 < i5; i11++) {
                if (!(((l) list.get(i11)).v() >= i10)) {
                    throw new IllegalArgumentException("Failed requirement.");
                }
            }
            l lVar = (l) list.get(i4);
            l lVar2 = (l) list.get(i5 - 1);
            int i12 = -1;
            if (i10 == lVar.v()) {
                int iIntValue = ((Number) list2.get(i4)).intValue();
                int i13 = i4 + 1;
                l lVar3 = (l) list.get(i13);
                i6 = i13;
                i7 = iIntValue;
                lVar = lVar3;
            } else {
                i6 = i4;
                i7 = -1;
            }
            if (lVar.f(i10) == lVar2.f(i10)) {
                int iMin = Math.min(lVar.v(), lVar2.v());
                int i14 = 0;
                for (int i15 = i10; i15 < iMin && lVar.f(i15) == lVar2.f(i15); i15++) {
                    i14++;
                }
                long jC = j3 + c(iVar) + ((long) 2) + ((long) i14) + 1;
                iVar.E(-i14);
                iVar.E(i7);
                int i16 = i10 + i14;
                while (i10 < i16) {
                    iVar.E(lVar.f(i10) & 255);
                    i10++;
                }
                if (i6 + 1 == i5) {
                    if (!(i16 == ((l) list.get(i6)).v())) {
                        throw new IllegalStateException("Check failed.");
                    }
                    iVar.E(((Number) list2.get(i6)).intValue());
                    return;
                } else {
                    i iVar3 = new i();
                    iVar.E(((int) (c(iVar3) + jC)) * (-1));
                    a(jC, iVar3, i16, list, i6, i5, list2);
                    iVar.T(iVar3);
                    return;
                }
            }
            int i17 = 1;
            for (int i18 = i6 + 1; i18 < i5; i18++) {
                if (((l) list.get(i18 - 1)).f(i10) != ((l) list.get(i18)).f(i10)) {
                    i17++;
                }
            }
            long jC2 = j3 + c(iVar) + ((long) 2) + ((long) (i17 * 2));
            iVar.E(i17);
            iVar.E(i7);
            for (int i19 = i6; i19 < i5; i19++) {
                byte bF = ((l) list.get(i19)).f(i10);
                if (i19 == i6 || bF != ((l) list.get(i19 - 1)).f(i10)) {
                    iVar.E(bF & 255);
                }
            }
            i iVar4 = new i();
            while (i6 < i5) {
                byte bF2 = ((l) list.get(i6)).f(i10);
                int i20 = i6 + 1;
                int i21 = i20;
                while (true) {
                    if (i21 >= i5) {
                        i8 = i5;
                        break;
                    } else {
                        if (bF2 != ((l) list.get(i21)).f(i10)) {
                            i8 = i21;
                            break;
                        }
                        i21++;
                    }
                }
                if (i20 == i8 && i10 + 1 == ((l) list.get(i6)).v()) {
                    iVar.E(((Number) list2.get(i6)).intValue());
                    i9 = i8;
                    iVar2 = iVar4;
                } else {
                    iVar.E(((int) (jC2 + c(iVar4))) * i12);
                    i9 = i8;
                    iVar2 = iVar4;
                    a(jC2, iVar4, i10 + 1, list, i6, i8, list2);
                }
                iVar4 = iVar2;
                i6 = i9;
                i12 = -1;
            }
            iVar.T(iVar4);
        }

        static /* synthetic */ void b(a aVar, long j3, i iVar, int i3, List list, int i4, int i5, List list2, int i6, Object obj) {
            aVar.a((i6 & 1) != 0 ? 0L : j3, iVar, (i6 & 4) != 0 ? 0 : i3, list, (i6 & 16) != 0 ? 0 : i4, (i6 & 32) != 0 ? list.size() : i5, list2);
        }

        private final long c(i iVar) {
            return iVar.F0() / ((long) 4);
        }

        /* JADX WARN: Code restructure failed: missing block: B:55:0x00e7, code lost:
        
            continue;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final b3.w d(b3.l... r17) {
            /*
                Method dump skipped, instruction units count: 316
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: b3.w.a.d(b3.l[]):b3.w");
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public /* synthetic */ w(l[] lVarArr, int[] iArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(lVarArr, iArr);
    }

    @Override // s2.AbstractC0704a
    public int a() {
        return this.f5662c.length;
    }

    public /* bridge */ boolean b(l lVar) {
        return super.contains(lVar);
    }

    @Override // s2.AbstractC0705b, java.util.List
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public l get(int i3) {
        return this.f5662c[i3];
    }

    @Override // s2.AbstractC0704a, java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof l) {
            return b((l) obj);
        }
        return false;
    }

    public final l[] e() {
        return this.f5662c;
    }

    public final int[] f() {
        return this.f5663d;
    }

    public /* bridge */ int h(l lVar) {
        return super.indexOf(lVar);
    }

    public /* bridge */ int i(l lVar) {
        return super.lastIndexOf(lVar);
    }

    @Override // s2.AbstractC0705b, java.util.List
    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof l) {
            return h((l) obj);
        }
        return -1;
    }

    @Override // s2.AbstractC0705b, java.util.List
    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof l) {
            return i((l) obj);
        }
        return -1;
    }

    private w(l[] lVarArr, int[] iArr) {
        this.f5662c = lVarArr;
        this.f5663d = iArr;
    }
}
