package androidx.core.graphics;

import android.graphics.Color;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ThreadLocal f4470a = new ThreadLocal();

    public static int a(double d4, double d5, double d6) {
        double d7 = (((3.2406d * d4) + ((-1.5372d) * d5)) + ((-0.4986d) * d6)) / 100.0d;
        double d8 = ((((-0.9689d) * d4) + (1.8758d * d5)) + (0.0415d * d6)) / 100.0d;
        double d9 = (((0.0557d * d4) + ((-0.204d) * d5)) + (1.057d * d6)) / 100.0d;
        return Color.rgb(f((int) Math.round((d7 > 0.0031308d ? (Math.pow(d7, 0.4166666666666667d) * 1.055d) - 0.055d : d7 * 12.92d) * 255.0d), 0, 255), f((int) Math.round((d8 > 0.0031308d ? (Math.pow(d8, 0.4166666666666667d) * 1.055d) - 0.055d : d8 * 12.92d) * 255.0d), 0, 255), f((int) Math.round((d9 > 0.0031308d ? (Math.pow(d9, 0.4166666666666667d) * 1.055d) - 0.055d : d9 * 12.92d) * 255.0d), 0, 255));
    }

    public static int b(int i3, int i4, float f3) {
        float f4 = 1.0f - f3;
        return Color.argb((int) ((Color.alpha(i3) * f4) + (Color.alpha(i4) * f3)), (int) ((Color.red(i3) * f4) + (Color.red(i4) * f3)), (int) ((Color.green(i3) * f4) + (Color.green(i4) * f3)), (int) ((Color.blue(i3) * f4) + (Color.blue(i4) * f3)));
    }

    private static int c(int i3, int i4) {
        return 255 - (((255 - i4) * (255 - i3)) / 255);
    }

    public static int d(int i3, int i4) {
        int iAlpha = Color.alpha(i4);
        int iAlpha2 = Color.alpha(i3);
        int iC = c(iAlpha2, iAlpha);
        return Color.argb(iC, e(Color.red(i3), iAlpha2, Color.red(i4), iAlpha, iC), e(Color.green(i3), iAlpha2, Color.green(i4), iAlpha, iC), e(Color.blue(i3), iAlpha2, Color.blue(i4), iAlpha, iC));
    }

    private static int e(int i3, int i4, int i5, int i6, int i7) {
        if (i7 == 0) {
            return 0;
        }
        return (((i3 * 255) * i4) + ((i5 * i6) * (255 - i4))) / (i7 * 255);
    }

    private static int f(int i3, int i4, int i5) {
        return i3 < i4 ? i4 : Math.min(i3, i5);
    }

    public static int g(int i3, int i4) {
        if (i4 < 0 || i4 > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (i3 & 16777215) | (i4 << 24);
    }
}
