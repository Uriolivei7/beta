package S0;

import O0.j;
import R0.i;
import R0.r;
import X.k;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.os.Build;
import b0.AbstractC0306a;
import b0.InterfaceC0313h;
import com.facebook.imagepipeline.platform.PreverificationHelper;
import d0.C0489a;
import d0.C0490b;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class c implements f {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Class f2300f = c.class;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final byte[] f2301g = {-1, -39};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final i f2302a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2303b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f2304c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final PreverificationHelper f2305d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final q.e f2306e;

    public c(i iVar, q.e eVar, h hVar) {
        this.f2305d = Build.VERSION.SDK_INT >= 26 ? new PreverificationHelper() : null;
        this.f2302a = iVar;
        if (iVar instanceof r) {
            this.f2303b = hVar.a();
            this.f2304c = hVar.b();
        }
        this.f2306e = eVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00c7 A[Catch: all -> 0x00a3, RuntimeException -> 0x00a6, IllegalArgumentException -> 0x00a8, TRY_LEAVE, TryCatch #8 {IllegalArgumentException -> 0x00a8, RuntimeException -> 0x00a6, blocks: (B:36:0x007d, B:39:0x0087, B:49:0x009f, B:68:0x00c7, B:64:0x00c0, B:65:0x00c3, B:62:0x00ba), top: B:98:0x007d, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00f1  */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private b0.AbstractC0306a c(java.io.InputStream r9, android.graphics.BitmapFactory.Options r10, android.graphics.Rect r11, android.graphics.ColorSpace r12) {
        /*
            Method dump skipped, instruction units count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: S0.c.c(java.io.InputStream, android.graphics.BitmapFactory$Options, android.graphics.Rect, android.graphics.ColorSpace):b0.a");
    }

    private static BitmapFactory.Options e(j jVar, Bitmap.Config config, boolean z3) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = jVar.a0();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        boolean z4 = Build.VERSION.SDK_INT >= 26 && config == Bitmap.Config.HARDWARE;
        if (!z4) {
            options.inPreferredConfig = config;
        }
        options.inMutable = true;
        if (!z3) {
            BitmapFactory.decodeStream(jVar.P(), null, options);
            if (options.outWidth == -1 || options.outHeight == -1) {
                throw new IllegalArgumentException();
            }
        }
        if (z4) {
            options.inPreferredConfig = config;
        }
        options.inJustDecodeBounds = false;
        return options;
    }

    @Override // S0.f
    public AbstractC0306a a(j jVar, Bitmap.Config config, Rect rect, int i3, ColorSpace colorSpace) {
        boolean zT0 = jVar.t0(i3);
        BitmapFactory.Options optionsE = e(jVar, config, this.f2303b);
        InputStream inputStreamP = jVar.P();
        k.g(inputStreamP);
        if (jVar.c0() > i3) {
            inputStreamP = new C0489a(inputStreamP, i3);
        }
        if (!zT0) {
            inputStreamP = new C0490b(inputStreamP, f2301g);
        }
        boolean z3 = optionsE.inPreferredConfig != Bitmap.Config.ARGB_8888;
        try {
            try {
                return c(inputStreamP, optionsE, rect, colorSpace);
            } catch (RuntimeException e4) {
                if (!z3) {
                    throw e4;
                }
                AbstractC0306a abstractC0306aA = a(jVar, Bitmap.Config.ARGB_8888, rect, i3, colorSpace);
                try {
                    inputStreamP.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                return abstractC0306aA;
            }
        } finally {
            try {
                inputStreamP.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
    }

    @Override // S0.f
    public AbstractC0306a b(j jVar, Bitmap.Config config, Rect rect, ColorSpace colorSpace) {
        BitmapFactory.Options optionsE = e(jVar, config, this.f2303b);
        boolean z3 = optionsE.inPreferredConfig != Bitmap.Config.ARGB_8888;
        try {
            return c((InputStream) k.g(jVar.P()), optionsE, rect, colorSpace);
        } catch (RuntimeException e4) {
            if (z3) {
                return b(jVar, Bitmap.Config.ARGB_8888, rect, colorSpace);
            }
            throw e4;
        }
    }

    public abstract int d(int i3, int i4, BitmapFactory.Options options);

    private static final class a implements InterfaceC0313h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final a f2307a = new a();

        private a() {
        }

        @Override // b0.InterfaceC0313h
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public void a(Bitmap bitmap) {
        }
    }
}
