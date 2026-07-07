package com.facebook.imagepipeline.producers;

/* JADX INFO: loaded from: classes.dex */
public final class G extends F implements Q0.d {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Q0.e f5996c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Q0.d f5997d;

    public G(Q0.e eVar, Q0.d dVar) {
        super(eVar, dVar);
        this.f5996c = eVar;
        this.f5997d = dVar;
    }

    @Override // Q0.d
    public void a(f0 f0Var) {
        D2.h.f(f0Var, "producerContext");
        Q0.e eVar = this.f5996c;
        if (eVar != null) {
            eVar.i(f0Var.getId());
        }
        Q0.d dVar = this.f5997d;
        if (dVar != null) {
            dVar.a(f0Var);
        }
    }

    @Override // Q0.d
    public void c(f0 f0Var) {
        D2.h.f(f0Var, "producerContext");
        Q0.e eVar = this.f5996c;
        if (eVar != null) {
            eVar.a(f0Var.X(), f0Var.i(), f0Var.getId(), f0Var.v());
        }
        Q0.d dVar = this.f5997d;
        if (dVar != null) {
            dVar.c(f0Var);
        }
    }

    @Override // Q0.d
    public void h(f0 f0Var) {
        D2.h.f(f0Var, "producerContext");
        Q0.e eVar = this.f5996c;
        if (eVar != null) {
            eVar.b(f0Var.X(), f0Var.getId(), f0Var.v());
        }
        Q0.d dVar = this.f5997d;
        if (dVar != null) {
            dVar.h(f0Var);
        }
    }

    @Override // Q0.d
    public void k(f0 f0Var, Throwable th) {
        D2.h.f(f0Var, "producerContext");
        Q0.e eVar = this.f5996c;
        if (eVar != null) {
            eVar.k(f0Var.X(), f0Var.getId(), th, f0Var.v());
        }
        Q0.d dVar = this.f5997d;
        if (dVar != null) {
            dVar.k(f0Var, th);
        }
    }
}
