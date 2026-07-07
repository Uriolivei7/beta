package com.facebook.react.views.image;

import android.graphics.Shader;
import t0.r;

/* JADX INFO: loaded from: classes.dex */
public final class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final d f7677a = new d();

    private d() {
    }

    public static final Shader.TileMode a() {
        return Shader.TileMode.CLAMP;
    }

    public static final r b() {
        r rVar = r.f10758i;
        D2.h.e(rVar, "CENTER_CROP");
        return rVar;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final r c(String str) {
        if (str != null) {
            switch (str.hashCode()) {
                case -1881872635:
                    if (str.equals("stretch")) {
                        r rVar = r.f10750a;
                        D2.h.e(rVar, "FIT_XY");
                        return rVar;
                    }
                    break;
                case -1364013995:
                    if (str.equals("center")) {
                        r rVar2 = r.f10757h;
                        D2.h.e(rVar2, "CENTER_INSIDE");
                        return rVar2;
                    }
                    break;
                case -934531685:
                    if (str.equals("repeat")) {
                        return i.f7708l.a();
                    }
                    break;
                case 3387192:
                    if (str.equals("none")) {
                        return i.f7708l.a();
                    }
                    break;
                case 94852023:
                    if (str.equals("cover")) {
                        r rVar3 = r.f10758i;
                        D2.h.e(rVar3, "CENTER_CROP");
                        return rVar3;
                    }
                    break;
                case 951526612:
                    if (str.equals("contain")) {
                        r rVar4 = r.f10754e;
                        D2.h.e(rVar4, "FIT_CENTER");
                        return rVar4;
                    }
                    break;
            }
        }
        if (str != null) {
            Y.a.I("ReactNative", "Invalid resize mode: '" + str + "'");
        }
        return b();
    }

    public static final Shader.TileMode d(String str) {
        if (D2.h.b("contain", str) || D2.h.b("cover", str) || D2.h.b("stretch", str) || D2.h.b("center", str) || D2.h.b("none", str)) {
            return Shader.TileMode.CLAMP;
        }
        if (D2.h.b("repeat", str)) {
            return Shader.TileMode.REPEAT;
        }
        if (str != null) {
            Y.a.I("ReactNative", "Invalid resize mode: '" + str + "'");
        }
        return a();
    }
}
