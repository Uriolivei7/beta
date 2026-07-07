package androidx.appcompat.app;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.widget.T;
import androidx.core.view.Z;
import androidx.core.widget.NestedScrollView;
import d.AbstractC0487a;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
class AlertController {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    NestedScrollView f3059A;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private Drawable f3061C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private ImageView f3062D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private TextView f3063E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private TextView f3064F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private View f3065G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    ListAdapter f3066H;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private int f3068J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private int f3069K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    int f3070L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    int f3071M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    int f3072N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    int f3073O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    private boolean f3074P;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    Handler f3076R;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f3078a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final r f3079b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Window f3080c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f3081d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private CharSequence f3082e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private CharSequence f3083f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    ListView f3084g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private View f3085h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int f3086i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private int f3087j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private int f3088k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private int f3089l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private int f3090m;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    Button f3092o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private CharSequence f3093p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    Message f3094q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Drawable f3095r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    Button f3096s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private CharSequence f3097t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    Message f3098u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private Drawable f3099v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    Button f3100w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private CharSequence f3101x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    Message f3102y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private Drawable f3103z;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f3091n = false;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private int f3060B = 0;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    int f3067I = -1;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private int f3075Q = 0;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private final View.OnClickListener f3077S = new a();

    public static class RecycleListView extends ListView {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f3104b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f3105c;

        public RecycleListView(Context context) {
            this(context, null);
        }

        public void a(boolean z3, boolean z4) {
            if (z4 && z3) {
                return;
            }
            setPadding(getPaddingLeft(), z3 ? getPaddingTop() : this.f3104b, getPaddingRight(), z4 ? getPaddingBottom() : this.f3105c);
        }

        public RecycleListView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, d.j.f8965c2);
            this.f3105c = typedArrayObtainStyledAttributes.getDimensionPixelOffset(d.j.f8970d2, -1);
            this.f3104b = typedArrayObtainStyledAttributes.getDimensionPixelOffset(d.j.f8975e2, -1);
        }
    }

    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Message message;
            Message message2;
            Message message3;
            AlertController alertController = AlertController.this;
            Message messageObtain = (view != alertController.f3092o || (message3 = alertController.f3094q) == null) ? (view != alertController.f3096s || (message2 = alertController.f3098u) == null) ? (view != alertController.f3100w || (message = alertController.f3102y) == null) ? null : Message.obtain(message) : Message.obtain(message2) : Message.obtain(message3);
            if (messageObtain != null) {
                messageObtain.sendToTarget();
            }
            AlertController alertController2 = AlertController.this;
            alertController2.f3076R.obtainMessage(1, alertController2.f3079b).sendToTarget();
        }
    }

    public static class b {

        /* JADX INFO: renamed from: A, reason: collision with root package name */
        public int f3107A;

        /* JADX INFO: renamed from: B, reason: collision with root package name */
        public int f3108B;

        /* JADX INFO: renamed from: C, reason: collision with root package name */
        public int f3109C;

        /* JADX INFO: renamed from: D, reason: collision with root package name */
        public int f3110D;

        /* JADX INFO: renamed from: F, reason: collision with root package name */
        public boolean[] f3112F;

        /* JADX INFO: renamed from: G, reason: collision with root package name */
        public boolean f3113G;

        /* JADX INFO: renamed from: H, reason: collision with root package name */
        public boolean f3114H;

        /* JADX INFO: renamed from: J, reason: collision with root package name */
        public DialogInterface.OnMultiChoiceClickListener f3116J;

        /* JADX INFO: renamed from: K, reason: collision with root package name */
        public Cursor f3117K;

        /* JADX INFO: renamed from: L, reason: collision with root package name */
        public String f3118L;

        /* JADX INFO: renamed from: M, reason: collision with root package name */
        public String f3119M;

        /* JADX INFO: renamed from: N, reason: collision with root package name */
        public AdapterView.OnItemSelectedListener f3120N;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Context f3122a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final LayoutInflater f3123b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public Drawable f3125d;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public CharSequence f3127f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public View f3128g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public CharSequence f3129h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public CharSequence f3130i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public Drawable f3131j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public DialogInterface.OnClickListener f3132k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        public CharSequence f3133l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public Drawable f3134m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public DialogInterface.OnClickListener f3135n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        public CharSequence f3136o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        public Drawable f3137p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        public DialogInterface.OnClickListener f3138q;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        public DialogInterface.OnCancelListener f3140s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        public DialogInterface.OnDismissListener f3141t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        public DialogInterface.OnKeyListener f3142u;

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        public CharSequence[] f3143v;

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        public ListAdapter f3144w;

        /* JADX INFO: renamed from: x, reason: collision with root package name */
        public DialogInterface.OnClickListener f3145x;

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        public int f3146y;

        /* JADX INFO: renamed from: z, reason: collision with root package name */
        public View f3147z;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f3124c = 0;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public int f3126e = 0;

        /* JADX INFO: renamed from: E, reason: collision with root package name */
        public boolean f3111E = false;

        /* JADX INFO: renamed from: I, reason: collision with root package name */
        public int f3115I = -1;

        /* JADX INFO: renamed from: O, reason: collision with root package name */
        public boolean f3121O = true;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        public boolean f3139r = true;

        class a extends ArrayAdapter {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ RecycleListView f3148b;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(Context context, int i3, int i4, CharSequence[] charSequenceArr, RecycleListView recycleListView) {
                super(context, i3, i4, charSequenceArr);
                this.f3148b = recycleListView;
            }

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public View getView(int i3, View view, ViewGroup viewGroup) {
                View view2 = super.getView(i3, view, viewGroup);
                boolean[] zArr = b.this.f3112F;
                if (zArr != null && zArr[i3]) {
                    this.f3148b.setItemChecked(i3, true);
                }
                return view2;
            }
        }

        /* JADX INFO: renamed from: androidx.appcompat.app.AlertController$b$b, reason: collision with other inner class name */
        class C0050b extends CursorAdapter {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            private final int f3150b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            private final int f3151c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            final /* synthetic */ RecycleListView f3152d;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ AlertController f3153e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0050b(Context context, Cursor cursor, boolean z3, RecycleListView recycleListView, AlertController alertController) {
                super(context, cursor, z3);
                this.f3152d = recycleListView;
                this.f3153e = alertController;
                Cursor cursor2 = getCursor();
                this.f3150b = cursor2.getColumnIndexOrThrow(b.this.f3118L);
                this.f3151c = cursor2.getColumnIndexOrThrow(b.this.f3119M);
            }

            @Override // android.widget.CursorAdapter
            public void bindView(View view, Context context, Cursor cursor) {
                ((CheckedTextView) view.findViewById(R.id.text1)).setText(cursor.getString(this.f3150b));
                this.f3152d.setItemChecked(cursor.getPosition(), cursor.getInt(this.f3151c) == 1);
            }

            @Override // android.widget.CursorAdapter
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return b.this.f3123b.inflate(this.f3153e.f3071M, viewGroup, false);
            }
        }

        class c implements AdapterView.OnItemClickListener {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ AlertController f3155b;

            c(AlertController alertController) {
                this.f3155b = alertController;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView adapterView, View view, int i3, long j3) {
                b.this.f3145x.onClick(this.f3155b.f3079b, i3);
                if (b.this.f3114H) {
                    return;
                }
                this.f3155b.f3079b.dismiss();
            }
        }

        class d implements AdapterView.OnItemClickListener {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ RecycleListView f3157b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            final /* synthetic */ AlertController f3158c;

            d(RecycleListView recycleListView, AlertController alertController) {
                this.f3157b = recycleListView;
                this.f3158c = alertController;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView adapterView, View view, int i3, long j3) {
                boolean[] zArr = b.this.f3112F;
                if (zArr != null) {
                    zArr[i3] = this.f3157b.isItemChecked(i3);
                }
                b.this.f3116J.onClick(this.f3158c.f3079b, i3, this.f3157b.isItemChecked(i3));
            }
        }

        public b(Context context) {
            this.f3122a = context;
            this.f3123b = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        private void b(AlertController alertController) {
            ListAdapter dVar;
            RecycleListView recycleListView = (RecycleListView) this.f3123b.inflate(alertController.f3070L, (ViewGroup) null);
            if (this.f3113G) {
                dVar = this.f3117K == null ? new a(this.f3122a, alertController.f3071M, R.id.text1, this.f3143v, recycleListView) : new C0050b(this.f3122a, this.f3117K, false, recycleListView, alertController);
            } else {
                int i3 = this.f3114H ? alertController.f3072N : alertController.f3073O;
                if (this.f3117K != null) {
                    dVar = new SimpleCursorAdapter(this.f3122a, i3, this.f3117K, new String[]{this.f3118L}, new int[]{R.id.text1});
                } else {
                    dVar = this.f3144w;
                    if (dVar == null) {
                        dVar = new d(this.f3122a, i3, R.id.text1, this.f3143v);
                    }
                }
            }
            alertController.f3066H = dVar;
            alertController.f3067I = this.f3115I;
            if (this.f3145x != null) {
                recycleListView.setOnItemClickListener(new c(alertController));
            } else if (this.f3116J != null) {
                recycleListView.setOnItemClickListener(new d(recycleListView, alertController));
            }
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.f3120N;
            if (onItemSelectedListener != null) {
                recycleListView.setOnItemSelectedListener(onItemSelectedListener);
            }
            if (this.f3114H) {
                recycleListView.setChoiceMode(1);
            } else if (this.f3113G) {
                recycleListView.setChoiceMode(2);
            }
            alertController.f3084g = recycleListView;
        }

        public void a(AlertController alertController) {
            View view = this.f3128g;
            if (view != null) {
                alertController.k(view);
            } else {
                CharSequence charSequence = this.f3127f;
                if (charSequence != null) {
                    alertController.p(charSequence);
                }
                Drawable drawable = this.f3125d;
                if (drawable != null) {
                    alertController.m(drawable);
                }
                int i3 = this.f3124c;
                if (i3 != 0) {
                    alertController.l(i3);
                }
                int i4 = this.f3126e;
                if (i4 != 0) {
                    alertController.l(alertController.c(i4));
                }
            }
            CharSequence charSequence2 = this.f3129h;
            if (charSequence2 != null) {
                alertController.n(charSequence2);
            }
            CharSequence charSequence3 = this.f3130i;
            if (charSequence3 != null || this.f3131j != null) {
                alertController.j(-1, charSequence3, this.f3132k, null, this.f3131j);
            }
            CharSequence charSequence4 = this.f3133l;
            if (charSequence4 != null || this.f3134m != null) {
                alertController.j(-2, charSequence4, this.f3135n, null, this.f3134m);
            }
            CharSequence charSequence5 = this.f3136o;
            if (charSequence5 != null || this.f3137p != null) {
                alertController.j(-3, charSequence5, this.f3138q, null, this.f3137p);
            }
            if (this.f3143v != null || this.f3117K != null || this.f3144w != null) {
                b(alertController);
            }
            View view2 = this.f3147z;
            if (view2 != null) {
                if (this.f3111E) {
                    alertController.s(view2, this.f3107A, this.f3108B, this.f3109C, this.f3110D);
                    return;
                } else {
                    alertController.r(view2);
                    return;
                }
            }
            int i5 = this.f3146y;
            if (i5 != 0) {
                alertController.q(i5);
            }
        }
    }

    private static final class c extends Handler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private WeakReference f3160a;

        public c(DialogInterface dialogInterface) {
            this.f3160a = new WeakReference(dialogInterface);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i3 = message.what;
            if (i3 == -3 || i3 == -2 || i3 == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick((DialogInterface) this.f3160a.get(), message.what);
            } else {
                if (i3 != 1) {
                    return;
                }
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    private static class d extends ArrayAdapter {
        public d(Context context, int i3, int i4, CharSequence[] charSequenceArr) {
            super(context, i3, i4, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i3) {
            return i3;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }

    public AlertController(Context context, r rVar, Window window) {
        this.f3078a = context;
        this.f3079b = rVar;
        this.f3080c = window;
        this.f3076R = new c(rVar);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(null, d.j.f8868F, AbstractC0487a.f8687o, 0);
        this.f3068J = typedArrayObtainStyledAttributes.getResourceId(d.j.f8872G, 0);
        this.f3069K = typedArrayObtainStyledAttributes.getResourceId(d.j.f8880I, 0);
        this.f3070L = typedArrayObtainStyledAttributes.getResourceId(d.j.f8888K, 0);
        this.f3071M = typedArrayObtainStyledAttributes.getResourceId(d.j.f8892L, 0);
        this.f3072N = typedArrayObtainStyledAttributes.getResourceId(d.j.f8900N, 0);
        this.f3073O = typedArrayObtainStyledAttributes.getResourceId(d.j.f8884J, 0);
        this.f3074P = typedArrayObtainStyledAttributes.getBoolean(d.j.f8896M, true);
        this.f3081d = typedArrayObtainStyledAttributes.getDimensionPixelSize(d.j.f8876H, 0);
        typedArrayObtainStyledAttributes.recycle();
        rVar.l(1);
    }

    static boolean a(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (a(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    private void b(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
    }

    private ViewGroup h(View view, View view2) {
        if (view == null) {
            if (view2 instanceof ViewStub) {
                view2 = ((ViewStub) view2).inflate();
            }
            return (ViewGroup) view2;
        }
        if (view2 != null) {
            ViewParent parent = view2.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view2);
            }
        }
        if (view instanceof ViewStub) {
            view = ((ViewStub) view).inflate();
        }
        return (ViewGroup) view;
    }

    private int i() {
        int i3 = this.f3069K;
        return i3 == 0 ? this.f3068J : this.f3075Q == 1 ? i3 : this.f3068J;
    }

    private void o(ViewGroup viewGroup, View view, int i3, int i4) {
        View viewFindViewById = this.f3080c.findViewById(d.f.f8805v);
        View viewFindViewById2 = this.f3080c.findViewById(d.f.f8804u);
        Z.k0(view, i3, i4);
        if (viewFindViewById != null) {
            viewGroup.removeView(viewFindViewById);
        }
        if (viewFindViewById2 != null) {
            viewGroup.removeView(viewFindViewById2);
        }
    }

    private void t(ViewGroup viewGroup) {
        int i3;
        Button button = (Button) viewGroup.findViewById(R.id.button1);
        this.f3092o = button;
        button.setOnClickListener(this.f3077S);
        if (TextUtils.isEmpty(this.f3093p) && this.f3095r == null) {
            this.f3092o.setVisibility(8);
            i3 = 0;
        } else {
            this.f3092o.setText(this.f3093p);
            Drawable drawable = this.f3095r;
            if (drawable != null) {
                int i4 = this.f3081d;
                drawable.setBounds(0, 0, i4, i4);
                this.f3092o.setCompoundDrawables(this.f3095r, null, null, null);
            }
            this.f3092o.setVisibility(0);
            i3 = 1;
        }
        Button button2 = (Button) viewGroup.findViewById(R.id.button2);
        this.f3096s = button2;
        button2.setOnClickListener(this.f3077S);
        if (TextUtils.isEmpty(this.f3097t) && this.f3099v == null) {
            this.f3096s.setVisibility(8);
        } else {
            this.f3096s.setText(this.f3097t);
            Drawable drawable2 = this.f3099v;
            if (drawable2 != null) {
                int i5 = this.f3081d;
                drawable2.setBounds(0, 0, i5, i5);
                this.f3096s.setCompoundDrawables(this.f3099v, null, null, null);
            }
            this.f3096s.setVisibility(0);
            i3 |= 2;
        }
        Button button3 = (Button) viewGroup.findViewById(R.id.button3);
        this.f3100w = button3;
        button3.setOnClickListener(this.f3077S);
        if (TextUtils.isEmpty(this.f3101x) && this.f3103z == null) {
            this.f3100w.setVisibility(8);
        } else {
            this.f3100w.setText(this.f3101x);
            Drawable drawable3 = this.f3103z;
            if (drawable3 != null) {
                int i6 = this.f3081d;
                drawable3.setBounds(0, 0, i6, i6);
                this.f3100w.setCompoundDrawables(this.f3103z, null, null, null);
            }
            this.f3100w.setVisibility(0);
            i3 |= 4;
        }
        if (y(this.f3078a)) {
            if (i3 == 1) {
                b(this.f3092o);
            } else if (i3 == 2) {
                b(this.f3096s);
            } else if (i3 == 4) {
                b(this.f3100w);
            }
        }
        if (i3 != 0) {
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void u(ViewGroup viewGroup) {
        NestedScrollView nestedScrollView = (NestedScrollView) this.f3080c.findViewById(d.f.f8806w);
        this.f3059A = nestedScrollView;
        nestedScrollView.setFocusable(false);
        this.f3059A.setNestedScrollingEnabled(false);
        TextView textView = (TextView) viewGroup.findViewById(R.id.message);
        this.f3064F = textView;
        if (textView == null) {
            return;
        }
        CharSequence charSequence = this.f3083f;
        if (charSequence != null) {
            textView.setText(charSequence);
            return;
        }
        textView.setVisibility(8);
        this.f3059A.removeView(this.f3064F);
        if (this.f3084g == null) {
            viewGroup.setVisibility(8);
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) this.f3059A.getParent();
        int iIndexOfChild = viewGroup2.indexOfChild(this.f3059A);
        viewGroup2.removeViewAt(iIndexOfChild);
        viewGroup2.addView(this.f3084g, iIndexOfChild, new ViewGroup.LayoutParams(-1, -1));
    }

    private void v(ViewGroup viewGroup) {
        View viewInflate = this.f3085h;
        if (viewInflate == null) {
            viewInflate = this.f3086i != 0 ? LayoutInflater.from(this.f3078a).inflate(this.f3086i, viewGroup, false) : null;
        }
        boolean z3 = viewInflate != null;
        if (!z3 || !a(viewInflate)) {
            this.f3080c.setFlags(131072, 131072);
        }
        if (!z3) {
            viewGroup.setVisibility(8);
            return;
        }
        FrameLayout frameLayout = (FrameLayout) this.f3080c.findViewById(d.f.f8797n);
        frameLayout.addView(viewInflate, new ViewGroup.LayoutParams(-1, -1));
        if (this.f3091n) {
            frameLayout.setPadding(this.f3087j, this.f3088k, this.f3089l, this.f3090m);
        }
        if (this.f3084g != null) {
            ((LinearLayout.LayoutParams) ((T.a) viewGroup.getLayoutParams())).weight = 0.0f;
        }
    }

    private void w(ViewGroup viewGroup) {
        if (this.f3065G != null) {
            viewGroup.addView(this.f3065G, 0, new ViewGroup.LayoutParams(-1, -2));
            this.f3080c.findViewById(d.f.f8782O).setVisibility(8);
            return;
        }
        this.f3062D = (ImageView) this.f3080c.findViewById(R.id.icon);
        if (TextUtils.isEmpty(this.f3082e) || !this.f3074P) {
            this.f3080c.findViewById(d.f.f8782O).setVisibility(8);
            this.f3062D.setVisibility(8);
            viewGroup.setVisibility(8);
            return;
        }
        TextView textView = (TextView) this.f3080c.findViewById(d.f.f8793j);
        this.f3063E = textView;
        textView.setText(this.f3082e);
        int i3 = this.f3060B;
        if (i3 != 0) {
            this.f3062D.setImageResource(i3);
            return;
        }
        Drawable drawable = this.f3061C;
        if (drawable != null) {
            this.f3062D.setImageDrawable(drawable);
        } else {
            this.f3063E.setPadding(this.f3062D.getPaddingLeft(), this.f3062D.getPaddingTop(), this.f3062D.getPaddingRight(), this.f3062D.getPaddingBottom());
            this.f3062D.setVisibility(8);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void x() {
        View viewFindViewById;
        ListAdapter listAdapter;
        View viewFindViewById2;
        View viewFindViewById3 = this.f3080c.findViewById(d.f.f8803t);
        View viewFindViewById4 = viewFindViewById3.findViewById(d.f.f8783P);
        View viewFindViewById5 = viewFindViewById3.findViewById(d.f.f8796m);
        View viewFindViewById6 = viewFindViewById3.findViewById(d.f.f8794k);
        ViewGroup viewGroup = (ViewGroup) viewFindViewById3.findViewById(d.f.f8798o);
        v(viewGroup);
        View viewFindViewById7 = viewGroup.findViewById(d.f.f8783P);
        View viewFindViewById8 = viewGroup.findViewById(d.f.f8796m);
        View viewFindViewById9 = viewGroup.findViewById(d.f.f8794k);
        ViewGroup viewGroupH = h(viewFindViewById7, viewFindViewById4);
        ViewGroup viewGroupH2 = h(viewFindViewById8, viewFindViewById5);
        ViewGroup viewGroupH3 = h(viewFindViewById9, viewFindViewById6);
        u(viewGroupH2);
        t(viewGroupH3);
        w(viewGroupH);
        boolean z3 = viewGroup.getVisibility() != 8;
        boolean z4 = (viewGroupH == null || viewGroupH.getVisibility() == 8) ? 0 : 1;
        boolean z5 = (viewGroupH3 == null || viewGroupH3.getVisibility() == 8) ? false : true;
        if (!z5 && viewGroupH2 != null && (viewFindViewById2 = viewGroupH2.findViewById(d.f.f8778K)) != null) {
            viewFindViewById2.setVisibility(0);
        }
        if (z4 != 0) {
            NestedScrollView nestedScrollView = this.f3059A;
            if (nestedScrollView != null) {
                nestedScrollView.setClipToPadding(true);
            }
            View viewFindViewById10 = (this.f3083f == null && this.f3084g == null) ? null : viewGroupH.findViewById(d.f.f8781N);
            if (viewFindViewById10 != null) {
                viewFindViewById10.setVisibility(0);
            }
        } else if (viewGroupH2 != null && (viewFindViewById = viewGroupH2.findViewById(d.f.f8779L)) != null) {
            viewFindViewById.setVisibility(0);
        }
        ListView listView = this.f3084g;
        if (listView instanceof RecycleListView) {
            ((RecycleListView) listView).a(z4, z5);
        }
        if (!z3) {
            View view = this.f3084g;
            if (view == null) {
                view = this.f3059A;
            }
            if (view != null) {
                o(viewGroupH2, view, z4 | (z5 ? 2 : 0), 3);
            }
        }
        ListView listView2 = this.f3084g;
        if (listView2 == null || (listAdapter = this.f3066H) == null) {
            return;
        }
        listView2.setAdapter(listAdapter);
        int i3 = this.f3067I;
        if (i3 > -1) {
            listView2.setItemChecked(i3, true);
            listView2.setSelection(i3);
        }
    }

    private static boolean y(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(AbstractC0487a.f8686n, typedValue, true);
        return typedValue.data != 0;
    }

    public int c(int i3) {
        TypedValue typedValue = new TypedValue();
        this.f3078a.getTheme().resolveAttribute(i3, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView d() {
        return this.f3084g;
    }

    public void e() {
        this.f3079b.setContentView(i());
        x();
    }

    public boolean f(int i3, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.f3059A;
        return nestedScrollView != null && nestedScrollView.t(keyEvent);
    }

    public boolean g(int i3, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.f3059A;
        return nestedScrollView != null && nestedScrollView.t(keyEvent);
    }

    public void j(int i3, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message, Drawable drawable) {
        if (message == null && onClickListener != null) {
            message = this.f3076R.obtainMessage(i3, onClickListener);
        }
        if (i3 == -3) {
            this.f3101x = charSequence;
            this.f3102y = message;
            this.f3103z = drawable;
        } else if (i3 == -2) {
            this.f3097t = charSequence;
            this.f3098u = message;
            this.f3099v = drawable;
        } else {
            if (i3 != -1) {
                throw new IllegalArgumentException("Button does not exist");
            }
            this.f3093p = charSequence;
            this.f3094q = message;
            this.f3095r = drawable;
        }
    }

    public void k(View view) {
        this.f3065G = view;
    }

    public void l(int i3) {
        this.f3061C = null;
        this.f3060B = i3;
        ImageView imageView = this.f3062D;
        if (imageView != null) {
            if (i3 == 0) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                this.f3062D.setImageResource(this.f3060B);
            }
        }
    }

    public void m(Drawable drawable) {
        this.f3061C = drawable;
        this.f3060B = 0;
        ImageView imageView = this.f3062D;
        if (imageView != null) {
            if (drawable == null) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                this.f3062D.setImageDrawable(drawable);
            }
        }
    }

    public void n(CharSequence charSequence) {
        this.f3083f = charSequence;
        TextView textView = this.f3064F;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void p(CharSequence charSequence) {
        this.f3082e = charSequence;
        TextView textView = this.f3063E;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void q(int i3) {
        this.f3085h = null;
        this.f3086i = i3;
        this.f3091n = false;
    }

    public void r(View view) {
        this.f3085h = view;
        this.f3086i = 0;
        this.f3091n = false;
    }

    public void s(View view, int i3, int i4, int i5, int i6) {
        this.f3085h = view;
        this.f3086i = 0;
        this.f3091n = true;
        this.f3087j = i3;
        this.f3088k = i4;
        this.f3089l = i5;
        this.f3090m = i6;
    }
}
