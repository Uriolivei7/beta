package androidx.fragment.app;

import android.R;
import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

/* JADX INFO: renamed from: androidx.fragment.app.k, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
abstract class AbstractC0289k {
    private static int a(Fragment fragment, boolean z3, boolean z4) {
        return z4 ? z3 ? fragment.G() : fragment.H() : z3 ? fragment.q() : fragment.v();
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0071 A[Catch: RuntimeException -> 0x0077, TRY_LEAVE, TryCatch #0 {RuntimeException -> 0x0077, blocks: (B:32:0x006b, B:34:0x0071), top: B:45:0x006b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static androidx.fragment.app.AbstractC0289k.a b(android.content.Context r4, androidx.fragment.app.Fragment r5, boolean r6, boolean r7) {
        /*
            int r0 = r5.C()
            int r7 = a(r5, r6, r7)
            r1 = 0
            r5.r1(r1, r1, r1, r1)
            android.view.ViewGroup r1 = r5.f4919I
            r2 = 0
            if (r1 == 0) goto L20
            int r3 = B.b.f44c
            java.lang.Object r1 = r1.getTag(r3)
            if (r1 == 0) goto L20
            android.view.ViewGroup r1 = r5.f4919I
            int r3 = B.b.f44c
            r1.setTag(r3, r2)
        L20:
            android.view.ViewGroup r1 = r5.f4919I
            if (r1 == 0) goto L2b
            android.animation.LayoutTransition r1 = r1.getLayoutTransition()
            if (r1 == 0) goto L2b
            return r2
        L2b:
            android.view.animation.Animation r1 = r5.l0(r0, r6, r7)
            if (r1 == 0) goto L37
            androidx.fragment.app.k$a r4 = new androidx.fragment.app.k$a
            r4.<init>(r1)
            return r4
        L37:
            android.animation.Animator r5 = r5.m0(r0, r6, r7)
            if (r5 == 0) goto L43
            androidx.fragment.app.k$a r4 = new androidx.fragment.app.k$a
            r4.<init>(r5)
            return r4
        L43:
            if (r7 != 0) goto L4b
            if (r0 == 0) goto L4b
            int r7 = d(r4, r0, r6)
        L4b:
            if (r7 == 0) goto L87
            android.content.res.Resources r5 = r4.getResources()
            java.lang.String r5 = r5.getResourceTypeName(r7)
            java.lang.String r6 = "anim"
            boolean r5 = r6.equals(r5)
            if (r5 == 0) goto L6b
            android.view.animation.Animation r6 = android.view.animation.AnimationUtils.loadAnimation(r4, r7)     // Catch: android.content.res.Resources.NotFoundException -> L69 java.lang.RuntimeException -> L6b
            if (r6 == 0) goto L87
            androidx.fragment.app.k$a r0 = new androidx.fragment.app.k$a     // Catch: android.content.res.Resources.NotFoundException -> L69 java.lang.RuntimeException -> L6b
            r0.<init>(r6)     // Catch: android.content.res.Resources.NotFoundException -> L69 java.lang.RuntimeException -> L6b
            return r0
        L69:
            r4 = move-exception
            throw r4
        L6b:
            android.animation.Animator r6 = android.animation.AnimatorInflater.loadAnimator(r4, r7)     // Catch: java.lang.RuntimeException -> L77
            if (r6 == 0) goto L87
            androidx.fragment.app.k$a r0 = new androidx.fragment.app.k$a     // Catch: java.lang.RuntimeException -> L77
            r0.<init>(r6)     // Catch: java.lang.RuntimeException -> L77
            return r0
        L77:
            r6 = move-exception
            if (r5 != 0) goto L86
            android.view.animation.Animation r4 = android.view.animation.AnimationUtils.loadAnimation(r4, r7)
            if (r4 == 0) goto L87
            androidx.fragment.app.k$a r5 = new androidx.fragment.app.k$a
            r5.<init>(r4)
            return r5
        L86:
            throw r6
        L87:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.AbstractC0289k.b(android.content.Context, androidx.fragment.app.Fragment, boolean, boolean):androidx.fragment.app.k$a");
    }

    private static int c(Context context, int i3) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(R.style.Animation.Activity, new int[]{i3});
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(0, -1);
        typedArrayObtainStyledAttributes.recycle();
        return resourceId;
    }

    private static int d(Context context, int i3, boolean z3) {
        if (i3 == 4097) {
            return z3 ? B.a.f40e : B.a.f41f;
        }
        if (i3 == 8194) {
            return z3 ? B.a.f36a : B.a.f37b;
        }
        if (i3 == 8197) {
            return z3 ? c(context, R.attr.activityCloseEnterAnimation) : c(context, R.attr.activityCloseExitAnimation);
        }
        if (i3 == 4099) {
            return z3 ? B.a.f38c : B.a.f39d;
        }
        if (i3 != 4100) {
            return -1;
        }
        return z3 ? c(context, R.attr.activityOpenEnterAnimation) : c(context, R.attr.activityOpenExitAnimation);
    }

    /* JADX INFO: renamed from: androidx.fragment.app.k$a */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Animation f5154a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final Animator f5155b;

        a(Animation animation) {
            this.f5154a = animation;
            this.f5155b = null;
            if (animation == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }

        a(Animator animator) {
            this.f5154a = null;
            this.f5155b = animator;
            if (animator == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
    }

    /* JADX INFO: renamed from: androidx.fragment.app.k$b */
    static class b extends AnimationSet implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ViewGroup f5156b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final View f5157c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private boolean f5158d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private boolean f5159e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f5160f;

        b(Animation animation, ViewGroup viewGroup, View view) {
            super(false);
            this.f5160f = true;
            this.f5156b = viewGroup;
            this.f5157c = view;
            addAnimation(animation);
            viewGroup.post(this);
        }

        @Override // android.view.animation.AnimationSet, android.view.animation.Animation
        public boolean getTransformation(long j3, Transformation transformation) {
            this.f5160f = true;
            if (this.f5158d) {
                return !this.f5159e;
            }
            if (!super.getTransformation(j3, transformation)) {
                this.f5158d = true;
                androidx.core.view.L.a(this.f5156b, this);
            }
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f5158d || !this.f5160f) {
                this.f5156b.endViewTransition(this.f5157c);
                this.f5159e = true;
            } else {
                this.f5160f = false;
                this.f5156b.post(this);
            }
        }

        @Override // android.view.animation.Animation
        public boolean getTransformation(long j3, Transformation transformation, float f3) {
            this.f5160f = true;
            if (this.f5158d) {
                return !this.f5159e;
            }
            if (!super.getTransformation(j3, transformation, f3)) {
                this.f5158d = true;
                androidx.core.view.L.a(this.f5156b, this);
            }
            return true;
        }
    }
}
