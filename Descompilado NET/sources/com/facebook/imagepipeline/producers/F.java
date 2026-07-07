package com.facebook.imagepipeline.producers;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class F implements h0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final i0 f5994a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final h0 f5995b;

    public F(i0 i0Var, h0 h0Var) {
        this.f5994a = i0Var;
        this.f5995b = h0Var;
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void b(f0 f0Var, String str, String str2) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        if (i0Var != null) {
            i0Var.d(f0Var.getId(), str, str2);
        }
        h0 h0Var = this.f5995b;
        if (h0Var != null) {
            h0Var.b(f0Var, str, str2);
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void d(f0 f0Var, String str, Map map) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        if (i0Var != null) {
            i0Var.e(f0Var.getId(), str, map);
        }
        h0 h0Var = this.f5995b;
        if (h0Var != null) {
            h0Var.d(f0Var, str, map);
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void e(f0 f0Var, String str, boolean z3) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        if (i0Var != null) {
            i0Var.j(f0Var.getId(), str, z3);
        }
        h0 h0Var = this.f5995b;
        if (h0Var != null) {
            h0Var.e(f0Var, str, z3);
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void f(f0 f0Var, String str, Map map) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        if (i0Var != null) {
            i0Var.h(f0Var.getId(), str, map);
        }
        h0 h0Var = this.f5995b;
        if (h0Var != null) {
            h0Var.f(f0Var, str, map);
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void g(f0 f0Var, String str) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        if (i0Var != null) {
            i0Var.f(f0Var.getId(), str);
        }
        h0 h0Var = this.f5995b;
        if (h0Var != null) {
            h0Var.g(f0Var, str);
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public void i(f0 f0Var, String str, Throwable th, Map map) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        if (i0Var != null) {
            i0Var.g(f0Var.getId(), str, th, map);
        }
        h0 h0Var = this.f5995b;
        if (h0Var != null) {
            h0Var.i(f0Var, str, th, map);
        }
    }

    @Override // com.facebook.imagepipeline.producers.h0
    public boolean j(f0 f0Var, String str) {
        D2.h.f(f0Var, "context");
        i0 i0Var = this.f5994a;
        Boolean boolValueOf = i0Var != null ? Boolean.valueOf(i0Var.c(f0Var.getId())) : null;
        if (!D2.h.b(boolValueOf, Boolean.TRUE)) {
            h0 h0Var = this.f5995b;
            boolValueOf = h0Var != null ? Boolean.valueOf(h0Var.j(f0Var, str)) : null;
        }
        if (boolValueOf != null) {
            return boolValueOf.booleanValue();
        }
        return false;
    }
}
