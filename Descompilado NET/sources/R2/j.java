package R2;

import java.io.IOException;
import r2.AbstractC0678a;

/* JADX INFO: loaded from: classes.dex */
public final class j extends RuntimeException {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private IOException f2192b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final IOException f2193c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j(IOException iOException) {
        super(iOException);
        D2.h.f(iOException, "firstConnectException");
        this.f2193c = iOException;
        this.f2192b = iOException;
    }

    public final void a(IOException iOException) {
        D2.h.f(iOException, "e");
        AbstractC0678a.a(this.f2193c, iOException);
        this.f2192b = iOException;
    }

    public final IOException b() {
        return this.f2193c;
    }

    public final IOException c() {
        return this.f2192b;
    }
}
