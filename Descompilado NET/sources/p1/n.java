package P1;

import P1.d;
import a1.C0210a;
import android.view.MotionEvent;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.events.RCTModernEventEmitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class n extends d {

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static final String f1651n = "n";

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final q.f f1652o = new q.f(6);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private MotionEvent f1653h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private String f1654i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private short f1655j = -1;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private List f1656k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private b f1657l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private d.b f1658m;

    class a implements d.b {
        a() {
        }

        @Override // P1.d.b
        public boolean a(int i3, String str) {
            if (!str.equals(n.this.f1654i)) {
                return false;
            }
            if (!o.f(str)) {
                return n.this.o() == i3;
            }
            Iterator it = n.this.f1657l.e().iterator();
            while (it.hasNext()) {
                if (((C0.b) it.next()).b() == i3) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f1660a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f1661b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f1662c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f1663d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private Map f1664e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private Map f1665f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private Map f1666g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private Map f1667h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private Set f1668i;

        public b(int i3, int i4, int i5, int i6, Map<Integer, float[]> map, Map<Integer, List<C0.b>> map2, Map<Integer, float[]> map3, Map<Integer, float[]> map4, Set<Integer> set) {
            this.f1660a = i3;
            this.f1661b = i4;
            this.f1662c = i5;
            this.f1663d = i6;
            this.f1664e = map;
            this.f1665f = map2;
            this.f1666g = map3;
            this.f1667h = map4;
            this.f1668i = new HashSet(set);
        }

        public int b() {
            return this.f1661b;
        }

        public final Map c() {
            return this.f1666g;
        }

        public final Map d() {
            return this.f1665f;
        }

        public final List e() {
            return (List) this.f1665f.get(Integer.valueOf(this.f1661b));
        }

        public Set f() {
            return this.f1668i;
        }

        public int g() {
            return this.f1662c;
        }

        public final Map h() {
            return this.f1664e;
        }

        public int i() {
            return this.f1660a;
        }

        public final Map j() {
            return this.f1667h;
        }

        public int k() {
            return this.f1663d;
        }

        public boolean l(int i3) {
            return this.f1668i.contains(Integer.valueOf(i3));
        }
    }

    private n() {
    }

    private void A(String str, int i3, b bVar, MotionEvent motionEvent, short s3) {
        super.r(bVar.k(), i3, motionEvent.getEventTime());
        this.f1654i = str;
        this.f1653h = MotionEvent.obtain(motionEvent);
        this.f1655j = s3;
        this.f1657l = bVar;
    }

    private boolean B() {
        return this.f1654i.equals("topClick");
    }

    public static n C(String str, int i3, b bVar, MotionEvent motionEvent) {
        n nVar = (n) f1652o.b();
        if (nVar == null) {
            nVar = new n();
        }
        nVar.A(str, i3, bVar, (MotionEvent) C0210a.c(motionEvent), (short) 0);
        return nVar;
    }

    public static n D(String str, int i3, b bVar, MotionEvent motionEvent, short s3) {
        n nVar = (n) f1652o.b();
        if (nVar == null) {
            nVar = new n();
        }
        nVar.A(str, i3, bVar, (MotionEvent) C0210a.c(motionEvent), s3);
        return nVar;
    }

    private void w(WritableMap writableMap, int i3) {
        writableMap.putBoolean("ctrlKey", (i3 & 4096) != 0);
        writableMap.putBoolean("shiftKey", (i3 & 1) != 0);
        writableMap.putBoolean("altKey", (i3 & 2) != 0);
        writableMap.putBoolean("metaKey", (i3 & 65536) != 0);
    }

    private List x() {
        int actionIndex;
        actionIndex = this.f1653h.getActionIndex();
        String str = this.f1654i;
        str.hashCode();
        switch (str) {
            case "topPointerEnter":
            case "topPointerLeave":
            case "topPointerDown":
            case "topPointerOver":
            case "topPointerUp":
            case "topClick":
            case "topPointerOut":
                return Arrays.asList(y(actionIndex));
            case "topPointerMove":
            case "topPointerCancel":
                return z();
            default:
                return null;
        }
    }

    private WritableMap y(int i3) {
        WritableMap writableMapCreateMap = Arguments.createMap();
        int pointerId = this.f1653h.getPointerId(i3);
        writableMapCreateMap.putDouble("pointerId", pointerId);
        String strE = o.e(this.f1653h.getToolType(i3));
        writableMapCreateMap.putString("pointerType", strE);
        writableMapCreateMap.putBoolean("isPrimary", !B() && (this.f1657l.l(pointerId) || pointerId == this.f1657l.f1660a));
        float[] fArr = (float[]) this.f1657l.c().get(Integer.valueOf(pointerId));
        double dF = C0429f0.f(fArr[0]);
        double dF2 = C0429f0.f(fArr[1]);
        writableMapCreateMap.putDouble("clientX", dF);
        writableMapCreateMap.putDouble("clientY", dF2);
        float[] fArr2 = (float[]) this.f1657l.j().get(Integer.valueOf(pointerId));
        double dF3 = C0429f0.f(fArr2[0]);
        double dF4 = C0429f0.f(fArr2[1]);
        writableMapCreateMap.putDouble("screenX", dF3);
        writableMapCreateMap.putDouble("screenY", dF4);
        writableMapCreateMap.putDouble("x", dF);
        writableMapCreateMap.putDouble("y", dF2);
        writableMapCreateMap.putDouble("pageX", dF);
        writableMapCreateMap.putDouble("pageY", dF2);
        float[] fArr3 = (float[]) this.f1657l.h().get(Integer.valueOf(pointerId));
        writableMapCreateMap.putDouble("offsetX", C0429f0.f(fArr3[0]));
        writableMapCreateMap.putDouble("offsetY", C0429f0.f(fArr3[1]));
        writableMapCreateMap.putInt("target", o());
        writableMapCreateMap.putDouble("timestamp", m());
        writableMapCreateMap.putInt("detail", 0);
        writableMapCreateMap.putDouble("tiltX", 0.0d);
        writableMapCreateMap.putDouble("tiltY", 0.0d);
        writableMapCreateMap.putInt("twist", 0);
        if (strE.equals("mouse") || B()) {
            writableMapCreateMap.putDouble("width", 1.0d);
            writableMapCreateMap.putDouble("height", 1.0d);
        } else {
            double dF5 = C0429f0.f(this.f1653h.getTouchMajor(i3));
            writableMapCreateMap.putDouble("width", dF5);
            writableMapCreateMap.putDouble("height", dF5);
        }
        int buttonState = this.f1653h.getButtonState();
        writableMapCreateMap.putInt("button", o.a(strE, this.f1657l.g(), buttonState));
        writableMapCreateMap.putInt("buttons", o.b(this.f1654i, strE, buttonState));
        writableMapCreateMap.putDouble("pressure", B() ? 0.0d : o.d(writableMapCreateMap.getInt("buttons"), this.f1654i));
        writableMapCreateMap.putDouble("tangentialPressure", 0.0d);
        w(writableMapCreateMap, this.f1653h.getMetaState());
        return writableMapCreateMap;
    }

    private List z() {
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < this.f1653h.getPointerCount(); i3++) {
            arrayList.add(y(i3));
        }
        return arrayList;
    }

    @Override // P1.d
    public void c(RCTEventEmitter rCTEventEmitter) {
        if (this.f1653h == null) {
            ReactSoftExceptionLogger.logSoftException(f1651n, new IllegalStateException("Cannot dispatch a Pointer that has no MotionEvent; the PointerEvehas been recycled"));
            return;
        }
        if (this.f1656k == null) {
            this.f1656k = x();
        }
        List list = this.f1656k;
        if (list == null) {
            return;
        }
        boolean z3 = list.size() > 1;
        for (WritableMap writableMapCopy : this.f1656k) {
            if (z3) {
                writableMapCopy = writableMapCopy.copy();
            }
            rCTEventEmitter.receiveEvent(o(), this.f1654i, writableMapCopy);
        }
    }

    @Override // P1.d
    public void d(RCTModernEventEmitter rCTModernEventEmitter) {
        if (this.f1653h == null) {
            ReactSoftExceptionLogger.logSoftException(f1651n, new IllegalStateException("Cannot dispatch a Pointer that has no MotionEvent; the PointerEvehas been recycled"));
            return;
        }
        if (this.f1656k == null) {
            this.f1656k = x();
        }
        List list = this.f1656k;
        if (list == null) {
            return;
        }
        boolean z3 = list.size() > 1;
        for (WritableMap writableMapCopy : this.f1656k) {
            if (z3) {
                writableMapCopy = writableMapCopy.copy();
            }
            WritableMap writableMap = writableMapCopy;
            int iL = l();
            int iO = o();
            String str = this.f1654i;
            short s3 = this.f1655j;
            rCTModernEventEmitter.receiveEvent(iL, iO, str, s3 != -1, s3, writableMap, o.c(str));
        }
    }

    @Override // P1.d
    public short g() {
        return this.f1655j;
    }

    @Override // P1.d
    public d.b h() {
        if (this.f1658m == null) {
            this.f1658m = new a();
        }
        return this.f1658m;
    }

    @Override // P1.d
    public String k() {
        return this.f1654i;
    }

    @Override // P1.d
    public void t() {
        this.f1656k = null;
        MotionEvent motionEvent = this.f1653h;
        this.f1653h = null;
        if (motionEvent != null) {
            motionEvent.recycle();
        }
        try {
            f1652o.a(this);
        } catch (IllegalStateException e4) {
            ReactSoftExceptionLogger.logSoftException(f1651n, e4);
        }
    }
}
