package com.facebook.react.views.text;

import com.facebook.react.uimanager.C0429f0;

/* JADX INFO: loaded from: classes.dex */
public class s {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f8042a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private float f8043b = Float.NaN;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private float f8044c = Float.NaN;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f8045d = Float.NaN;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private float f8046e = Float.NaN;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private float f8047f = Float.NaN;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private u f8048g = u.f8058g;

    public s a(s sVar) {
        s sVar2 = new s();
        sVar2.f8042a = this.f8042a;
        sVar2.f8043b = !Float.isNaN(sVar.f8043b) ? sVar.f8043b : this.f8043b;
        sVar2.f8044c = !Float.isNaN(sVar.f8044c) ? sVar.f8044c : this.f8044c;
        sVar2.f8045d = !Float.isNaN(sVar.f8045d) ? sVar.f8045d : this.f8045d;
        sVar2.f8046e = !Float.isNaN(sVar.f8046e) ? sVar.f8046e : this.f8046e;
        sVar2.f8047f = !Float.isNaN(sVar.f8047f) ? sVar.f8047f : this.f8047f;
        u uVar = sVar.f8048g;
        if (uVar == u.f8058g) {
            uVar = this.f8048g;
        }
        sVar2.f8048g = uVar;
        return sVar2;
    }

    public boolean b() {
        return this.f8042a;
    }

    public int c() {
        float f3 = !Float.isNaN(this.f8043b) ? this.f8043b : 14.0f;
        return (int) (this.f8042a ? Math.ceil(C0429f0.k(f3, f())) : Math.ceil(C0429f0.h(f3)));
    }

    public float d() {
        if (Float.isNaN(this.f8045d)) {
            return Float.NaN;
        }
        return (this.f8042a ? C0429f0.k(this.f8045d, f()) : C0429f0.h(this.f8045d)) / c();
    }

    public float e() {
        if (Float.isNaN(this.f8044c)) {
            return Float.NaN;
        }
        float fK = this.f8042a ? C0429f0.k(this.f8044c, f()) : C0429f0.h(this.f8044c);
        if (Float.isNaN(this.f8047f)) {
            return fK;
        }
        float f3 = this.f8047f;
        return f3 > fK ? f3 : fK;
    }

    public float f() {
        if (Float.isNaN(this.f8046e)) {
            return 0.0f;
        }
        return this.f8046e;
    }

    public float g() {
        return this.f8043b;
    }

    public float h() {
        return this.f8047f;
    }

    public float i() {
        return this.f8045d;
    }

    public float j() {
        return this.f8044c;
    }

    public float k() {
        return this.f8046e;
    }

    public u l() {
        return this.f8048g;
    }

    public void m(boolean z3) {
        this.f8042a = z3;
    }

    public void n(float f3) {
        this.f8043b = f3;
    }

    public void o(float f3) {
        this.f8047f = f3;
    }

    public void p(float f3) {
        this.f8045d = f3;
    }

    public void q(float f3) {
        this.f8044c = f3;
    }

    public void r(float f3) {
        if (f3 == 0.0f || f3 >= 1.0f) {
            this.f8046e = f3;
        } else {
            Y.a.I("ReactNative", "maxFontSizeMultiplier must be NaN, 0, or >= 1");
            this.f8046e = Float.NaN;
        }
    }

    public void s(u uVar) {
        this.f8048g = uVar;
    }

    public String toString() {
        return "TextAttributes {\n  getAllowFontScaling(): " + b() + "\n  getFontSize(): " + g() + "\n  getEffectiveFontSize(): " + c() + "\n  getHeightOfTallestInlineViewOrImage(): " + h() + "\n  getLetterSpacing(): " + i() + "\n  getEffectiveLetterSpacing(): " + d() + "\n  getLineHeight(): " + j() + "\n  getEffectiveLineHeight(): " + e() + "\n  getTextTransform(): " + l() + "\n  getMaxFontSizeMultiplier(): " + k() + "\n  getEffectiveMaxFontSizeMultiplier(): " + f() + "\n}";
    }
}
