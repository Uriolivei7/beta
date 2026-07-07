package V0;

import D2.h;
import V0.b;
import android.os.Trace;

/* JADX INFO: loaded from: classes.dex */
public final class a implements b.c {
    @Override // V0.b.c
    public void a(String str) {
        h.f(str, "name");
        if (isTracing()) {
            Trace.beginSection(str);
        }
    }

    @Override // V0.b.c
    public void b() {
        if (isTracing()) {
            Trace.endSection();
        }
    }

    @Override // V0.b.c
    public boolean isTracing() {
        return false;
    }
}
