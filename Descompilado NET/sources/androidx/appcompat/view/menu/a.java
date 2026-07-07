package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.view.menu.k;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements j {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected Context f3463b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected Context f3464c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected e f3465d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    protected LayoutInflater f3466e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    protected LayoutInflater f3467f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private j.a f3468g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private int f3469h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f3470i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    protected k f3471j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f3472k;

    public a(Context context, int i3, int i4) {
        this.f3463b = context;
        this.f3466e = LayoutInflater.from(context);
        this.f3469h = i3;
        this.f3470i = i4;
    }

    protected void a(View view, int i3) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        ((ViewGroup) this.f3471j).addView(view, i3);
    }

    public abstract void b(g gVar, k.a aVar);

    @Override // androidx.appcompat.view.menu.j
    public void c(e eVar, boolean z3) {
        j.a aVar = this.f3468g;
        if (aVar != null) {
            aVar.c(eVar, z3);
        }
    }

    @Override // androidx.appcompat.view.menu.j
    public void d(Context context, e eVar) {
        this.f3464c = context;
        this.f3467f = LayoutInflater.from(context);
        this.f3465d = eVar;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // androidx.appcompat.view.menu.j
    public boolean e(m mVar) {
        j.a aVar = this.f3468g;
        e eVar = mVar;
        if (aVar == null) {
            return false;
        }
        if (mVar == null) {
            eVar = this.f3465d;
        }
        return aVar.d(eVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.appcompat.view.menu.j
    public void f(boolean z3) {
        ViewGroup viewGroup = (ViewGroup) this.f3471j;
        if (viewGroup == null) {
            return;
        }
        e eVar = this.f3465d;
        int i3 = 0;
        if (eVar != null) {
            eVar.r();
            ArrayList arrayListE = this.f3465d.E();
            int size = arrayListE.size();
            int i4 = 0;
            for (int i5 = 0; i5 < size; i5++) {
                g gVar = (g) arrayListE.get(i5);
                if (q(i4, gVar)) {
                    View childAt = viewGroup.getChildAt(i4);
                    g itemData = childAt instanceof k.a ? ((k.a) childAt).getItemData() : null;
                    View viewN = n(gVar, childAt, viewGroup);
                    if (gVar != itemData) {
                        viewN.setPressed(false);
                        viewN.jumpDrawablesToCurrentState();
                    }
                    if (viewN != childAt) {
                        a(viewN, i4);
                    }
                    i4++;
                }
            }
            i3 = i4;
        }
        while (i3 < viewGroup.getChildCount()) {
            if (!l(viewGroup, i3)) {
                i3++;
            }
        }
    }

    public k.a g(ViewGroup viewGroup) {
        return (k.a) this.f3466e.inflate(this.f3470i, viewGroup, false);
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean h() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean i(e eVar, g gVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.j
    public boolean j(e eVar, g gVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.j
    public void k(j.a aVar) {
        this.f3468g = aVar;
    }

    protected boolean l(ViewGroup viewGroup, int i3) {
        viewGroup.removeViewAt(i3);
        return true;
    }

    public j.a m() {
        return this.f3468g;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public View n(g gVar, View view, ViewGroup viewGroup) {
        k.a aVarG = view instanceof k.a ? (k.a) view : g(viewGroup);
        b(gVar, aVarG);
        return (View) aVarG;
    }

    public k o(ViewGroup viewGroup) {
        if (this.f3471j == null) {
            k kVar = (k) this.f3466e.inflate(this.f3469h, viewGroup, false);
            this.f3471j = kVar;
            kVar.b(this.f3465d);
            f(true);
        }
        return this.f3471j;
    }

    public void p(int i3) {
        this.f3472k = i3;
    }

    public boolean q(int i3, g gVar) {
        return true;
    }
}
