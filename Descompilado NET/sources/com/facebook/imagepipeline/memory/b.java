package com.facebook.imagepipeline.memory;

import X.k;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f5929a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f5930b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final Queue f5931c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final boolean f5932d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f5933e;

    public b(int i3, int i4, int i5, boolean z3) {
        k.i(i3 > 0);
        k.i(i4 >= 0);
        k.i(i5 >= 0);
        this.f5929a = i3;
        this.f5930b = i4;
        this.f5931c = new LinkedList();
        this.f5933e = i5;
        this.f5932d = z3;
    }

    void a(Object obj) {
        this.f5931c.add(obj);
    }

    public void b() {
        k.i(this.f5933e > 0);
        this.f5933e--;
    }

    public Object c() {
        Object objG = g();
        if (objG != null) {
            this.f5933e++;
        }
        return objG;
    }

    int d() {
        return this.f5931c.size();
    }

    public void e() {
        this.f5933e++;
    }

    public boolean f() {
        return this.f5933e + d() > this.f5930b;
    }

    public Object g() {
        return this.f5931c.poll();
    }

    public void h(Object obj) {
        k.g(obj);
        if (this.f5932d) {
            k.i(this.f5933e > 0);
            this.f5933e--;
            a(obj);
        } else {
            int i3 = this.f5933e;
            if (i3 <= 0) {
                Y.a.o("BUCKET", "Tried to release value %s from an empty bucket!", obj);
            } else {
                this.f5933e = i3 - 1;
                a(obj);
            }
        }
    }
}
