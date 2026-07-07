package com.facebook.react.uimanager;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableType;

/* JADX INFO: loaded from: classes.dex */
public class U extends C0452r0 {

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final b f7387y = new b();

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    boolean f7388z;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f7389a;

        static {
            int[] iArr = new int[com.facebook.yoga.w.values().length];
            f7389a = iArr;
            try {
                iArr[com.facebook.yoga.w.POINT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7389a[com.facebook.yoga.w.UNDEFINED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f7389a[com.facebook.yoga.w.AUTO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f7389a[com.facebook.yoga.w.PERCENT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        float f7390a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        com.facebook.yoga.w f7391b;

        void a(Dynamic dynamic) {
            if (dynamic.isNull()) {
                this.f7391b = com.facebook.yoga.w.UNDEFINED;
                this.f7390a = Float.NaN;
                return;
            }
            if (dynamic.getType() != ReadableType.String) {
                if (dynamic.getType() == ReadableType.Number) {
                    this.f7391b = com.facebook.yoga.w.POINT;
                    this.f7390a = C0429f0.g(dynamic.asDouble());
                    return;
                } else {
                    this.f7391b = com.facebook.yoga.w.UNDEFINED;
                    this.f7390a = Float.NaN;
                    return;
                }
            }
            String strAsString = dynamic.asString();
            if (strAsString.equals("auto")) {
                this.f7391b = com.facebook.yoga.w.AUTO;
                this.f7390a = Float.NaN;
            } else {
                if (strAsString.endsWith("%")) {
                    this.f7391b = com.facebook.yoga.w.PERCENT;
                    this.f7390a = Float.parseFloat(strAsString.substring(0, strAsString.length() - 1));
                    return;
                }
                Y.a.I("ReactNative", "Unknown value: " + strAsString);
                this.f7391b = com.facebook.yoga.w.UNDEFINED;
                this.f7390a = Float.NaN;
            }
        }

        private b() {
        }
    }

    private int v1(int i3) {
        if (!com.facebook.react.modules.i18nmanager.a.f().d(l())) {
            return i3;
        }
        if (i3 == 0) {
            return 4;
        }
        if (i3 != 2) {
            return i3;
        }
        return 5;
    }

    @L1.a(name = "alignContent")
    public void setAlignContent(String str) {
        if (R()) {
        }
        if (str == null) {
            D0(com.facebook.yoga.a.FLEX_START);
            return;
        }
        switch (str) {
            case "stretch":
                D0(com.facebook.yoga.a.STRETCH);
                break;
            case "baseline":
                D0(com.facebook.yoga.a.BASELINE);
                break;
            case "center":
                D0(com.facebook.yoga.a.CENTER);
                break;
            case "flex-start":
                D0(com.facebook.yoga.a.FLEX_START);
                break;
            case "auto":
                D0(com.facebook.yoga.a.AUTO);
                break;
            case "space-between":
                D0(com.facebook.yoga.a.SPACE_BETWEEN);
                break;
            case "flex-end":
                D0(com.facebook.yoga.a.FLEX_END);
                break;
            case "space-around":
                D0(com.facebook.yoga.a.SPACE_AROUND);
                break;
            case "space-evenly":
                D0(com.facebook.yoga.a.SPACE_EVENLY);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for alignContent: " + str);
                D0(com.facebook.yoga.a.FLEX_START);
                break;
        }
    }

    @L1.a(name = "alignItems")
    public void setAlignItems(String str) {
        if (R()) {
        }
        if (str == null) {
            E0(com.facebook.yoga.a.STRETCH);
            return;
        }
        switch (str) {
            case "stretch":
                E0(com.facebook.yoga.a.STRETCH);
                break;
            case "baseline":
                E0(com.facebook.yoga.a.BASELINE);
                break;
            case "center":
                E0(com.facebook.yoga.a.CENTER);
                break;
            case "flex-start":
                E0(com.facebook.yoga.a.FLEX_START);
                break;
            case "auto":
                E0(com.facebook.yoga.a.AUTO);
                break;
            case "space-between":
                E0(com.facebook.yoga.a.SPACE_BETWEEN);
                break;
            case "flex-end":
                E0(com.facebook.yoga.a.FLEX_END);
                break;
            case "space-around":
                E0(com.facebook.yoga.a.SPACE_AROUND);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for alignItems: " + str);
                E0(com.facebook.yoga.a.STRETCH);
                break;
        }
    }

    @L1.a(name = "alignSelf")
    public void setAlignSelf(String str) {
        if (R()) {
        }
        if (str == null) {
            F0(com.facebook.yoga.a.AUTO);
            return;
        }
        switch (str) {
            case "stretch":
                F0(com.facebook.yoga.a.STRETCH);
                break;
            case "baseline":
                F0(com.facebook.yoga.a.BASELINE);
                break;
            case "center":
                F0(com.facebook.yoga.a.CENTER);
                break;
            case "flex-start":
                F0(com.facebook.yoga.a.FLEX_START);
                break;
            case "auto":
                F0(com.facebook.yoga.a.AUTO);
                break;
            case "space-between":
                F0(com.facebook.yoga.a.SPACE_BETWEEN);
                break;
            case "flex-end":
                F0(com.facebook.yoga.a.FLEX_END);
                break;
            case "space-around":
                F0(com.facebook.yoga.a.SPACE_AROUND);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for alignSelf: " + str);
                F0(com.facebook.yoga.a.AUTO);
                break;
        }
    }

    @L1.a(defaultFloat = Float.NaN, name = "aspectRatio")
    public void setAspectRatio(float f3) {
        g1(f3);
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderWidth", "borderStartWidth", "borderEndWidth", "borderTopWidth", "borderBottomWidth", "borderLeftWidth", "borderRightWidth"})
    public void setBorderWidths(int i3, float f3) {
        if (R()) {
            return;
        }
        H0(v1(Z0.f7437b[i3]), C0429f0.h(f3));
    }

    @L1.a(name = "collapsable")
    public void setCollapsable(boolean z3) {
        this.f7388z = z3;
    }

    @L1.a(name = "collapsableChildren")
    public void setCollapsableChildren(boolean z3) {
    }

    @L1.a(name = "columnGap")
    public void setColumnGap(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2 || i3 == 3) {
            I0(this.f7387y.f7390a);
        } else if (i3 == 4) {
            J0(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "display")
    public void setDisplay(String str) {
        if (R()) {
            return;
        }
        if (str == null) {
            L0(com.facebook.yoga.i.FLEX);
            return;
        }
        if (str.equals("flex")) {
            L0(com.facebook.yoga.i.FLEX);
            return;
        }
        if (str.equals("none")) {
            L0(com.facebook.yoga.i.NONE);
            return;
        }
        Y.a.I("ReactNative", "invalid value for display: " + str);
        L0(com.facebook.yoga.i.FLEX);
    }

    @Override // com.facebook.react.uimanager.C0452r0
    @L1.a(defaultFloat = 0.0f, name = "flex")
    public void setFlex(float f3) {
        if (R()) {
            return;
        }
        super.setFlex(f3);
    }

    @L1.a(name = "flexBasis")
    public void setFlexBasis(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            M0(this.f7387y.f7390a);
        } else if (i3 == 3) {
            N0();
        } else if (i3 == 4) {
            O0(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "flexDirection")
    public void setFlexDirection(String str) {
        if (R()) {
        }
        if (str == null) {
            P0(com.facebook.yoga.l.COLUMN);
            return;
        }
        switch (str) {
            case "row-reverse":
                P0(com.facebook.yoga.l.ROW_REVERSE);
                break;
            case "column":
                P0(com.facebook.yoga.l.COLUMN);
                break;
            case "row":
                P0(com.facebook.yoga.l.ROW);
                break;
            case "column-reverse":
                P0(com.facebook.yoga.l.COLUMN_REVERSE);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for flexDirection: " + str);
                P0(com.facebook.yoga.l.COLUMN);
                break;
        }
    }

    @Override // com.facebook.react.uimanager.C0452r0
    @L1.a(defaultFloat = 0.0f, name = "flexGrow")
    public void setFlexGrow(float f3) {
        if (R()) {
            return;
        }
        super.setFlexGrow(f3);
    }

    @Override // com.facebook.react.uimanager.C0452r0
    @L1.a(defaultFloat = 0.0f, name = "flexShrink")
    public void setFlexShrink(float f3) {
        if (R()) {
            return;
        }
        super.setFlexShrink(f3);
    }

    @L1.a(name = "flexWrap")
    public void setFlexWrap(String str) {
        if (R()) {
        }
        if (str == null) {
            Q0(com.facebook.yoga.x.NO_WRAP);
            return;
        }
        switch (str) {
            case "nowrap":
                Q0(com.facebook.yoga.x.NO_WRAP);
                break;
            case "wrap-reverse":
                Q0(com.facebook.yoga.x.WRAP_REVERSE);
                break;
            case "wrap":
                Q0(com.facebook.yoga.x.WRAP);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for flexWrap: " + str);
                Q0(com.facebook.yoga.x.NO_WRAP);
                break;
        }
    }

    @L1.a(name = "gap")
    public void setGap(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2 || i3 == 3) {
            R0(this.f7387y.f7390a);
        } else if (i3 == 4) {
            S0(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "height")
    public void setHeight(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            g(this.f7387y.f7390a);
        } else if (i3 == 3) {
            h1();
        } else if (i3 == 4) {
            i1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "inset")
    public void setInset(Dynamic dynamic) {
    }

    @L1.b(names = {"insetBlock", "insetBlockEnd", "insetBlockStart"})
    public void setInsetBlock(int i3, Dynamic dynamic) {
    }

    @L1.b(names = {"insetInline", "insetInlineEnd", "insetInlineStart"})
    public void setInsetInline(int i3, Dynamic dynamic) {
    }

    @L1.a(name = "justifyContent")
    public void setJustifyContent(String str) {
        if (R()) {
        }
        if (str == null) {
            T0(com.facebook.yoga.n.FLEX_START);
            return;
        }
        switch (str) {
            case "center":
                T0(com.facebook.yoga.n.CENTER);
                break;
            case "flex-start":
                T0(com.facebook.yoga.n.FLEX_START);
                break;
            case "space-between":
                T0(com.facebook.yoga.n.SPACE_BETWEEN);
                break;
            case "flex-end":
                T0(com.facebook.yoga.n.FLEX_END);
                break;
            case "space-around":
                T0(com.facebook.yoga.n.SPACE_AROUND);
                break;
            case "space-evenly":
                T0(com.facebook.yoga.n.SPACE_EVENLY);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for justifyContent: " + str);
                T0(com.facebook.yoga.n.FLEX_START);
                break;
        }
    }

    @L1.b(names = {"marginBlock", "marginBlockEnd", "marginBlockStart"})
    public void setMarginBlock(int i3, Dynamic dynamic) {
    }

    @L1.b(names = {"marginInline", "marginInlineEnd", "marginInlineStart"})
    public void setMarginInline(int i3, Dynamic dynamic) {
    }

    @L1.b(names = {"margin", "marginVertical", "marginHorizontal", "marginStart", "marginEnd", "marginTop", "marginBottom", "marginLeft", "marginRight"})
    public void setMargins(int i3, Dynamic dynamic) {
        if (R()) {
            return;
        }
        int iV1 = v1(Z0.f7438c[i3]);
        this.f7387y.a(dynamic);
        int i4 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i4 == 1 || i4 == 2) {
            V0(iV1, this.f7387y.f7390a);
        } else if (i4 == 3) {
            W0(iV1);
        } else if (i4 == 4) {
            X0(iV1, this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "maxHeight")
    public void setMaxHeight(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            j1(this.f7387y.f7390a);
        } else if (i3 == 4) {
            k1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "maxWidth")
    public void setMaxWidth(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            l1(this.f7387y.f7390a);
        } else if (i3 == 4) {
            m1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "minHeight")
    public void setMinHeight(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            n1(this.f7387y.f7390a);
        } else if (i3 == 4) {
            o1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "minWidth")
    public void setMinWidth(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            p1(this.f7387y.f7390a);
        } else if (i3 == 4) {
            q1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "overflow")
    public void setOverflow(String str) {
        if (R()) {
        }
        if (str == null) {
            Z0(com.facebook.yoga.u.VISIBLE);
            return;
        }
        switch (str) {
            case "hidden":
                Z0(com.facebook.yoga.u.HIDDEN);
                break;
            case "scroll":
                Z0(com.facebook.yoga.u.SCROLL);
                break;
            case "visible":
                Z0(com.facebook.yoga.u.VISIBLE);
                break;
            default:
                Y.a.I("ReactNative", "invalid value for overflow: " + str);
                Z0(com.facebook.yoga.u.VISIBLE);
                break;
        }
    }

    @L1.b(names = {"paddingBlock", "paddingBlockEnd", "paddingBlockStart"})
    public void setPaddingBlock(int i3, Dynamic dynamic) {
    }

    @L1.b(names = {"paddingInline", "paddingInlineEnd", "paddingInlineStart"})
    public void setPaddingInline(int i3, Dynamic dynamic) {
    }

    @L1.b(names = {"padding", "paddingVertical", "paddingHorizontal", "paddingStart", "paddingEnd", "paddingTop", "paddingBottom", "paddingLeft", "paddingRight"})
    public void setPaddings(int i3, Dynamic dynamic) {
        if (R()) {
            return;
        }
        int iV1 = v1(Z0.f7438c[i3]);
        this.f7387y.a(dynamic);
        int i4 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i4 == 1 || i4 == 2) {
            S(iV1, this.f7387y.f7390a);
        } else if (i4 == 4) {
            a1(iV1, this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "position")
    public void setPosition(String str) {
        if (R()) {
            return;
        }
        if (str == null) {
            d1(com.facebook.yoga.v.RELATIVE);
            return;
        }
        if (str.equals("relative")) {
            d1(com.facebook.yoga.v.RELATIVE);
            return;
        }
        if (str.equals("absolute")) {
            d1(com.facebook.yoga.v.ABSOLUTE);
            return;
        }
        Y.a.I("ReactNative", "invalid value for position: " + str);
        d1(com.facebook.yoga.v.RELATIVE);
    }

    @L1.b(names = {"start", "end", "left", "right", "top", "bottom"})
    public void setPositionValues(int i3, Dynamic dynamic) {
        if (R()) {
            return;
        }
        int iV1 = v1(new int[]{4, 5, 0, 2, 1, 3}[i3]);
        this.f7387y.a(dynamic);
        int i4 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i4 == 1 || i4 == 2) {
            b1(iV1, this.f7387y.f7390a);
        } else if (i4 == 4) {
            c1(iV1, this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @L1.a(name = "rowGap")
    public void setRowGap(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2 || i3 == 3) {
            e1(this.f7387y.f7390a);
        } else if (i3 == 4) {
            f1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }

    @Override // com.facebook.react.uimanager.C0452r0
    @L1.a(name = "onLayout")
    public void setShouldNotifyOnLayout(boolean z3) {
        super.setShouldNotifyOnLayout(z3);
    }

    @L1.a(name = "onPointerEnter")
    public void setShouldNotifyPointerEnter(boolean z3) {
    }

    @L1.a(name = "onPointerLeave")
    public void setShouldNotifyPointerLeave(boolean z3) {
    }

    @L1.a(name = "onPointerMove")
    public void setShouldNotifyPointerMove(boolean z3) {
    }

    @L1.a(name = "width")
    public void setWidth(Dynamic dynamic) {
        if (R()) {
            return;
        }
        this.f7387y.a(dynamic);
        int i3 = a.f7389a[this.f7387y.f7391b.ordinal()];
        if (i3 == 1 || i3 == 2) {
            d0(this.f7387y.f7390a);
        } else if (i3 == 3) {
            r1();
        } else if (i3 == 4) {
            s1(this.f7387y.f7390a);
        }
        dynamic.recycle();
    }
}
