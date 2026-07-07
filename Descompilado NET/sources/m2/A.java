package M2;

import java.io.IOException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public enum A {
    HTTP_1_0("http/1.0"),
    HTTP_1_1("http/1.1"),
    SPDY_3("spdy/3.1"),
    HTTP_2("h2"),
    H2_PRIOR_KNOWLEDGE("h2_prior_knowledge"),
    QUIC("quic");


    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static final a f895j = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f896b;

    public static final class a {
        private a() {
        }

        public final A a(String str) throws IOException {
            D2.h.f(str, "protocol");
            A a4 = A.HTTP_1_0;
            if (!D2.h.b(str, a4.f896b)) {
                a4 = A.HTTP_1_1;
                if (!D2.h.b(str, a4.f896b)) {
                    a4 = A.H2_PRIOR_KNOWLEDGE;
                    if (!D2.h.b(str, a4.f896b)) {
                        a4 = A.HTTP_2;
                        if (!D2.h.b(str, a4.f896b)) {
                            a4 = A.SPDY_3;
                            if (!D2.h.b(str, a4.f896b)) {
                                a4 = A.QUIC;
                                if (!D2.h.b(str, a4.f896b)) {
                                    throw new IOException("Unexpected protocol: " + str);
                                }
                            }
                        }
                    }
                }
            }
            return a4;
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    A(String str) {
        this.f896b = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.f896b;
    }
}
