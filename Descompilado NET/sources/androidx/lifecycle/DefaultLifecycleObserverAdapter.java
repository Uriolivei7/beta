package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultLifecycleObserverAdapter implements InterfaceC0302j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final InterfaceC0295c f5264a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final InterfaceC0302j f5265b;

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f5266a;

        static {
            int[] iArr = new int[AbstractC0299g.a.values().length];
            try {
                iArr[AbstractC0299g.a.ON_CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[AbstractC0299g.a.ON_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[AbstractC0299g.a.ON_RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[AbstractC0299g.a.ON_PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[AbstractC0299g.a.ON_STOP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[AbstractC0299g.a.ON_DESTROY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[AbstractC0299g.a.ON_ANY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            f5266a = iArr;
        }
    }

    public DefaultLifecycleObserverAdapter(InterfaceC0295c interfaceC0295c, InterfaceC0302j interfaceC0302j) {
        D2.h.f(interfaceC0295c, "defaultLifecycleObserver");
        this.f5264a = interfaceC0295c;
        this.f5265b = interfaceC0302j;
    }

    @Override // androidx.lifecycle.InterfaceC0302j
    public void d(l lVar, AbstractC0299g.a aVar) {
        D2.h.f(lVar, "source");
        D2.h.f(aVar, "event");
        switch (a.f5266a[aVar.ordinal()]) {
            case 1:
                this.f5264a.c(lVar);
                break;
            case 2:
                this.f5264a.f(lVar);
                break;
            case 3:
                this.f5264a.a(lVar);
                break;
            case 4:
                this.f5264a.e(lVar);
                break;
            case 5:
                this.f5264a.g(lVar);
                break;
            case 6:
                this.f5264a.b(lVar);
                break;
            case 7:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
        }
        InterfaceC0302j interfaceC0302j = this.f5265b;
        if (interfaceC0302j != null) {
            interfaceC0302j.d(lVar, aVar);
        }
    }
}
