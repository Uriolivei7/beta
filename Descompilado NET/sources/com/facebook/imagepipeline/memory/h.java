package com.facebook.imagepipeline.memory;

import X.k;
import b0.C0311f;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
class h extends b {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private LinkedList f5942f;

    public h(int i3, int i4, int i5) {
        super(i3, i4, i5, false);
        this.f5942f = new LinkedList();
    }

    @Override // com.facebook.imagepipeline.memory.b
    void a(Object obj) {
        C0311f c0311f = (C0311f) this.f5942f.poll();
        if (c0311f == null) {
            c0311f = new C0311f();
        }
        c0311f.c(obj);
        this.f5931c.add(c0311f);
    }

    @Override // com.facebook.imagepipeline.memory.b
    public Object g() {
        C0311f c0311f = (C0311f) this.f5931c.poll();
        k.g(c0311f);
        Object objB = c0311f.b();
        c0311f.a();
        this.f5942f.add(c0311f);
        return objB;
    }
}
