package androidx.appcompat.widget;

import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
final class B {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private TextView f3740a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private TextClassifier f3741b;

    private static final class a {
        static TextClassifier a(TextView textView) {
            TextClassificationManager textClassificationManager = (TextClassificationManager) textView.getContext().getSystemService(TextClassificationManager.class);
            return textClassificationManager != null ? textClassificationManager.getTextClassifier() : TextClassifier.NO_OP;
        }
    }

    B(TextView textView) {
        this.f3740a = (TextView) q.g.g(textView);
    }

    public TextClassifier a() {
        TextClassifier textClassifier = this.f3741b;
        return textClassifier == null ? a.a(this.f3740a) : textClassifier;
    }

    public void b(TextClassifier textClassifier) {
        this.f3741b = textClassifier;
    }
}
