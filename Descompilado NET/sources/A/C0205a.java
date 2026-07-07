package a;

import D2.h;
import android.content.Context;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: renamed from: a.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0205a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Set f2848a = new CopyOnWriteArraySet();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile Context f2849b;

    public final void a(InterfaceC0206b interfaceC0206b) {
        h.f(interfaceC0206b, "listener");
        Context context = this.f2849b;
        if (context != null) {
            interfaceC0206b.a(context);
        }
        this.f2848a.add(interfaceC0206b);
    }

    public final void b() {
        this.f2849b = null;
    }

    public final void c(Context context) {
        h.f(context, "context");
        this.f2849b = context;
        Iterator it = this.f2848a.iterator();
        while (it.hasNext()) {
            ((InterfaceC0206b) it.next()).a(context);
        }
    }
}
