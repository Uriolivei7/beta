package b3;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/* JADX INFO: loaded from: classes.dex */
public abstract class t {
    public static final D a(File file) {
        return u.b(file);
    }

    public static final D b() {
        return v.a();
    }

    public static final j c(D d4) {
        return v.b(d4);
    }

    public static final k d(F f3) {
        return v.c(f3);
    }

    public static final boolean e(AssertionError assertionError) {
        return u.c(assertionError);
    }

    public static final D f(File file) {
        return u.g(file, false, 1, null);
    }

    public static final D g(File file, boolean z3) {
        return u.d(file, z3);
    }

    public static final D h(OutputStream outputStream) {
        return u.e(outputStream);
    }

    public static final D i(Socket socket) {
        return u.f(socket);
    }

    public static final F k(File file) {
        return u.h(file);
    }

    public static final F l(InputStream inputStream) {
        return u.i(inputStream);
    }

    public static final F m(Socket socket) {
        return u.j(socket);
    }
}
