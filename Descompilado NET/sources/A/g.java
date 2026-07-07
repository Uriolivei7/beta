package A;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.emoji2.text.f;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
final class g implements TextWatcher {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final EditText f21b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final boolean f22c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private f.AbstractC0072f f23d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f24e = Integer.MAX_VALUE;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f25f = 0;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f26g = true;

    private static class a extends f.AbstractC0072f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Reference f27a;

        a(EditText editText) {
            this.f27a = new WeakReference(editText);
        }

        @Override // androidx.emoji2.text.f.AbstractC0072f
        public void b() {
            super.b();
            g.b((EditText) this.f27a.get(), 1);
        }
    }

    g(EditText editText, boolean z3) {
        this.f21b = editText;
        this.f22c = z3;
    }

    private f.AbstractC0072f a() {
        if (this.f23d == null) {
            this.f23d = new a(this.f21b);
        }
        return this.f23d;
    }

    static void b(EditText editText, int i3) {
        if (i3 == 1 && editText != null && editText.isAttachedToWindow()) {
            Editable editableText = editText.getEditableText();
            int selectionStart = Selection.getSelectionStart(editableText);
            int selectionEnd = Selection.getSelectionEnd(editableText);
            androidx.emoji2.text.f.c().p(editableText);
            d.b(editableText, selectionStart, selectionEnd);
        }
    }

    private boolean d() {
        return (this.f26g && (this.f22c || androidx.emoji2.text.f.i())) ? false : true;
    }

    public void c(boolean z3) {
        if (this.f26g != z3) {
            if (this.f23d != null) {
                androidx.emoji2.text.f.c().u(this.f23d);
            }
            this.f26g = z3;
            if (z3) {
                b(this.f21b, androidx.emoji2.text.f.c().e());
            }
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        if (this.f21b.isInEditMode() || d() || i4 > i5 || !(charSequence instanceof Spannable)) {
            return;
        }
        int iE = androidx.emoji2.text.f.c().e();
        if (iE != 0) {
            if (iE == 1) {
                androidx.emoji2.text.f.c().s((Spannable) charSequence, i3, i3 + i5, this.f24e, this.f25f);
                return;
            } else if (iE != 3) {
                return;
            }
        }
        androidx.emoji2.text.f.c().t(a());
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
    }
}
