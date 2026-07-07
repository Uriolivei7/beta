package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import b.AbstractC0303a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public abstract class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Random f3046a = new Random();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Map f3047b = new HashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final Map f3048c = new HashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Map f3049d = new HashMap();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    ArrayList f3050e = new ArrayList();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    final transient Map f3051f = new HashMap();

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    final Map f3052g = new HashMap();

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    final Bundle f3053h = new Bundle();

    class a extends c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f3054a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ AbstractC0303a f3055b;

        a(String str, AbstractC0303a abstractC0303a) {
            this.f3054a = str;
            this.f3055b = abstractC0303a;
        }

        @Override // androidx.activity.result.c
        public void a() {
            e.this.i(this.f3054a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final androidx.activity.result.b f3057a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final AbstractC0303a f3058b;

        b(androidx.activity.result.b bVar, AbstractC0303a abstractC0303a) {
            this.f3057a = bVar;
            this.f3058b = abstractC0303a;
        }
    }

    private void a(int i3, String str) {
        this.f3047b.put(Integer.valueOf(i3), str);
        this.f3048c.put(str, Integer.valueOf(i3));
    }

    private void c(String str, int i3, Intent intent, b bVar) {
        if (bVar == null || bVar.f3057a == null || !this.f3050e.contains(str)) {
            this.f3052g.remove(str);
            this.f3053h.putParcelable(str, new androidx.activity.result.a(i3, intent));
        } else {
            bVar.f3057a.a(bVar.f3058b.a(i3, intent));
            this.f3050e.remove(str);
        }
    }

    private int d() {
        int iNextInt = this.f3046a.nextInt(2147418112);
        while (true) {
            int i3 = iNextInt + 65536;
            if (!this.f3047b.containsKey(Integer.valueOf(i3))) {
                return i3;
            }
            iNextInt = this.f3046a.nextInt(2147418112);
        }
    }

    private void h(String str) {
        if (((Integer) this.f3048c.get(str)) != null) {
            return;
        }
        a(d(), str);
    }

    public final boolean b(int i3, int i4, Intent intent) {
        String str = (String) this.f3047b.get(Integer.valueOf(i3));
        if (str == null) {
            return false;
        }
        c(str, i4, intent, (b) this.f3051f.get(str));
        return true;
    }

    public final void e(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS");
        ArrayList<String> stringArrayList = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS");
        if (stringArrayList == null || integerArrayList == null) {
            return;
        }
        this.f3050e = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS");
        this.f3046a = (Random) bundle.getSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT");
        this.f3053h.putAll(bundle.getBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT"));
        for (int i3 = 0; i3 < stringArrayList.size(); i3++) {
            String str = stringArrayList.get(i3);
            if (this.f3048c.containsKey(str)) {
                Integer num = (Integer) this.f3048c.remove(str);
                if (!this.f3053h.containsKey(str)) {
                    this.f3047b.remove(num);
                }
            }
            a(integerArrayList.get(i3).intValue(), stringArrayList.get(i3));
        }
    }

    public final void f(Bundle bundle) {
        bundle.putIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS", new ArrayList<>(this.f3048c.values()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS", new ArrayList<>(this.f3048c.keySet()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS", new ArrayList<>(this.f3050e));
        bundle.putBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT", (Bundle) this.f3053h.clone());
        bundle.putSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT", this.f3046a);
    }

    public final c g(String str, AbstractC0303a abstractC0303a, androidx.activity.result.b bVar) {
        h(str);
        this.f3051f.put(str, new b(bVar, abstractC0303a));
        if (this.f3052g.containsKey(str)) {
            Object obj = this.f3052g.get(str);
            this.f3052g.remove(str);
            bVar.a(obj);
        }
        androidx.activity.result.a aVar = (androidx.activity.result.a) this.f3053h.getParcelable(str);
        if (aVar != null) {
            this.f3053h.remove(str);
            bVar.a(abstractC0303a.a(aVar.b(), aVar.a()));
        }
        return new a(str, abstractC0303a);
    }

    final void i(String str) {
        Integer num;
        if (!this.f3050e.contains(str) && (num = (Integer) this.f3048c.remove(str)) != null) {
            this.f3047b.remove(num);
        }
        this.f3051f.remove(str);
        if (this.f3052g.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.f3052g.get(str));
            this.f3052g.remove(str);
        }
        if (this.f3053h.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.f3053h.getParcelable(str));
            this.f3053h.remove(str);
        }
        d.a(this.f3049d.get(str));
    }
}
