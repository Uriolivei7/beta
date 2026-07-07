package com.facebook.react.views.debuggingoverlay;

import D2.h;
import U1.k;
import U1.l;
import android.graphics.RectF;
import com.facebook.react.bridge.NoSuchKeyException;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UnexpectedNativeTypeException;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.Q0;
import com.facebook.react.uimanager.SimpleViewManager;
import java.util.ArrayList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.r;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = DebuggingOverlayManager.REACT_CLASS)
public final class DebuggingOverlayManager extends SimpleViewManager<b> implements l {
    public static final a Companion = new a(null);
    public static final String REACT_CLASS = "DebuggingOverlay";
    private final Q0 delegate = new k(this);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    protected Q0 getDelegate() {
        return this.delegate;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // U1.l
    public void clearElementsHighlights(b bVar) {
        h.f(bVar, "view");
        bVar.b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public b createViewInstance(B0 b02) {
        h.f(b02, "context");
        return new b(b02);
    }

    @Override // U1.l
    public void highlightElements(b bVar, ReadableArray readableArray) throws Exception {
        ReadableArray array;
        h.f(bVar, "view");
        if (readableArray == null || (array = readableArray.getArray(0)) == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int size = array.size();
        boolean z3 = true;
        for (int i3 = 0; i3 < size; i3++) {
            ReadableMap map = array.getMap(i3);
            if (map != null) {
                try {
                    float f3 = (float) map.getDouble("x");
                    float f4 = (float) map.getDouble("y");
                    float f5 = (float) (((double) f3) + map.getDouble("width"));
                    float f6 = (float) (((double) f4) + map.getDouble("height"));
                    C0429f0 c0429f0 = C0429f0.f7476a;
                    arrayList.add(new RectF(c0429f0.b(f3), c0429f0.b(f4), c0429f0.b(f5), c0429f0.b(f6)));
                } catch (Exception e4) {
                    if (!(e4 instanceof NoSuchKeyException) && !(e4 instanceof UnexpectedNativeTypeException)) {
                        throw e4;
                    }
                    ReactSoftExceptionLogger.logSoftException(REACT_CLASS, new ReactNoCrashSoftException("Unexpected payload for highlighting elements: every element should have x, y, width, height fields"));
                    r rVar = r.f10584a;
                    z3 = false;
                }
            }
        }
        if (z3) {
            bVar.setHighlightedElementsRectangles(arrayList);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00aa, code lost:
    
        if (r3 == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00ac, code lost:
    
        r20.setTraceUpdates(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00af, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
    
        return;
     */
    @Override // U1.l
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void highlightTraceUpdates(com.facebook.react.views.debuggingoverlay.b r20, com.facebook.react.bridge.ReadableArray r21) throws java.lang.Exception {
        /*
            r19 = this;
            r1 = r20
            r0 = r21
            java.lang.String r3 = "view"
            D2.h.f(r1, r3)
            if (r0 == 0) goto Laf
            r3 = 0
            com.facebook.react.bridge.ReadableArray r4 = r0.getArray(r3)
            if (r4 != 0) goto L14
            goto Laf
        L14:
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            int r6 = r4.size()
            r7 = r3
            r0 = 1
        L1f:
            if (r7 >= r6) goto La9
            com.facebook.react.bridge.ReadableMap r8 = r4.getMap(r7)
            if (r8 != 0) goto L28
            goto L8a
        L28:
            java.lang.String r9 = "rectangle"
            com.facebook.react.bridge.ReadableMap r9 = r8.getMap(r9)
            java.lang.String r10 = "DebuggingOverlay"
            if (r9 != 0) goto L3d
            com.facebook.react.bridge.ReactNoCrashSoftException r0 = new com.facebook.react.bridge.ReactNoCrashSoftException
            java.lang.String r2 = "Unexpected payload for highlighting trace updates: rectangle field is null"
            r0.<init>(r2)
            com.facebook.react.bridge.ReactSoftExceptionLogger.logSoftException(r10, r0)
            goto Laa
        L3d:
            java.lang.String r11 = "id"
            int r11 = r8.getInt(r11)
            java.lang.String r12 = "color"
            int r8 = r8.getInt(r12)
            java.lang.String r12 = "x"
            double r12 = r9.getDouble(r12)     // Catch: java.lang.Exception -> L8c
            float r12 = (float) r12     // Catch: java.lang.Exception -> L8c
            java.lang.String r13 = "y"
            double r13 = r9.getDouble(r13)     // Catch: java.lang.Exception -> L8c
            float r13 = (float) r13     // Catch: java.lang.Exception -> L8c
            double r14 = (double) r12     // Catch: java.lang.Exception -> L8c
            java.lang.String r3 = "width"
            double r16 = r9.getDouble(r3)     // Catch: java.lang.Exception -> L8c
            double r14 = r14 + r16
            float r3 = (float) r14     // Catch: java.lang.Exception -> L8c
            double r14 = (double) r13     // Catch: java.lang.Exception -> L8c
            java.lang.String r2 = "height"
            double r17 = r9.getDouble(r2)     // Catch: java.lang.Exception -> L8c
            double r14 = r14 + r17
            float r2 = (float) r14     // Catch: java.lang.Exception -> L8c
            android.graphics.RectF r9 = new android.graphics.RectF     // Catch: java.lang.Exception -> L8c
            com.facebook.react.uimanager.f0 r14 = com.facebook.react.uimanager.C0429f0.f7476a     // Catch: java.lang.Exception -> L8c
            float r12 = r14.b(r12)     // Catch: java.lang.Exception -> L8c
            float r13 = r14.b(r13)     // Catch: java.lang.Exception -> L8c
            float r3 = r14.b(r3)     // Catch: java.lang.Exception -> L8c
            float r2 = r14.b(r2)     // Catch: java.lang.Exception -> L8c
            r9.<init>(r12, r13, r3, r2)     // Catch: java.lang.Exception -> L8c
            com.facebook.react.views.debuggingoverlay.c r2 = new com.facebook.react.views.debuggingoverlay.c     // Catch: java.lang.Exception -> L8c
            r2.<init>(r11, r9, r8)     // Catch: java.lang.Exception -> L8c
            r5.add(r2)     // Catch: java.lang.Exception -> L8c
        L8a:
            r2 = 1
            goto La5
        L8c:
            r0 = move-exception
            boolean r2 = r0 instanceof com.facebook.react.bridge.NoSuchKeyException
            if (r2 != 0) goto L97
            boolean r2 = r0 instanceof com.facebook.react.bridge.UnexpectedNativeTypeException
            if (r2 == 0) goto L96
            goto L97
        L96:
            throw r0
        L97:
            com.facebook.react.bridge.ReactNoCrashSoftException r0 = new com.facebook.react.bridge.ReactNoCrashSoftException
            java.lang.String r2 = "Unexpected payload for highlighting trace updates: rectangle field should have x, y, width, height fields"
            r0.<init>(r2)
            com.facebook.react.bridge.ReactSoftExceptionLogger.logSoftException(r10, r0)
            r2.r r0 = r2.r.f10584a
            r0 = 0
            goto L8a
        La5:
            int r7 = r7 + r2
            r3 = 0
            goto L1f
        La9:
            r3 = r0
        Laa:
            if (r3 == 0) goto Laf
            r1.setTraceUpdates(r5)
        Laf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.debuggingoverlay.DebuggingOverlayManager.highlightTraceUpdates(com.facebook.react.views.debuggingoverlay.b, com.facebook.react.bridge.ReadableArray):void");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(b bVar, String str, ReadableArray readableArray) throws Exception {
        h.f(bVar, "view");
        h.f(str, "commandId");
        int iHashCode = str.hashCode();
        if (iHashCode != -1942063165) {
            if (iHashCode != 1326903961) {
                if (iHashCode == 1385348555 && str.equals("highlightElements")) {
                    highlightElements(bVar, readableArray);
                    return;
                }
            } else if (str.equals("highlightTraceUpdates")) {
                highlightTraceUpdates(bVar, readableArray);
                return;
            }
        } else if (str.equals("clearElementsHighlights")) {
            clearElementsHighlights(bVar);
            return;
        }
        ReactSoftExceptionLogger.logSoftException(REACT_CLASS, new ReactNoCrashSoftException("Received unexpected command in DebuggingOverlayManager"));
    }
}
