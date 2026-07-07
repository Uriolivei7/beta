package W;

import X.k;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class c {

    public static class a extends IOException {
        public a(String str) {
            super(str);
        }

        public a(String str, Throwable th) {
            super(str);
            initCause(th);
        }
    }

    public static class b extends IOException {
        public b(String str) {
            super(str);
        }
    }

    /* JADX INFO: renamed from: W.c$c, reason: collision with other inner class name */
    public static class C0040c extends FileNotFoundException {
        public C0040c(String str) {
            super(str);
        }
    }

    public static class d extends IOException {
        public d(String str) {
            super(str);
        }

        public d(String str, Throwable th) {
            super(str);
            initCause(th);
        }
    }

    public static void a(File file) throws a {
        if (file.exists()) {
            if (file.isDirectory()) {
                return;
            }
            if (!file.delete()) {
                throw new a(file.getAbsolutePath(), new b(file.getAbsolutePath()));
            }
        }
        if (!file.mkdirs() && !file.isDirectory()) {
            throw new a(file.getAbsolutePath());
        }
    }

    public static void b(File file, File file2) throws d {
        k.g(file);
        k.g(file2);
        file2.delete();
        if (file.renameTo(file2)) {
            return;
        }
        throw new d("Unknown error renaming " + file.getAbsolutePath() + " to " + file2.getAbsolutePath(), !file2.exists() ? file.getParentFile().exists() ? !file.exists() ? new FileNotFoundException(file.getAbsolutePath()) : null : new C0040c(file.getAbsolutePath()) : new b(file2.getAbsolutePath()));
    }
}
