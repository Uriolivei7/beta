package com.facebook.react.views.image;

import android.graphics.Bitmap;
import b0.AbstractC0306a;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class e implements U0.d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final a f7678b = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final List f7679a;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final U0.d a(List list) {
            D2.h.f(list, "postprocessors");
            int size = list.size();
            DefaultConstructorMarker defaultConstructorMarker = null;
            if (size != 0) {
                return size != 1 ? new e(list, defaultConstructorMarker) : (U0.d) list.get(0);
            }
            return null;
        }

        private a() {
        }
    }

    public /* synthetic */ e(List list, DefaultConstructorMarker defaultConstructorMarker) {
        this(list);
    }

    @Override // U0.d
    public AbstractC0306a a(Bitmap bitmap, G0.b bVar) {
        Bitmap bitmap2;
        D2.h.f(bitmap, "sourceBitmap");
        D2.h.f(bVar, "bitmapFactory");
        AbstractC0306a abstractC0306aA = null;
        try {
            AbstractC0306a abstractC0306aClone = null;
            for (U0.d dVar : this.f7679a) {
                if (abstractC0306aClone == null || (bitmap2 = (Bitmap) abstractC0306aClone.P()) == null) {
                    bitmap2 = bitmap;
                }
                abstractC0306aA = dVar.a(bitmap2, bVar);
                AbstractC0306a.D(abstractC0306aClone);
                abstractC0306aClone = abstractC0306aA.clone();
            }
            if (abstractC0306aA != null) {
                AbstractC0306a abstractC0306aClone2 = abstractC0306aA.clone();
                D2.h.e(abstractC0306aClone2, "clone(...)");
                AbstractC0306a.D(abstractC0306aA);
                return abstractC0306aClone2;
            }
            throw new IllegalStateException(("MultiPostprocessor returned null bitmap - Number of Postprocessors: " + this.f7679a.size()).toString());
        } catch (Throwable th) {
            AbstractC0306a.D(null);
            throw th;
        }
    }

    @Override // U0.d
    public R.d b() {
        List list = this.f7679a;
        ArrayList arrayList = new ArrayList(AbstractC0717n.q(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(((U0.d) it.next()).b());
        }
        return new R.f(arrayList);
    }

    @Override // U0.d
    public String getName() {
        return "MultiPostProcessor (" + AbstractC0717n.S(this.f7679a, ",", null, null, 0, null, null, 62, null) + ")";
    }

    private e(List list) {
        this.f7679a = new LinkedList(list);
    }
}
