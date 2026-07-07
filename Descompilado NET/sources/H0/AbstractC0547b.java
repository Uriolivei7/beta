package h0;

/* JADX INFO: renamed from: h0.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0547b implements InterfaceC0550e {
    @Override // h0.InterfaceC0550e
    public void a(InterfaceC0548c interfaceC0548c) {
        boolean zE = interfaceC0548c.e();
        try {
            f(interfaceC0548c);
        } finally {
            if (zE) {
                interfaceC0548c.close();
            }
        }
    }

    @Override // h0.InterfaceC0550e
    public void c(InterfaceC0548c interfaceC0548c) {
        try {
            e(interfaceC0548c);
        } finally {
            interfaceC0548c.close();
        }
    }

    protected abstract void e(InterfaceC0548c interfaceC0548c);

    protected abstract void f(InterfaceC0548c interfaceC0548c);

    @Override // h0.InterfaceC0550e
    public void b(InterfaceC0548c interfaceC0548c) {
    }

    @Override // h0.InterfaceC0550e
    public void d(InterfaceC0548c interfaceC0548c) {
    }
}
