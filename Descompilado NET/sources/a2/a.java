package A2;

import java.io.Closeable;
import java.io.IOException;
import r2.AbstractC0678a;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    public static final void a(Closeable closeable, Throwable th) throws IOException {
        if (closeable != null) {
            if (th == null) {
                closeable.close();
                return;
            }
            try {
                closeable.close();
            } catch (Throwable th2) {
                AbstractC0678a.a(th, th2);
            }
        }
    }
}
