package androidx.lifecycle;

import android.content.Context;
import androidx.lifecycle.t;
import java.util.List;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class ProcessLifecycleInitializer implements H.a {
    @Override // H.a
    public List a() {
        return AbstractC0717n.g();
    }

    @Override // H.a
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public l b(Context context) {
        D2.h.f(context, "context");
        androidx.startup.a aVarE = androidx.startup.a.e(context);
        D2.h.e(aVarE, "getInstance(context)");
        if (!aVarE.g(ProcessLifecycleInitializer.class)) {
            throw new IllegalStateException("ProcessLifecycleInitializer cannot be initialized lazily.\n               Please ensure that you have:\n               <meta-data\n                   android:name='androidx.lifecycle.ProcessLifecycleInitializer'\n                   android:value='androidx.startup' />\n               under InitializationProvider in your AndroidManifest.xml");
        }
        C0301i.a(context);
        t.b bVar = t.f5346j;
        bVar.b(context);
        return bVar.a();
    }
}
