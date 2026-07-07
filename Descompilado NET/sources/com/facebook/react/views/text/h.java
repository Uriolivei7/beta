package com.facebook.react.views.text;

import a1.C0210a;
import android.os.Build;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.facebook.react.uimanager.C0423c0;
import com.facebook.react.uimanager.InterfaceC0451q0;
import com.facebook.react.uimanager.M0;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class h extends c {

    /* JADX INFO: renamed from: f0, reason: collision with root package name */
    private static final TextPaint f7969f0 = new TextPaint(1);

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    private Spannable f7970b0;

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    private boolean f7971c0;

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    private final com.facebook.yoga.o f7972d0;

    /* JADX INFO: renamed from: e0, reason: collision with root package name */
    private final com.facebook.yoga.b f7973e0;

    class a implements com.facebook.yoga.o {
        a() {
        }

        /* JADX WARN: Removed duplicated region for block: B:58:0x016a  */
        @Override // com.facebook.yoga.o
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public long K(com.facebook.yoga.r r18, float r19, com.facebook.yoga.p r20, float r21, com.facebook.yoga.p r22) {
            /*
                Method dump skipped, instruction units count: 369
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.h.a.K(com.facebook.yoga.r, float, com.facebook.yoga.p, float, com.facebook.yoga.p):long");
        }
    }

    class b implements com.facebook.yoga.b {
        b() {
        }

        @Override // com.facebook.yoga.b
        public float a(com.facebook.yoga.r rVar, float f3, float f4) {
            Layout layoutE1 = h.this.E1((Spannable) C0210a.d(h.this.f7970b0, "Spannable element has not been prepared in onBeforeLayout"), f3, com.facebook.yoga.p.EXACTLY);
            return layoutE1.getLineBaseline(layoutE1.getLineCount() - 1);
        }
    }

    public h() {
        this(null);
    }

    private int C1() {
        int i3 = this.f7927I;
        if (getLayoutDirection() != com.facebook.yoga.h.RTL) {
            return i3;
        }
        if (i3 == 5) {
            return 3;
        }
        if (i3 == 3) {
            return 5;
        }
        return i3;
    }

    private void D1() {
        if (R()) {
            return;
        }
        Y0(this.f7972d0);
        G0(this.f7973e0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Layout E1(Spannable spannable, float f3, com.facebook.yoga.p pVar) {
        TextPaint textPaint = f7969f0;
        textPaint.setTextSize(this.f7919A.c());
        BoringLayout.Metrics metricsIsBoring = BoringLayout.isBoring(spannable, textPaint);
        float desiredWidth = metricsIsBoring == null ? Layout.getDesiredWidth(spannable, textPaint) : Float.NaN;
        boolean z3 = pVar == com.facebook.yoga.p.UNDEFINED || f3 < 0.0f;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        int iC1 = C1();
        if (iC1 == 1) {
            alignment = Layout.Alignment.ALIGN_CENTER;
        } else if (iC1 != 3 && iC1 == 5) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        }
        Layout.Alignment alignment2 = alignment;
        if (metricsIsBoring == null && (z3 || (!com.facebook.yoga.g.a(desiredWidth) && desiredWidth <= f3))) {
            StaticLayout.Builder hyphenationFrequency = StaticLayout.Builder.obtain(spannable, 0, spannable.length(), textPaint, (int) Math.ceil(desiredWidth)).setAlignment(alignment2).setLineSpacing(0.0f, 1.0f).setIncludePad(this.f7937S).setBreakStrategy(this.f7928J).setHyphenationFrequency(this.f7929K);
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 26) {
                hyphenationFrequency.setJustificationMode(this.f7930L);
            }
            if (i3 >= 28) {
                hyphenationFrequency.setUseLineSpacingFromFallbacks(true);
            }
            return hyphenationFrequency.build();
        }
        if (metricsIsBoring != null && (z3 || metricsIsBoring.width <= f3)) {
            return BoringLayout.make(spannable, textPaint, Math.max(metricsIsBoring.width, 0), alignment2, 1.0f, 0.0f, metricsIsBoring, this.f7937S);
        }
        int i4 = Build.VERSION.SDK_INT;
        if (i4 > 29) {
            f3 = (float) Math.ceil(f3);
        }
        StaticLayout.Builder hyphenationFrequency2 = StaticLayout.Builder.obtain(spannable, 0, spannable.length(), textPaint, (int) f3).setAlignment(alignment2).setLineSpacing(0.0f, 1.0f).setIncludePad(this.f7937S).setBreakStrategy(this.f7928J).setHyphenationFrequency(this.f7929K);
        if (i4 >= 26) {
            hyphenationFrequency2.setJustificationMode(this.f7930L);
        }
        if (i4 >= 28) {
            hyphenationFrequency2.setUseLineSpacingFromFallbacks(true);
        }
        return hyphenationFrequency2.build();
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public void A0(M0 m02) {
        super.A0(m02);
        if (this.f7970b0 != null) {
            m02.O(H(), new i(this.f7970b0, -1, this.f7944Z, l0(4), l0(1), l0(5), l0(3), C1(), this.f7928J, this.f7930L));
        }
    }

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public Iterable E() {
        Map map = this.f7945a0;
        if (map == null || map.isEmpty()) {
            return null;
        }
        Spanned spanned = (Spanned) C0210a.d(this.f7970b0, "Spannable element has not been prepared in onBeforeLayout");
        Z1.q[] qVarArr = (Z1.q[]) spanned.getSpans(0, spanned.length(), Z1.q.class);
        ArrayList arrayList = new ArrayList(qVarArr.length);
        for (Z1.q qVar : qVarArr) {
            InterfaceC0451q0 interfaceC0451q0 = (InterfaceC0451q0) this.f7945a0.get(Integer.valueOf(qVar.b()));
            interfaceC0451q0.M();
            arrayList.add(interfaceC0451q0);
        }
        return arrayList;
    }

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public void O(C0423c0 c0423c0) {
        this.f7970b0 = x1(this, null, true, c0423c0);
        y0();
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public boolean p0() {
        return true;
    }

    @L1.a(name = "onTextLayout")
    public void setShouldNotifyOnTextLayout(boolean z3) {
        this.f7971c0 = z3;
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public boolean v0() {
        return false;
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public void y0() {
        super.y0();
        super.i();
    }

    public h(o oVar) {
        super(oVar);
        this.f7972d0 = new a();
        this.f7973e0 = new b();
        D1();
    }
}
