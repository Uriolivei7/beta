package d1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

/* JADX INFO: renamed from: d1.s, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractActivityC0510s extends androidx.appcompat.app.c implements B1.a, B1.f {

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private final C0514w f9308D = p0();

    protected AbstractActivityC0510s() {
    }

    @Override // B1.a
    public void c() {
        super.onBackPressed();
    }

    @Override // B1.f
    public void m(String[] strArr, int i3, B1.g gVar) {
        this.f9308D.D(strArr, i3, gVar);
    }

    @Override // androidx.fragment.app.ActivityC0288j, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i3, int i4, Intent intent) {
        super.onActivityResult(i3, i4, intent);
        this.f9308D.p(i3, i4, intent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.f9308D.q()) {
            return;
        }
        super.onBackPressed();
    }

    @Override // androidx.appcompat.app.c, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f9308D.r(configuration);
    }

    @Override // androidx.fragment.app.ActivityC0288j, androidx.activity.ComponentActivity, androidx.core.app.f, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f9308D.s(bundle);
    }

    @Override // androidx.appcompat.app.c, androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.f9308D.t();
    }

    @Override // androidx.appcompat.app.c, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i3, KeyEvent keyEvent) {
        return this.f9308D.u(i3, keyEvent) || super.onKeyDown(i3, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int i3, KeyEvent keyEvent) {
        return this.f9308D.v(i3, keyEvent) || super.onKeyLongPress(i3, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i3, KeyEvent keyEvent) {
        return this.f9308D.w(i3, keyEvent) || super.onKeyUp(i3, keyEvent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        if (this.f9308D.x(intent)) {
            return;
        }
        super.onNewIntent(intent);
    }

    @Override // androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.f9308D.y();
    }

    @Override // androidx.fragment.app.ActivityC0288j, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i3, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i3, strArr, iArr);
        this.f9308D.z(i3, strArr, iArr);
    }

    @Override // androidx.fragment.app.ActivityC0288j, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.f9308D.A();
    }

    @Override // android.app.Activity
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        this.f9308D.B();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z3) {
        super.onWindowFocusChanged(z3);
        this.f9308D.C(z3);
    }

    protected abstract C0514w p0();
}
