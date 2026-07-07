package Q;

import D2.h;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class b implements Q.a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f1794b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final File f1795a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final b a(File file) {
            h.f(file, "file");
            return new b(file, null);
        }

        public final b b(File file) {
            DefaultConstructorMarker defaultConstructorMarker = null;
            if (file != null) {
                return new b(file, defaultConstructorMarker);
            }
            return null;
        }

        private a() {
        }
    }

    public /* synthetic */ b(File file, DefaultConstructorMarker defaultConstructorMarker) {
        this(file);
    }

    public static final b b(File file) {
        return f1794b.a(file);
    }

    public static final b c(File file) {
        return f1794b.b(file);
    }

    @Override // Q.a
    public InputStream a() {
        return new FileInputStream(this.f1795a);
    }

    public final File d() {
        return this.f1795a;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof b)) {
            return false;
        }
        return h.b(this.f1795a, ((b) obj).f1795a);
    }

    public int hashCode() {
        return this.f1795a.hashCode();
    }

    @Override // Q.a
    public long size() {
        return this.f1795a.length();
    }

    private b(File file) {
        this.f1795a = file;
    }
}
