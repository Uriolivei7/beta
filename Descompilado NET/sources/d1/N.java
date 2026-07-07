package d1;

import a1.C0210a;
import android.app.Application;
import com.facebook.react.bridge.JSExceptionHandler;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.bridge.UIManagerProvider;
import com.facebook.react.common.LifecycleState;
import d1.V;
import java.util.Iterator;
import java.util.List;
import k1.InterfaceC0585c;
import q1.InterfaceC0651b;

/* JADX INFO: loaded from: classes.dex */
public abstract class N {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Application f9149a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private J f9150b;

    class a implements e1.k {
        a() {
        }

        @Override // e1.k
        public e1.j g(String str) {
            return null;
        }
    }

    protected N(Application application) {
        this.f9149a = application;
    }

    protected J a() {
        ReactMarker.logMarker(ReactMarkerConstants.BUILD_REACT_INSTANCE_MANAGER_START);
        M mB = b();
        ReactMarker.logMarker(ReactMarkerConstants.BUILD_REACT_INSTANCE_MANAGER_END);
        return mB.b();
    }

    protected M b() {
        M mP = J.u().d(this.f9149a).n(j()).w(u()).h(f()).g(e()).t(r()).u(s()).m(i()).p(l());
        q();
        M mQ = mP.s(null).o(k()).v(t()).i(LifecycleState.f6516b).r(p()).l(h()).f(d()).q(n());
        Iterator it = m().iterator();
        while (it.hasNext()) {
            mQ.a((O) it.next());
        }
        String strG = g();
        if (strG != null) {
            mQ.j(strG);
        } else {
            mQ.e((String) C0210a.c(c()));
        }
        return mQ;
    }

    protected String c() {
        return "index.android.bundle";
    }

    protected InterfaceC0651b d() {
        return null;
    }

    protected InterfaceC0585c e() {
        return null;
    }

    protected com.facebook.react.devsupport.H f() {
        return null;
    }

    protected String g() {
        return null;
    }

    protected abstract EnumC0498f h();

    protected JSExceptionHandler i() {
        return null;
    }

    protected abstract String j();

    protected JavaScriptExecutorFactory k() {
        return null;
    }

    public boolean l() {
        return false;
    }

    protected abstract List m();

    protected k1.h n() {
        return null;
    }

    public synchronized J o() {
        try {
            if (this.f9150b == null) {
                ReactMarker.logMarker(ReactMarkerConstants.INIT_REACT_RUNTIME_START);
                ReactMarker.logMarker(ReactMarkerConstants.GET_REACT_INSTANCE_MANAGER_START);
                this.f9150b = a();
                ReactMarker.logMarker(ReactMarkerConstants.GET_REACT_INSTANCE_MANAGER_END);
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.f9150b;
    }

    protected abstract V.a p();

    protected k1.i q() {
        return null;
    }

    public boolean r() {
        return true;
    }

    public e1.k s() {
        return new a();
    }

    protected abstract UIManagerProvider t();

    public abstract boolean u();

    public synchronized boolean v() {
        return this.f9150b != null;
    }
}
