package com.facebook.react.views.textinput;

import android.text.SpannableStringBuilder;
import android.widget.EditText;

/* JADX INFO: loaded from: classes.dex */
public final class r {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final SpannableStringBuilder f8151a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final float f8152b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final int f8153c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f8154d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final int f8155e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f8156f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final CharSequence f8157g;

    public r(EditText editText) {
        this.f8151a = new SpannableStringBuilder(editText.getText());
        this.f8152b = editText.getTextSize();
        this.f8155e = editText.getInputType();
        this.f8157g = editText.getHint();
        this.f8153c = editText.getMinLines();
        this.f8154d = editText.getMaxLines();
        this.f8156f = editText.getBreakStrategy();
    }

    public void a(EditText editText) {
        editText.setText(this.f8151a);
        editText.setTextSize(0, this.f8152b);
        editText.setMinLines(this.f8153c);
        editText.setMaxLines(this.f8154d);
        editText.setInputType(this.f8155e);
        editText.setHint(this.f8157g);
        editText.setBreakStrategy(this.f8156f);
    }
}
