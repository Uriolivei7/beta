package com.facebook.soloader;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class B extends C {
    public B(String str) {
        super(str);
    }

    public static B b(String str, Context context, E[] eArr) {
        StringBuilder sb = new StringBuilder("couldn't find DSO to load: ");
        sb.append(str);
        sb.append("\n\texisting SO sources: ");
        for (int i3 = 0; i3 < eArr.length; i3++) {
            sb.append("\n\t\tSoSource ");
            sb.append(i3);
            sb.append(": ");
            sb.append(eArr[i3].toString());
        }
        if (context != null) {
            sb.append("\n\tNative lib dir: ");
            sb.append(context.getApplicationInfo().nativeLibraryDir);
            sb.append("\n");
        }
        return new B(str, sb.toString());
    }

    public B(String str, String str2) {
        super(str, str2);
    }
}
