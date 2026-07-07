package com.facebook.react.views.text;

import a1.C0210a;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.C0423c0;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0433h0;
import com.facebook.react.uimanager.C0452r0;
import com.facebook.react.uimanager.InterfaceC0451q0;
import com.facebook.react.uimanager.P;
import com.facebook.react.uimanager.U;
import com.facebook.yoga.YogaValue;
import com.facebook.yoga.w;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class c extends U {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    protected s f7919A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    protected boolean f7920B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    protected int f7921C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    protected boolean f7922D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    protected int f7923E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    protected C0433h0.d f7924F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    protected C0433h0.e f7925G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    protected int f7926H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    protected int f7927I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    protected int f7928J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    protected int f7929K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    protected int f7930L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    protected float f7931M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    protected float f7932N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    protected float f7933O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    protected int f7934P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    protected boolean f7935Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    protected boolean f7936R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    protected boolean f7937S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    protected boolean f7938T;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    protected float f7939U;

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    protected int f7940V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    protected int f7941W;

    /* JADX INFO: renamed from: X, reason: collision with root package name */
    protected String f7942X;

    /* JADX INFO: renamed from: Y, reason: collision with root package name */
    protected String f7943Y;

    /* JADX INFO: renamed from: Z, reason: collision with root package name */
    protected boolean f7944Z;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    protected Map f7945a0;

    public c() {
        this(null);
    }

    private static void w1(c cVar, SpannableStringBuilder spannableStringBuilder, List list, s sVar, boolean z3, Map map, int i3) {
        float fE0;
        float fU;
        s sVarA = sVar != null ? sVar.a(cVar.f7919A) : cVar.f7919A;
        int iC = cVar.C();
        for (int i4 = 0; i4 < iC; i4++) {
            C0452r0 c0452r0N = cVar.N(i4);
            if (c0452r0N instanceof e) {
                spannableStringBuilder.append((CharSequence) u.b(((e) c0452r0N).v1(), sVarA.l()));
            } else if (c0452r0N instanceof c) {
                w1((c) c0452r0N, spannableStringBuilder, list, sVarA, z3, map, spannableStringBuilder.length());
            } else if (c0452r0N instanceof Y1.a) {
                spannableStringBuilder.append("0");
                list.add(new Z1.n(spannableStringBuilder.length() - 1, spannableStringBuilder.length(), ((Y1.a) c0452r0N).w1()));
            } else {
                if (!z3) {
                    throw new P("Unexpected view type nested under a <Text> or <TextInput> node: " + c0452r0N.getClass());
                }
                int iH = c0452r0N.H();
                YogaValue yogaValueC = c0452r0N.c();
                YogaValue yogaValueZ = c0452r0N.z();
                w wVar = yogaValueC.f8292b;
                w wVar2 = w.POINT;
                if (wVar == wVar2 && yogaValueZ.f8292b == wVar2) {
                    fE0 = yogaValueC.f8291a;
                    fU = yogaValueZ.f8291a;
                } else {
                    c0452r0N.M();
                    fE0 = c0452r0N.e0();
                    fU = c0452r0N.u();
                }
                spannableStringBuilder.append("0");
                list.add(new Z1.n(spannableStringBuilder.length() - 1, spannableStringBuilder.length(), new Z1.q(iH, (int) fE0, (int) fU)));
                map.put(Integer.valueOf(iH), c0452r0N);
                c0452r0N.d();
            }
            c0452r0N.d();
        }
        int length = spannableStringBuilder.length();
        if (length >= i3) {
            if (cVar.f7920B) {
                list.add(new Z1.n(i3, length, new Z1.g(cVar.f7921C)));
            }
            if (cVar.f7922D) {
                list.add(new Z1.n(i3, length, new Z1.e(cVar.f7923E)));
            }
            C0433h0.e eVar = cVar.f7925G;
            if (eVar == null ? cVar.f7924F == C0433h0.d.LINK : eVar == C0433h0.e.LINK) {
                list.add(new Z1.n(i3, length, new Z1.f(cVar.H())));
            }
            float fD = sVarA.d();
            if (!Float.isNaN(fD) && (sVar == null || sVar.d() != fD)) {
                list.add(new Z1.n(i3, length, new Z1.a(fD)));
            }
            int iC2 = sVarA.c();
            if (sVar == null || sVar.c() != iC2) {
                list.add(new Z1.n(i3, length, new Z1.d(iC2)));
            }
            if (cVar.f7940V != -1 || cVar.f7941W != -1 || cVar.f7942X != null) {
                list.add(new Z1.n(i3, length, new Z1.c(cVar.f7940V, cVar.f7941W, cVar.f7943Y, cVar.f7942X, cVar.l().getAssets())));
            }
            if (cVar.f7935Q) {
                list.add(new Z1.n(i3, length, new Z1.m()));
            }
            if (cVar.f7936R) {
                list.add(new Z1.n(i3, length, new Z1.j()));
            }
            if ((cVar.f7931M != 0.0f || cVar.f7932N != 0.0f || cVar.f7933O != 0.0f) && Color.alpha(cVar.f7934P) != 0) {
                list.add(new Z1.n(i3, length, new Z1.o(cVar.f7931M, cVar.f7932N, cVar.f7933O, cVar.f7934P)));
            }
            float fE = sVarA.e();
            if (!Float.isNaN(fE) && (sVar == null || sVar.e() != fE)) {
                list.add(new Z1.n(i3, length, new Z1.b(fE)));
            }
            list.add(new Z1.n(i3, length, new Z1.k(cVar.H())));
        }
    }

    @L1.a(name = "accessibilityRole")
    public void setAccessibilityRole(String str) {
        if (R()) {
            this.f7924F = C0433h0.d.c(str);
            y0();
        }
    }

    @L1.a(name = "adjustsFontSizeToFit")
    public void setAdjustFontSizeToFit(boolean z3) {
        if (z3 != this.f7938T) {
            this.f7938T = z3;
            y0();
        }
    }

    @L1.a(defaultBoolean = true, name = "allowFontScaling")
    public void setAllowFontScaling(boolean z3) {
        if (z3 != this.f7919A.b()) {
            this.f7919A.m(z3);
            y0();
        }
    }

    @L1.a(customType = "Color", name = "backgroundColor")
    public void setBackgroundColor(Integer num) {
        if (R()) {
            boolean z3 = num != null;
            this.f7922D = z3;
            if (z3) {
                this.f7923E = num.intValue();
            }
            y0();
        }
    }

    @L1.a(customType = "Color", name = "color")
    public void setColor(Integer num) {
        boolean z3 = num != null;
        this.f7920B = z3;
        if (z3) {
            this.f7921C = num.intValue();
        }
        y0();
    }

    @L1.a(name = "fontFamily")
    public void setFontFamily(String str) {
        this.f7942X = str;
        y0();
    }

    @L1.a(defaultFloat = Float.NaN, name = "fontSize")
    public void setFontSize(float f3) {
        this.f7919A.n(f3);
        y0();
    }

    @L1.a(name = "fontStyle")
    public void setFontStyle(String str) {
        int iB = p.b(str);
        if (iB != this.f7940V) {
            this.f7940V = iB;
            y0();
        }
    }

    @L1.a(name = "fontVariant")
    public void setFontVariant(ReadableArray readableArray) {
        String strC = p.c(readableArray);
        if (TextUtils.equals(strC, this.f7943Y)) {
            return;
        }
        this.f7943Y = strC;
        y0();
    }

    @L1.a(name = "fontWeight")
    public void setFontWeight(String str) {
        int iD = p.d(str);
        if (iD != this.f7941W) {
            this.f7941W = iD;
            y0();
        }
    }

    @L1.a(defaultBoolean = true, name = "includeFontPadding")
    public void setIncludeFontPadding(boolean z3) {
        this.f7937S = z3;
    }

    @L1.a(defaultFloat = 0.0f, name = "letterSpacing")
    public void setLetterSpacing(float f3) {
        this.f7919A.p(f3);
        y0();
    }

    @L1.a(defaultFloat = Float.NaN, name = "lineHeight")
    public void setLineHeight(float f3) {
        this.f7919A.q(f3);
        y0();
    }

    @L1.a(defaultFloat = Float.NaN, name = "maxFontSizeMultiplier")
    public void setMaxFontSizeMultiplier(float f3) {
        if (f3 != this.f7919A.k()) {
            this.f7919A.r(f3);
            y0();
        }
    }

    @L1.a(name = "minimumFontScale")
    public void setMinimumFontScale(float f3) {
        if (f3 != this.f7939U) {
            this.f7939U = f3;
            y0();
        }
    }

    @L1.a(defaultInt = -1, name = "numberOfLines")
    public void setNumberOfLines(int i3) {
        if (i3 == 0) {
            i3 = -1;
        }
        this.f7926H = i3;
        y0();
    }

    @L1.a(name = "role")
    public void setRole(String str) {
        if (R()) {
            this.f7925G = C0433h0.e.b(str);
            y0();
        }
    }

    @L1.a(name = "textAlign")
    public void setTextAlign(String str) {
        if ("justify".equals(str)) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.f7930L = 1;
            }
            this.f7927I = 3;
        } else {
            if (Build.VERSION.SDK_INT >= 26) {
                this.f7930L = 0;
            }
            if (str == null || "auto".equals(str)) {
                this.f7927I = 0;
            } else if ("left".equals(str)) {
                this.f7927I = 3;
            } else if ("right".equals(str)) {
                this.f7927I = 5;
            } else if ("center".equals(str)) {
                this.f7927I = 1;
            } else {
                Y.a.I("ReactNative", "Invalid textAlign: " + str);
                this.f7927I = 0;
            }
        }
        y0();
    }

    @L1.a(name = "textBreakStrategy")
    public void setTextBreakStrategy(String str) {
        if (str == null || "highQuality".equals(str)) {
            this.f7928J = 1;
        } else if ("simple".equals(str)) {
            this.f7928J = 0;
        } else if ("balanced".equals(str)) {
            this.f7928J = 2;
        } else {
            Y.a.I("ReactNative", "Invalid textBreakStrategy: " + str);
            this.f7928J = 1;
        }
        y0();
    }

    @L1.a(name = "textDecorationLine")
    public void setTextDecorationLine(String str) {
        this.f7935Q = false;
        this.f7936R = false;
        if (str != null) {
            for (String str2 : str.split(" ")) {
                if ("underline".equals(str2)) {
                    this.f7935Q = true;
                } else if ("line-through".equals(str2)) {
                    this.f7936R = true;
                }
            }
        }
        y0();
    }

    @L1.a(customType = "Color", defaultInt = 1426063360, name = "textShadowColor")
    public void setTextShadowColor(int i3) {
        if (i3 != this.f7934P) {
            this.f7934P = i3;
            y0();
        }
    }

    @L1.a(name = "textShadowOffset")
    public void setTextShadowOffset(ReadableMap readableMap) {
        this.f7931M = 0.0f;
        this.f7932N = 0.0f;
        if (readableMap != null) {
            if (readableMap.hasKey("width") && !readableMap.isNull("width")) {
                this.f7931M = C0429f0.g(readableMap.getDouble("width"));
            }
            if (readableMap.hasKey("height") && !readableMap.isNull("height")) {
                this.f7932N = C0429f0.g(readableMap.getDouble("height"));
            }
        }
        y0();
    }

    @L1.a(defaultInt = 1, name = "textShadowRadius")
    public void setTextShadowRadius(float f3) {
        if (f3 != this.f7933O) {
            this.f7933O = f3;
            y0();
        }
    }

    @L1.a(name = "textTransform")
    public void setTextTransform(String str) {
        if (str == null) {
            this.f7919A.s(u.f8058g);
        } else if ("none".equals(str)) {
            this.f7919A.s(u.f8054c);
        } else if ("uppercase".equals(str)) {
            this.f7919A.s(u.f8055d);
        } else if ("lowercase".equals(str)) {
            this.f7919A.s(u.f8056e);
        } else if ("capitalize".equals(str)) {
            this.f7919A.s(u.f8057f);
        } else {
            Y.a.I("ReactNative", "Invalid textTransform: " + str);
            this.f7919A.s(u.f8058g);
        }
        y0();
    }

    protected Spannable x1(c cVar, String str, boolean z3, C0423c0 c0423c0) {
        int iB;
        C0210a.b((z3 && c0423c0 == null) ? false : true, "nativeViewHierarchyOptimizer is required when inline views are supported");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ArrayList arrayList = new ArrayList();
        HashMap map = z3 ? new HashMap() : null;
        if (str != null) {
            spannableStringBuilder.append((CharSequence) u.b(str, cVar.f7919A.l()));
        }
        w1(cVar, spannableStringBuilder, arrayList, null, z3, map, 0);
        cVar.f7944Z = false;
        cVar.f7945a0 = map;
        float f3 = Float.NaN;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            Z1.n nVar = (Z1.n) arrayList.get((arrayList.size() - i3) - 1);
            Z1.i iVar = nVar.f2834c;
            boolean z4 = iVar instanceof Z1.p;
            if (z4 || (iVar instanceof Z1.q)) {
                if (z4) {
                    iB = ((Z1.p) iVar).b();
                    cVar.f7944Z = true;
                } else {
                    Z1.q qVar = (Z1.q) iVar;
                    int iA = qVar.a();
                    InterfaceC0451q0 interfaceC0451q0 = (InterfaceC0451q0) map.get(Integer.valueOf(qVar.b()));
                    c0423c0.h(interfaceC0451q0);
                    interfaceC0451q0.w(cVar);
                    iB = iA;
                }
                if (Float.isNaN(f3) || iB > f3) {
                    f3 = iB;
                }
            }
            nVar.a(spannableStringBuilder, i3);
        }
        cVar.f7919A.o(f3);
        return spannableStringBuilder;
    }

    public c(o oVar) {
        this.f7920B = false;
        this.f7922D = false;
        this.f7924F = null;
        this.f7925G = null;
        this.f7926H = -1;
        this.f7927I = 0;
        this.f7928J = 1;
        this.f7929K = 0;
        this.f7930L = 0;
        this.f7931M = 0.0f;
        this.f7932N = 0.0f;
        this.f7933O = 0.0f;
        this.f7934P = 1426063360;
        this.f7935Q = false;
        this.f7936R = false;
        this.f7937S = true;
        this.f7938T = false;
        this.f7939U = 0.0f;
        this.f7940V = -1;
        this.f7941W = -1;
        this.f7942X = null;
        this.f7943Y = null;
        this.f7944Z = false;
        this.f7919A = new s();
    }
}
