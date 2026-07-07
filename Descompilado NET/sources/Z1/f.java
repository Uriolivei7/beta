package Z1;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.H0;
import com.facebook.react.uimanager.events.EventDispatcher;

/* JADX INFO: loaded from: classes.dex */
public final class f extends ClickableSpan implements i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2825a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2826b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2827c;

    public f(int i3) {
        this.f2825a = i3;
    }

    public final void a(int i3) {
        this.f2827c = i3;
    }

    public final void b(boolean z3) {
        this.f2826b = z3;
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        D2.h.f(view, "view");
        Context context = view.getContext();
        D2.h.d(context, "null cannot be cast to non-null type com.facebook.react.bridge.ReactContext");
        ReactContext reactContext = (ReactContext) context;
        EventDispatcher eventDispatcherC = H0.c(reactContext, this.f2825a);
        if (eventDispatcherC != null) {
            eventDispatcherC.b(new com.facebook.react.views.view.j(H0.e(reactContext), this.f2825a));
        }
    }

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        D2.h.f(textPaint, "ds");
        if (this.f2826b) {
            textPaint.bgColor = this.f2827c;
        }
    }
}
