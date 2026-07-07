package W0;

import O0.j;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.os.Build;
import java.io.OutputStream;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class g implements c {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f2689d = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f2690a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2691b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f2692c = "SimpleImageTranscoder";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Bitmap.CompressFormat b(D0.c cVar) {
            return cVar == null ? Bitmap.CompressFormat.JPEG : cVar == D0.b.f135b ? Bitmap.CompressFormat.JPEG : cVar == D0.b.f136c ? Bitmap.CompressFormat.PNG : D0.b.a(cVar) ? Bitmap.CompressFormat.WEBP : Bitmap.CompressFormat.JPEG;
        }

        private a() {
        }
    }

    public g(boolean z3, int i3) {
        this.f2690a = z3;
        this.f2691b = i3;
    }

    private final int e(j jVar, I0.h hVar, I0.g gVar) {
        if (this.f2690a) {
            return W0.a.b(hVar, gVar, jVar, this.f2691b);
        }
        return 1;
    }

    @Override // W0.c
    public String a() {
        return this.f2692c;
    }

    @Override // W0.c
    public boolean b(D0.c cVar) {
        D2.h.f(cVar, "imageFormat");
        return cVar == D0.b.f145l || cVar == D0.b.f135b;
    }

    @Override // W0.c
    public b c(j jVar, OutputStream outputStream, I0.h hVar, I0.g gVar, D0.c cVar, Integer num, ColorSpace colorSpace) throws Throwable {
        g gVar2;
        I0.h hVarA;
        Bitmap bitmapCreateBitmap;
        b bVar;
        D2.h.f(jVar, "encodedImage");
        D2.h.f(outputStream, "outputStream");
        Integer num2 = num == null ? 85 : num;
        if (hVar == null) {
            hVarA = I0.h.f425c.a();
            gVar2 = this;
        } else {
            gVar2 = this;
            hVarA = hVar;
        }
        int iE = gVar2.e(jVar, hVarA, gVar);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = iE;
        if (colorSpace != null && Build.VERSION.SDK_INT >= 26) {
            options.inPreferredColorSpace = colorSpace;
        }
        try {
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(jVar.P(), null, options);
            if (bitmapDecodeStream == null) {
                Y.a.m("SimpleImageTranscoder", "Couldn't decode the EncodedImage InputStream ! ");
                return new b(2);
            }
            Matrix matrixG = e.g(jVar, hVarA);
            if (matrixG != null) {
                try {
                    bitmapCreateBitmap = Bitmap.createBitmap(bitmapDecodeStream, 0, 0, bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight(), matrixG, false);
                } catch (OutOfMemoryError e4) {
                    e = e4;
                    bitmapCreateBitmap = bitmapDecodeStream;
                    Y.a.n("SimpleImageTranscoder", "Out-Of-Memory during transcode", e);
                    bVar = new b(2);
                    bitmapCreateBitmap.recycle();
                    bitmapDecodeStream.recycle();
                    return bVar;
                } catch (Throwable th) {
                    th = th;
                    bitmapCreateBitmap = bitmapDecodeStream;
                    bitmapCreateBitmap.recycle();
                    bitmapDecodeStream.recycle();
                    throw th;
                }
            } else {
                bitmapCreateBitmap = bitmapDecodeStream;
            }
            try {
                try {
                    bitmapCreateBitmap.compress(f2689d.b(cVar), num2.intValue(), outputStream);
                    bVar = new b(iE > 1 ? 0 : 1);
                } catch (OutOfMemoryError e5) {
                    e = e5;
                    Y.a.n("SimpleImageTranscoder", "Out-Of-Memory during transcode", e);
                    bVar = new b(2);
                }
                bitmapCreateBitmap.recycle();
                bitmapDecodeStream.recycle();
                return bVar;
            } catch (Throwable th2) {
                th = th2;
                bitmapCreateBitmap.recycle();
                bitmapDecodeStream.recycle();
                throw th;
            }
        } catch (OutOfMemoryError e6) {
            Y.a.n("SimpleImageTranscoder", "Out-Of-Memory during transcode", e6);
            return new b(2);
        }
    }

    @Override // W0.c
    public boolean d(j jVar, I0.h hVar, I0.g gVar) {
        D2.h.f(jVar, "encodedImage");
        if (hVar == null) {
            hVar = I0.h.f425c.a();
        }
        return this.f2690a && W0.a.b(hVar, gVar, jVar, this.f2691b) > 1;
    }
}
