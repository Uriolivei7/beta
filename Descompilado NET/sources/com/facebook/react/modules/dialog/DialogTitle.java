package com.facebook.react.modules.dialog;

import D2.h;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public final class DialogTitle extends TextView {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DialogTitle(Context context, AttributeSet attributeSet, int i3, int i4) {
        super(context, attributeSet, i3, i4);
        h.f(context, "context");
        h.f(attributeSet, "attrs");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i3, int i4) {
        int lineCount;
        super.onMeasure(i3, i4);
        Layout layout = getLayout();
        if (layout == null || (lineCount = layout.getLineCount()) <= 0 || layout.getEllipsisCount(lineCount - 1) <= 0) {
            return;
        }
        setSingleLine(false);
        setMaxLines(2);
        super.onMeasure(i3, i4);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DialogTitle(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        h.f(context, "context");
        h.f(attributeSet, "attrs");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DialogTitle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        h.f(context, "context");
        h.f(attributeSet, "attrs");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DialogTitle(Context context) {
        super(context);
        h.f(context, "context");
    }
}
