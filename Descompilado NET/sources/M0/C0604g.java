package m0;

import J0.C0185t;
import J0.y;
import X.n;
import android.content.Context;
import java.util.Set;
import p0.AbstractC0635a;
import q0.InterfaceC0648d;
import z0.InterfaceC0783b;

/* JADX INFO: renamed from: m0.g, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0604g implements n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f9826a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0185t f9827b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final C0605h f9828c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Set f9829d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Set f9830e;

    public C0604g(Context context) {
        this(context, null);
    }

    @Override // X.n
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public C0603f get() {
        return new C0603f(this.f9826a, this.f9828c, this.f9827b, this.f9829d, this.f9830e).N(null);
    }

    public C0604g(Context context, C0599b c0599b) {
        this(context, y.l(), c0599b);
    }

    public C0604g(Context context, y yVar, C0599b c0599b) {
        this(context, yVar, null, null, c0599b);
    }

    public C0604g(Context context, y yVar, Set<InterfaceC0648d> set, Set<InterfaceC0783b> set2, C0599b c0599b) {
        this.f9826a = context;
        C0185t c0185tJ = yVar.j();
        this.f9827b = c0185tJ;
        if (c0599b != null && c0599b.d() != null) {
            this.f9828c = c0599b.d();
        } else {
            this.f9828c = new C0605h();
        }
        this.f9828c.a(context.getResources(), AbstractC0635a.b(), yVar.b(context), yVar.q(), V.f.h(), c0185tJ.o(), c0599b != null ? c0599b.a() : null, c0599b != null ? c0599b.b() : null);
        this.f9829d = set;
        this.f9830e = set2;
        if (c0599b != null) {
            c0599b.c();
        }
    }
}
