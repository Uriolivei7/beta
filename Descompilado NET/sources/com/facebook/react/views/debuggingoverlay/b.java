package com.facebook.react.views.debuggingoverlay;

import D2.h;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class b extends View {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Paint f7649b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final HashMap f7650c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final HashMap f7651d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Paint f7652e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private List f7653f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(Context context) {
        super(context);
        h.f(context, "context");
        Paint paint = new Paint();
        this.f7649b = paint;
        this.f7650c = new HashMap();
        this.f7651d = new HashMap();
        Paint paint2 = new Paint();
        this.f7652e = paint2;
        this.f7653f = new ArrayList();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6.0f);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(-859248897);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(b bVar, int i3) {
        bVar.f7650c.remove(Integer.valueOf(i3));
        bVar.f7651d.remove(Integer.valueOf(i3));
        bVar.invalidate();
    }

    public final void b() {
        this.f7653f.clear();
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        h.f(canvas, "canvas");
        super.onDraw(canvas);
        for (Object obj : this.f7650c.values()) {
            h.e(obj, "next(...)");
            c cVar = (c) obj;
            this.f7649b.setColor(cVar.a());
            canvas.drawRect(cVar.c(), this.f7649b);
            final int iB = cVar.b();
            Runnable runnable = new Runnable() { // from class: com.facebook.react.views.debuggingoverlay.a
                @Override // java.lang.Runnable
                public final void run() {
                    b.c(this.f7647b, iB);
                }
            };
            if (!this.f7651d.containsKey(Integer.valueOf(iB))) {
                this.f7651d.put(Integer.valueOf(iB), runnable);
                UiThreadUtil.runOnUiThread(runnable, 2000L);
            }
        }
        Iterator it = this.f7653f.iterator();
        while (it.hasNext()) {
            canvas.drawRect((RectF) it.next(), this.f7652e);
        }
    }

    public final void setHighlightedElementsRectangles(List<RectF> list) {
        h.f(list, "elementsRectangles");
        this.f7653f = list;
        invalidate();
    }

    public final void setTraceUpdates(List<c> list) {
        h.f(list, "traceUpdates");
        for (c cVar : list) {
            int iB = cVar.b();
            if (this.f7651d.containsKey(Integer.valueOf(iB))) {
                UiThreadUtil.removeOnUiThread((Runnable) this.f7651d.get(Integer.valueOf(iB)));
                this.f7651d.remove(Integer.valueOf(iB));
            }
            this.f7650c.put(Integer.valueOf(iB), cVar);
        }
        invalidate();
    }
}
