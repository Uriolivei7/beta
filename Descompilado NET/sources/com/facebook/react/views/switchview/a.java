package com.facebook.react.views.switchview;

import D2.h;
import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import androidx.appcompat.widget.c0;

/* JADX INFO: loaded from: classes.dex */
public final class a extends c0 {

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    private boolean f7909V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private Integer f7910W;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    private Integer f7911a0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(Context context) {
        super(context);
        h.f(context, "context");
        this.f7909V = true;
    }

    private final ColorStateList r(int i3) {
        return new ColorStateList(new int[][]{new int[]{R.attr.state_pressed}}, new int[]{i3});
    }

    public final void s(Drawable drawable, Integer num) {
        h.f(drawable, "drawable");
        if (num == null) {
            drawable.clearColorFilter();
        } else {
            drawable.setColorFilter(new PorterDuffColorFilter(num.intValue(), PorterDuff.Mode.MULTIPLY));
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i3) {
        setBackground(new RippleDrawable(r(i3), new ColorDrawable(i3), null));
    }

    @Override // androidx.appcompat.widget.c0, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z3) {
        if (!this.f7909V || isChecked() == z3) {
            super.setChecked(isChecked());
            return;
        }
        this.f7909V = false;
        super.setChecked(z3);
        setTrackColor(z3);
    }

    public final void setOn(boolean z3) {
        if (isChecked() != z3) {
            super.setChecked(z3);
            setTrackColor(z3);
        }
        this.f7909V = true;
    }

    public final void setThumbColor(Integer num) {
        Drawable thumbDrawable = super.getThumbDrawable();
        h.e(thumbDrawable, "getThumbDrawable(...)");
        s(thumbDrawable, num);
        if (num == null || !(super.getBackground() instanceof RippleDrawable)) {
            return;
        }
        ColorStateList colorStateListR = r(num.intValue());
        Drawable background = super.getBackground();
        h.d(background, "null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
        ((RippleDrawable) background).setColor(colorStateListR);
    }

    public final void setTrackColor(Integer num) {
        Drawable trackDrawable = super.getTrackDrawable();
        h.e(trackDrawable, "getTrackDrawable(...)");
        s(trackDrawable, num);
    }

    public final void setTrackColorForFalse(Integer num) {
        if (h.b(num, this.f7910W)) {
            return;
        }
        this.f7910W = num;
        if (isChecked()) {
            return;
        }
        setTrackColor(this.f7910W);
    }

    public final void setTrackColorForTrue(Integer num) {
        if (h.b(num, this.f7911a0)) {
            return;
        }
        this.f7911a0 = num;
        if (isChecked()) {
            setTrackColor(this.f7911a0);
        }
    }

    private final void setTrackColor(boolean z3) {
        Integer num = this.f7911a0;
        if (num == null && this.f7910W == null) {
            return;
        }
        if (!z3) {
            num = this.f7910W;
        }
        setTrackColor(num);
    }
}
