package androidx.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertController;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class b extends r implements DialogInterface {

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    final AlertController f3162g;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final AlertController.b f3163a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f3164b;

        public a(Context context) {
            this(context, b.n(context, 0));
        }

        public b a() {
            b bVar = new b(this.f3163a.f3122a, this.f3164b);
            this.f3163a.a(bVar.f3162g);
            bVar.setCancelable(this.f3163a.f3139r);
            if (this.f3163a.f3139r) {
                bVar.setCanceledOnTouchOutside(true);
            }
            bVar.setOnCancelListener(this.f3163a.f3140s);
            bVar.setOnDismissListener(this.f3163a.f3141t);
            DialogInterface.OnKeyListener onKeyListener = this.f3163a.f3142u;
            if (onKeyListener != null) {
                bVar.setOnKeyListener(onKeyListener);
            }
            return bVar;
        }

        public Context b() {
            return this.f3163a.f3122a;
        }

        public a c(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f3163a;
            bVar.f3144w = listAdapter;
            bVar.f3145x = onClickListener;
            return this;
        }

        public a d(View view) {
            this.f3163a.f3128g = view;
            return this;
        }

        public a e(Drawable drawable) {
            this.f3163a.f3125d = drawable;
            return this;
        }

        public a f(CharSequence[] charSequenceArr, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f3163a;
            bVar.f3143v = charSequenceArr;
            bVar.f3145x = onClickListener;
            return this;
        }

        public a g(CharSequence charSequence) {
            this.f3163a.f3129h = charSequence;
            return this;
        }

        public a h(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f3163a;
            bVar.f3133l = charSequence;
            bVar.f3135n = onClickListener;
            return this;
        }

        public a i(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f3163a;
            bVar.f3136o = charSequence;
            bVar.f3138q = onClickListener;
            return this;
        }

        public a j(DialogInterface.OnKeyListener onKeyListener) {
            this.f3163a.f3142u = onKeyListener;
            return this;
        }

        public a k(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f3163a;
            bVar.f3130i = charSequence;
            bVar.f3132k = onClickListener;
            return this;
        }

        public a l(ListAdapter listAdapter, int i3, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f3163a;
            bVar.f3144w = listAdapter;
            bVar.f3145x = onClickListener;
            bVar.f3115I = i3;
            bVar.f3114H = true;
            return this;
        }

        public a m(CharSequence charSequence) {
            this.f3163a.f3127f = charSequence;
            return this;
        }

        public a(Context context, int i3) {
            this.f3163a = new AlertController.b(new ContextThemeWrapper(context, b.n(context, i3)));
            this.f3164b = i3;
        }
    }

    protected b(Context context, int i3) {
        super(context, n(context, i3));
        this.f3162g = new AlertController(getContext(), this, getWindow());
    }

    static int n(Context context, int i3) {
        if (((i3 >>> 24) & 255) >= 1) {
            return i3;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(AbstractC0487a.f8688p, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView m() {
        return this.f3162g.d();
    }

    @Override // androidx.appcompat.app.r, androidx.activity.i, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f3162g.e();
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i3, KeyEvent keyEvent) {
        if (this.f3162g.f(i3, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i3, keyEvent);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i3, KeyEvent keyEvent) {
        if (this.f3162g.g(i3, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i3, keyEvent);
    }

    @Override // androidx.appcompat.app.r, android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.f3162g.p(charSequence);
    }
}
