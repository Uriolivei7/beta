package U0;

import J0.z;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import b0.AbstractC0306a;
import com.facebook.imagepipeline.nativecode.Bitmaps;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Bitmap.Config f2378a = Bitmap.Config.ARGB_8888;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Method f2379b;

    private static void c(Bitmap bitmap, Bitmap bitmap2) {
        if (!z.a() || bitmap.getConfig() != bitmap2.getConfig()) {
            new Canvas(bitmap).drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
            return;
        }
        try {
            if (f2379b == null) {
                int i3 = Bitmaps.f5943a;
                f2379b = Bitmaps.class.getDeclaredMethod("copyBitmap", Bitmap.class, Bitmap.class);
            }
            f2379b.invoke(null, bitmap, bitmap2);
        } catch (ClassNotFoundException e4) {
            throw new RuntimeException("Wrong Native code setup, reflection failed.", e4);
        } catch (IllegalAccessException e5) {
            throw new RuntimeException("Wrong Native code setup, reflection failed.", e5);
        } catch (NoSuchMethodException e6) {
            throw new RuntimeException("Wrong Native code setup, reflection failed.", e6);
        } catch (InvocationTargetException e7) {
            throw new RuntimeException("Wrong Native code setup, reflection failed.", e7);
        }
    }

    @Override // U0.d
    public AbstractC0306a a(Bitmap bitmap, G0.b bVar) {
        Bitmap.Config config = bitmap.getConfig();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (config == null) {
            config = f2378a;
        }
        AbstractC0306a abstractC0306aD = bVar.d(width, height, config);
        try {
            e((Bitmap) abstractC0306aD.P(), bitmap);
            return abstractC0306aD.clone();
        } finally {
            AbstractC0306a.D(abstractC0306aD);
        }
    }

    @Override // U0.d
    public R.d b() {
        return null;
    }

    public void e(Bitmap bitmap, Bitmap bitmap2) {
        c(bitmap, bitmap2);
        d(bitmap);
    }

    @Override // U0.d
    public String getName() {
        return "Unknown postprocessor";
    }

    public void d(Bitmap bitmap) {
    }
}
