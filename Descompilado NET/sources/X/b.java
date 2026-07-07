package X;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static final Logger f2733a = Logger.getLogger(b.class.getName());

    public static void a(Closeable closeable, boolean z3) throws IOException {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e4) {
            if (!z3) {
                throw e4;
            }
            f2733a.log(Level.WARNING, "IOException thrown while closing Closeable.", (Throwable) e4);
        }
    }

    public static void b(InputStream inputStream) {
        try {
            a(inputStream, true);
        } catch (IOException e4) {
            throw new AssertionError(e4);
        }
    }
}
