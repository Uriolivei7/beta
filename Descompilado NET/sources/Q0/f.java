package q0;

import android.graphics.drawable.Animatable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class f implements InterfaceC0648d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List f10392b = new ArrayList(2);

    private synchronized void e(String str, Throwable th) {
        Log.e("FdingControllerListener", str, th);
    }

    @Override // q0.InterfaceC0648d
    public void a(String str, Object obj) {
        int size = this.f10392b.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                InterfaceC0648d interfaceC0648d = (InterfaceC0648d) this.f10392b.get(i3);
                if (interfaceC0648d != null) {
                    interfaceC0648d.a(str, obj);
                }
            } catch (Exception e4) {
                e("InternalListener exception in onIntermediateImageSet", e4);
            }
        }
    }

    @Override // q0.InterfaceC0648d
    public synchronized void b(String str) {
        int size = this.f10392b.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                InterfaceC0648d interfaceC0648d = (InterfaceC0648d) this.f10392b.get(i3);
                if (interfaceC0648d != null) {
                    interfaceC0648d.b(str);
                }
            } catch (Exception e4) {
                e("InternalListener exception in onRelease", e4);
            }
        }
    }

    public synchronized void c(InterfaceC0648d interfaceC0648d) {
        this.f10392b.add(interfaceC0648d);
    }

    public synchronized void d() {
        this.f10392b.clear();
    }

    @Override // q0.InterfaceC0648d
    public synchronized void j(String str, Object obj) {
        int size = this.f10392b.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                InterfaceC0648d interfaceC0648d = (InterfaceC0648d) this.f10392b.get(i3);
                if (interfaceC0648d != null) {
                    interfaceC0648d.j(str, obj);
                }
            } catch (Exception e4) {
                e("InternalListener exception in onSubmit", e4);
            }
        }
    }

    @Override // q0.InterfaceC0648d
    public synchronized void k(String str, Object obj, Animatable animatable) {
        int size = this.f10392b.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                InterfaceC0648d interfaceC0648d = (InterfaceC0648d) this.f10392b.get(i3);
                if (interfaceC0648d != null) {
                    interfaceC0648d.k(str, obj, animatable);
                }
            } catch (Exception e4) {
                e("InternalListener exception in onFinalImageSet", e4);
            }
        }
    }

    @Override // q0.InterfaceC0648d
    public void l(String str, Throwable th) {
        int size = this.f10392b.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                InterfaceC0648d interfaceC0648d = (InterfaceC0648d) this.f10392b.get(i3);
                if (interfaceC0648d != null) {
                    interfaceC0648d.l(str, th);
                }
            } catch (Exception e4) {
                e("InternalListener exception in onIntermediateImageFailed", e4);
            }
        }
    }

    @Override // q0.InterfaceC0648d
    public synchronized void r(String str, Throwable th) {
        int size = this.f10392b.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                InterfaceC0648d interfaceC0648d = (InterfaceC0648d) this.f10392b.get(i3);
                if (interfaceC0648d != null) {
                    interfaceC0648d.r(str, th);
                }
            } catch (Exception e4) {
                e("InternalListener exception in onFailure", e4);
            }
        }
    }
}
