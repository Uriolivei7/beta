package com.facebook.react.views.text;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.uimanager.C0429f0;
import g1.C0542a;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class t {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final boolean f8049a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f8050b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final ThreadLocal f8051c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final ConcurrentHashMap f8052d;

    class a extends ThreadLocal {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public TextPaint initialValue() {
            return new TextPaint(1);
        }
    }

    static {
        C0542a c0542a = C0542a.f9422a;
        f8049a = false;
        f8050b = t.class.getSimpleName();
        f8051c = new a();
        f8052d = new ConcurrentHashMap();
    }

    static void a(Spannable spannable, float f3, com.facebook.yoga.p pVar, float f4, com.facebook.yoga.p pVar2, double d4, int i3, boolean z3, int i4, int i5, Layout.Alignment alignment, int i6, TextPaint textPaint) {
        BoringLayout.Metrics metricsIsBoring = BoringLayout.isBoring(spannable, textPaint);
        Layout layoutD = d(spannable, metricsIsBoring, f3, pVar, z3, i4, i5, alignment, i6, textPaint);
        int iH = (int) (Double.isNaN(d4) ? C0429f0.h(4.0f) : d4);
        int i7 = 0;
        Class<Z1.d> cls = Z1.d.class;
        int iMax = iH;
        for (Z1.d dVar : (Z1.d[]) spannable.getSpans(0, spannable.length(), cls)) {
            iMax = Math.max(iMax, dVar.getSize());
        }
        int i8 = iMax;
        while (i8 > iH) {
            if ((i3 == -1 || i3 == 0 || layoutD.getLineCount() <= i3) && ((pVar2 == com.facebook.yoga.p.UNDEFINED || layoutD.getHeight() <= f4) && (spannable.length() != 1 || layoutD.getLineWidth(i7) <= f3))) {
                return;
            }
            int iMax2 = i8 - Math.max(1, (int) C0429f0.h(1.0f));
            float f5 = iMax2 / iMax;
            float f6 = iH;
            textPaint.setTextSize(Math.max(textPaint.getTextSize() * f5, f6));
            Z1.d[] dVarArr = (Z1.d[]) spannable.getSpans(i7, spannable.length(), cls);
            int length = dVarArr.length;
            int i9 = i7;
            while (i9 < length) {
                Z1.d dVar2 = dVarArr[i9];
                spannable.setSpan(new Z1.d((int) Math.max(dVar2.getSize() * f5, f6)), spannable.getSpanStart(dVar2), spannable.getSpanEnd(dVar2), spannable.getSpanFlags(dVar2));
                spannable.removeSpan(dVar2);
                i9++;
                dVarArr = dVarArr;
                f5 = f5;
                f6 = f6;
            }
            if (metricsIsBoring != null) {
                metricsIsBoring = BoringLayout.isBoring(spannable, textPaint);
            }
            layoutD = d(spannable, metricsIsBoring, f3, pVar, z3, i4, i5, alignment, i6, textPaint);
            i8 = iMax2;
            iMax = iMax;
            cls = cls;
            i7 = 0;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0088, code lost:
    
        r21.add(new Z1.n(r6, r8, new Z1.f(r11)));
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x013d  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0184  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void b(android.content.Context r18, com.facebook.react.common.mapbuffer.a r19, android.text.SpannableStringBuilder r20, java.util.List r21) {
        /*
            Method dump skipped, instruction units count: 423
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.t.b(android.content.Context, com.facebook.react.common.mapbuffer.a, android.text.SpannableStringBuilder, java.util.List):void");
    }

    private static Layout c(Context context, com.facebook.react.common.mapbuffer.a aVar, com.facebook.react.common.mapbuffer.a aVar2, float f3, float f4, o oVar) {
        TextPaint textPaintA;
        Spannable spannableG = g(context, aVar, oVar);
        if (aVar.g(3)) {
            textPaintA = ((Z1.l[]) spannableG.getSpans(0, 0, Z1.l.class))[0].a();
        } else {
            r rVarA = r.a(aVar.d(4));
            TextPaint textPaint = (TextPaint) q.g.g((TextPaint) f8051c.get());
            p(textPaint, rVarA, context);
            textPaintA = textPaint;
        }
        BoringLayout.Metrics metricsIsBoring = BoringLayout.isBoring(spannableG, textPaintA);
        int iM = r.m(aVar2.getString(2));
        boolean z3 = aVar2.g(4) ? aVar2.getBoolean(4) : true;
        int iG = r.g(aVar2.getString(5));
        boolean z4 = aVar2.g(3) ? aVar2.getBoolean(3) : false;
        int i3 = aVar2.g(0) ? aVar2.getInt(0) : -1;
        String strI = i(aVar);
        Layout.Alignment alignmentH = h(aVar, spannableG, strI);
        int iK = k(strI);
        if (z4) {
            a(spannableG, f3, com.facebook.yoga.p.EXACTLY, f4, com.facebook.yoga.p.UNDEFINED, aVar2.g(6) ? aVar2.getDouble(6) : Double.NaN, i3, z3, iM, iG, alignmentH, iK, textPaintA);
        }
        return d(spannableG, metricsIsBoring, f3, com.facebook.yoga.p.EXACTLY, z3, iM, iG, alignmentH, iK, textPaintA);
    }

    private static Layout d(Spannable spannable, BoringLayout.Metrics metrics, float f3, com.facebook.yoga.p pVar, boolean z3, int i3, int i4, Layout.Alignment alignment, int i5, TextPaint textPaint) {
        int i6;
        int length = spannable.length();
        boolean z4 = pVar == com.facebook.yoga.p.UNDEFINED || f3 < 0.0f;
        float desiredWidth = metrics == null ? Layout.getDesiredWidth(spannable, textPaint) : Float.NaN;
        boolean zIsRtl = TextDirectionHeuristics.FIRSTSTRONG_LTR.isRtl(spannable, 0, length);
        if (metrics == null && (z4 || (!com.facebook.yoga.g.a(desiredWidth) && desiredWidth <= f3))) {
            if (pVar == com.facebook.yoga.p.EXACTLY) {
                desiredWidth = f3;
            }
            StaticLayout.Builder textDirection = StaticLayout.Builder.obtain(spannable, 0, length, textPaint, (int) Math.ceil(desiredWidth)).setAlignment(alignment).setLineSpacing(0.0f, 1.0f).setIncludePad(z3).setBreakStrategy(i3).setHyphenationFrequency(i4).setTextDirection(zIsRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);
            if (Build.VERSION.SDK_INT >= 28) {
                textDirection.setUseLineSpacingFromFallbacks(true);
            }
            return textDirection.build();
        }
        if (metrics == null || (!z4 && metrics.width > f3)) {
            StaticLayout.Builder textDirection2 = StaticLayout.Builder.obtain(spannable, 0, length, textPaint, (int) Math.ceil(f3)).setAlignment(alignment).setLineSpacing(0.0f, 1.0f).setIncludePad(z3).setBreakStrategy(i3).setHyphenationFrequency(i4).setTextDirection(zIsRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);
            int i7 = Build.VERSION.SDK_INT;
            if (i7 >= 26) {
                textDirection2.setJustificationMode(i5);
            }
            if (i7 >= 28) {
                textDirection2.setUseLineSpacingFromFallbacks(true);
            }
            return textDirection2.build();
        }
        int iCeil = metrics.width;
        if (pVar == com.facebook.yoga.p.EXACTLY) {
            iCeil = (int) Math.ceil(f3);
        }
        if (metrics.width < 0) {
            ReactSoftExceptionLogger.logSoftException(f8050b, new ReactNoCrashSoftException("Text width is invalid: " + metrics.width));
            i6 = 0;
        } else {
            i6 = iCeil;
        }
        return BoringLayout.make(spannable, textPaint, i6, alignment, 1.0f, 0.0f, metrics, z3);
    }

    private static Spannable e(Context context, com.facebook.react.common.mapbuffer.a aVar, o oVar) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ArrayList arrayList = new ArrayList();
        b(context, aVar.d(2), spannableStringBuilder, arrayList);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            ((Z1.n) arrayList.get((arrayList.size() - i3) - 1)).a(spannableStringBuilder, i3);
        }
        if (oVar != null) {
            oVar.a(spannableStringBuilder);
        }
        return spannableStringBuilder;
    }

    public static void f(int i3) {
        if (f8049a) {
            Y.a.m(f8050b, "Delete cached spannable for tag[" + i3 + "]");
        }
        f8052d.remove(Integer.valueOf(i3));
    }

    public static Spannable g(Context context, com.facebook.react.common.mapbuffer.a aVar, o oVar) {
        if (!aVar.g(3)) {
            return e(context, aVar, oVar);
        }
        return (Spannable) f8052d.get(Integer.valueOf(aVar.getInt(3)));
    }

    private static Layout.Alignment h(com.facebook.react.common.mapbuffer.a aVar, Spannable spannable, String str) {
        boolean z3 = l(aVar) != TextDirectionHeuristics.FIRSTSTRONG_LTR.isRtl(spannable, 0, spannable.length());
        Layout.Alignment alignment = z3 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        return str == null ? alignment : str.equals("center") ? Layout.Alignment.ALIGN_CENTER : str.equals("right") ? z3 ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE : alignment;
    }

    private static String i(com.facebook.react.common.mapbuffer.a aVar) {
        if (!aVar.g(2)) {
            return null;
        }
        com.facebook.react.common.mapbuffer.a aVarD = aVar.d(2);
        if (aVarD.getCount() != 0) {
            com.facebook.react.common.mapbuffer.a aVarD2 = aVarD.d(0).d(5);
            if (aVarD2.g(12)) {
                return aVarD2.getString(12);
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:?, code lost:
    
        return 5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:?, code lost:
    
        return 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0019, code lost:
    
        if (r4 != false) goto L5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0023, code lost:
    
        if (r4 != false) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int j(com.facebook.react.common.mapbuffer.a r3, android.text.Spannable r4, int r5) {
        /*
            java.lang.String r0 = i(r3)
            android.text.Layout$Alignment r3 = h(r3, r4, r0)
            android.text.TextDirectionHeuristic r0 = android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR
            int r1 = r4.length()
            r2 = 0
            boolean r4 = r0.isRtl(r4, r2, r1)
            android.text.Layout$Alignment r0 = android.text.Layout.Alignment.ALIGN_NORMAL
            r1 = 3
            r2 = 5
            if (r3 != r0) goto L1f
            if (r4 == 0) goto L1d
        L1b:
            r5 = r2
            goto L2b
        L1d:
            r5 = r1
            goto L2b
        L1f:
            android.text.Layout$Alignment r0 = android.text.Layout.Alignment.ALIGN_OPPOSITE
            if (r3 != r0) goto L26
            if (r4 == 0) goto L1b
            goto L1d
        L26:
            android.text.Layout$Alignment r4 = android.text.Layout.Alignment.ALIGN_CENTER
            if (r3 != r4) goto L2b
            r5 = 1
        L2b:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.t.j(com.facebook.react.common.mapbuffer.a, android.text.Spannable, int):int");
    }

    private static int k(String str) {
        if (Build.VERSION.SDK_INT < 26) {
            return -1;
        }
        return (str == null || !str.equals("justified")) ? 0 : 1;
    }

    public static boolean l(com.facebook.react.common.mapbuffer.a aVar) {
        if (!aVar.g(2)) {
            return false;
        }
        com.facebook.react.common.mapbuffer.a aVarD = aVar.d(2);
        if (aVarD.getCount() == 0) {
            return false;
        }
        com.facebook.react.common.mapbuffer.a aVarD2 = aVarD.d(0).d(5);
        return aVarD2.g(23) && r.i(aVarD2.getString(23)) == 1;
    }

    public static WritableArray m(Context context, com.facebook.react.common.mapbuffer.a aVar, com.facebook.react.common.mapbuffer.a aVar2, float f3, float f4) {
        Layout layoutC = c(context, aVar, aVar2, f3, f4, null);
        return b.a(layoutC.getText(), layoutC, (TextPaint) q.g.g((TextPaint) f8051c.get()), context);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0133  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long n(android.content.Context r18, com.facebook.react.common.mapbuffer.a r19, com.facebook.react.common.mapbuffer.a r20, float r21, com.facebook.yoga.p r22, float r23, com.facebook.yoga.p r24, com.facebook.react.views.text.o r25, float[] r26) {
        /*
            Method dump skipped, instruction units count: 458
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.text.t.n(android.content.Context, com.facebook.react.common.mapbuffer.a, com.facebook.react.common.mapbuffer.a, float, com.facebook.yoga.p, float, com.facebook.yoga.p, com.facebook.react.views.text.o, float[]):long");
    }

    public static void o(int i3, Spannable spannable) {
        if (f8049a) {
            Y.a.m(f8050b, "Set cached spannable for tag[" + i3 + "]: " + spannable.toString());
        }
        f8052d.put(Integer.valueOf(i3), spannable);
    }

    private static void p(TextPaint textPaint, r rVar, Context context) {
        textPaint.reset();
        textPaint.setAntiAlias(true);
        if (rVar.b() != -1) {
            textPaint.setTextSize(rVar.b());
        }
        if (rVar.e() == -1 && rVar.f() == -1 && rVar.d() == null) {
            textPaint.setTypeface(null);
            return;
        }
        Typeface typefaceA = p.a(null, rVar.e(), rVar.f(), rVar.d(), context.getAssets());
        textPaint.setTypeface(typefaceA);
        if (rVar.e() == -1 || rVar.e() == typefaceA.getStyle()) {
            return;
        }
        int iE = rVar.e() & (~typefaceA.getStyle());
        textPaint.setFakeBoldText((iE & 1) != 0);
        textPaint.setTextSkewX((iE & 2) != 0 ? -0.25f : 0.0f);
    }
}
