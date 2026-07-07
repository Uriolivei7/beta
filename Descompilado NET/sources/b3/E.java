package b3;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;

/* JADX INFO: loaded from: classes.dex */
final class E extends C0324g {

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final Socket f5604m;

    public E(Socket socket) {
        D2.h.f(socket, "socket");
        this.f5604m = socket;
    }

    @Override // b3.C0324g
    protected IOException t(IOException iOException) {
        SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
        if (iOException != null) {
            socketTimeoutException.initCause(iOException);
        }
        return socketTimeoutException;
    }

    @Override // b3.C0324g
    protected void x() {
        try {
            this.f5604m.close();
        } catch (AssertionError e4) {
            if (!t.e(e4)) {
                throw e4;
            }
            u.f5660a.log(Level.WARNING, "Failed to close timed out socket " + this.f5604m, (Throwable) e4);
        } catch (Exception e5) {
            u.f5660a.log(Level.WARNING, "Failed to close timed out socket " + this.f5604m, (Throwable) e5);
        }
    }
}
