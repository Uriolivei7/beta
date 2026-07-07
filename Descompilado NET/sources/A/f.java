package A;

import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.SparseArray;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final b f16a;

    private static class a extends b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final TextView f17a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final d f18b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f19c = true;

        a(TextView textView) {
            this.f17a = textView;
            this.f18b = new d(textView);
        }

        private InputFilter[] f(InputFilter[] inputFilterArr) {
            int length = inputFilterArr.length;
            for (InputFilter inputFilter : inputFilterArr) {
                if (inputFilter == this.f18b) {
                    return inputFilterArr;
                }
            }
            InputFilter[] inputFilterArr2 = new InputFilter[inputFilterArr.length + 1];
            System.arraycopy(inputFilterArr, 0, inputFilterArr2, 0, length);
            inputFilterArr2[length] = this.f18b;
            return inputFilterArr2;
        }

        private SparseArray g(InputFilter[] inputFilterArr) {
            SparseArray sparseArray = new SparseArray(1);
            for (int i3 = 0; i3 < inputFilterArr.length; i3++) {
                InputFilter inputFilter = inputFilterArr[i3];
                if (inputFilter instanceof d) {
                    sparseArray.put(i3, inputFilter);
                }
            }
            return sparseArray;
        }

        private InputFilter[] h(InputFilter[] inputFilterArr) {
            SparseArray sparseArrayG = g(inputFilterArr);
            if (sparseArrayG.size() == 0) {
                return inputFilterArr;
            }
            int length = inputFilterArr.length;
            InputFilter[] inputFilterArr2 = new InputFilter[inputFilterArr.length - sparseArrayG.size()];
            int i3 = 0;
            for (int i4 = 0; i4 < length; i4++) {
                if (sparseArrayG.indexOfKey(i4) < 0) {
                    inputFilterArr2[i3] = inputFilterArr[i4];
                    i3++;
                }
            }
            return inputFilterArr2;
        }

        private TransformationMethod j(TransformationMethod transformationMethod) {
            return transformationMethod instanceof h ? ((h) transformationMethod).a() : transformationMethod;
        }

        private void k() {
            this.f17a.setFilters(a(this.f17a.getFilters()));
        }

        private TransformationMethod m(TransformationMethod transformationMethod) {
            return ((transformationMethod instanceof h) || (transformationMethod instanceof PasswordTransformationMethod)) ? transformationMethod : new h(transformationMethod);
        }

        @Override // A.f.b
        InputFilter[] a(InputFilter[] inputFilterArr) {
            return !this.f19c ? h(inputFilterArr) : f(inputFilterArr);
        }

        @Override // A.f.b
        public boolean b() {
            return this.f19c;
        }

        @Override // A.f.b
        void c(boolean z3) {
            if (z3) {
                l();
            }
        }

        @Override // A.f.b
        void d(boolean z3) {
            this.f19c = z3;
            l();
            k();
        }

        @Override // A.f.b
        TransformationMethod e(TransformationMethod transformationMethod) {
            return this.f19c ? m(transformationMethod) : j(transformationMethod);
        }

        void i(boolean z3) {
            this.f19c = z3;
        }

        void l() {
            this.f17a.setTransformationMethod(e(this.f17a.getTransformationMethod()));
        }
    }

    static class b {
        b() {
        }

        abstract InputFilter[] a(InputFilter[] inputFilterArr);

        public abstract boolean b();

        abstract void c(boolean z3);

        abstract void d(boolean z3);

        abstract TransformationMethod e(TransformationMethod transformationMethod);
    }

    private static class c extends b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final a f20a;

        c(TextView textView) {
            this.f20a = new a(textView);
        }

        private boolean f() {
            return !androidx.emoji2.text.f.i();
        }

        @Override // A.f.b
        InputFilter[] a(InputFilter[] inputFilterArr) {
            return f() ? inputFilterArr : this.f20a.a(inputFilterArr);
        }

        @Override // A.f.b
        public boolean b() {
            return this.f20a.b();
        }

        @Override // A.f.b
        void c(boolean z3) {
            if (f()) {
                return;
            }
            this.f20a.c(z3);
        }

        @Override // A.f.b
        void d(boolean z3) {
            if (f()) {
                this.f20a.i(z3);
            } else {
                this.f20a.d(z3);
            }
        }

        @Override // A.f.b
        TransformationMethod e(TransformationMethod transformationMethod) {
            return f() ? transformationMethod : this.f20a.e(transformationMethod);
        }
    }

    public f(TextView textView) {
        this(textView, true);
    }

    public InputFilter[] a(InputFilter[] inputFilterArr) {
        return this.f16a.a(inputFilterArr);
    }

    public boolean b() {
        return this.f16a.b();
    }

    public void c(boolean z3) {
        this.f16a.c(z3);
    }

    public void d(boolean z3) {
        this.f16a.d(z3);
    }

    public TransformationMethod e(TransformationMethod transformationMethod) {
        return this.f16a.e(transformationMethod);
    }

    public f(TextView textView, boolean z3) {
        q.g.h(textView, "textView cannot be null");
        if (z3) {
            this.f16a = new a(textView);
        } else {
            this.f16a = new c(textView);
        }
    }
}
