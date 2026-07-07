package P1;

import android.view.MotionEvent;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/* JADX INFO: loaded from: classes.dex */
public final class t {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final t f1709a = new t();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f1710b = "target";

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f1711a;

        static {
            int[] iArr = new int[s.values().length];
            try {
                iArr[s.f1702d.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[s.f1703e.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[s.f1704f.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[s.f1705g.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f1711a = iArr;
        }
    }

    private t() {
    }

    private final WritableMap[] a(q qVar) {
        MotionEvent motionEventW = qVar.w();
        WritableMap[] writableMapArr = new WritableMap[motionEventW.getPointerCount()];
        float x3 = motionEventW.getX() - qVar.y();
        float y3 = motionEventW.getY() - qVar.z();
        int pointerCount = motionEventW.getPointerCount();
        for (int i3 = 0; i3 < pointerCount; i3++) {
            WritableMap writableMapCreateMap = Arguments.createMap();
            C0429f0 c0429f0 = C0429f0.f7476a;
            writableMapCreateMap.putDouble("pageX", c0429f0.d(motionEventW.getX(i3)));
            writableMapCreateMap.putDouble("pageY", c0429f0.d(motionEventW.getY(i3)));
            float x4 = motionEventW.getX(i3) - x3;
            float y4 = motionEventW.getY(i3) - y3;
            writableMapCreateMap.putDouble("locationX", c0429f0.d(x4));
            writableMapCreateMap.putDouble("locationY", c0429f0.d(y4));
            writableMapCreateMap.putInt("targetSurface", qVar.l());
            writableMapCreateMap.putInt(f1710b, qVar.o());
            writableMapCreateMap.putDouble("timestamp", qVar.m());
            writableMapCreateMap.putDouble("identifier", motionEventW.getPointerId(i3));
            writableMapArr[i3] = writableMapCreateMap;
        }
        return writableMapArr;
    }

    private final WritableArray b(boolean z3, WritableMap[] writableMapArr) {
        WritableArray writableArrayCreateArray = Arguments.createArray();
        for (WritableMap writableMapCopy : writableMapArr) {
            if (writableMapCopy != null) {
                if (z3) {
                    writableMapCopy = writableMapCopy.copy();
                }
                writableArrayCreateArray.pushMap(writableMapCopy);
            }
        }
        D2.h.c(writableArrayCreateArray);
        return writableArrayCreateArray;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x009f A[Catch: all -> 0x0055, TryCatch #0 {all -> 0x0055, blocks: (B:3:0x002f, B:11:0x0051, B:31:0x0095, B:32:0x0099, B:34:0x009f, B:36:0x00a7, B:38:0x00c2, B:14:0x0058, B:15:0x005d, B:16:0x005e, B:17:0x0061, B:19:0x0064, B:21:0x0068, B:23:0x006e, B:25:0x0074, B:26:0x0081, B:28:0x0089, B:30:0x008f), top: B:44:0x002f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final void c(com.facebook.react.uimanager.events.RCTModernEventEmitter r17, P1.q r18) {
        /*
            Method dump skipped, instruction units count: 229
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: P1.t.c(com.facebook.react.uimanager.events.RCTModernEventEmitter, P1.q):void");
    }

    public static final void d(RCTEventEmitter rCTEventEmitter, q qVar) {
        D2.h.f(rCTEventEmitter, "rctEventEmitter");
        D2.h.f(qVar, "touchEvent");
        s sVarX = qVar.x();
        t tVar = f1709a;
        WritableArray writableArrayB = tVar.b(false, tVar.a(qVar));
        MotionEvent motionEventW = qVar.w();
        WritableArray writableArrayCreateArray = Arguments.createArray();
        if (sVarX == s.f1704f || sVarX == s.f1705g) {
            int pointerCount = motionEventW.getPointerCount();
            for (int i3 = 0; i3 < pointerCount; i3++) {
                writableArrayCreateArray.pushInt(i3);
            }
        } else {
            if (sVarX != s.f1702d && sVarX != s.f1703e) {
                throw new RuntimeException("Unknown touch type: " + sVarX);
            }
            writableArrayCreateArray.pushInt(motionEventW.getActionIndex());
        }
        String strA = s.f1701c.a(sVarX);
        D2.h.c(writableArrayCreateArray);
        rCTEventEmitter.receiveTouches(strA, writableArrayB, writableArrayCreateArray);
    }
}
