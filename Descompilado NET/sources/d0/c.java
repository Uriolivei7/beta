package D0;

import D2.h;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f150c = new a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final c f151d = new c("UNKNOWN", null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f152a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f153b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public interface b {
        int a();

        c b(byte[] bArr, int i3);
    }

    public c(String str, String str2) {
        h.f(str, "name");
        this.f152a = str;
        this.f153b = str2;
    }

    public final String a() {
        return this.f152a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        return h.b(this.f152a, cVar.f152a) && h.b(this.f153b, cVar.f153b);
    }

    public int hashCode() {
        int iHashCode = this.f152a.hashCode() * 31;
        String str = this.f153b;
        return iHashCode + (str == null ? 0 : str.hashCode());
    }

    public String toString() {
        return this.f152a;
    }
}
