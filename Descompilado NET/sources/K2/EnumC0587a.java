package k2;

import android.net.NetworkInfo;

/* JADX INFO: renamed from: k2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public enum EnumC0587a {
    CG_2G("2g"),
    CG_3G("3g"),
    CG_4G("4g"),
    CG_5G("5g");


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f9598b;

    EnumC0587a(String str) {
        this.f9598b = str;
    }

    public static EnumC0587a b(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return null;
        }
        int subtype = networkInfo.getSubtype();
        if (subtype == 20) {
            return CG_5G;
        }
        switch (subtype) {
        }
        return null;
    }
}
