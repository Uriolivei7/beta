package com.facebook.react.uimanager;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class O0 {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static Comparator f7351c = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f7352a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final int f7353b;

    class a implements Comparator {
        a() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(O0 o02, O0 o03) {
            return o02.f7353b - o03.f7353b;
        }
    }

    public O0(int i3, int i4) {
        this.f7352a = i3;
        this.f7353b = i4;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        O0 o02 = (O0) obj;
        return this.f7353b == o02.f7353b && this.f7352a == o02.f7352a;
    }

    public String toString() {
        return "[" + this.f7352a + ", " + this.f7353b + "]";
    }
}
