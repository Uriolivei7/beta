package androidx.core.graphics.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;

/* JADX INFO: loaded from: classes.dex */
public class IconCompat extends CustomVersionedParcelable {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    static final PorterDuff.Mode f4480k = PorterDuff.Mode.SRC_IN;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Object f4482b;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public String f4490j;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f4481a = -1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte[] f4483c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Parcelable f4484d = null;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public int f4485e = 0;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public int f4486f = 0;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public ColorStateList f4487g = null;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    PorterDuff.Mode f4488h = f4480k;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public String f4489i = null;

    static class a {
        static int a(Object obj) {
            if (Build.VERSION.SDK_INT >= 28) {
                return c.a(obj);
            }
            try {
                return ((Integer) obj.getClass().getMethod("getResId", new Class[0]).invoke(obj, new Object[0])).intValue();
            } catch (IllegalAccessException e4) {
                Log.e("IconCompat", "Unable to get icon resource", e4);
                return 0;
            } catch (NoSuchMethodException e5) {
                Log.e("IconCompat", "Unable to get icon resource", e5);
                return 0;
            } catch (InvocationTargetException e6) {
                Log.e("IconCompat", "Unable to get icon resource", e6);
                return 0;
            }
        }

        static String b(Object obj) {
            if (Build.VERSION.SDK_INT >= 28) {
                return c.b(obj);
            }
            try {
                return (String) obj.getClass().getMethod("getResPackage", new Class[0]).invoke(obj, new Object[0]);
            } catch (IllegalAccessException e4) {
                Log.e("IconCompat", "Unable to get icon package", e4);
                return null;
            } catch (NoSuchMethodException e5) {
                Log.e("IconCompat", "Unable to get icon package", e5);
                return null;
            } catch (InvocationTargetException e6) {
                Log.e("IconCompat", "Unable to get icon package", e6);
                return null;
            }
        }

        static Uri c(Object obj) {
            if (Build.VERSION.SDK_INT >= 28) {
                return c.d(obj);
            }
            try {
                return (Uri) obj.getClass().getMethod("getUri", new Class[0]).invoke(obj, new Object[0]);
            } catch (IllegalAccessException e4) {
                Log.e("IconCompat", "Unable to get icon uri", e4);
                return null;
            } catch (NoSuchMethodException e5) {
                Log.e("IconCompat", "Unable to get icon uri", e5);
                return null;
            } catch (InvocationTargetException e6) {
                Log.e("IconCompat", "Unable to get icon uri", e6);
                return null;
            }
        }

        static Drawable d(Icon icon, Context context) {
            return icon.loadDrawable(context);
        }

        static Icon e(IconCompat iconCompat, Context context) {
            Icon iconCreateWithBitmap;
            switch (iconCompat.f4481a) {
                case -1:
                    return (Icon) iconCompat.f4482b;
                case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                default:
                    throw new IllegalArgumentException("Unknown type");
                case 1:
                    iconCreateWithBitmap = Icon.createWithBitmap((Bitmap) iconCompat.f4482b);
                    break;
                case 2:
                    iconCreateWithBitmap = Icon.createWithResource(iconCompat.c(), iconCompat.f4485e);
                    break;
                case 3:
                    iconCreateWithBitmap = Icon.createWithData((byte[]) iconCompat.f4482b, iconCompat.f4485e, iconCompat.f4486f);
                    break;
                case 4:
                    iconCreateWithBitmap = Icon.createWithContentUri((String) iconCompat.f4482b);
                    break;
                case 5:
                    iconCreateWithBitmap = Build.VERSION.SDK_INT < 26 ? Icon.createWithBitmap(IconCompat.a((Bitmap) iconCompat.f4482b, false)) : b.b((Bitmap) iconCompat.f4482b);
                    break;
                case 6:
                    int i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 30) {
                        iconCreateWithBitmap = d.a(iconCompat.d());
                    } else {
                        if (context == null) {
                            throw new IllegalArgumentException("Context is required to resolve the file uri of the icon: " + iconCompat.d());
                        }
                        InputStream inputStreamE = iconCompat.e(context);
                        if (inputStreamE == null) {
                            throw new IllegalStateException("Cannot load adaptive icon from uri: " + iconCompat.d());
                        }
                        if (i3 < 26) {
                            iconCreateWithBitmap = Icon.createWithBitmap(IconCompat.a(BitmapFactory.decodeStream(inputStreamE), false));
                        } else {
                            iconCreateWithBitmap = b.b(BitmapFactory.decodeStream(inputStreamE));
                        }
                    }
                    break;
            }
            ColorStateList colorStateList = iconCompat.f4487g;
            if (colorStateList != null) {
                iconCreateWithBitmap.setTintList(colorStateList);
            }
            PorterDuff.Mode mode = iconCompat.f4488h;
            if (mode != IconCompat.f4480k) {
                iconCreateWithBitmap.setTintMode(mode);
            }
            return iconCreateWithBitmap;
        }
    }

    static class b {
        static Drawable a(Drawable drawable, Drawable drawable2) {
            return new AdaptiveIconDrawable(drawable, drawable2);
        }

        static Icon b(Bitmap bitmap) {
            return Icon.createWithAdaptiveBitmap(bitmap);
        }
    }

    static class c {
        static int a(Object obj) {
            return ((Icon) obj).getResId();
        }

        static String b(Object obj) {
            return ((Icon) obj).getResPackage();
        }

        static int c(Object obj) {
            return ((Icon) obj).getType();
        }

        static Uri d(Object obj) {
            return ((Icon) obj).getUri();
        }
    }

    static class d {
        static Icon a(Uri uri) {
            return Icon.createWithAdaptiveBitmapContentUri(uri);
        }
    }

    static Bitmap a(Bitmap bitmap, boolean z3) {
        int iMin = (int) (Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMin, iMin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(3);
        float f3 = iMin;
        float f4 = 0.5f * f3;
        float f5 = 0.9166667f * f4;
        if (z3) {
            float f6 = 0.010416667f * f3;
            paint.setColor(0);
            paint.setShadowLayer(f6, 0.0f, f3 * 0.020833334f, 1023410176);
            canvas.drawCircle(f4, f4, f5, paint);
            paint.setShadowLayer(f6, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f4, f4, f5, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        Matrix matrix = new Matrix();
        matrix.setTranslate((-(bitmap.getWidth() - iMin)) / 2.0f, (-(bitmap.getHeight() - iMin)) / 2.0f);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f4, f4, f5, paint);
        canvas.setBitmap(null);
        return bitmapCreateBitmap;
    }

    private static String h(int i3) {
        switch (i3) {
            case 1:
                return "BITMAP";
            case 2:
                return "RESOURCE";
            case 3:
                return "DATA";
            case 4:
                return "URI";
            case 5:
                return "BITMAP_MASKABLE";
            case 6:
                return "URI_MASKABLE";
            default:
                return "UNKNOWN";
        }
    }

    public int b() {
        int i3 = this.f4481a;
        if (i3 == -1) {
            return a.a(this.f4482b);
        }
        if (i3 == 2) {
            return this.f4485e;
        }
        throw new IllegalStateException("called getResId() on " + this);
    }

    public String c() {
        int i3 = this.f4481a;
        if (i3 == -1) {
            return a.b(this.f4482b);
        }
        if (i3 == 2) {
            String str = this.f4490j;
            return (str == null || TextUtils.isEmpty(str)) ? ((String) this.f4482b).split(":", -1)[0] : this.f4490j;
        }
        throw new IllegalStateException("called getResPackage() on " + this);
    }

    public Uri d() {
        int i3 = this.f4481a;
        if (i3 == -1) {
            return a.c(this.f4482b);
        }
        if (i3 == 4 || i3 == 6) {
            return Uri.parse((String) this.f4482b);
        }
        throw new IllegalStateException("called getUri() on " + this);
    }

    public InputStream e(Context context) {
        Uri uriD = d();
        String scheme = uriD.getScheme();
        if ("content".equals(scheme) || "file".equals(scheme)) {
            try {
                return context.getContentResolver().openInputStream(uriD);
            } catch (Exception e4) {
                Log.w("IconCompat", "Unable to load image from URI: " + uriD, e4);
                return null;
            }
        }
        try {
            return new FileInputStream(new File((String) this.f4482b));
        } catch (FileNotFoundException e5) {
            Log.w("IconCompat", "Unable to load image from path: " + uriD, e5);
            return null;
        }
    }

    public void f() {
        this.f4488h = PorterDuff.Mode.valueOf(this.f4489i);
        switch (this.f4481a) {
            case -1:
                Parcelable parcelable = this.f4484d;
                if (parcelable == null) {
                    throw new IllegalArgumentException("Invalid icon");
                }
                this.f4482b = parcelable;
                return;
            case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
            default:
                return;
            case 1:
            case 5:
                Parcelable parcelable2 = this.f4484d;
                if (parcelable2 != null) {
                    this.f4482b = parcelable2;
                    return;
                }
                byte[] bArr = this.f4483c;
                this.f4482b = bArr;
                this.f4481a = 3;
                this.f4485e = 0;
                this.f4486f = bArr.length;
                return;
            case 2:
            case 4:
            case 6:
                String str = new String(this.f4483c, Charset.forName("UTF-16"));
                this.f4482b = str;
                if (this.f4481a == 2 && this.f4490j == null) {
                    this.f4490j = str.split(":", -1)[0];
                    return;
                }
                return;
            case 3:
                this.f4482b = this.f4483c;
                return;
        }
    }

    public void g(boolean z3) {
        this.f4489i = this.f4488h.name();
        switch (this.f4481a) {
            case -1:
                if (z3) {
                    throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
                }
                this.f4484d = (Parcelable) this.f4482b;
                return;
            case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
            default:
                return;
            case 1:
            case 5:
                if (!z3) {
                    this.f4484d = (Parcelable) this.f4482b;
                    return;
                }
                Bitmap bitmap = (Bitmap) this.f4482b;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                this.f4483c = byteArrayOutputStream.toByteArray();
                return;
            case 2:
                this.f4483c = ((String) this.f4482b).getBytes(Charset.forName("UTF-16"));
                return;
            case 3:
                this.f4483c = (byte[]) this.f4482b;
                return;
            case 4:
            case 6:
                this.f4483c = this.f4482b.toString().getBytes(Charset.forName("UTF-16"));
                return;
        }
    }

    public String toString() {
        if (this.f4481a == -1) {
            return String.valueOf(this.f4482b);
        }
        StringBuilder sb = new StringBuilder("Icon(typ=");
        sb.append(h(this.f4481a));
        switch (this.f4481a) {
            case 1:
            case 5:
                sb.append(" size=");
                sb.append(((Bitmap) this.f4482b).getWidth());
                sb.append("x");
                sb.append(((Bitmap) this.f4482b).getHeight());
                break;
            case 2:
                sb.append(" pkg=");
                sb.append(this.f4490j);
                sb.append(" id=");
                sb.append(String.format("0x%08x", Integer.valueOf(b())));
                break;
            case 3:
                sb.append(" len=");
                sb.append(this.f4485e);
                if (this.f4486f != 0) {
                    sb.append(" off=");
                    sb.append(this.f4486f);
                }
                break;
            case 4:
            case 6:
                sb.append(" uri=");
                sb.append(this.f4482b);
                break;
        }
        if (this.f4487g != null) {
            sb.append(" tint=");
            sb.append(this.f4487g);
        }
        if (this.f4488h != f4480k) {
            sb.append(" mode=");
            sb.append(this.f4488h);
        }
        sb.append(")");
        return sb.toString();
    }
}
