package com.facebook.react.views.switchview;

import D2.h;
import android.view.View;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.U;
import com.facebook.yoga.o;
import com.facebook.yoga.p;
import com.facebook.yoga.q;
import com.facebook.yoga.r;

/* JADX INFO: loaded from: classes.dex */
public final class d extends U implements o {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private int f7914A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private int f7915B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private boolean f7916C;

    public d() {
        w1();
    }

    private final void w1() {
        Y0(this);
    }

    @Override // com.facebook.yoga.o
    public long K(r rVar, float f3, p pVar, float f4, p pVar2) {
        h.f(rVar, "node");
        h.f(pVar, "widthMode");
        h.f(pVar2, "heightMode");
        if (!this.f7916C) {
            B0 b0L = l();
            h.e(b0L, "getThemedContext(...)");
            a aVar = new a(b0L);
            aVar.setShowText(false);
            int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            aVar.measure(iMakeMeasureSpec, iMakeMeasureSpec);
            this.f7914A = aVar.getMeasuredWidth();
            this.f7915B = aVar.getMeasuredHeight();
            this.f7916C = true;
        }
        return q.b(this.f7914A, this.f7915B);
    }
}
