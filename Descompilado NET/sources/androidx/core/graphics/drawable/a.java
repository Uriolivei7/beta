package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: androidx.core.graphics.drawable.a$a, reason: collision with other inner class name */
    static class C0062a {
        static void a(Drawable drawable, Resources.Theme theme) {
            drawable.applyTheme(theme);
        }

        static boolean b(Drawable drawable) {
            return drawable.canApplyTheme();
        }

        static ColorFilter c(Drawable drawable) {
            return drawable.getColorFilter();
        }

        static void d(Drawable drawable, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
            drawable.inflate(resources, xmlPullParser, attributeSet, theme);
        }

        static void e(Drawable drawable, float f3, float f4) {
            drawable.setHotspot(f3, f4);
        }

        static void f(Drawable drawable, int i3, int i4, int i5, int i6) {
            drawable.setHotspotBounds(i3, i4, i5, i6);
        }

        static void g(Drawable drawable, int i3) {
            drawable.setTint(i3);
        }

        static void h(Drawable drawable, ColorStateList colorStateList) {
            drawable.setTintList(colorStateList);
        }

        static void i(Drawable drawable, PorterDuff.Mode mode) {
            drawable.setTintMode(mode);
        }
    }

    static class b {
        static int a(Drawable drawable) {
            return drawable.getLayoutDirection();
        }

        static boolean b(Drawable drawable, int i3) {
            return drawable.setLayoutDirection(i3);
        }
    }

    public static boolean a(Drawable drawable) {
        return drawable.isAutoMirrored();
    }

    public static void b(Drawable drawable, boolean z3) {
        drawable.setAutoMirrored(z3);
    }

    public static void c(Drawable drawable, float f3, float f4) {
        C0062a.e(drawable, f3, f4);
    }

    public static void d(Drawable drawable, int i3, int i4, int i5, int i6) {
        C0062a.f(drawable, i3, i4, i5, i6);
    }

    public static boolean e(Drawable drawable, int i3) {
        return b.b(drawable, i3);
    }

    public static void f(Drawable drawable, int i3) {
        C0062a.g(drawable, i3);
    }

    public static void g(Drawable drawable, ColorStateList colorStateList) {
        C0062a.h(drawable, colorStateList);
    }

    public static void h(Drawable drawable, PorterDuff.Mode mode) {
        C0062a.i(drawable, mode);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Drawable i(Drawable drawable) {
        return drawable instanceof androidx.core.graphics.drawable.b ? ((androidx.core.graphics.drawable.b) drawable).b() : drawable;
    }

    public static Drawable j(Drawable drawable) {
        return drawable;
    }
}
