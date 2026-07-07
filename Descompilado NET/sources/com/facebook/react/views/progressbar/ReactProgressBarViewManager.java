package com.facebook.react.views.progressbar;

import D2.h;
import U1.e;
import U1.f;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.Q0;
import com.facebook.yoga.p;
import com.facebook.yoga.q;
import java.util.WeakHashMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactProgressBarViewManager.REACT_CLASS)
public final class ReactProgressBarViewManager extends BaseViewManager<com.facebook.react.views.progressbar.a, b> implements f {
    public static final String DEFAULT_STYLE = "Normal";
    public static final String PROP_ANIMATING = "animating";
    public static final String PROP_ATTR = "typeAttr";
    public static final String PROP_INDETERMINATE = "indeterminate";
    public static final String PROP_PROGRESS = "progress";
    public static final String PROP_STYLE = "styleAttr";
    public static final String REACT_CLASS = "AndroidProgressBar";
    public static final a Companion = new a(null);
    private static final Object progressBarCtorLock = new Object();
    private final WeakHashMap<Integer, Pair<Integer, Integer>> measuredStyles = new WeakHashMap<>();
    private final Q0 delegate = new e(this);

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final ProgressBar a(Context context, int i3) {
            ProgressBar progressBar;
            synchronized (ReactProgressBarViewManager.progressBarCtorLock) {
                progressBar = new ProgressBar(context, null, i3);
            }
            return progressBar;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0065  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final int b(java.lang.String r5) {
            /*
                r4 = this;
                java.lang.String r0 = "ReactNative"
                r1 = 16842871(0x1010077, float:2.3693892E-38)
                if (r5 == 0) goto L7a
                int r2 = r5.hashCode()
                switch(r2) {
                    case -1955878649: goto L5d;
                    case -1414214583: goto L50;
                    case -913872828: goto L43;
                    case -670403824: goto L36;
                    case -142408811: goto L29;
                    case 73190171: goto L1c;
                    case 79996135: goto Lf;
                    default: goto Le;
                }
            Le:
                goto L65
            Lf:
                java.lang.String r2 = "Small"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L18
                goto L65
            L18:
                r5 = 16842873(0x1010079, float:2.3693897E-38)
                return r5
            L1c:
                java.lang.String r2 = "Large"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L25
                goto L65
            L25:
                r5 = 16842874(0x101007a, float:2.36939E-38)
                return r5
            L29:
                java.lang.String r2 = "LargeInverse"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L32
                goto L65
            L32:
                r5 = 16843401(0x1010289, float:2.3695377E-38)
                return r5
            L36:
                java.lang.String r2 = "Inverse"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L3f
                goto L65
            L3f:
                r5 = 16843399(0x1010287, float:2.369537E-38)
                return r5
            L43:
                java.lang.String r2 = "Horizontal"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L4c
                goto L65
            L4c:
                r5 = 16842872(0x1010078, float:2.3693894E-38)
                return r5
            L50:
                java.lang.String r2 = "SmallInverse"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L59
                goto L65
            L59:
                r5 = 16843400(0x1010288, float:2.3695374E-38)
                return r5
            L5d:
                java.lang.String r2 = "Normal"
                boolean r2 = r5.equals(r2)
                if (r2 != 0) goto L79
            L65:
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Unknown ProgressBar style: "
                r2.append(r3)
                r2.append(r5)
                java.lang.String r5 = r2.toString()
                Y.a.I(r0, r5)
            L79:
                return r1
            L7a:
                java.lang.String r5 = "ProgressBar needs to have a style, null received"
                Y.a.I(r0, r5)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.progressbar.ReactProgressBarViewManager.a.b(java.lang.String):int");
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

    @Override // com.facebook.react.uimanager.ViewManager
    public Class<b> getShadowNodeClass() {
        return b.class;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public long measure(Context context, ReadableMap readableMap, ReadableMap readableMap2, ReadableMap readableMap3, float f3, p pVar, float f4, p pVar2, float[] fArr) {
        h.f(context, "context");
        h.f(readableMap, "localData");
        h.f(readableMap2, "props");
        h.f(readableMap3, "state");
        h.f(pVar, "widthMode");
        h.f(pVar2, "heightMode");
        a aVar = Companion;
        int iB = aVar.b(readableMap2.getString(PROP_STYLE));
        WeakHashMap<Integer, Pair<Integer, Integer>> weakHashMap = this.measuredStyles;
        Integer numValueOf = Integer.valueOf(iB);
        Pair<Integer, Integer> pairCreate = weakHashMap.get(numValueOf);
        if (pairCreate == null) {
            ProgressBar progressBarA = aVar.a(context, iB);
            int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            progressBarA.measure(iMakeMeasureSpec, iMakeMeasureSpec);
            pairCreate = Pair.create(Integer.valueOf(progressBarA.getMeasuredWidth()), Integer.valueOf(progressBarA.getMeasuredHeight()));
            weakHashMap.put(numValueOf, pairCreate);
        }
        Pair<Integer, Integer> pair = pairCreate;
        return q.a(C0429f0.f(((Number) pair.first).intValue()), C0429f0.f(((Number) pair.second).intValue()));
    }

    @Override // U1.f
    @L1.a(name = PROP_ATTR)
    public void setTypeAttr(com.facebook.react.views.progressbar.a aVar, String str) {
        h.f(aVar, "view");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(com.facebook.react.views.progressbar.a aVar, Object obj) {
        h.f(aVar, "root");
        h.f(obj, "extraData");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public b createShadowNodeInstance() {
        return new b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.ViewManager
    public com.facebook.react.views.progressbar.a createViewInstance(B0 b02) {
        h.f(b02, "context");
        return new com.facebook.react.views.progressbar.a(b02);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(com.facebook.react.views.progressbar.a aVar) {
        h.f(aVar, "view");
        aVar.a();
    }

    @Override // U1.f
    @L1.a(name = PROP_ANIMATING)
    public void setAnimating(com.facebook.react.views.progressbar.a aVar, boolean z3) {
        h.f(aVar, "view");
        aVar.setAnimating$ReactAndroid_release(z3);
    }

    @Override // U1.f
    @L1.a(customType = "Color", name = "color")
    public void setColor(com.facebook.react.views.progressbar.a aVar, Integer num) {
        h.f(aVar, "view");
        aVar.setColor$ReactAndroid_release(num);
    }

    @Override // U1.f
    @L1.a(name = PROP_INDETERMINATE)
    public void setIndeterminate(com.facebook.react.views.progressbar.a aVar, boolean z3) {
        h.f(aVar, "view");
        aVar.setIndeterminate$ReactAndroid_release(z3);
    }

    @Override // U1.f
    @L1.a(name = PROP_PROGRESS)
    public void setProgress(com.facebook.react.views.progressbar.a aVar, double d4) {
        h.f(aVar, "view");
        aVar.setProgress$ReactAndroid_release(d4);
    }

    @Override // U1.f
    @L1.a(name = PROP_STYLE)
    public void setStyleAttr(com.facebook.react.views.progressbar.a aVar, String str) {
        h.f(aVar, "view");
        aVar.setStyle$ReactAndroid_release(str);
    }

    @Override // U1.f
    public void setTestID(com.facebook.react.views.progressbar.a aVar, String str) {
        h.f(aVar, "view");
        super.setTestId(aVar, str);
    }
}
