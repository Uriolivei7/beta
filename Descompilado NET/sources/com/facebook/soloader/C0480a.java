package com.facebook.soloader;

import android.content.Context;
import android.os.StrictMode;
import java.io.File;

/* JADX INFO: renamed from: com.facebook.soloader.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0480a extends E implements w {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f8218a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private C0485f f8219b;

    public C0480a(Context context, int i3) {
        this.f8218a = i3;
        this.f8219b = new C0485f(f(context), i3);
    }

    private static File f(Context context) {
        return new File(context.getApplicationInfo().nativeLibraryDir);
    }

    @Override // com.facebook.soloader.w
    public E a(Context context) {
        this.f8219b = new C0485f(f(context), this.f8218a | 1);
        return this;
    }

    @Override // com.facebook.soloader.E
    public String c() {
        return "ApplicationSoSource";
    }

    @Override // com.facebook.soloader.E
    public int d(String str, int i3, StrictMode.ThreadPolicy threadPolicy) {
        return this.f8219b.d(str, i3, threadPolicy);
    }

    @Override // com.facebook.soloader.E
    protected void e(int i3) {
        this.f8219b.e(i3);
    }

    @Override // com.facebook.soloader.E
    public String toString() {
        return c() + "[" + this.f8219b.toString() + "]";
    }
}
