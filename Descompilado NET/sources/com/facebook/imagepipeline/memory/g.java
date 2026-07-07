package com.facebook.imagepipeline.memory;

import R0.v;
import R0.x;
import a0.k;
import b0.AbstractC0306a;
import java.io.IOException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class g extends k {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final f f5939b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private AbstractC0306a f5940c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f5941d;

    public static final class a extends RuntimeException {
        public a() {
            super("OutputStream no longer valid");
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public g(f fVar) {
        this(fVar, 0, 2, null);
        D2.h.f(fVar, "pool");
    }

    private final void i() {
        if (!AbstractC0306a.c0(this.f5940c)) {
            throw new a();
        }
    }

    @Override // a0.k, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws Throwable {
        AbstractC0306a.D(this.f5940c);
        this.f5940c = null;
        this.f5941d = -1;
        super.close();
    }

    public final void o(int i3) throws Throwable {
        i();
        AbstractC0306a abstractC0306a = this.f5940c;
        if (abstractC0306a == null) {
            throw new IllegalStateException("Required value was null.");
        }
        D2.h.c(abstractC0306a);
        if (i3 <= ((v) abstractC0306a.P()).i()) {
            return;
        }
        Object obj = this.f5939b.get(i3);
        D2.h.e(obj, "get(...)");
        v vVar = (v) obj;
        AbstractC0306a abstractC0306a2 = this.f5940c;
        if (abstractC0306a2 == null) {
            throw new IllegalStateException("Required value was null.");
        }
        D2.h.c(abstractC0306a2);
        ((v) abstractC0306a2.P()).y(0, vVar, 0, this.f5941d);
        AbstractC0306a abstractC0306a3 = this.f5940c;
        D2.h.c(abstractC0306a3);
        abstractC0306a3.close();
        this.f5940c = AbstractC0306a.n0(vVar, this.f5939b);
    }

    @Override // a0.k
    /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
    public x a() {
        i();
        AbstractC0306a abstractC0306a = this.f5940c;
        if (abstractC0306a != null) {
            return new x(abstractC0306a, this.f5941d);
        }
        throw new IllegalStateException("Required value was null.");
    }

    @Override // a0.k
    public int size() {
        return this.f5941d;
    }

    @Override // java.io.OutputStream
    public void write(int i3) throws IOException {
        write(new byte[]{(byte) i3});
    }

    public /* synthetic */ g(f fVar, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(fVar, (i4 & 2) != 0 ? fVar.B() : i3);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i3, int i4) throws Throwable {
        D2.h.f(bArr, "buffer");
        if (i3 >= 0 && i4 >= 0 && i3 + i4 <= bArr.length) {
            i();
            o(this.f5941d + i4);
            AbstractC0306a abstractC0306a = this.f5940c;
            if (abstractC0306a != null) {
                ((v) abstractC0306a.P()).v(this.f5941d, bArr, i3, i4);
                this.f5941d += i4;
                return;
            }
            throw new IllegalStateException("Required value was null.");
        }
        throw new ArrayIndexOutOfBoundsException("length=" + bArr.length + "; regionStart=" + i3 + "; regionLength=" + i4);
    }

    public g(f fVar, int i3) {
        D2.h.f(fVar, "pool");
        if (i3 > 0) {
            this.f5939b = fVar;
            this.f5941d = 0;
            this.f5940c = AbstractC0306a.n0(fVar.get(i3), fVar);
            return;
        }
        throw new IllegalStateException("Check failed.");
    }
}
