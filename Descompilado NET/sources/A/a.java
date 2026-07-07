package A;

import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final b f0a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f1b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2c;

    /* JADX INFO: renamed from: A.a$a, reason: collision with other inner class name */
    private static class C0000a extends b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final EditText f3a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final g f4b;

        C0000a(EditText editText, boolean z3) {
            this.f3a = editText;
            g gVar = new g(editText, z3);
            this.f4b = gVar;
            editText.addTextChangedListener(gVar);
            editText.setEditableFactory(A.b.getInstance());
        }

        @Override // A.a.b
        KeyListener a(KeyListener keyListener) {
            if (keyListener instanceof e) {
                return keyListener;
            }
            if (keyListener == null) {
                return null;
            }
            return keyListener instanceof NumberKeyListener ? keyListener : new e(keyListener);
        }

        @Override // A.a.b
        InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
            return inputConnection instanceof c ? inputConnection : new c(this.f3a, inputConnection, editorInfo);
        }

        @Override // A.a.b
        void c(boolean z3) {
            this.f4b.c(z3);
        }
    }

    static class b {
        b() {
        }

        abstract KeyListener a(KeyListener keyListener);

        abstract InputConnection b(InputConnection inputConnection, EditorInfo editorInfo);

        abstract void c(boolean z3);
    }

    public a(EditText editText) {
        this(editText, true);
    }

    public KeyListener a(KeyListener keyListener) {
        return this.f0a.a(keyListener);
    }

    public InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
        if (inputConnection == null) {
            return null;
        }
        return this.f0a.b(inputConnection, editorInfo);
    }

    public void c(boolean z3) {
        this.f0a.c(z3);
    }

    public a(EditText editText, boolean z3) {
        this.f1b = Integer.MAX_VALUE;
        this.f2c = 0;
        q.g.h(editText, "editText cannot be null");
        this.f0a = new C0000a(editText, z3);
    }
}
