package q2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;

/* JADX INFO: loaded from: classes.dex */
public class p implements InterfaceC0653a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f10415b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f10416c;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public InterfaceC0653a f10418e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Context f10419f;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final RenderNode f10414a = o.a("BlurViewNode");

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private float f10417d = 1.0f;

    @Override // q2.InterfaceC0653a
    public Bitmap.Config a() {
        return Bitmap.Config.ARGB_8888;
    }

    @Override // q2.InterfaceC0653a
    public boolean b() {
        return true;
    }

    @Override // q2.InterfaceC0653a
    public float c() {
        return 6.0f;
    }

    @Override // q2.InterfaceC0653a
    public void d(Canvas canvas, Bitmap bitmap) {
        if (canvas.isHardwareAccelerated()) {
            canvas.drawRenderNode(this.f10414a);
            return;
        }
        if (this.f10418e == null) {
            this.f10418e = new q(this.f10419f);
        }
        this.f10418e.e(bitmap, this.f10417d);
        this.f10418e.d(canvas, bitmap);
    }

    @Override // q2.InterfaceC0653a
    public void destroy() {
        this.f10414a.discardDisplayList();
        InterfaceC0653a interfaceC0653a = this.f10418e;
        if (interfaceC0653a != null) {
            interfaceC0653a.destroy();
        }
    }

    @Override // q2.InterfaceC0653a
    public Bitmap e(Bitmap bitmap, float f3) {
        this.f10417d = f3;
        if (bitmap.getHeight() != this.f10415b || bitmap.getWidth() != this.f10416c) {
            this.f10415b = bitmap.getHeight();
            int width = bitmap.getWidth();
            this.f10416c = width;
            this.f10414a.setPosition(0, 0, width, this.f10415b);
        }
        this.f10414a.beginRecording().drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        this.f10414a.endRecording();
        this.f10414a.setRenderEffect(RenderEffect.createBlurEffect(f3, f3, Shader.TileMode.MIRROR));
        return bitmap;
    }

    void f(Context context) {
        this.f10419f = context;
    }
}
