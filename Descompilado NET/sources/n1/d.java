package n1;

import android.view.View;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.fabric.events.EventEmitterWrapper;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.RootViewManager;
import com.facebook.react.uimanager.U0;
import com.facebook.yoga.p;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static final String f9863i = "d";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private g f9866c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private g f9867d;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final U0 f9869f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final a f9870g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ConcurrentHashMap f9864a = new ConcurrentHashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final CopyOnWriteArrayList f9865b = new CopyOnWriteArrayList();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final K1.a f9868e = new K1.a();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final RootViewManager f9871h = new RootViewManager();

    public interface a {
        void a(Queue queue);
    }

    public d(U0 u02, a aVar) {
        this.f9869f = u02;
        this.f9870g = aVar;
    }

    private g j(int i3, int i4) {
        return i3 == -1 ? h(i4) : f(i3);
    }

    public void a(int i3, View view, B0 b02) {
        g gVarG = g(i3, "attachView");
        if (gVarG.u()) {
            ReactSoftExceptionLogger.logSoftException(f9863i, new IllegalStateException("Trying to attach a view to a stopped surface"));
        } else {
            gVarG.f(view, b02);
        }
    }

    public void b() {
        this.f9868e.b();
    }

    public void c(int i3, int i4, String str, boolean z3, WritableMap writableMap, int i5) {
        g gVarJ = j(i3, i4);
        if (gVarJ == null) {
            Y.a.d(f9863i, "Cannot queue event without valid surface mounting manager for tag: %d, surfaceId: %d", Integer.valueOf(i4), Integer.valueOf(i3));
        } else {
            gVarJ.j(i4, str, z3, writableMap, i5);
        }
    }

    public void d(ReactContext reactContext, String str, int i3, int i4, com.facebook.react.common.mapbuffer.a aVar) {
        this.f9869f.c(str).experimental_prefetchResource(reactContext, i3, i4, aVar);
    }

    public EventEmitterWrapper e(int i3, int i4) {
        g gVarJ = j(i3, i4);
        if (gVarJ == null) {
            return null;
        }
        return gVarJ.m(i4);
    }

    public g f(int i3) {
        g gVar = this.f9867d;
        if (gVar != null && gVar.o() == i3) {
            return this.f9867d;
        }
        g gVar2 = this.f9866c;
        if (gVar2 != null && gVar2.o() == i3) {
            return this.f9866c;
        }
        g gVar3 = (g) this.f9864a.get(Integer.valueOf(i3));
        this.f9867d = gVar3;
        return gVar3;
    }

    public g g(int i3, String str) {
        g gVarF = f(i3);
        if (gVarF != null) {
            return gVarF;
        }
        throw new RetryableMountingLayerException("Unable to find SurfaceMountingManager for surfaceId: [" + i3 + "]. Context: " + str);
    }

    public g h(int i3) {
        g gVar = this.f9866c;
        if (gVar != null && gVar.q(i3)) {
            return this.f9866c;
        }
        Iterator it = this.f9864a.entrySet().iterator();
        while (it.hasNext()) {
            g gVar2 = (g) ((Map.Entry) it.next()).getValue();
            if (gVar2 != this.f9866c && gVar2.q(i3)) {
                if (this.f9866c == null) {
                    this.f9866c = gVar2;
                }
                return gVar2;
            }
        }
        return null;
    }

    public g i(int i3) {
        g gVarH = h(i3);
        if (gVarH != null) {
            return gVarH;
        }
        throw new RetryableMountingLayerException("Unable to find SurfaceMountingManager for tag: [" + i3 + "]");
    }

    public boolean k(int i3) {
        return h(i3) != null;
    }

    public boolean l(int i3) {
        g gVarF = f(i3);
        if (gVarF == null || gVarF.u()) {
            return false;
        }
        return !gVarF.t();
    }

    public long m(ReactContext reactContext, String str, ReadableMap readableMap, ReadableMap readableMap2, ReadableMap readableMap3, float f3, p pVar, float f4, p pVar2, float[] fArr) {
        return this.f9869f.c(str).measure(reactContext, readableMap, readableMap2, readableMap3, f3, pVar, f4, pVar2, fArr);
    }

    public long n(ReactContext reactContext, String str, com.facebook.react.common.mapbuffer.a aVar, com.facebook.react.common.mapbuffer.a aVar2, com.facebook.react.common.mapbuffer.a aVar3, float f3, p pVar, float f4, p pVar2, float[] fArr) {
        return this.f9869f.c(str).measure(reactContext, aVar, aVar2, aVar3, f3, pVar, f4, pVar2, fArr);
    }

    public void o(int i3, int i4, int i5, ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        g(i3, "receiveCommand:int").C(i4, i5, readableArray);
    }

    public void p(int i3, int i4, String str, ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        g(i3, "receiveCommand:string").D(i4, str, readableArray);
    }

    public void q(int i3, int i4, int i5) {
        UiThreadUtil.assertOnUiThread();
        if (i3 == -1) {
            i(i4).G(i4, i5);
        } else {
            g(i3, "sendAccessibilityEvent").G(i4, i5);
        }
    }

    public g r(int i3, B0 b02, View view) {
        g gVar = new g(i3, this.f9868e, this.f9869f, this.f9871h, this.f9870g, b02);
        this.f9864a.putIfAbsent(Integer.valueOf(i3), gVar);
        if (this.f9864a.get(Integer.valueOf(i3)) != gVar) {
            ReactSoftExceptionLogger.logSoftException(f9863i, new IllegalStateException("Called startSurface more than once for the SurfaceId [" + i3 + "]"));
        }
        this.f9866c = (g) this.f9864a.get(Integer.valueOf(i3));
        if (view != null) {
            gVar.f(view, b02);
        }
        return gVar;
    }

    public void s(int i3) {
        g gVar = (g) this.f9864a.get(Integer.valueOf(i3));
        if (gVar == null) {
            ReactSoftExceptionLogger.logSoftException(f9863i, new IllegalStateException("Cannot call stopSurface on non-existent surface: [" + i3 + "]"));
            return;
        }
        while (this.f9865b.size() >= 15) {
            Integer num = (Integer) this.f9865b.get(0);
            ConcurrentHashMap concurrentHashMap = this.f9864a;
            num.intValue();
            concurrentHashMap.remove(num);
            this.f9865b.remove(num);
            Y.a.c(f9863i, "Removing stale SurfaceMountingManager: [%d]", num);
        }
        this.f9865b.add(Integer.valueOf(i3));
        gVar.I();
        if (this.f9866c == gVar) {
            this.f9866c = null;
        }
        if (this.f9867d == gVar) {
            this.f9867d = null;
        }
    }

    public boolean t(int i3) {
        if (this.f9865b.contains(Integer.valueOf(i3))) {
            return true;
        }
        g gVarF = f(i3);
        return gVarF != null && gVarF.u();
    }

    public void u(int i3, ReadableMap readableMap) {
        UiThreadUtil.assertOnUiThread();
        if (readableMap == null) {
            return;
        }
        i(i3).O(i3, readableMap);
    }
}
