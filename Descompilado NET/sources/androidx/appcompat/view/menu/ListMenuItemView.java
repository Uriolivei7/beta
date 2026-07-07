package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.view.menu.k;
import androidx.appcompat.widget.h0;
import d.AbstractC0487a;

/* JADX INFO: loaded from: classes.dex */
public class ListMenuItemView extends LinearLayout implements k.a, AbsListView.SelectionBoundsAdjuster {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private g f3446b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ImageView f3447c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private RadioButton f3448d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private TextView f3449e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private CheckBox f3450f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private TextView f3451g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private ImageView f3452h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private ImageView f3453i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private LinearLayout f3454j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private Drawable f3455k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f3456l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private Context f3457m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f3458n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private Drawable f3459o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private boolean f3460p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private LayoutInflater f3461q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private boolean f3462r;

    public ListMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8660E);
    }

    private void b(View view) {
        c(view, -1);
    }

    private void c(View view, int i3) {
        LinearLayout linearLayout = this.f3454j;
        if (linearLayout != null) {
            linearLayout.addView(view, i3);
        } else {
            addView(view, i3);
        }
    }

    private void d() {
        CheckBox checkBox = (CheckBox) getInflater().inflate(d.g.f8817h, (ViewGroup) this, false);
        this.f3450f = checkBox;
        b(checkBox);
    }

    private void f() {
        ImageView imageView = (ImageView) getInflater().inflate(d.g.f8818i, (ViewGroup) this, false);
        this.f3447c = imageView;
        c(imageView, 0);
    }

    private void g() {
        RadioButton radioButton = (RadioButton) getInflater().inflate(d.g.f8820k, (ViewGroup) this, false);
        this.f3448d = radioButton;
        b(radioButton);
    }

    private LayoutInflater getInflater() {
        if (this.f3461q == null) {
            this.f3461q = LayoutInflater.from(getContext());
        }
        return this.f3461q;
    }

    private void setSubMenuArrowVisible(boolean z3) {
        ImageView imageView = this.f3452h;
        if (imageView != null) {
            imageView.setVisibility(z3 ? 0 : 8);
        }
    }

    @Override // androidx.appcompat.view.menu.k.a
    public boolean a() {
        return false;
    }

    @Override // android.widget.AbsListView.SelectionBoundsAdjuster
    public void adjustListItemSelectionBounds(Rect rect) {
        ImageView imageView = this.f3453i;
        if (imageView == null || imageView.getVisibility() != 0) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f3453i.getLayoutParams();
        rect.top += this.f3453i.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    @Override // androidx.appcompat.view.menu.k.a
    public void e(g gVar, int i3) {
        this.f3446b = gVar;
        setVisibility(gVar.isVisible() ? 0 : 8);
        setTitle(gVar.i(this));
        setCheckable(gVar.isCheckable());
        h(gVar.A(), gVar.g());
        setIcon(gVar.getIcon());
        setEnabled(gVar.isEnabled());
        setSubMenuArrowVisible(gVar.hasSubMenu());
        setContentDescription(gVar.getContentDescription());
    }

    @Override // androidx.appcompat.view.menu.k.a
    public g getItemData() {
        return this.f3446b;
    }

    public void h(boolean z3, char c4) {
        int i3 = (z3 && this.f3446b.A()) ? 0 : 8;
        if (i3 == 0) {
            this.f3451g.setText(this.f3446b.h());
        }
        if (this.f3451g.getVisibility() != i3) {
            this.f3451g.setVisibility(i3);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackground(this.f3455k);
        TextView textView = (TextView) findViewById(d.f.f8780M);
        this.f3449e = textView;
        int i3 = this.f3456l;
        if (i3 != -1) {
            textView.setTextAppearance(this.f3457m, i3);
        }
        this.f3451g = (TextView) findViewById(d.f.f8773F);
        ImageView imageView = (ImageView) findViewById(d.f.f8776I);
        this.f3452h = imageView;
        if (imageView != null) {
            imageView.setImageDrawable(this.f3459o);
        }
        this.f3453i = (ImageView) findViewById(d.f.f8801r);
        this.f3454j = (LinearLayout) findViewById(d.f.f8795l);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i3, int i4) {
        if (this.f3447c != null && this.f3458n) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.f3447c.getLayoutParams();
            int i5 = layoutParams.height;
            if (i5 > 0 && layoutParams2.width <= 0) {
                layoutParams2.width = i5;
            }
        }
        super.onMeasure(i3, i4);
    }

    public void setCheckable(boolean z3) {
        CompoundButton compoundButton;
        View view;
        if (!z3 && this.f3448d == null && this.f3450f == null) {
            return;
        }
        if (this.f3446b.m()) {
            if (this.f3448d == null) {
                g();
            }
            compoundButton = this.f3448d;
            view = this.f3450f;
        } else {
            if (this.f3450f == null) {
                d();
            }
            compoundButton = this.f3450f;
            view = this.f3448d;
        }
        if (z3) {
            compoundButton.setChecked(this.f3446b.isChecked());
            if (compoundButton.getVisibility() != 0) {
                compoundButton.setVisibility(0);
            }
            if (view == null || view.getVisibility() == 8) {
                return;
            }
            view.setVisibility(8);
            return;
        }
        CheckBox checkBox = this.f3450f;
        if (checkBox != null) {
            checkBox.setVisibility(8);
        }
        RadioButton radioButton = this.f3448d;
        if (radioButton != null) {
            radioButton.setVisibility(8);
        }
    }

    public void setChecked(boolean z3) {
        CompoundButton compoundButton;
        if (this.f3446b.m()) {
            if (this.f3448d == null) {
                g();
            }
            compoundButton = this.f3448d;
        } else {
            if (this.f3450f == null) {
                d();
            }
            compoundButton = this.f3450f;
        }
        compoundButton.setChecked(z3);
    }

    public void setForceShowIcon(boolean z3) {
        this.f3462r = z3;
        this.f3458n = z3;
    }

    public void setGroupDividerEnabled(boolean z3) {
        ImageView imageView = this.f3453i;
        if (imageView != null) {
            imageView.setVisibility((this.f3460p || !z3) ? 8 : 0);
        }
    }

    public void setIcon(Drawable drawable) {
        boolean z3 = this.f3446b.z() || this.f3462r;
        if (z3 || this.f3458n) {
            ImageView imageView = this.f3447c;
            if (imageView == null && drawable == null && !this.f3458n) {
                return;
            }
            if (imageView == null) {
                f();
            }
            if (drawable == null && !this.f3458n) {
                this.f3447c.setVisibility(8);
                return;
            }
            ImageView imageView2 = this.f3447c;
            if (!z3) {
                drawable = null;
            }
            imageView2.setImageDrawable(drawable);
            if (this.f3447c.getVisibility() != 0) {
                this.f3447c.setVisibility(0);
            }
        }
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence == null) {
            if (this.f3449e.getVisibility() != 8) {
                this.f3449e.setVisibility(8);
            }
        } else {
            this.f3449e.setText(charSequence);
            if (this.f3449e.getVisibility() != 0) {
                this.f3449e.setVisibility(0);
            }
        }
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet);
        h0 h0VarU = h0.u(getContext(), attributeSet, d.j.f8926T1, i3, 0);
        this.f3455k = h0VarU.f(d.j.f8934V1);
        this.f3456l = h0VarU.m(d.j.f8930U1, -1);
        this.f3458n = h0VarU.a(d.j.f8938W1, false);
        this.f3457m = context;
        this.f3459o = h0VarU.f(d.j.f8942X1);
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(null, new int[]{R.attr.divider}, AbstractC0487a.f8657B, 0);
        this.f3460p = typedArrayObtainStyledAttributes.hasValue(0);
        h0VarU.w();
        typedArrayObtainStyledAttributes.recycle();
    }
}
