package com.facebook.soloader;

import b2.C0317b;

/* JADX INFO: loaded from: classes.dex */
public class o implements x {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final x f8254a;

    public o(x xVar) {
        this.f8254a = xVar;
    }

    @Override // com.facebook.soloader.x
    public void a(String str, int i3) {
        C0317b.j(this.f8254a, "load", i3);
        try {
            this.f8254a.a(str, i3);
            C0317b.i(null);
        } finally {
        }
    }
}
