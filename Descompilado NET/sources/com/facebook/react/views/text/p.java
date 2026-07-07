package com.facebook.react.views.text;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import com.facebook.react.bridge.ReadableArray;
import f1.C0534a;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class p {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final p f8009a = new p();

    private p() {
    }

    public static final Typeface a(Typeface typeface, int i3, int i4, String str, AssetManager assetManager) {
        D2.h.f(assetManager, "assetManager");
        C0534a.c cVar = new C0534a.c(i3, i4);
        if (str != null) {
            return C0534a.f9381c.c().e(str, cVar, assetManager);
        }
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        return cVar.a(typeface);
    }

    public static final int b(String str) {
        if (D2.h.b(str, "italic")) {
            return 2;
        }
        return D2.h.b(str, "normal") ? 0 : -1;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final String c(ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int size = readableArray.size();
        for (int i3 = 0; i3 < size; i3++) {
            String string = readableArray.getString(i3);
            if (string != null) {
                switch (string.hashCode()) {
                    case -1983120972:
                        if (string.equals("stylistic-thirteen")) {
                            arrayList.add("'ss13'");
                        }
                        break;
                    case -1933522176:
                        if (string.equals("stylistic-fifteen")) {
                            arrayList.add("'ss15'");
                        }
                        break;
                    case -1534462052:
                        if (string.equals("stylistic-eighteen")) {
                            arrayList.add("'ss18'");
                        }
                        break;
                    case -1195362251:
                        if (string.equals("proportional-nums")) {
                            arrayList.add("'pnum'");
                        }
                        break;
                    case -1061392823:
                        if (string.equals("lining-nums")) {
                            arrayList.add("'lnum'");
                        }
                        break;
                    case -899039099:
                        if (string.equals("historical-ligatures")) {
                            arrayList.add("'hlig'");
                        }
                        break;
                    case -771984547:
                        if (string.equals("tabular-nums")) {
                            arrayList.add("'tnum'");
                        }
                        break;
                    case -672279417:
                        if (string.equals("discretionary-ligatures")) {
                            arrayList.add("'dlig'");
                        }
                        break;
                    case -659678800:
                        if (string.equals("oldstyle-nums")) {
                            arrayList.add("'onum'");
                        }
                        break;
                    case 249095901:
                        if (string.equals("no-contextual")) {
                            arrayList.add("'calt' off");
                        }
                        break;
                    case 273808209:
                        if (string.equals("contextual")) {
                            arrayList.add("'calt'");
                        }
                        break;
                    case 289909490:
                        if (string.equals("no-common-ligatures")) {
                            arrayList.add("'liga' off");
                            arrayList.add("'clig' off");
                        }
                        break;
                    case 296506098:
                        if (string.equals("stylistic-eight")) {
                            arrayList.add("'ss08'");
                        }
                        break;
                    case 309330544:
                        if (string.equals("stylistic-seven")) {
                            arrayList.add("'ss07'");
                        }
                        break;
                    case 310339585:
                        if (string.equals("stylistic-three")) {
                            arrayList.add("'ss03'");
                        }
                        break;
                    case 604478526:
                        if (string.equals("stylistic-eleven")) {
                            arrayList.add("'ss11'");
                        }
                        break;
                    case 915975441:
                        if (string.equals("no-historical-ligatures")) {
                            arrayList.add("'hlig' off");
                        }
                        break;
                    case 979426287:
                        if (string.equals("stylistic-five")) {
                            arrayList.add("'ss05'");
                        }
                        break;
                    case 979432035:
                        if (string.equals("stylistic-four")) {
                            arrayList.add("'ss04'");
                        }
                        break;
                    case 979664367:
                        if (string.equals("stylistic-nine")) {
                            arrayList.add("'ss09'");
                        }
                        break;
                    case 1001434505:
                        if (string.equals("stylistic-one")) {
                            arrayList.add("'ss01'");
                        }
                        break;
                    case 1001438213:
                        if (string.equals("stylistic-six")) {
                            arrayList.add("'ss06'");
                        }
                        break;
                    case 1001439040:
                        if (string.equals("stylistic-ten")) {
                            arrayList.add("'ss10'");
                        }
                        break;
                    case 1001439599:
                        if (string.equals("stylistic-two")) {
                            arrayList.add("'ss02'");
                        }
                        break;
                    case 1030714463:
                        if (string.equals("stylistic-sixteen")) {
                            arrayList.add("'ss16'");
                        }
                        break;
                    case 1044065430:
                        if (string.equals("stylistic-twelve")) {
                            arrayList.add("'ss12'");
                        }
                        break;
                    case 1044067310:
                        if (string.equals("stylistic-twenty")) {
                            arrayList.add("'ss20'");
                        }
                        break;
                    case 1082592379:
                        if (string.equals("no-discretionary-ligatures")) {
                            arrayList.add("'dlig' off");
                        }
                        break;
                    case 1183323111:
                        if (string.equals("small-caps")) {
                            arrayList.add("'smcp'");
                        }
                        break;
                    case 1223989350:
                        if (string.equals("common-ligatures")) {
                            arrayList.add("'liga'");
                            arrayList.add("'clig'");
                        }
                        break;
                    case 1463562569:
                        if (string.equals("stylistic-nineteen")) {
                            arrayList.add("'ss19'");
                        }
                        break;
                    case 1648446397:
                        if (string.equals("stylistic-fourteen")) {
                            arrayList.add("'ss14'");
                        }
                        break;
                    case 2097122634:
                        if (string.equals("stylistic-seventeen")) {
                            arrayList.add("'ss17'");
                        }
                        break;
                }
            }
        }
        return TextUtils.join(", ", arrayList);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003a A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x008b A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final int d(java.lang.String r1) {
        /*
            if (r1 == 0) goto L8e
            int r0 = r1.hashCode()
            switch(r0) {
                case -1039745817: goto L82;
                case 48625: goto L76;
                case 49586: goto L6a;
                case 50547: goto L5e;
                case 51508: goto L55;
                case 52469: goto L49;
                case 53430: goto L3d;
                case 54391: goto L31;
                case 55352: goto L23;
                case 56313: goto L15;
                case 3029637: goto Lb;
                default: goto L9;
            }
        L9:
            goto L8e
        Lb:
            java.lang.String r0 = "bold"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L3a
            goto L8e
        L15:
            java.lang.String r0 = "900"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L1f
            goto L8e
        L1f:
            r1 = 900(0x384, float:1.261E-42)
            goto L8f
        L23:
            java.lang.String r0 = "800"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L2d
            goto L8e
        L2d:
            r1 = 800(0x320, float:1.121E-42)
            goto L8f
        L31:
            java.lang.String r0 = "700"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L3a
            goto L8e
        L3a:
            r1 = 700(0x2bc, float:9.81E-43)
            goto L8f
        L3d:
            java.lang.String r0 = "600"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L46
            goto L8e
        L46:
            r1 = 600(0x258, float:8.41E-43)
            goto L8f
        L49:
            java.lang.String r0 = "500"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L52
            goto L8e
        L52:
            r1 = 500(0x1f4, float:7.0E-43)
            goto L8f
        L55:
            java.lang.String r0 = "400"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L8b
            goto L8e
        L5e:
            java.lang.String r0 = "300"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L67
            goto L8e
        L67:
            r1 = 300(0x12c, float:4.2E-43)
            goto L8f
        L6a:
            java.lang.String r0 = "200"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L73
            goto L8e
        L73:
            r1 = 200(0xc8, float:2.8E-43)
            goto L8f
        L76:
            java.lang.String r0 = "100"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L7f
            goto L8e
        L7f:
            r1 = 100
            goto L8f
        L82:
            java.lang.String r0 = "normal"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L8b
            goto L8e
        L8b:
            r1 = 400(0x190, float:5.6E-43)
            goto L8f
        L8e:
            r1 = -1
        L8f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.p.d(java.lang.String):int");
    }
}
