package com.facebook.soloader;

import android.os.StrictMode;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: renamed from: com.facebook.soloader.e, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0484e extends E {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected final String f8226a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected q f8227b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected Set f8228c = null;

    public C0484e(String str) {
        this.f8226a = str;
    }

    static String g(String str) {
        if ("base".equals(str)) {
            return SoLoader.f8206d.getApplicationInfo().sourceDir;
        }
        String[] strArr = SoLoader.f8206d.getApplicationInfo().splitSourceDirs;
        if (strArr == null) {
            throw new IllegalStateException("No splits avaiable");
        }
        String str2 = "split_" + str + ".apk";
        for (String str3 : strArr) {
            if (str3.endsWith(str2)) {
                return str3;
            }
        }
        throw new IllegalStateException("Could not find " + str + " split");
    }

    @Override // com.facebook.soloader.E
    public String c() {
        return "DirectSplitSoSource";
    }

    @Override // com.facebook.soloader.E
    public int d(String str, int i3, StrictMode.ThreadPolicy threadPolicy) {
        Set set = this.f8228c;
        if (set == null) {
            throw new IllegalStateException("prepare not called");
        }
        if (set.contains(str)) {
            return h(str, i3);
        }
        return 0;
    }

    @Override // com.facebook.soloader.E
    protected void e(int i3) throws IOException {
        InputStream inputStreamOpen = SoLoader.f8206d.getAssets().open(this.f8226a + ".soloader-manifest");
        try {
            this.f8227b = q.b(inputStreamOpen);
            if (inputStreamOpen != null) {
                inputStreamOpen.close();
            }
            this.f8228c = new HashSet(this.f8227b.f8256b);
        } catch (Throwable th) {
            if (inputStreamOpen != null) {
                try {
                    inputStreamOpen.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public String f(String str) {
        Set set = this.f8228c;
        if (set == null || this.f8227b == null) {
            throw new IllegalStateException("prepare not called");
        }
        if (!set.contains(str)) {
            return null;
        }
        return g(this.f8226a) + "!/lib/" + this.f8227b.f8255a + "/" + str;
    }

    protected int h(String str, int i3) {
        String strF = f(str);
        strF.getClass();
        System.load(strF);
        return 1;
    }
}
