package W0;

import J0.z;

/* JADX INFO: loaded from: classes.dex */
public final class f implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2684a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f2685b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final d f2686c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Integer f2687d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final boolean f2688e;

    public f(int i3, boolean z3, d dVar, Integer num, boolean z4) {
        this.f2684a = i3;
        this.f2685b = z3;
        this.f2686c = dVar;
        this.f2687d = num;
        this.f2688e = z4;
    }

    private final c a(D0.c cVar, boolean z3) {
        d dVar = this.f2686c;
        if (dVar != null) {
            return dVar.createImageTranscoder(cVar, z3);
        }
        return null;
    }

    private final c b(D0.c cVar, boolean z3) {
        Integer num = this.f2687d;
        if (num == null) {
            return null;
        }
        if (num != null && num.intValue() == 0) {
            return c(cVar, z3);
        }
        if (num == null || num.intValue() != 1) {
            throw new IllegalArgumentException("Invalid ImageTranscoderType");
        }
        return d(cVar, z3);
    }

    private final c c(D0.c cVar, boolean z3) {
        return com.facebook.imagepipeline.nativecode.f.a(this.f2684a, this.f2685b, this.f2688e).createImageTranscoder(cVar, z3);
    }

    private final c d(D0.c cVar, boolean z3) {
        c cVarCreateImageTranscoder = new h(this.f2684a).createImageTranscoder(cVar, z3);
        D2.h.e(cVarCreateImageTranscoder, "createImageTranscoder(...)");
        return cVarCreateImageTranscoder;
    }

    @Override // W0.d
    public c createImageTranscoder(D0.c cVar, boolean z3) {
        D2.h.f(cVar, "imageFormat");
        c cVarA = a(cVar, z3);
        if (cVarA == null) {
            cVarA = b(cVar, z3);
        }
        if (cVarA == null && z.a()) {
            cVarA = c(cVar, z3);
        }
        return cVarA == null ? d(cVar, z3) : cVarA;
    }
}
