package com.facebook.react.runtime;

import android.app.Activity;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;

/* JADX INFO: loaded from: classes.dex */
class c0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    LifecycleState f7170a = LifecycleState.f6516b;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final C0395c f7171b;

    c0(C0395c c0395c) {
        this.f7171b = c0395c;
    }

    public LifecycleState a() {
        return this.f7170a;
    }

    public void b(ReactContext reactContext) {
        if (reactContext != null) {
            LifecycleState lifecycleState = this.f7170a;
            if (lifecycleState == LifecycleState.f6517c) {
                this.f7171b.a("ReactContext.onHostDestroy()");
                reactContext.onHostDestroy();
            } else if (lifecycleState == LifecycleState.f6518d) {
                this.f7171b.a("ReactContext.onHostPause()");
                reactContext.onHostPause();
                this.f7171b.a("ReactContext.onHostDestroy()");
                reactContext.onHostDestroy();
            }
        }
        this.f7170a = LifecycleState.f6516b;
    }

    public void c(ReactContext reactContext, Activity activity) {
        if (reactContext != null) {
            LifecycleState lifecycleState = this.f7170a;
            if (lifecycleState == LifecycleState.f6516b) {
                this.f7171b.a("ReactContext.onHostResume()");
                reactContext.onHostResume(activity);
                this.f7171b.a("ReactContext.onHostPause()");
                reactContext.onHostPause();
            } else if (lifecycleState == LifecycleState.f6518d) {
                this.f7171b.a("ReactContext.onHostPause()");
                reactContext.onHostPause();
            }
        }
        this.f7170a = LifecycleState.f6517c;
    }

    public void d(ReactContext reactContext, Activity activity) {
        LifecycleState lifecycleState = this.f7170a;
        LifecycleState lifecycleState2 = LifecycleState.f6518d;
        if (lifecycleState == lifecycleState2) {
            return;
        }
        if (reactContext != null) {
            this.f7171b.a("ReactContext.onHostResume()");
            reactContext.onHostResume(activity);
        }
        this.f7170a = lifecycleState2;
    }

    public void e(ReactContext reactContext, Activity activity) {
        if (this.f7170a == LifecycleState.f6518d) {
            this.f7171b.a("ReactContext.onHostResume()");
            reactContext.onHostResume(activity);
        }
    }
}
