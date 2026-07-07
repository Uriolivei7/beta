package M2;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public enum G {
    TLS_1_3("TLSv1.3"),
    TLS_1_2("TLSv1.2"),
    TLS_1_1("TLSv1.1"),
    TLS_1_0("TLSv1"),
    SSL_3_0("SSLv3");


    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final a f955i = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f956b;

    public static final class a {
        private a() {
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        public final G a(String str) {
            D2.h.f(str, "javaName");
            int iHashCode = str.hashCode();
            if (iHashCode != 79201641) {
                if (iHashCode != 79923350) {
                    switch (iHashCode) {
                        case -503070503:
                            if (str.equals("TLSv1.1")) {
                                return G.TLS_1_1;
                            }
                            break;
                        case -503070502:
                            if (str.equals("TLSv1.2")) {
                                return G.TLS_1_2;
                            }
                            break;
                        case -503070501:
                            if (str.equals("TLSv1.3")) {
                                return G.TLS_1_3;
                            }
                            break;
                    }
                } else if (str.equals("TLSv1")) {
                    return G.TLS_1_0;
                }
            } else if (str.equals("SSLv3")) {
                return G.SSL_3_0;
            }
            throw new IllegalArgumentException("Unexpected TLS version: " + str);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    G(String str) {
        this.f956b = str;
    }

    public final String a() {
        return this.f956b;
    }
}
