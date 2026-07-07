package B0;

import android.icu.util.ULocale;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class g implements b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ULocale f59a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ULocale.Builder f60b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f61c;

    private g(ULocale uLocale) {
        this.f60b = null;
        this.f61c = false;
        this.f59a = uLocale;
    }

    public static b i() {
        return new g(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public static b j(String str) {
        return new g(str);
    }

    public static b k(ULocale uLocale) {
        return new g(uLocale);
    }

    private void l() throws e {
        if (this.f61c) {
            try {
                this.f59a = this.f60b.build();
                this.f61c = false;
            } catch (RuntimeException e4) {
                throw new e(e4.getMessage());
            }
        }
    }

    @Override // B0.b
    public String a() {
        return h().toLanguageTag();
    }

    @Override // B0.b
    public HashMap b() throws e {
        l();
        HashMap map = new HashMap();
        Iterator<String> keywords = this.f59a.getKeywords();
        if (keywords != null) {
            while (keywords.hasNext()) {
                String next = keywords.next();
                map.put(i.b(next), this.f59a.getKeywordValue(next));
            }
        }
        return map;
    }

    @Override // B0.b
    public ArrayList c(String str) throws e {
        l();
        String strA = i.a(str);
        ArrayList arrayList = new ArrayList();
        String keywordValue = this.f59a.getKeywordValue(strA);
        if (keywordValue != null && !keywordValue.isEmpty()) {
            Collections.addAll(arrayList, keywordValue.split("-|_"));
        }
        return arrayList;
    }

    @Override // B0.b
    public b e() throws e {
        l();
        return new g(this.f59a);
    }

    @Override // B0.b
    public String f() {
        return d().toLanguageTag();
    }

    @Override // B0.b
    public void g(String str, ArrayList arrayList) throws e {
        l();
        if (this.f60b == null) {
            this.f60b = new ULocale.Builder().setLocale(this.f59a);
        }
        try {
            this.f60b.setUnicodeLocaleKeyword(str, TextUtils.join("-", arrayList));
            this.f61c = true;
        } catch (RuntimeException e4) {
            throw new e(e4.getMessage());
        }
    }

    @Override // B0.b
    /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
    public ULocale h() throws e {
        l();
        return this.f59a;
    }

    @Override // B0.b
    /* JADX INFO: renamed from: n, reason: merged with bridge method [inline-methods] */
    public ULocale d() throws e {
        l();
        ULocale.Builder builder = new ULocale.Builder();
        builder.setLocale(this.f59a);
        builder.clearExtensions();
        return builder.build();
    }

    private g(String str) throws e {
        this.f59a = null;
        this.f60b = null;
        this.f61c = false;
        ULocale.Builder builder = new ULocale.Builder();
        this.f60b = builder;
        try {
            builder.setLanguageTag(str);
            this.f61c = true;
        } catch (RuntimeException e4) {
            throw new e(e4.getMessage());
        }
    }
}
