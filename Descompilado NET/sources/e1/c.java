package E1;

import D2.h;
import F0.b;
import M2.B;
import M2.C0193d;
import M2.t;
import M2.z;
import android.net.Uri;
import android.os.SystemClock;
import com.facebook.imagepipeline.producers.Y;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import r2.C0685h;

/* JADX INFO: loaded from: classes.dex */
public final class c extends F0.b {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final z f205e;

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f206a;

        static {
            int[] iArr = new int[E1.a.values().length];
            try {
                iArr[E1.a.f197c.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[E1.a.f198d.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[E1.a.f199e.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[E1.a.f196b.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f206a = iArr;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(z zVar) {
        super(zVar);
        h.f(zVar, "okHttpClient");
        this.f205e = zVar;
    }

    private final Map p(ReadableMap readableMap) {
        if (readableMap == null) {
            return null;
        }
        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = readableMap.keySetIterator();
        HashMap map = new HashMap();
        while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
            String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
            String string = readableMap.getString(strNextKey);
            if (string != null) {
                map.put(strNextKey, string);
            }
        }
        return map;
    }

    @Override // F0.b, com.facebook.imagepipeline.producers.Y
    /* JADX INFO: renamed from: j */
    public void b(b.C0004b c0004b, Y.a aVar) {
        Map mapP;
        h.f(c0004b, "fetchState");
        h.f(aVar, "callback");
        c0004b.f222f = SystemClock.elapsedRealtime();
        Uri uriG = c0004b.g();
        h.e(uriG, "getUri(...)");
        C0193d.a aVar2 = new C0193d.a();
        if (c0004b.b().X() instanceof b) {
            U0.b bVarX = c0004b.b().X();
            h.d(bVarX, "null cannot be cast to non-null type com.facebook.react.modules.fresco.ReactNetworkImageRequest");
            b bVar = (b) bVarX;
            mapP = p(bVar.C());
            int i3 = a.f206a[bVar.B().ordinal()];
            if (i3 == 1) {
                aVar2.e().d();
            } else if (i3 == 2) {
                aVar2.c(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } else if (i3 == 3) {
                aVar2.f().c(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } else {
                if (i3 != 4) {
                    throw new C0685h();
                }
                aVar2.e();
            }
        } else {
            aVar2.e();
            mapP = null;
        }
        t tVarB = com.facebook.react.modules.network.h.b(mapP);
        B.a aVar3 = new B.a();
        h.c(tVarB);
        B.a aVarC = aVar3.f(tVarB).c(aVar2.a());
        String string = uriG.toString();
        h.e(string, "toString(...)");
        k(c0004b, aVar, aVarC.m(string).d().b());
    }
}
