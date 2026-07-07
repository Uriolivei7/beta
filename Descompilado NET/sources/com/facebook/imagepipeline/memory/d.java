package com.facebook.imagepipeline.memory;

import R0.E;
import R0.F;
import R0.z;
import X.k;
import b0.AbstractC0306a;
import b0.InterfaceC0313h;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0313h f5934a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final b f5935b;

    class a implements InterfaceC0313h {
        a() {
        }

        @Override // b0.InterfaceC0313h
        /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(byte[] bArr) {
            d.this.b(bArr);
        }
    }

    static class b extends e {
        public b(a0.d dVar, E e4, F f3) {
            super(dVar, e4, f3);
        }

        @Override // com.facebook.imagepipeline.memory.a
        com.facebook.imagepipeline.memory.b w(int i3) {
            return new h(o(i3), this.f5919c.f1952g, 0);
        }
    }

    public d(a0.d dVar, E e4) {
        k.b(Boolean.valueOf(e4.f1952g > 0));
        this.f5935b = new b(dVar, e4, z.h());
        this.f5934a = new a();
    }

    public AbstractC0306a a(int i3) {
        return AbstractC0306a.n0((byte[]) this.f5935b.get(i3), this.f5934a);
    }

    public void b(byte[] bArr) {
        this.f5935b.a(bArr);
    }
}
