package z0;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;
import z0.InterfaceC0783b;

/* JADX INFO: renamed from: z0.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0785d extends C0782a {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static final a f11050e = new a(null);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final List f11051d = new ArrayList(2);

    /* JADX INFO: renamed from: z0.d$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    public final synchronized void A(InterfaceC0783b interfaceC0783b) {
        D2.h.f(interfaceC0783b, "listener");
        this.f11051d.add(interfaceC0783b);
    }

    public final synchronized void D(InterfaceC0783b interfaceC0783b) {
        D2.h.f(interfaceC0783b, "listener");
        this.f11051d.remove(interfaceC0783b);
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void a(String str, Object obj) {
        D2.h.f(str, "id");
        int size = this.f11051d.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                try {
                    ((InterfaceC0783b) this.f11051d.get(i3)).a(str, obj);
                    r rVar = r.f10584a;
                } catch (Exception e4) {
                    Log.e("FwdControllerListener2", "InternalListener exception in onIntermediateImageSet", e4);
                }
            } catch (IndexOutOfBoundsException unused) {
                return;
            }
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void o(String str, Object obj, InterfaceC0783b.a aVar) {
        D2.h.f(str, "id");
        int size = this.f11051d.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                try {
                    ((InterfaceC0783b) this.f11051d.get(i3)).o(str, obj, aVar);
                    r rVar = r.f10584a;
                } catch (Exception e4) {
                    Log.e("FwdControllerListener2", "InternalListener exception in onSubmit", e4);
                }
            } catch (IndexOutOfBoundsException unused) {
                return;
            }
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void q(String str) {
        D2.h.f(str, "id");
        int size = this.f11051d.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                try {
                    ((InterfaceC0783b) this.f11051d.get(i3)).q(str);
                    r rVar = r.f10584a;
                } catch (Exception e4) {
                    Log.e("FwdControllerListener2", "InternalListener exception in onIntermediateImageFailed", e4);
                }
            } catch (IndexOutOfBoundsException unused) {
                return;
            }
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void v(String str, InterfaceC0783b.a aVar) {
        D2.h.f(str, "id");
        int size = this.f11051d.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                try {
                    ((InterfaceC0783b) this.f11051d.get(i3)).v(str, aVar);
                    r rVar = r.f10584a;
                } catch (Exception e4) {
                    Log.e("FwdControllerListener2", "InternalListener exception in onRelease", e4);
                }
            } catch (IndexOutOfBoundsException unused) {
                return;
            }
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void y(String str, Throwable th, InterfaceC0783b.a aVar) {
        D2.h.f(str, "id");
        int size = this.f11051d.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                try {
                    ((InterfaceC0783b) this.f11051d.get(i3)).y(str, th, aVar);
                    r rVar = r.f10584a;
                } catch (Exception e4) {
                    Log.e("FwdControllerListener2", "InternalListener exception in onFailure", e4);
                }
            } catch (IndexOutOfBoundsException unused) {
                return;
            }
        }
    }

    @Override // z0.C0782a, z0.InterfaceC0783b
    public void z(String str, Object obj, InterfaceC0783b.a aVar) {
        D2.h.f(str, "id");
        int size = this.f11051d.size();
        for (int i3 = 0; i3 < size; i3++) {
            try {
                try {
                    ((InterfaceC0783b) this.f11051d.get(i3)).z(str, obj, aVar);
                    r rVar = r.f10584a;
                } catch (Exception e4) {
                    Log.e("FwdControllerListener2", "InternalListener exception in onFinalImageSet", e4);
                }
            } catch (IndexOutOfBoundsException unused) {
                return;
            }
        }
    }
}
