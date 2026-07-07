package androidx.appcompat.widget;

import android.R;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import d.AbstractC0487a;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;
import v.AbstractC0754c;

/* JADX INFO: loaded from: classes.dex */
class b0 extends AbstractC0754c implements View.OnClickListener {

    /* JADX INFO: renamed from: A, reason: collision with root package name */
    private int f4092A;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final SearchView f4093n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final SearchableInfo f4094o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final Context f4095p;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final WeakHashMap f4096q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private final int f4097r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean f4098s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    private int f4099t;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private ColorStateList f4100u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private int f4101v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private int f4102w;

    /* JADX INFO: renamed from: x, reason: collision with root package name */
    private int f4103x;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private int f4104y;

    /* JADX INFO: renamed from: z, reason: collision with root package name */
    private int f4105z;

    private static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final TextView f4106a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final TextView f4107b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final ImageView f4108c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final ImageView f4109d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public final ImageView f4110e;

        public a(View view) {
            this.f4106a = (TextView) view.findViewById(R.id.text1);
            this.f4107b = (TextView) view.findViewById(R.id.text2);
            this.f4108c = (ImageView) view.findViewById(R.id.icon1);
            this.f4109d = (ImageView) view.findViewById(R.id.icon2);
            this.f4110e = (ImageView) view.findViewById(d.f.f8800q);
        }
    }

    public b0(Context context, SearchView searchView, SearchableInfo searchableInfo, WeakHashMap<String, Drawable.ConstantState> weakHashMap) {
        super(context, searchView.getSuggestionRowLayout(), (Cursor) null, true);
        this.f4098s = false;
        this.f4099t = 1;
        this.f4101v = -1;
        this.f4102w = -1;
        this.f4103x = -1;
        this.f4104y = -1;
        this.f4105z = -1;
        this.f4092A = -1;
        this.f4093n = searchView;
        this.f4094o = searchableInfo;
        this.f4097r = searchView.getSuggestionCommitIconResId();
        this.f4095p = context;
        this.f4096q = weakHashMap;
    }

    private void A(String str, Drawable drawable) {
        if (drawable != null) {
            this.f4096q.put(str, drawable.getConstantState());
        }
    }

    private void B(Cursor cursor) {
        Bundle extras = cursor != null ? cursor.getExtras() : null;
        if (extras != null) {
            extras.getBoolean("in_progress");
        }
    }

    private Drawable k(String str) {
        Drawable.ConstantState constantState = (Drawable.ConstantState) this.f4096q.get(str);
        if (constantState == null) {
            return null;
        }
        return constantState.newDrawable();
    }

    private CharSequence l(CharSequence charSequence) {
        if (this.f4100u == null) {
            TypedValue typedValue = new TypedValue();
            this.f4095p.getTheme().resolveAttribute(AbstractC0487a.f8670O, typedValue, true);
            this.f4100u = this.f4095p.getResources().getColorStateList(typedValue.resourceId);
        }
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(new TextAppearanceSpan(null, 0, 0, this.f4100u, null), 0, charSequence.length(), 33);
        return spannableString;
    }

    private Drawable m(ComponentName componentName) {
        PackageManager packageManager = this.f4095p.getPackageManager();
        try {
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, 128);
            int iconResource = activityInfo.getIconResource();
            if (iconResource == 0) {
                return null;
            }
            Drawable drawable = packageManager.getDrawable(componentName.getPackageName(), iconResource, activityInfo.applicationInfo);
            if (drawable != null) {
                return drawable;
            }
            Log.w("SuggestionsAdapter", "Invalid icon resource " + iconResource + " for " + componentName.flattenToShortString());
            return null;
        } catch (PackageManager.NameNotFoundException e4) {
            Log.w("SuggestionsAdapter", e4.toString());
            return null;
        }
    }

    private Drawable n(ComponentName componentName) {
        String strFlattenToShortString = componentName.flattenToShortString();
        if (!this.f4096q.containsKey(strFlattenToShortString)) {
            Drawable drawableM = m(componentName);
            this.f4096q.put(strFlattenToShortString, drawableM != null ? drawableM.getConstantState() : null);
            return drawableM;
        }
        Drawable.ConstantState constantState = (Drawable.ConstantState) this.f4096q.get(strFlattenToShortString);
        if (constantState == null) {
            return null;
        }
        return constantState.newDrawable(this.f4095p.getResources());
    }

    public static String o(Cursor cursor, String str) {
        return w(cursor, cursor.getColumnIndex(str));
    }

    private Drawable p() {
        Drawable drawableN = n(this.f4094o.getSearchActivity());
        return drawableN != null ? drawableN : this.f4095p.getPackageManager().getDefaultActivityIcon();
    }

    private Drawable q(Uri uri) {
        try {
            if ("android.resource".equals(uri.getScheme())) {
                try {
                    return r(uri);
                } catch (Resources.NotFoundException unused) {
                    throw new FileNotFoundException("Resource does not exist: " + uri);
                }
            }
            InputStream inputStreamOpenInputStream = this.f4095p.getContentResolver().openInputStream(uri);
            if (inputStreamOpenInputStream == null) {
                throw new FileNotFoundException("Failed to open " + uri);
            }
            try {
                return Drawable.createFromStream(inputStreamOpenInputStream, null);
            } finally {
                try {
                    inputStreamOpenInputStream.close();
                } catch (IOException e4) {
                    Log.e("SuggestionsAdapter", "Error closing icon stream for " + uri, e4);
                }
            }
        } catch (FileNotFoundException e5) {
            Log.w("SuggestionsAdapter", "Icon not found: " + uri + ", " + e5.getMessage());
            return null;
        }
        Log.w("SuggestionsAdapter", "Icon not found: " + uri + ", " + e5.getMessage());
        return null;
    }

    private Drawable s(String str) {
        if (str == null || str.isEmpty() || "0".equals(str)) {
            return null;
        }
        try {
            int i3 = Integer.parseInt(str);
            String str2 = "android.resource://" + this.f4095p.getPackageName() + "/" + i3;
            Drawable drawableK = k(str2);
            if (drawableK != null) {
                return drawableK;
            }
            Drawable drawableD = androidx.core.content.a.d(this.f4095p, i3);
            A(str2, drawableD);
            return drawableD;
        } catch (Resources.NotFoundException unused) {
            Log.w("SuggestionsAdapter", "Icon resource not found: " + str);
            return null;
        } catch (NumberFormatException unused2) {
            Drawable drawableK2 = k(str);
            if (drawableK2 != null) {
                return drawableK2;
            }
            Drawable drawableQ = q(Uri.parse(str));
            A(str, drawableQ);
            return drawableQ;
        }
    }

    private Drawable t(Cursor cursor) {
        int i3 = this.f4104y;
        if (i3 == -1) {
            return null;
        }
        Drawable drawableS = s(cursor.getString(i3));
        return drawableS != null ? drawableS : p();
    }

    private Drawable u(Cursor cursor) {
        int i3 = this.f4105z;
        if (i3 == -1) {
            return null;
        }
        return s(cursor.getString(i3));
    }

    private static String w(Cursor cursor, int i3) {
        if (i3 == -1) {
            return null;
        }
        try {
            return cursor.getString(i3);
        } catch (Exception e4) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", e4);
            return null;
        }
    }

    private void y(ImageView imageView, Drawable drawable, int i3) {
        imageView.setImageDrawable(drawable);
        if (drawable == null) {
            imageView.setVisibility(i3);
            return;
        }
        imageView.setVisibility(0);
        drawable.setVisible(false, false);
        drawable.setVisible(true, false);
    }

    private void z(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
        }
    }

    @Override // v.AbstractC0752a, v.C0753b.a
    public void a(Cursor cursor) {
        if (this.f4098s) {
            Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
            if (cursor != null) {
                cursor.close();
                return;
            }
            return;
        }
        try {
            super.a(cursor);
            if (cursor != null) {
                this.f4101v = cursor.getColumnIndex("suggest_text_1");
                this.f4102w = cursor.getColumnIndex("suggest_text_2");
                this.f4103x = cursor.getColumnIndex("suggest_text_2_url");
                this.f4104y = cursor.getColumnIndex("suggest_icon_1");
                this.f4105z = cursor.getColumnIndex("suggest_icon_2");
                this.f4092A = cursor.getColumnIndex("suggest_flags");
            }
        } catch (Exception e4) {
            Log.e("SuggestionsAdapter", "error changing cursor and caching columns", e4);
        }
    }

    @Override // v.AbstractC0752a, v.C0753b.a
    public CharSequence c(Cursor cursor) {
        String strO;
        String strO2;
        if (cursor == null) {
            return null;
        }
        String strO3 = o(cursor, "suggest_intent_query");
        if (strO3 != null) {
            return strO3;
        }
        if (this.f4094o.shouldRewriteQueryFromData() && (strO2 = o(cursor, "suggest_intent_data")) != null) {
            return strO2;
        }
        if (!this.f4094o.shouldRewriteQueryFromText() || (strO = o(cursor, "suggest_text_1")) == null) {
            return null;
        }
        return strO;
    }

    @Override // v.AbstractC0752a, v.C0753b.a
    public Cursor d(CharSequence charSequence) {
        String string = charSequence == null ? "" : charSequence.toString();
        if (this.f4093n.getVisibility() == 0 && this.f4093n.getWindowVisibility() == 0) {
            try {
                Cursor cursorV = v(this.f4094o, string, 50);
                if (cursorV != null) {
                    cursorV.getCount();
                    return cursorV;
                }
            } catch (RuntimeException e4) {
                Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", e4);
            }
        }
        return null;
    }

    @Override // v.AbstractC0752a
    public void e(View view, Context context, Cursor cursor) {
        a aVar = (a) view.getTag();
        int i3 = this.f4092A;
        int i4 = i3 != -1 ? cursor.getInt(i3) : 0;
        if (aVar.f4106a != null) {
            z(aVar.f4106a, w(cursor, this.f4101v));
        }
        if (aVar.f4107b != null) {
            String strW = w(cursor, this.f4103x);
            CharSequence charSequenceL = strW != null ? l(strW) : w(cursor, this.f4102w);
            if (TextUtils.isEmpty(charSequenceL)) {
                TextView textView = aVar.f4106a;
                if (textView != null) {
                    textView.setSingleLine(false);
                    aVar.f4106a.setMaxLines(2);
                }
            } else {
                TextView textView2 = aVar.f4106a;
                if (textView2 != null) {
                    textView2.setSingleLine(true);
                    aVar.f4106a.setMaxLines(1);
                }
            }
            z(aVar.f4107b, charSequenceL);
        }
        ImageView imageView = aVar.f4108c;
        if (imageView != null) {
            y(imageView, t(cursor), 4);
        }
        ImageView imageView2 = aVar.f4109d;
        if (imageView2 != null) {
            y(imageView2, u(cursor), 8);
        }
        int i5 = this.f4099t;
        if (i5 != 2 && (i5 != 1 || (i4 & 1) == 0)) {
            aVar.f4110e.setVisibility(8);
            return;
        }
        aVar.f4110e.setVisibility(0);
        aVar.f4110e.setTag(aVar.f4106a.getText());
        aVar.f4110e.setOnClickListener(this);
    }

    @Override // v.AbstractC0752a, android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int i3, View view, ViewGroup viewGroup) {
        try {
            return super.getDropDownView(i3, view, viewGroup);
        } catch (RuntimeException e4) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", e4);
            View viewG = g(this.f4095p, b(), viewGroup);
            if (viewG != null) {
                ((a) viewG.getTag()).f4106a.setText(e4.toString());
            }
            return viewG;
        }
    }

    @Override // v.AbstractC0752a, android.widget.Adapter
    public View getView(int i3, View view, ViewGroup viewGroup) {
        try {
            return super.getView(i3, view, viewGroup);
        } catch (RuntimeException e4) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", e4);
            View viewH = h(this.f4095p, b(), viewGroup);
            if (viewH != null) {
                ((a) viewH.getTag()).f4106a.setText(e4.toString());
            }
            return viewH;
        }
    }

    @Override // v.AbstractC0754c, v.AbstractC0752a
    public View h(Context context, Cursor cursor, ViewGroup viewGroup) {
        View viewH = super.h(context, cursor, viewGroup);
        viewH.setTag(new a(viewH));
        ((ImageView) viewH.findViewById(d.f.f8800q)).setImageResource(this.f4097r);
        return viewH;
    }

    @Override // v.AbstractC0752a, android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        B(b());
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        B(b());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag instanceof CharSequence) {
            this.f4093n.S((CharSequence) tag);
        }
    }

    Drawable r(Uri uri) throws FileNotFoundException {
        int identifier;
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        }
        try {
            Resources resourcesForApplication = this.f4095p.getPackageManager().getResourcesForApplication(authority);
            List<String> pathSegments = uri.getPathSegments();
            if (pathSegments == null) {
                throw new FileNotFoundException("No path: " + uri);
            }
            int size = pathSegments.size();
            if (size == 1) {
                try {
                    identifier = Integer.parseInt(pathSegments.get(0));
                } catch (NumberFormatException unused) {
                    throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                }
            } else {
                if (size != 2) {
                    throw new FileNotFoundException("More than two path segments: " + uri);
                }
                identifier = resourcesForApplication.getIdentifier(pathSegments.get(1), pathSegments.get(0), authority);
            }
            if (identifier != 0) {
                return resourcesForApplication.getDrawable(identifier);
            }
            throw new FileNotFoundException("No resource found for: " + uri);
        } catch (PackageManager.NameNotFoundException unused2) {
            throw new FileNotFoundException("No package found for authority: " + uri);
        }
    }

    Cursor v(SearchableInfo searchableInfo, String str, int i3) {
        String suggestAuthority;
        String[] strArr = null;
        if (searchableInfo == null || (suggestAuthority = searchableInfo.getSuggestAuthority()) == null) {
            return null;
        }
        Uri.Builder builderFragment = new Uri.Builder().scheme("content").authority(suggestAuthority).query("").fragment("");
        String suggestPath = searchableInfo.getSuggestPath();
        if (suggestPath != null) {
            builderFragment.appendEncodedPath(suggestPath);
        }
        builderFragment.appendPath("search_suggest_query");
        String suggestSelection = searchableInfo.getSuggestSelection();
        if (suggestSelection != null) {
            strArr = new String[]{str};
        } else {
            builderFragment.appendPath(str);
        }
        String[] strArr2 = strArr;
        if (i3 > 0) {
            builderFragment.appendQueryParameter("limit", String.valueOf(i3));
        }
        return this.f4095p.getContentResolver().query(builderFragment.build(), null, suggestSelection, strArr2, null);
    }

    public void x(int i3) {
        this.f4099t = i3;
    }
}
