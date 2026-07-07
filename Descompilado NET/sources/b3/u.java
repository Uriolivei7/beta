package b3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
abstract /* synthetic */ class u {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Logger f5660a = Logger.getLogger("okio.Okio");

    public static final D b(File file) {
        D2.h.f(file, "$this$appendingSink");
        return t.h(new FileOutputStream(file, true));
    }

    public static final boolean c(AssertionError assertionError) {
        D2.h.f(assertionError, "$this$isAndroidGetsocknameError");
        if (assertionError.getCause() == null) {
            return false;
        }
        String message = assertionError.getMessage();
        return message != null ? K2.o.E(message, "getsockname failed", false, 2, null) : false;
    }

    public static final D d(File file, boolean z3) {
        D2.h.f(file, "$this$sink");
        return t.h(new FileOutputStream(file, z3));
    }

    public static final D e(OutputStream outputStream) {
        D2.h.f(outputStream, "$this$sink");
        return new x(outputStream, new G());
    }

    public static final D f(Socket socket) throws IOException {
        D2.h.f(socket, "$this$sink");
        E e4 = new E(socket);
        OutputStream outputStream = socket.getOutputStream();
        D2.h.e(outputStream, "getOutputStream()");
        return e4.v(new x(outputStream, e4));
    }

    public static /* synthetic */ D g(File file, boolean z3, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            z3 = false;
        }
        return t.g(file, z3);
    }

    public static final F h(File file) {
        D2.h.f(file, "$this$source");
        return t.l(new FileInputStream(file));
    }

    public static final F i(InputStream inputStream) {
        D2.h.f(inputStream, "$this$source");
        return new s(inputStream, new G());
    }

    public static final F j(Socket socket) throws IOException {
        D2.h.f(socket, "$this$source");
        E e4 = new E(socket);
        InputStream inputStream = socket.getInputStream();
        D2.h.e(inputStream, "getInputStream()");
        return e4.w(new s(inputStream, e4));
    }
}
