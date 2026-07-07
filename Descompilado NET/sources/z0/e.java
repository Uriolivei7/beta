package Z0;

import a0.C0208b;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.util.Pair;
import java.io.InputStream;
import java.nio.ByteBuffer;
import kotlin.Lazy;
import r2.AbstractC0681d;

/* JADX INFO: loaded from: classes.dex */
public final class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final e f2801a = new e();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Lazy f2802b = AbstractC0681d.a(new C2.a() { // from class: Z0.b
        @Override // C2.a
        public final Object a() {
            return e.b();
        }
    });

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static boolean f2803c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static boolean f2804d;

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f2805a;

        static {
            int[] iArr = new int[Bitmap.Config.values().length];
            try {
                iArr[Bitmap.Config.ARGB_8888.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[Bitmap.Config.ALPHA_8.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[Bitmap.Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[Bitmap.Config.RGB_565.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[Bitmap.Config.RGBA_F16.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[Bitmap.Config.RGBA_1010102.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[Bitmap.Config.HARDWARE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            f2805a = iArr;
        }
    }

    private e() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final q.f b() {
        return new q.f(12);
    }

    private final ByteBuffer c() {
        return f2803c ? C0208b.f2850a.b() : (ByteBuffer) g().b();
    }

    public static final Pair d(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalStateException("Required value was null.");
        }
        e eVar = f2801a;
        ByteBuffer byteBufferK = eVar.k();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            options.inTempStorage = byteBufferK.array();
            Pair pair = null;
            eVar.f(inputStream, null, options);
            if (options.outWidth != -1 && options.outHeight != -1) {
                pair = new Pair(Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
            }
            eVar.l(byteBufferK);
            return pair;
        } catch (Throwable th) {
            f2801a.l(byteBufferK);
            throw th;
        }
    }

    public static final g e(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalStateException("Required value was null.");
        }
        e eVar = f2801a;
        ByteBuffer byteBufferK = eVar.k();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            options.inTempStorage = byteBufferK.array();
            eVar.f(inputStream, null, options);
            g gVar = new g(options.outWidth, options.outHeight, Build.VERSION.SDK_INT >= 26 ? options.outColorSpace : null);
            eVar.l(byteBufferK);
            return gVar;
        } catch (Throwable th) {
            f2801a.l(byteBufferK);
            throw th;
        }
    }

    private final q.f g() {
        return (q.f) f2802b.getValue();
    }

    public static final int h(Bitmap.Config config) {
        switch (config == null ? -1 : a.f2805a[config.ordinal()]) {
            case 1:
            case 6:
            case 7:
                return 4;
            case 2:
                return 1;
            case 3:
            case 4:
                return 2;
            case 5:
                return 8;
            default:
                throw new UnsupportedOperationException("The provided Bitmap.Config is not supported");
        }
    }

    public static final int i(int i3, int i4, Bitmap.Config config) {
        if (i3 <= 0) {
            throw new IllegalArgumentException(("width must be > 0, width is: " + i3).toString());
        }
        if (i4 <= 0) {
            throw new IllegalArgumentException(("height must be > 0, height is: " + i4).toString());
        }
        int iH = h(config);
        int i5 = i3 * i4 * iH;
        if (i5 > 0) {
            return i5;
        }
        throw new IllegalStateException(("size must be > 0: size: " + i5 + ", width: " + i3 + ", height: " + i4 + ", pixelSize: " + iH).toString());
    }

    public static final int j(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        try {
            return bitmap.getAllocationByteCount();
        } catch (NullPointerException unused) {
            return bitmap.getByteCount();
        }
    }

    private final ByteBuffer k() {
        ByteBuffer byteBufferC = c();
        if (byteBufferC != null) {
            return byteBufferC;
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(C0208b.e());
        D2.h.e(byteBufferAllocate, "allocate(...)");
        return byteBufferAllocate;
    }

    private final void l(ByteBuffer byteBuffer) {
        if (f2803c) {
            return;
        }
        g().a(byteBuffer);
    }

    public final Bitmap f(InputStream inputStream, Rect rect, BitmapFactory.Options options) {
        if (!f2804d) {
            return BitmapFactory.decodeStream(inputStream, rect, options);
        }
        try {
            return BitmapFactory.decodeStream(inputStream, rect, options);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }
}
