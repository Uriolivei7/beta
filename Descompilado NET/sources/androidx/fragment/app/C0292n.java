package androidx.fragment.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

/* JADX INFO: renamed from: androidx.fragment.app.n, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0292n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final p f5166a;

    private C0292n(p pVar) {
        this.f5166a = pVar;
    }

    public static C0292n b(p pVar) {
        return new C0292n((p) q.g.h(pVar, "callbacks == null"));
    }

    public void a(Fragment fragment) {
        p pVar = this.f5166a;
        pVar.f5172f.m(pVar, pVar, fragment);
    }

    public void c() {
        this.f5166a.f5172f.x();
    }

    public boolean d(MenuItem menuItem) {
        return this.f5166a.f5172f.A(menuItem);
    }

    public void e() {
        this.f5166a.f5172f.B();
    }

    public void f() {
        this.f5166a.f5172f.D();
    }

    public void g() {
        this.f5166a.f5172f.M();
    }

    public void h() {
        this.f5166a.f5172f.Q();
    }

    public void i() {
        this.f5166a.f5172f.R();
    }

    public void j() {
        this.f5166a.f5172f.T();
    }

    public boolean k() {
        return this.f5166a.f5172f.a0(true);
    }

    public x l() {
        return this.f5166a.f5172f;
    }

    public void m() {
        this.f5166a.f5172f.U0();
    }

    public View n(View view, String str, Context context, AttributeSet attributeSet) {
        return this.f5166a.f5172f.u0().onCreateView(view, str, context, attributeSet);
    }
}
