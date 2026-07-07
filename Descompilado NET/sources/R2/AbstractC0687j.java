package r2;

import java.io.Serializable;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: r2.j, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0687j implements Serializable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f10572b = new a(null);

    /* JADX INFO: renamed from: r2.j$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX INFO: renamed from: r2.j$b */
    public static final class b implements Serializable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final Throwable f10573b;

        public b(Throwable th) {
            D2.h.f(th, "exception");
            this.f10573b = th;
        }

        public boolean equals(Object obj) {
            return (obj instanceof b) && D2.h.b(this.f10573b, ((b) obj).f10573b);
        }

        public int hashCode() {
            return this.f10573b.hashCode();
        }

        public String toString() {
            return "Failure(" + this.f10573b + ')';
        }
    }

    public static final boolean b(Object obj) {
        return obj instanceof b;
    }

    public static Object a(Object obj) {
        return obj;
    }
}
