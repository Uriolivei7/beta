package com.facebook.react.views.image;

import android.graphics.Matrix;
import android.graphics.Rect;
import kotlin.jvm.internal.DefaultConstructorMarker;
import t0.q;
import t0.r;

/* JADX INFO: loaded from: classes.dex */
public final class i extends q {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final a f7708l = new a(null);

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static final r f7709m = new i();

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final r a() {
            return i.f7709m;
        }

        private a() {
        }
    }

    @Override // t0.q
    public void b(Matrix matrix, Rect rect, int i3, int i4, float f3, float f4, float f5, float f6) {
        D2.h.f(matrix, "outTransform");
        D2.h.f(rect, "parentRect");
        float fD = H2.d.d(Math.min(f5, f6), 1.0f);
        float f7 = rect.left;
        float f8 = rect.top;
        matrix.setScale(fD, fD);
        matrix.postTranslate(Math.round(f7), Math.round(f8));
    }

    public String toString() {
        return "start_inside";
    }
}
