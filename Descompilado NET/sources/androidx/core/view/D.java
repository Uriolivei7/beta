package androidx.core.view;

import android.view.MotionEvent;

/* JADX INFO: loaded from: classes.dex */
public abstract class D {
    public static boolean a(MotionEvent motionEvent, int i3) {
        return (motionEvent.getSource() & i3) == i3;
    }
}
