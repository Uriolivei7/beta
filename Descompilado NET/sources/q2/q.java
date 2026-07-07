package q2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/* JADX INFO: loaded from: classes.dex */
public class q implements InterfaceC0653a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final RenderScript f10421b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ScriptIntrinsicBlur f10422c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Allocation f10423d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Paint f10420a = new Paint(2);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f10424e = -1;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f10425f = -1;

    public q(Context context) {
        RenderScript renderScriptCreate = RenderScript.create(context);
        this.f10421b = renderScriptCreate;
        this.f10422c = ScriptIntrinsicBlur.create(renderScriptCreate, Element.U8_4(renderScriptCreate));
    }

    private boolean f(Bitmap bitmap) {
        return bitmap.getHeight() == this.f10425f && bitmap.getWidth() == this.f10424e;
    }

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
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.f10420a);
    }

    @Override // q2.InterfaceC0653a
    public final void destroy() {
        this.f10422c.destroy();
        this.f10421b.destroy();
        Allocation allocation = this.f10423d;
        if (allocation != null) {
            allocation.destroy();
        }
    }

    @Override // q2.InterfaceC0653a
    public Bitmap e(Bitmap bitmap, float f3) {
        Allocation allocationCreateFromBitmap = Allocation.createFromBitmap(this.f10421b, bitmap);
        if (!f(bitmap)) {
            Allocation allocation = this.f10423d;
            if (allocation != null) {
                allocation.destroy();
            }
            this.f10423d = Allocation.createTyped(this.f10421b, allocationCreateFromBitmap.getType());
            this.f10424e = bitmap.getWidth();
            this.f10425f = bitmap.getHeight();
        }
        this.f10422c.setRadius(f3);
        this.f10422c.setInput(allocationCreateFromBitmap);
        this.f10422c.forEach(this.f10423d);
        this.f10423d.copyTo(bitmap);
        allocationCreateFromBitmap.destroy();
        return bitmap;
    }
}
