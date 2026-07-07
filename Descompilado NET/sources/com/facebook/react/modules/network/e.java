package com.facebook.react.modules.network;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f7006a = new a(null);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(String str) {
            D2.h.f(str, "name");
            StringBuilder sb = new StringBuilder(str.length());
            int length = str.length();
            boolean z3 = false;
            for (int i3 = 0; i3 < length; i3++) {
                char cCharAt = str.charAt(i3);
                if (D2.h.g(cCharAt, 32) <= 0 || D2.h.g(cCharAt, 127) >= 0) {
                    z3 = true;
                } else {
                    sb.append(cCharAt);
                }
            }
            if (!z3) {
                return str;
            }
            String string = sb.toString();
            D2.h.e(string, "toString(...)");
            return string;
        }

        private a() {
        }
    }

    public static final String a(String str) {
        return f7006a.a(str);
    }
}
