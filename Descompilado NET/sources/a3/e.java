package a3;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static final a f2930g = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final boolean f2931a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Integer f2932b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final boolean f2933c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Integer f2934d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public final boolean f2935e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public final boolean f2936f;

    public static final class a {
        private a() {
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x008b A[PHI: r7 r9
          0x008b: PHI (r7v6 java.lang.Integer) = (r7v4 java.lang.Integer), (r7v4 java.lang.Integer), (r7v7 java.lang.Integer) binds: [B:47:0x00ba, B:44:0x00b1, B:27:0x0089] A[DONT_GENERATE, DONT_INLINE]
          0x008b: PHI (r9v7 java.lang.Integer) = (r9v4 java.lang.Integer), (r9v5 java.lang.Integer), (r9v4 java.lang.Integer) binds: [B:47:0x00ba, B:44:0x00b1, B:27:0x0089] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final a3.e a(M2.t r21) {
            /*
                Method dump skipped, instruction units count: 216
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: a3.e.a.a(M2.t):a3.e");
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public e() {
        this(false, null, false, null, false, false, 63, null);
    }

    public final boolean a(boolean z3) {
        return z3 ? this.f2933c : this.f2935e;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof e)) {
            return false;
        }
        e eVar = (e) obj;
        return this.f2931a == eVar.f2931a && D2.h.b(this.f2932b, eVar.f2932b) && this.f2933c == eVar.f2933c && D2.h.b(this.f2934d, eVar.f2934d) && this.f2935e == eVar.f2935e && this.f2936f == eVar.f2936f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [int] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v4, types: [int] */
    /* JADX WARN: Type inference failed for: r2v7, types: [int] */
    /* JADX WARN: Type inference failed for: r2v9 */
    public int hashCode() {
        boolean z3 = this.f2931a;
        ?? r02 = z3;
        if (z3) {
            r02 = 1;
        }
        int i3 = r02 * 31;
        Integer num = this.f2932b;
        int iHashCode = (i3 + (num != null ? num.hashCode() : 0)) * 31;
        boolean z4 = this.f2933c;
        ?? r22 = z4;
        if (z4) {
            r22 = 1;
        }
        int i4 = (iHashCode + r22) * 31;
        Integer num2 = this.f2934d;
        int iHashCode2 = (i4 + (num2 != null ? num2.hashCode() : 0)) * 31;
        boolean z5 = this.f2935e;
        ?? r23 = z5;
        if (z5) {
            r23 = 1;
        }
        int i5 = (iHashCode2 + r23) * 31;
        boolean z6 = this.f2936f;
        return i5 + (z6 ? 1 : z6);
    }

    public String toString() {
        return "WebSocketExtensions(perMessageDeflate=" + this.f2931a + ", clientMaxWindowBits=" + this.f2932b + ", clientNoContextTakeover=" + this.f2933c + ", serverMaxWindowBits=" + this.f2934d + ", serverNoContextTakeover=" + this.f2935e + ", unknownValues=" + this.f2936f + ")";
    }

    public e(boolean z3, Integer num, boolean z4, Integer num2, boolean z5, boolean z6) {
        this.f2931a = z3;
        this.f2932b = num;
        this.f2933c = z4;
        this.f2934d = num2;
        this.f2935e = z5;
        this.f2936f = z6;
    }

    public /* synthetic */ e(boolean z3, Integer num, boolean z4, Integer num2, boolean z5, boolean z6, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? false : z3, (i3 & 2) != 0 ? null : num, (i3 & 4) != 0 ? false : z4, (i3 & 8) == 0 ? num2 : null, (i3 & 16) != 0 ? false : z5, (i3 & 32) != 0 ? false : z6);
    }
}
