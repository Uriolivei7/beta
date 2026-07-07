package com.facebook.react.devsupport;

import android.app.Activity;
import android.util.Pair;
import android.view.View;
import com.facebook.react.bridge.DefaultJSExceptionHandler;
import com.facebook.react.bridge.ReactContext;
import k1.InterfaceC0586d;
import k1.e;

/* JADX INFO: loaded from: classes.dex */
public class k0 implements k1.e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final DefaultJSExceptionHandler f6763a = new DefaultJSExceptionHandler();

    @Override // k1.e
    public void A(boolean z3) {
    }

    @Override // k1.e
    public k1.f B() {
        return null;
    }

    @Override // k1.e
    public ReactContext C() {
        return null;
    }

    @Override // k1.e
    public void D() {
    }

    @Override // k1.e
    public String E() {
        return null;
    }

    @Override // k1.e
    public View a(String str) {
        return null;
    }

    @Override // k1.e
    public void b(View view) {
    }

    @Override // k1.e
    public void c(boolean z3) {
    }

    @Override // k1.e
    public void d(String str, e.a aVar) {
        D2.h.f(str, "message");
        D2.h.f(aVar, "listener");
    }

    @Override // k1.e
    public void e() {
    }

    @Override // k1.e
    public void f(boolean z3) {
    }

    @Override // k1.e
    public e1.j g(String str) {
        return null;
    }

    @Override // k1.e
    public void h() {
    }

    @Override // com.facebook.react.bridge.JSExceptionHandler
    public void handleException(Exception exc) {
        D2.h.f(exc, "e");
        this.f6763a.handleException(exc);
    }

    @Override // k1.e
    public Activity i() {
        return null;
    }

    @Override // k1.e
    public String j() {
        return null;
    }

    @Override // k1.e
    public void k(k1.g gVar) {
        D2.h.f(gVar, "callback");
        gVar.a(false);
    }

    @Override // k1.e
    public String l() {
        return null;
    }

    @Override // k1.e
    public void m() {
    }

    @Override // k1.e
    public boolean n() {
        return false;
    }

    @Override // k1.e
    public C1.a o() {
        return null;
    }

    @Override // k1.e
    public void q() {
    }

    @Override // k1.e
    public void r(ReactContext reactContext) {
        D2.h.f(reactContext, "reactContext");
    }

    @Override // k1.e
    public void s() {
    }

    @Override // k1.e
    public k1.i t() {
        return null;
    }

    @Override // k1.e
    public void u() {
    }

    @Override // k1.e
    public boolean v() {
        return false;
    }

    @Override // k1.e
    public k1.j[] w() {
        return null;
    }

    @Override // k1.e
    public void x() {
    }

    @Override // k1.e
    public Pair y(Pair pair) {
        return pair;
    }

    @Override // k1.e
    public void z(ReactContext reactContext) {
        D2.h.f(reactContext, "reactContext");
    }

    @Override // k1.e
    public void p(String str, InterfaceC0586d interfaceC0586d) {
    }
}
