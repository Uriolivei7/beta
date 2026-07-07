package com.facebook.react.views.progressbar;

import D2.h;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ProgressBar;
import com.facebook.react.uimanager.U;
import com.facebook.react.views.progressbar.ReactProgressBarViewManager;
import com.facebook.yoga.o;
import com.facebook.yoga.p;
import com.facebook.yoga.q;
import com.facebook.yoga.r;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class b extends U implements o {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private final SparseIntArray f7743A = new SparseIntArray();

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private final SparseIntArray f7744B = new SparseIntArray();

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private final Set f7745C = new HashSet();

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private String f7746D;

    public b() {
        Y0(this);
        this.f7746D = ReactProgressBarViewManager.DEFAULT_STYLE;
    }

    @Override // com.facebook.yoga.o
    public long K(r rVar, float f3, p pVar, float f4, p pVar2) {
        h.f(rVar, "node");
        h.f(pVar, "widthMode");
        h.f(pVar2, "heightMode");
        ReactProgressBarViewManager.a aVar = ReactProgressBarViewManager.Companion;
        int iB = aVar.b(this.f7746D);
        if (!this.f7745C.contains(Integer.valueOf(iB))) {
            ProgressBar progressBarA = aVar.a(l(), iB);
            int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(-2, 0);
            progressBarA.measure(iMakeMeasureSpec, iMakeMeasureSpec);
            this.f7743A.put(iB, progressBarA.getMeasuredHeight());
            this.f7744B.put(iB, progressBarA.getMeasuredWidth());
            this.f7745C.add(Integer.valueOf(iB));
        }
        return q.b(this.f7744B.get(iB), this.f7743A.get(iB));
    }

    @L1.a(name = ReactProgressBarViewManager.PROP_STYLE)
    public final void setStyle(String str) {
        if (str == null) {
            str = ReactProgressBarViewManager.DEFAULT_STYLE;
        }
        this.f7746D = str;
    }
}
