package com.facebook.imagepipeline.nativecode;

import I0.i;
import O0.j;
import R0.h;
import X.k;
import X.p;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.os.Build;
import b0.AbstractC0306a;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class DalvikPurgeableDecoder implements S0.f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected static final byte[] f5944b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final R0.g f5945a = h.a();

    private static class OreoUtils {
        private OreoUtils() {
        }

        static void a(BitmapFactory.Options options, ColorSpace colorSpace) {
            if (colorSpace == null) {
                colorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
            }
            options.inPreferredColorSpace = colorSpace;
        }
    }

    static {
        d.a();
        f5944b = new byte[]{-1, -39};
    }

    protected DalvikPurgeableDecoder() {
    }

    public static boolean e(AbstractC0306a abstractC0306a, int i3) {
        a0.h hVar = (a0.h) abstractC0306a.P();
        return i3 >= 2 && hVar.g(i3 + (-2)) == -1 && hVar.g(i3 - 1) == -39;
    }

    public static BitmapFactory.Options f(int i3, Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = config;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = i3;
        options.inMutable = true;
        return options;
    }

    private static native void nativePinBitmap(Bitmap bitmap);

    @Override // S0.f
    public AbstractC0306a a(j jVar, Bitmap.Config config, Rect rect, int i3, ColorSpace colorSpace) {
        BitmapFactory.Options optionsF = f(jVar.a0(), config);
        if (Build.VERSION.SDK_INT >= 26) {
            OreoUtils.a(optionsF, colorSpace);
        }
        AbstractC0306a abstractC0306aV = jVar.v();
        k.g(abstractC0306aV);
        try {
            return g(d(abstractC0306aV, i3, optionsF));
        } finally {
            AbstractC0306a.D(abstractC0306aV);
        }
    }

    @Override // S0.f
    public AbstractC0306a b(j jVar, Bitmap.Config config, Rect rect, ColorSpace colorSpace) {
        BitmapFactory.Options optionsF = f(jVar.a0(), config);
        if (Build.VERSION.SDK_INT >= 26) {
            OreoUtils.a(optionsF, colorSpace);
        }
        AbstractC0306a abstractC0306aV = jVar.v();
        k.g(abstractC0306aV);
        try {
            return g(c(abstractC0306aV, optionsF));
        } finally {
            AbstractC0306a.D(abstractC0306aV);
        }
    }

    protected abstract Bitmap c(AbstractC0306a abstractC0306a, BitmapFactory.Options options);

    protected abstract Bitmap d(AbstractC0306a abstractC0306a, int i3, BitmapFactory.Options options);

    public AbstractC0306a g(Bitmap bitmap) {
        k.g(bitmap);
        try {
            nativePinBitmap(bitmap);
            if (this.f5945a.g(bitmap)) {
                return AbstractC0306a.n0(bitmap, this.f5945a.e());
            }
            int iJ = Z0.e.j(bitmap);
            bitmap.recycle();
            throw new i(String.format(Locale.US, "Attempted to pin a bitmap of size %d bytes. The current pool count is %d, the current pool size is %d bytes. The current pool max count is %d, the current pool max size is %d bytes.", Integer.valueOf(iJ), Integer.valueOf(this.f5945a.b()), Long.valueOf(this.f5945a.f()), Integer.valueOf(this.f5945a.c()), Integer.valueOf(this.f5945a.d())));
        } catch (Exception e4) {
            bitmap.recycle();
            throw p.a(e4);
        }
    }
}
