package com.facebook.react.views.textinput;

import a1.C0210a;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.view.Z;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.M0;
import d1.AbstractC0509q;

/* JADX INFO: loaded from: classes.dex */
public class H extends com.facebook.react.views.text.c implements com.facebook.yoga.o {

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    private int f8068b0;

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    private EditText f8069c0;

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    private r f8070d0;

    /* JADX INFO: renamed from: e0, reason: collision with root package name */
    private String f8071e0;

    /* JADX INFO: renamed from: f0, reason: collision with root package name */
    private String f8072f0;

    public H(com.facebook.react.views.text.o oVar) {
        super(oVar);
        this.f8068b0 = -1;
        this.f8071e0 = null;
        this.f8072f0 = null;
        this.f7928J = 1;
        B1();
    }

    private void B1() {
        Y0(this);
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public void A0(M0 m02) {
        super.A0(m02);
        if (this.f8068b0 != -1) {
            m02.O(H(), new com.facebook.react.views.text.i(x1(this, A1(), false, null), this.f8068b0, this.f7944Z, l0(0), l0(1), l0(2), l0(3), this.f7927I, this.f7928J, this.f7930L));
        }
    }

    public String A1() {
        return this.f8071e0;
    }

    @Override // com.facebook.yoga.o
    public long K(com.facebook.yoga.r rVar, float f3, com.facebook.yoga.p pVar, float f4, com.facebook.yoga.p pVar2) {
        EditText editText = (EditText) C0210a.c(this.f8069c0);
        r rVar2 = this.f8070d0;
        if (rVar2 != null) {
            rVar2.a(editText);
        } else {
            editText.setTextSize(0, this.f7919A.c());
            int i3 = this.f7926H;
            if (i3 != -1) {
                editText.setLines(i3);
            }
            int breakStrategy = editText.getBreakStrategy();
            int i4 = this.f7928J;
            if (breakStrategy != i4) {
                editText.setBreakStrategy(i4);
            }
        }
        editText.setHint(z1());
        editText.measure(com.facebook.react.views.view.e.a(f3, pVar), com.facebook.react.views.view.e.a(f4, pVar2));
        return com.facebook.yoga.q.b(editText.getMeasuredWidth(), editText.getMeasuredHeight());
    }

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public void S(int i3, float f3) {
        super.S(i3, f3);
        y0();
    }

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public void c0(B0 b02) {
        super.c0(b02);
        EditText editTextY1 = y1();
        K0(4, Z.w(editTextY1));
        K0(1, editTextY1.getPaddingTop());
        K0(5, Z.v(editTextY1));
        K0(3, editTextY1.getPaddingBottom());
        this.f8069c0 = editTextY1;
        editTextY1.setPadding(0, 0, 0, 0);
        this.f8069c0.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    }

    @Override // com.facebook.react.uimanager.C0452r0, com.facebook.react.uimanager.InterfaceC0451q0
    public void k(Object obj) {
        C0210a.a(obj instanceof r);
        this.f8070d0 = (r) obj;
        i();
    }

    @L1.a(name = "mostRecentEventCount")
    public void setMostRecentEventCount(int i3) {
        this.f8068b0 = i3;
    }

    @L1.a(name = "placeholder")
    public void setPlaceholder(String str) {
        this.f8072f0 = str;
        y0();
    }

    @L1.a(name = "text")
    public void setText(String str) {
        this.f8071e0 = str;
        y0();
    }

    @Override // com.facebook.react.views.text.c
    public void setTextBreakStrategy(String str) {
        if (str == null || "simple".equals(str)) {
            this.f7928J = 0;
            return;
        }
        if ("highQuality".equals(str)) {
            this.f7928J = 1;
            return;
        }
        if ("balanced".equals(str)) {
            this.f7928J = 2;
            return;
        }
        Y.a.I("ReactNative", "Invalid textBreakStrategy: " + str);
        this.f7928J = 0;
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public boolean v0() {
        return true;
    }

    @Override // com.facebook.react.uimanager.C0452r0
    public boolean w0() {
        return true;
    }

    protected EditText y1() {
        return new EditText(new androidx.appcompat.view.d(l(), AbstractC0509q.f9306g));
    }

    public String z1() {
        return this.f8072f0;
    }

    public H() {
        this(null);
    }
}
