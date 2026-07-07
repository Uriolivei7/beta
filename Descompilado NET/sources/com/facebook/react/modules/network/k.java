package com.facebook.react.modules.network;

import M2.E;
import M2.x;
import b3.F;
import b3.t;

/* JADX INFO: loaded from: classes.dex */
public class k extends E {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final E f7017c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final i f7018d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private b3.k f7019e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private long f7020f = 0;

    class a extends b3.o {
        a(F f3) {
            super(f3);
        }

        @Override // b3.o, b3.F
        public long x(b3.i iVar, long j3) {
            long jX = super.x(iVar, j3);
            k.this.f7020f += jX != -1 ? jX : 0L;
            k.this.f7018d.a(k.this.f7020f, k.this.f7017c.q(), jX == -1);
            return jX;
        }
    }

    public k(E e4, i iVar) {
        this.f7017c = e4;
        this.f7018d = iVar;
    }

    private F c0(F f3) {
        return new a(f3);
    }

    public long d0() {
        return this.f7020f;
    }

    @Override // M2.E
    public long q() {
        return this.f7017c.q();
    }

    @Override // M2.E
    public x v() {
        return this.f7017c.v();
    }

    @Override // M2.E
    public b3.k z() {
        if (this.f7019e == null) {
            this.f7019e = t.d(c0(this.f7017c.z()));
        }
        return this.f7019e;
    }
}
