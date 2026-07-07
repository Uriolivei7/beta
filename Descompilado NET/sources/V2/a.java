package V2;

import D2.h;
import b3.D;
import b3.F;
import b3.t;
import b3.u;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public interface a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final C0038a f2679b = new C0038a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f2678a = new C0038a.C0039a();

    /* JADX INFO: renamed from: V2.a$a, reason: collision with other inner class name */
    public static final class C0038a {

        /* JADX INFO: renamed from: V2.a$a$a, reason: collision with other inner class name */
        private static final class C0039a implements a {
            @Override // V2.a
            public void a(File file) throws IOException {
                h.f(file, "file");
                if (file.delete() || !file.exists()) {
                    return;
                }
                throw new IOException("failed to delete " + file);
            }

            @Override // V2.a
            public F b(File file) {
                h.f(file, "file");
                return t.k(file);
            }

            @Override // V2.a
            public D c(File file) {
                h.f(file, "file");
                try {
                    return u.g(file, false, 1, null);
                } catch (FileNotFoundException unused) {
                    file.getParentFile().mkdirs();
                    return u.g(file, false, 1, null);
                }
            }

            @Override // V2.a
            public void d(File file) throws IOException {
                h.f(file, "directory");
                File[] fileArrListFiles = file.listFiles();
                if (fileArrListFiles == null) {
                    throw new IOException("not a readable directory: " + file);
                }
                for (File file2 : fileArrListFiles) {
                    h.e(file2, "file");
                    if (file2.isDirectory()) {
                        d(file2);
                    }
                    if (!file2.delete()) {
                        throw new IOException("failed to delete " + file2);
                    }
                }
            }

            @Override // V2.a
            public D e(File file) {
                h.f(file, "file");
                try {
                    return t.a(file);
                } catch (FileNotFoundException unused) {
                    file.getParentFile().mkdirs();
                    return t.a(file);
                }
            }

            @Override // V2.a
            public boolean f(File file) {
                h.f(file, "file");
                return file.exists();
            }

            @Override // V2.a
            public void g(File file, File file2) throws IOException {
                h.f(file, "from");
                h.f(file2, "to");
                a(file2);
                if (file.renameTo(file2)) {
                    return;
                }
                throw new IOException("failed to rename " + file + " to " + file2);
            }

            @Override // V2.a
            public long h(File file) {
                h.f(file, "file");
                return file.length();
            }

            public String toString() {
                return "FileSystem.SYSTEM";
            }
        }

        private C0038a() {
        }

        public /* synthetic */ C0038a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    void a(File file);

    F b(File file);

    D c(File file);

    void d(File file);

    D e(File file);

    boolean f(File file);

    void g(File file, File file2);

    long h(File file);
}
