package androidx.appcompat.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import d.AbstractC0487a;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import v.AbstractC0752a;
import w.AbstractC0758a;

/* JADX INFO: loaded from: classes.dex */
public class SearchView extends T implements androidx.appcompat.view.c {

    /* JADX INFO: renamed from: o0, reason: collision with root package name */
    static final o f3842o0;

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private Rect f3843A;

    /* JADX INFO: renamed from: B, reason: collision with root package name */
    private Rect f3844B;

    /* JADX INFO: renamed from: C, reason: collision with root package name */
    private int[] f3845C;

    /* JADX INFO: renamed from: D, reason: collision with root package name */
    private int[] f3846D;

    /* JADX INFO: renamed from: E, reason: collision with root package name */
    private final ImageView f3847E;

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private final Drawable f3848F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private final int f3849G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private final int f3850H;

    /* JADX INFO: renamed from: I, reason: collision with root package name */
    private final Intent f3851I;

    /* JADX INFO: renamed from: J, reason: collision with root package name */
    private final Intent f3852J;

    /* JADX INFO: renamed from: K, reason: collision with root package name */
    private final CharSequence f3853K;

    /* JADX INFO: renamed from: L, reason: collision with root package name */
    View.OnFocusChangeListener f3854L;

    /* JADX INFO: renamed from: M, reason: collision with root package name */
    private View.OnClickListener f3855M;

    /* JADX INFO: renamed from: N, reason: collision with root package name */
    private boolean f3856N;

    /* JADX INFO: renamed from: O, reason: collision with root package name */
    private boolean f3857O;

    /* JADX INFO: renamed from: P, reason: collision with root package name */
    AbstractC0752a f3858P;

    /* JADX INFO: renamed from: Q, reason: collision with root package name */
    private boolean f3859Q;

    /* JADX INFO: renamed from: R, reason: collision with root package name */
    private CharSequence f3860R;

    /* JADX INFO: renamed from: S, reason: collision with root package name */
    private boolean f3861S;

    /* JADX INFO: renamed from: T, reason: collision with root package name */
    private boolean f3862T;

    /* JADX INFO: renamed from: U, reason: collision with root package name */
    private int f3863U;

    /* JADX INFO: renamed from: V, reason: collision with root package name */
    private boolean f3864V;

    /* JADX INFO: renamed from: W, reason: collision with root package name */
    private CharSequence f3865W;

    /* JADX INFO: renamed from: a0, reason: collision with root package name */
    private CharSequence f3866a0;

    /* JADX INFO: renamed from: b0, reason: collision with root package name */
    private boolean f3867b0;

    /* JADX INFO: renamed from: c0, reason: collision with root package name */
    private int f3868c0;

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    SearchableInfo f3869d0;

    /* JADX INFO: renamed from: e0, reason: collision with root package name */
    private Bundle f3870e0;

    /* JADX INFO: renamed from: f0, reason: collision with root package name */
    private final Runnable f3871f0;

    /* JADX INFO: renamed from: g0, reason: collision with root package name */
    private Runnable f3872g0;

    /* JADX INFO: renamed from: h0, reason: collision with root package name */
    private final WeakHashMap f3873h0;

    /* JADX INFO: renamed from: i0, reason: collision with root package name */
    private final View.OnClickListener f3874i0;

    /* JADX INFO: renamed from: j0, reason: collision with root package name */
    View.OnKeyListener f3875j0;

    /* JADX INFO: renamed from: k0, reason: collision with root package name */
    private final TextView.OnEditorActionListener f3876k0;

    /* JADX INFO: renamed from: l0, reason: collision with root package name */
    private final AdapterView.OnItemClickListener f3877l0;

    /* JADX INFO: renamed from: m0, reason: collision with root package name */
    private final AdapterView.OnItemSelectedListener f3878m0;

    /* JADX INFO: renamed from: n0, reason: collision with root package name */
    private TextWatcher f3879n0;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    final SearchAutoComplete f3880q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final View f3881r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final View f3882s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private final View f3883t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    final ImageView f3884u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    final ImageView f3885v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    final ImageView f3886w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    final ImageView f3887x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final View f3888y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private q f3889z;

    public static class SearchAutoComplete extends C0215d {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private int f3890f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private SearchView f3891g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private boolean f3892h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final Runnable f3893i;

        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                SearchAutoComplete.this.d();
            }
        }

        public SearchAutoComplete(Context context) {
            this(context, null);
        }

        private int getSearchViewTextMinWidthDp() {
            Configuration configuration = getResources().getConfiguration();
            int i3 = configuration.screenWidthDp;
            int i4 = configuration.screenHeightDp;
            if (i3 >= 960 && i4 >= 720 && configuration.orientation == 2) {
                return 256;
            }
            if (i3 < 600) {
                return (i3 < 640 || i4 < 480) ? 160 : 192;
            }
            return 192;
        }

        void b() {
            if (Build.VERSION.SDK_INT < 29) {
                SearchView.f3842o0.c(this);
                return;
            }
            k.b(this, 1);
            if (enoughToFilter()) {
                showDropDown();
            }
        }

        boolean c() {
            return TextUtils.getTrimmedLength(getText()) == 0;
        }

        void d() {
            if (this.f3892h) {
                ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this, 0);
                this.f3892h = false;
            }
        }

        @Override // android.widget.AutoCompleteTextView
        public boolean enoughToFilter() {
            return this.f3890f <= 0 || super.enoughToFilter();
        }

        @Override // androidx.appcompat.widget.C0215d, android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (this.f3892h) {
                removeCallbacks(this.f3893i);
                post(this.f3893i);
            }
            return inputConnectionOnCreateInputConnection;
        }

        @Override // android.view.View
        protected void onFinishInflate() {
            super.onFinishInflate();
            setMinWidth((int) TypedValue.applyDimension(1, getSearchViewTextMinWidthDp(), getResources().getDisplayMetrics()));
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        protected void onFocusChanged(boolean z3, int i3, Rect rect) {
            super.onFocusChanged(z3, i3, rect);
            this.f3891g.X();
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        public boolean onKeyPreIme(int i3, KeyEvent keyEvent) {
            if (i3 == 4) {
                if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState keyDispatcherState = getKeyDispatcherState();
                    if (keyDispatcherState != null) {
                        keyDispatcherState.startTracking(keyEvent, this);
                    }
                    return true;
                }
                if (keyEvent.getAction() == 1) {
                    KeyEvent.DispatcherState keyDispatcherState2 = getKeyDispatcherState();
                    if (keyDispatcherState2 != null) {
                        keyDispatcherState2.handleUpEvent(keyEvent);
                    }
                    if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                        this.f3891g.clearFocus();
                        setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(i3, keyEvent);
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        public void onWindowFocusChanged(boolean z3) {
            super.onWindowFocusChanged(z3);
            if (z3 && this.f3891g.hasFocus() && getVisibility() == 0) {
                this.f3892h = true;
                if (SearchView.K(getContext())) {
                    b();
                }
            }
        }

        @Override // android.widget.AutoCompleteTextView
        public void performCompletion() {
        }

        @Override // android.widget.AutoCompleteTextView
        protected void replaceText(CharSequence charSequence) {
        }

        void setImeVisibility(boolean z3) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
            if (!z3) {
                this.f3892h = false;
                removeCallbacks(this.f3893i);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else {
                if (!inputMethodManager.isActive(this)) {
                    this.f3892h = true;
                    return;
                }
                this.f3892h = false;
                removeCallbacks(this.f3893i);
                inputMethodManager.showSoftInput(this, 0);
            }
        }

        void setSearchView(SearchView searchView) {
            this.f3891g = searchView;
        }

        @Override // android.widget.AutoCompleteTextView
        public void setThreshold(int i3) {
            super.setThreshold(i3);
            this.f3890f = i3;
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, AbstractC0487a.f8689q);
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet, int i3) {
            super(context, attributeSet, i3);
            this.f3893i = new a();
            this.f3890f = getThreshold();
        }
    }

    class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            SearchView.this.W(charSequence);
        }
    }

    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SearchView.this.d0();
        }
    }

    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AbstractC0752a abstractC0752a = SearchView.this.f3858P;
            if (abstractC0752a instanceof b0) {
                abstractC0752a.a(null);
            }
        }
    }

    class d implements View.OnFocusChangeListener {
        d() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z3) {
            SearchView searchView = SearchView.this;
            View.OnFocusChangeListener onFocusChangeListener = searchView.f3854L;
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChange(searchView, z3);
            }
        }
    }

    class e implements View.OnLayoutChangeListener {
        e() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            SearchView.this.z();
        }
    }

    class f implements View.OnClickListener {
        f() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SearchView searchView = SearchView.this;
            if (view == searchView.f3884u) {
                searchView.T();
                return;
            }
            if (view == searchView.f3886w) {
                searchView.P();
                return;
            }
            if (view == searchView.f3885v) {
                searchView.U();
            } else if (view == searchView.f3887x) {
                searchView.Y();
            } else if (view == searchView.f3880q) {
                searchView.F();
            }
        }
    }

    class g implements View.OnKeyListener {
        g() {
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i3, KeyEvent keyEvent) {
            SearchView searchView = SearchView.this;
            if (searchView.f3869d0 == null) {
                return false;
            }
            if (searchView.f3880q.isPopupShowing() && SearchView.this.f3880q.getListSelection() != -1) {
                return SearchView.this.V(view, i3, keyEvent);
            }
            if (SearchView.this.f3880q.c() || !keyEvent.hasNoModifiers() || keyEvent.getAction() != 1 || i3 != 66) {
                return false;
            }
            view.cancelLongPress();
            SearchView searchView2 = SearchView.this;
            searchView2.N(0, null, searchView2.f3880q.getText().toString());
            return true;
        }
    }

    class h implements TextView.OnEditorActionListener {
        h() {
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int i3, KeyEvent keyEvent) {
            SearchView.this.U();
            return true;
        }
    }

    class i implements AdapterView.OnItemClickListener {
        i() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView adapterView, View view, int i3, long j3) {
            SearchView.this.Q(i3, 0, null);
        }
    }

    class j implements AdapterView.OnItemSelectedListener {
        j() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView adapterView, View view, int i3, long j3) {
            SearchView.this.R(i3);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView adapterView) {
        }
    }

    static class k {
        static void a(AutoCompleteTextView autoCompleteTextView) {
            autoCompleteTextView.refreshAutoCompleteResults();
        }

        static void b(SearchAutoComplete searchAutoComplete, int i3) {
            searchAutoComplete.setInputMethodMode(i3);
        }
    }

    public interface l {
    }

    public interface m {
    }

    public interface n {
    }

    private static class o {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Method f3905a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Method f3906b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private Method f3907c;

        o() {
            this.f3905a = null;
            this.f3906b = null;
            this.f3907c = null;
            d();
            try {
                Method declaredMethod = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                this.f3905a = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException unused) {
            }
            try {
                Method declaredMethod2 = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                this.f3906b = declaredMethod2;
                declaredMethod2.setAccessible(true);
            } catch (NoSuchMethodException unused2) {
            }
            try {
                Method method = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE);
                this.f3907c = method;
                method.setAccessible(true);
            } catch (NoSuchMethodException unused3) {
            }
        }

        private static void d() {
            if (Build.VERSION.SDK_INT >= 29) {
                throw new UnsupportedClassVersionError("This function can only be used for API Level < 29.");
            }
        }

        void a(AutoCompleteTextView autoCompleteTextView) {
            d();
            Method method = this.f3906b;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, new Object[0]);
                } catch (Exception unused) {
                }
            }
        }

        void b(AutoCompleteTextView autoCompleteTextView) {
            d();
            Method method = this.f3905a;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, new Object[0]);
                } catch (Exception unused) {
                }
            }
        }

        void c(AutoCompleteTextView autoCompleteTextView) {
            d();
            Method method = this.f3907c;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, Boolean.TRUE);
                } catch (Exception unused) {
                }
            }
        }
    }

    static class p extends AbstractC0758a {
        public static final Parcelable.Creator<p> CREATOR = new a();

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        boolean f3908c;

        class a implements Parcelable.ClassLoaderCreator {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public p createFromParcel(Parcel parcel) {
                return new p(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
            public p createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new p(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
            public p[] newArray(int i3) {
                return new p[i3];
            }
        }

        p(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.f3908c + "}";
        }

        @Override // w.AbstractC0758a, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i3) {
            super.writeToParcel(parcel, i3);
            parcel.writeValue(Boolean.valueOf(this.f3908c));
        }

        public p(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f3908c = ((Boolean) parcel.readValue(null)).booleanValue();
        }
    }

    private static class q extends TouchDelegate {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final View f3909a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Rect f3910b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Rect f3911c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final Rect f3912d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int f3913e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f3914f;

        public q(Rect rect, Rect rect2, View view) {
            super(rect, view);
            this.f3913e = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            this.f3910b = new Rect();
            this.f3912d = new Rect();
            this.f3911c = new Rect();
            a(rect, rect2);
            this.f3909a = view;
        }

        public void a(Rect rect, Rect rect2) {
            this.f3910b.set(rect);
            this.f3912d.set(rect);
            Rect rect3 = this.f3912d;
            int i3 = this.f3913e;
            rect3.inset(-i3, -i3);
            this.f3911c.set(rect2);
        }

        @Override // android.view.TouchDelegate
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z3;
            boolean z4;
            int x3 = (int) motionEvent.getX();
            int y3 = (int) motionEvent.getY();
            int action = motionEvent.getAction();
            boolean z5 = true;
            if (action != 0) {
                if (action == 1 || action == 2) {
                    z4 = this.f3914f;
                    if (z4 && !this.f3912d.contains(x3, y3)) {
                        z5 = z4;
                        z3 = false;
                    }
                } else {
                    if (action == 3) {
                        z4 = this.f3914f;
                        this.f3914f = false;
                    }
                    z3 = true;
                    z5 = false;
                }
                z5 = z4;
                z3 = true;
            } else if (this.f3910b.contains(x3, y3)) {
                this.f3914f = true;
                z3 = true;
            } else {
                z3 = true;
                z5 = false;
            }
            if (!z5) {
                return false;
            }
            if (!z3 || this.f3911c.contains(x3, y3)) {
                Rect rect = this.f3911c;
                motionEvent.setLocation(x3 - rect.left, y3 - rect.top);
            } else {
                motionEvent.setLocation(this.f3909a.getWidth() / 2, this.f3909a.getHeight() / 2);
            }
            return this.f3909a.dispatchTouchEvent(motionEvent);
        }
    }

    static {
        f3842o0 = Build.VERSION.SDK_INT < 29 ? new o() : null;
    }

    public SearchView(Context context) {
        this(context, null);
    }

    private Intent A(String str, Uri uri, String str2, String str3, int i3, String str4) {
        Intent intent = new Intent(str);
        intent.addFlags(268435456);
        if (uri != null) {
            intent.setData(uri);
        }
        intent.putExtra("user_query", this.f3866a0);
        if (str3 != null) {
            intent.putExtra("query", str3);
        }
        if (str2 != null) {
            intent.putExtra("intent_extra_data_key", str2);
        }
        Bundle bundle = this.f3870e0;
        if (bundle != null) {
            intent.putExtra("app_data", bundle);
        }
        if (i3 != 0) {
            intent.putExtra("action_key", i3);
            intent.putExtra("action_msg", str4);
        }
        intent.setComponent(this.f3869d0.getSearchActivity());
        return intent;
    }

    private Intent B(Cursor cursor, int i3, String str) {
        int position;
        String strO;
        try {
            String strO2 = b0.o(cursor, "suggest_intent_action");
            if (strO2 == null) {
                strO2 = this.f3869d0.getSuggestIntentAction();
            }
            if (strO2 == null) {
                strO2 = "android.intent.action.SEARCH";
            }
            String str2 = strO2;
            String strO3 = b0.o(cursor, "suggest_intent_data");
            if (strO3 == null) {
                strO3 = this.f3869d0.getSuggestIntentData();
            }
            if (strO3 != null && (strO = b0.o(cursor, "suggest_intent_data_id")) != null) {
                strO3 = strO3 + "/" + Uri.encode(strO);
            }
            return A(str2, strO3 == null ? null : Uri.parse(strO3), b0.o(cursor, "suggest_intent_extra_data"), b0.o(cursor, "suggest_intent_query"), i3, str);
        } catch (RuntimeException e4) {
            try {
                position = cursor.getPosition();
            } catch (RuntimeException unused) {
                position = -1;
            }
            Log.w("SearchView", "Search suggestions cursor at row " + position + " returned exception.", e4);
            return null;
        }
    }

    private Intent C(Intent intent, SearchableInfo searchableInfo) {
        ComponentName searchActivity = searchableInfo.getSearchActivity();
        Intent intent2 = new Intent("android.intent.action.SEARCH");
        intent2.setComponent(searchActivity);
        PendingIntent activity = PendingIntent.getActivity(getContext(), 0, intent2, 1107296256);
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.f3870e0;
        if (bundle2 != null) {
            bundle.putParcelable("app_data", bundle2);
        }
        Intent intent3 = new Intent(intent);
        Resources resources = getResources();
        String string = searchableInfo.getVoiceLanguageModeId() != 0 ? resources.getString(searchableInfo.getVoiceLanguageModeId()) : "free_form";
        String string2 = searchableInfo.getVoicePromptTextId() != 0 ? resources.getString(searchableInfo.getVoicePromptTextId()) : null;
        String string3 = searchableInfo.getVoiceLanguageId() != 0 ? resources.getString(searchableInfo.getVoiceLanguageId()) : null;
        int voiceMaxResults = searchableInfo.getVoiceMaxResults() != 0 ? searchableInfo.getVoiceMaxResults() : 1;
        intent3.putExtra("android.speech.extra.LANGUAGE_MODEL", string);
        intent3.putExtra("android.speech.extra.PROMPT", string2);
        intent3.putExtra("android.speech.extra.LANGUAGE", string3);
        intent3.putExtra("android.speech.extra.MAX_RESULTS", voiceMaxResults);
        intent3.putExtra("calling_package", searchActivity != null ? searchActivity.flattenToShortString() : null);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", activity);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent3;
    }

    private Intent D(Intent intent, SearchableInfo searchableInfo) {
        Intent intent2 = new Intent(intent);
        ComponentName searchActivity = searchableInfo.getSearchActivity();
        intent2.putExtra("calling_package", searchActivity == null ? null : searchActivity.flattenToShortString());
        return intent2;
    }

    private void E() {
        this.f3880q.dismissDropDown();
    }

    private void G(View view, Rect rect) {
        view.getLocationInWindow(this.f3845C);
        getLocationInWindow(this.f3846D);
        int[] iArr = this.f3845C;
        int i3 = iArr[1];
        int[] iArr2 = this.f3846D;
        int i4 = i3 - iArr2[1];
        int i5 = iArr[0] - iArr2[0];
        rect.set(i5, i4, view.getWidth() + i5, view.getHeight() + i4);
    }

    private CharSequence H(CharSequence charSequence) {
        if (!this.f3856N || this.f3848F == null) {
            return charSequence;
        }
        int textSize = (int) (((double) this.f3880q.getTextSize()) * 1.25d);
        this.f3848F.setBounds(0, 0, textSize, textSize);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("   ");
        spannableStringBuilder.setSpan(new ImageSpan(this.f3848F), 1, 2, 33);
        spannableStringBuilder.append(charSequence);
        return spannableStringBuilder;
    }

    private boolean I() {
        SearchableInfo searchableInfo = this.f3869d0;
        if (searchableInfo == null || !searchableInfo.getVoiceSearchEnabled()) {
            return false;
        }
        Intent intent = this.f3869d0.getVoiceSearchLaunchWebSearch() ? this.f3851I : this.f3869d0.getVoiceSearchLaunchRecognizer() ? this.f3852J : null;
        return (intent == null || getContext().getPackageManager().resolveActivity(intent, 65536) == null) ? false : true;
    }

    static boolean K(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    private boolean L() {
        return (this.f3859Q || this.f3864V) && !J();
    }

    private void M(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            getContext().startActivity(intent);
        } catch (RuntimeException e4) {
            Log.e("SearchView", "Failed launch activity: " + intent, e4);
        }
    }

    private boolean O(int i3, int i4, String str) {
        Cursor cursorB = this.f3858P.b();
        if (cursorB == null || !cursorB.moveToPosition(i3)) {
            return false;
        }
        M(B(cursorB, i4, str));
        return true;
    }

    private void Z() {
        post(this.f3871f0);
    }

    private void a0(int i3) {
        Editable text = this.f3880q.getText();
        Cursor cursorB = this.f3858P.b();
        if (cursorB == null) {
            return;
        }
        if (!cursorB.moveToPosition(i3)) {
            setQuery(text);
            return;
        }
        CharSequence charSequenceC = this.f3858P.c(cursorB);
        if (charSequenceC != null) {
            setQuery(charSequenceC);
        } else {
            setQuery(text);
        }
    }

    private void c0() {
        boolean zIsEmpty = TextUtils.isEmpty(this.f3880q.getText());
        this.f3886w.setVisibility(!zIsEmpty || (this.f3856N && !this.f3867b0) ? 0 : 8);
        Drawable drawable = this.f3886w.getDrawable();
        if (drawable != null) {
            drawable.setState(!zIsEmpty ? ViewGroup.ENABLED_STATE_SET : ViewGroup.EMPTY_STATE_SET);
        }
    }

    private void e0() {
        CharSequence queryHint = getQueryHint();
        SearchAutoComplete searchAutoComplete = this.f3880q;
        if (queryHint == null) {
            queryHint = "";
        }
        searchAutoComplete.setHint(H(queryHint));
    }

    private void f0() {
        this.f3880q.setThreshold(this.f3869d0.getSuggestThreshold());
        this.f3880q.setImeOptions(this.f3869d0.getImeOptions());
        int inputType = this.f3869d0.getInputType();
        if ((inputType & 15) == 1) {
            inputType &= -65537;
            if (this.f3869d0.getSuggestAuthority() != null) {
                inputType |= 589824;
            }
        }
        this.f3880q.setInputType(inputType);
        AbstractC0752a abstractC0752a = this.f3858P;
        if (abstractC0752a != null) {
            abstractC0752a.a(null);
        }
        if (this.f3869d0.getSuggestAuthority() != null) {
            b0 b0Var = new b0(getContext(), this, this.f3869d0, this.f3873h0);
            this.f3858P = b0Var;
            this.f3880q.setAdapter(b0Var);
            ((b0) this.f3858P).x(this.f3861S ? 2 : 1);
        }
    }

    private void g0() {
        this.f3883t.setVisibility((L() && (this.f3885v.getVisibility() == 0 || this.f3887x.getVisibility() == 0)) ? 0 : 8);
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(d.d.f8714g);
    }

    private int getPreferredWidth() {
        return getContext().getResources().getDimensionPixelSize(d.d.f8715h);
    }

    private void h0(boolean z3) {
        this.f3885v.setVisibility((this.f3859Q && L() && hasFocus() && (z3 || !this.f3864V)) ? 0 : 8);
    }

    private void i0(boolean z3) {
        this.f3857O = z3;
        int i3 = 8;
        int i4 = z3 ? 0 : 8;
        boolean zIsEmpty = TextUtils.isEmpty(this.f3880q.getText());
        this.f3884u.setVisibility(i4);
        h0(!zIsEmpty);
        this.f3881r.setVisibility(z3 ? 8 : 0);
        if (this.f3847E.getDrawable() != null && !this.f3856N) {
            i3 = 0;
        }
        this.f3847E.setVisibility(i3);
        c0();
        j0(zIsEmpty);
        g0();
    }

    private void j0(boolean z3) {
        int i3 = 8;
        if (this.f3864V && !J() && z3) {
            this.f3885v.setVisibility(8);
            i3 = 0;
        }
        this.f3887x.setVisibility(i3);
    }

    private void setQuery(CharSequence charSequence) {
        this.f3880q.setText(charSequence);
        this.f3880q.setSelection(TextUtils.isEmpty(charSequence) ? 0 : charSequence.length());
    }

    void F() {
        if (Build.VERSION.SDK_INT >= 29) {
            k.a(this.f3880q);
            return;
        }
        o oVar = f3842o0;
        oVar.b(this.f3880q);
        oVar.a(this.f3880q);
    }

    public boolean J() {
        return this.f3857O;
    }

    void N(int i3, String str, String str2) {
        getContext().startActivity(A("android.intent.action.SEARCH", null, null, str2, i3, str));
    }

    void P() {
        if (!TextUtils.isEmpty(this.f3880q.getText())) {
            this.f3880q.setText("");
            this.f3880q.requestFocus();
            this.f3880q.setImeVisibility(true);
        } else if (this.f3856N) {
            clearFocus();
            i0(true);
        }
    }

    boolean Q(int i3, int i4, String str) {
        O(i3, 0, null);
        this.f3880q.setImeVisibility(false);
        E();
        return true;
    }

    boolean R(int i3) {
        a0(i3);
        return true;
    }

    protected void S(CharSequence charSequence) {
        setQuery(charSequence);
    }

    void T() {
        i0(false);
        this.f3880q.requestFocus();
        this.f3880q.setImeVisibility(true);
        View.OnClickListener onClickListener = this.f3855M;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    void U() {
        Editable text = this.f3880q.getText();
        if (text == null || TextUtils.getTrimmedLength(text) <= 0) {
            return;
        }
        if (this.f3869d0 != null) {
            N(0, null, text.toString());
        }
        this.f3880q.setImeVisibility(false);
        E();
    }

    boolean V(View view, int i3, KeyEvent keyEvent) {
        if (this.f3869d0 != null && this.f3858P != null && keyEvent.getAction() == 0 && keyEvent.hasNoModifiers()) {
            if (i3 == 66 || i3 == 84 || i3 == 61) {
                return Q(this.f3880q.getListSelection(), 0, null);
            }
            if (i3 == 21 || i3 == 22) {
                this.f3880q.setSelection(i3 == 21 ? 0 : this.f3880q.length());
                this.f3880q.setListSelection(0);
                this.f3880q.clearListSelection();
                this.f3880q.b();
                return true;
            }
            if (i3 == 19) {
                this.f3880q.getListSelection();
                return false;
            }
        }
        return false;
    }

    void W(CharSequence charSequence) {
        Editable text = this.f3880q.getText();
        this.f3866a0 = text;
        boolean zIsEmpty = TextUtils.isEmpty(text);
        h0(!zIsEmpty);
        j0(zIsEmpty);
        c0();
        g0();
        this.f3865W = charSequence.toString();
    }

    void X() {
        i0(J());
        Z();
        if (this.f3880q.hasFocus()) {
            F();
        }
    }

    void Y() {
        SearchableInfo searchableInfo = this.f3869d0;
        if (searchableInfo == null) {
            return;
        }
        try {
            if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
                getContext().startActivity(D(this.f3851I, searchableInfo));
            } else if (searchableInfo.getVoiceSearchLaunchRecognizer()) {
                getContext().startActivity(C(this.f3852J, searchableInfo));
            }
        } catch (ActivityNotFoundException unused) {
            Log.w("SearchView", "Could not find voice search activity");
        }
    }

    public void b0(CharSequence charSequence, boolean z3) {
        this.f3880q.setText(charSequence);
        if (charSequence != null) {
            SearchAutoComplete searchAutoComplete = this.f3880q;
            searchAutoComplete.setSelection(searchAutoComplete.length());
            this.f3866a0 = charSequence;
        }
        if (!z3 || TextUtils.isEmpty(charSequence)) {
            return;
        }
        U();
    }

    @Override // androidx.appcompat.view.c
    public void c() {
        if (this.f3867b0) {
            return;
        }
        this.f3867b0 = true;
        int imeOptions = this.f3880q.getImeOptions();
        this.f3868c0 = imeOptions;
        this.f3880q.setImeOptions(imeOptions | 33554432);
        this.f3880q.setText("");
        setIconified(false);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void clearFocus() {
        this.f3862T = true;
        super.clearFocus();
        this.f3880q.clearFocus();
        this.f3880q.setImeVisibility(false);
        this.f3862T = false;
    }

    @Override // androidx.appcompat.view.c
    public void d() {
        b0("", false);
        clearFocus();
        i0(true);
        this.f3880q.setImeOptions(this.f3868c0);
        this.f3867b0 = false;
    }

    void d0() {
        int[] iArr = this.f3880q.hasFocus() ? ViewGroup.FOCUSED_STATE_SET : ViewGroup.EMPTY_STATE_SET;
        Drawable background = this.f3882s.getBackground();
        if (background != null) {
            background.setState(iArr);
        }
        Drawable background2 = this.f3883t.getBackground();
        if (background2 != null) {
            background2.setState(iArr);
        }
        invalidate();
    }

    public int getImeOptions() {
        return this.f3880q.getImeOptions();
    }

    public int getInputType() {
        return this.f3880q.getInputType();
    }

    public int getMaxWidth() {
        return this.f3863U;
    }

    public CharSequence getQuery() {
        return this.f3880q.getText();
    }

    public CharSequence getQueryHint() {
        CharSequence charSequence = this.f3860R;
        if (charSequence != null) {
            return charSequence;
        }
        SearchableInfo searchableInfo = this.f3869d0;
        return (searchableInfo == null || searchableInfo.getHintId() == 0) ? this.f3853K : getContext().getText(this.f3869d0.getHintId());
    }

    int getSuggestionCommitIconResId() {
        return this.f3850H;
    }

    int getSuggestionRowLayout() {
        return this.f3849G;
    }

    public AbstractC0752a getSuggestionsAdapter() {
        return this.f3858P;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.f3871f0);
        post(this.f3872g0);
        super.onDetachedFromWindow();
    }

    @Override // androidx.appcompat.widget.T, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z3, int i3, int i4, int i5, int i6) {
        super.onLayout(z3, i3, i4, i5, i6);
        if (z3) {
            G(this.f3880q, this.f3843A);
            Rect rect = this.f3844B;
            Rect rect2 = this.f3843A;
            rect.set(rect2.left, 0, rect2.right, i6 - i4);
            q qVar = this.f3889z;
            if (qVar != null) {
                qVar.a(this.f3844B, this.f3843A);
                return;
            }
            q qVar2 = new q(this.f3844B, this.f3843A, this.f3880q);
            this.f3889z = qVar2;
            setTouchDelegate(qVar2);
        }
    }

    @Override // androidx.appcompat.widget.T, android.view.View
    protected void onMeasure(int i3, int i4) {
        int i5;
        if (J()) {
            super.onMeasure(i3, i4);
            return;
        }
        int mode = View.MeasureSpec.getMode(i3);
        int size = View.MeasureSpec.getSize(i3);
        if (mode == Integer.MIN_VALUE) {
            int i6 = this.f3863U;
            size = i6 > 0 ? Math.min(i6, size) : Math.min(getPreferredWidth(), size);
        } else if (mode == 0) {
            size = this.f3863U;
            if (size <= 0) {
                size = getPreferredWidth();
            }
        } else if (mode == 1073741824 && (i5 = this.f3863U) > 0) {
            size = Math.min(i5, size);
        }
        int mode2 = View.MeasureSpec.getMode(i4);
        int size2 = View.MeasureSpec.getSize(i4);
        if (mode2 == Integer.MIN_VALUE) {
            size2 = Math.min(getPreferredHeight(), size2);
        } else if (mode2 == 0) {
            size2 = getPreferredHeight();
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof p)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        p pVar = (p) parcelable;
        super.onRestoreInstanceState(pVar.a());
        i0(pVar.f3908c);
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        p pVar = new p(super.onSaveInstanceState());
        pVar.f3908c = J();
        return pVar;
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z3) {
        super.onWindowFocusChanged(z3);
        Z();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean requestFocus(int i3, Rect rect) {
        if (this.f3862T || !isFocusable()) {
            return false;
        }
        if (J()) {
            return super.requestFocus(i3, rect);
        }
        boolean zRequestFocus = this.f3880q.requestFocus(i3, rect);
        if (zRequestFocus) {
            i0(false);
        }
        return zRequestFocus;
    }

    public void setAppSearchData(Bundle bundle) {
        this.f3870e0 = bundle;
    }

    public void setIconified(boolean z3) {
        if (z3) {
            P();
        } else {
            T();
        }
    }

    public void setIconifiedByDefault(boolean z3) {
        if (this.f3856N == z3) {
            return;
        }
        this.f3856N = z3;
        i0(z3);
        e0();
    }

    public void setImeOptions(int i3) {
        this.f3880q.setImeOptions(i3);
    }

    public void setInputType(int i3) {
        this.f3880q.setInputType(i3);
    }

    public void setMaxWidth(int i3) {
        this.f3863U = i3;
        requestLayout();
    }

    public void setOnCloseListener(l lVar) {
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.f3854L = onFocusChangeListener;
    }

    public void setOnQueryTextListener(m mVar) {
    }

    public void setOnSearchClickListener(View.OnClickListener onClickListener) {
        this.f3855M = onClickListener;
    }

    public void setOnSuggestionListener(n nVar) {
    }

    public void setQueryHint(CharSequence charSequence) {
        this.f3860R = charSequence;
        e0();
    }

    public void setQueryRefinementEnabled(boolean z3) {
        this.f3861S = z3;
        AbstractC0752a abstractC0752a = this.f3858P;
        if (abstractC0752a instanceof b0) {
            ((b0) abstractC0752a).x(z3 ? 2 : 1);
        }
    }

    public void setSearchableInfo(SearchableInfo searchableInfo) {
        this.f3869d0 = searchableInfo;
        if (searchableInfo != null) {
            f0();
            e0();
        }
        boolean zI = I();
        this.f3864V = zI;
        if (zI) {
            this.f3880q.setPrivateImeOptions("nm");
        }
        i0(J());
    }

    public void setSubmitButtonEnabled(boolean z3) {
        this.f3859Q = z3;
        i0(J());
    }

    public void setSuggestionsAdapter(AbstractC0752a abstractC0752a) {
        this.f3858P = abstractC0752a;
        this.f3880q.setAdapter(abstractC0752a);
    }

    void z() {
        if (this.f3888y.getWidth() > 1) {
            Resources resources = getContext().getResources();
            int paddingLeft = this.f3882s.getPaddingLeft();
            Rect rect = new Rect();
            boolean zB = s0.b(this);
            int dimensionPixelSize = this.f3856N ? resources.getDimensionPixelSize(d.d.f8712e) + resources.getDimensionPixelSize(d.d.f8713f) : 0;
            this.f3880q.getDropDownBackground().getPadding(rect);
            this.f3880q.setDropDownHorizontalOffset(zB ? -rect.left : paddingLeft - (rect.left + dimensionPixelSize));
            this.f3880q.setDropDownWidth((((this.f3888y.getWidth() + rect.left) + rect.right) + dimensionPixelSize) - paddingLeft);
        }
    }

    public SearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, AbstractC0487a.f8666K);
    }

    public SearchView(Context context, AttributeSet attributeSet, int i3) {
        super(context, attributeSet, i3);
        this.f3843A = new Rect();
        this.f3844B = new Rect();
        this.f3845C = new int[2];
        this.f3846D = new int[2];
        this.f3871f0 = new b();
        this.f3872g0 = new c();
        this.f3873h0 = new WeakHashMap();
        f fVar = new f();
        this.f3874i0 = fVar;
        this.f3875j0 = new g();
        h hVar = new h();
        this.f3876k0 = hVar;
        i iVar = new i();
        this.f3877l0 = iVar;
        j jVar = new j();
        this.f3878m0 = jVar;
        this.f3879n0 = new a();
        h0 h0VarU = h0.u(context, attributeSet, d.j.f8980f2, i3, 0);
        androidx.core.view.Z.V(this, context, d.j.f8980f2, attributeSet, h0VarU.q(), i3, 0);
        LayoutInflater.from(context).inflate(h0VarU.m(d.j.f9020p2, d.g.f8827r), (ViewGroup) this, true);
        SearchAutoComplete searchAutoComplete = (SearchAutoComplete) findViewById(d.f.f8771D);
        this.f3880q = searchAutoComplete;
        searchAutoComplete.setSearchView(this);
        this.f3881r = findViewById(d.f.f8809z);
        View viewFindViewById = findViewById(d.f.f8770C);
        this.f3882s = viewFindViewById;
        View viewFindViewById2 = findViewById(d.f.f8777J);
        this.f3883t = viewFindViewById2;
        ImageView imageView = (ImageView) findViewById(d.f.f8807x);
        this.f3884u = imageView;
        ImageView imageView2 = (ImageView) findViewById(d.f.f8768A);
        this.f3885v = imageView2;
        ImageView imageView3 = (ImageView) findViewById(d.f.f8808y);
        this.f3886w = imageView3;
        ImageView imageView4 = (ImageView) findViewById(d.f.f8772E);
        this.f3887x = imageView4;
        ImageView imageView5 = (ImageView) findViewById(d.f.f8769B);
        this.f3847E = imageView5;
        androidx.core.view.Z.b0(viewFindViewById, h0VarU.f(d.j.f9024q2));
        androidx.core.view.Z.b0(viewFindViewById2, h0VarU.f(d.j.f9040u2));
        imageView.setImageDrawable(h0VarU.f(d.j.f9036t2));
        imageView2.setImageDrawable(h0VarU.f(d.j.f9012n2));
        imageView3.setImageDrawable(h0VarU.f(d.j.f9000k2));
        imageView4.setImageDrawable(h0VarU.f(d.j.f9048w2));
        imageView5.setImageDrawable(h0VarU.f(d.j.f9036t2));
        this.f3848F = h0VarU.f(d.j.f9032s2);
        m0.a(imageView, getResources().getString(d.h.f8843n));
        this.f3849G = h0VarU.m(d.j.f9044v2, d.g.f8826q);
        this.f3850H = h0VarU.m(d.j.f9004l2, 0);
        imageView.setOnClickListener(fVar);
        imageView3.setOnClickListener(fVar);
        imageView2.setOnClickListener(fVar);
        imageView4.setOnClickListener(fVar);
        searchAutoComplete.setOnClickListener(fVar);
        searchAutoComplete.addTextChangedListener(this.f3879n0);
        searchAutoComplete.setOnEditorActionListener(hVar);
        searchAutoComplete.setOnItemClickListener(iVar);
        searchAutoComplete.setOnItemSelectedListener(jVar);
        searchAutoComplete.setOnKeyListener(this.f3875j0);
        searchAutoComplete.setOnFocusChangeListener(new d());
        setIconifiedByDefault(h0VarU.a(d.j.f9016o2, true));
        int iE = h0VarU.e(d.j.f8988h2, -1);
        if (iE != -1) {
            setMaxWidth(iE);
        }
        this.f3853K = h0VarU.o(d.j.f9008m2);
        this.f3860R = h0VarU.o(d.j.f9028r2);
        int iJ = h0VarU.j(d.j.f8996j2, -1);
        if (iJ != -1) {
            setImeOptions(iJ);
        }
        int iJ2 = h0VarU.j(d.j.f8992i2, -1);
        if (iJ2 != -1) {
            setInputType(iJ2);
        }
        setFocusable(h0VarU.a(d.j.f8984g2, true));
        h0VarU.w();
        Intent intent = new Intent("android.speech.action.WEB_SEARCH");
        this.f3851I = intent;
        intent.addFlags(268435456);
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        Intent intent2 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.f3852J = intent2;
        intent2.addFlags(268435456);
        View viewFindViewById3 = findViewById(searchAutoComplete.getDropDownAnchor());
        this.f3888y = viewFindViewById3;
        if (viewFindViewById3 != null) {
            viewFindViewById3.addOnLayoutChangeListener(new e());
        }
        i0(this.f3856N);
        e0();
    }
}
