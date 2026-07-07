package f0;

import X.k;
import a1.C0210a;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Uri f9380a = Uri.withAppendedPath((Uri) C0210a.e(ContactsContract.AUTHORITY_URI), "display_photo");

    public static AssetFileDescriptor a(ContentResolver contentResolver, Uri uri) {
        if (l(uri)) {
            try {
                return contentResolver.openAssetFileDescriptor(uri, "r");
            } catch (FileNotFoundException unused) {
            }
        }
        return null;
    }

    private static String b(boolean z3) {
        return "_data";
    }

    private static Uri c(boolean z3) {
        return z3 ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    private static String d(boolean z3) {
        return "_id";
    }

    public static String e(ContentResolver contentResolver, Uri uri) {
        Uri uri2;
        String str;
        String[] strArr;
        int columnIndexOrThrow;
        String type = contentResolver.getType(uri);
        String string = null;
        if (!l(uri)) {
            if (m(uri)) {
                return uri.getPath();
            }
            return null;
        }
        boolean z3 = type != null && type.startsWith("video/");
        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
            String documentId = DocumentsContract.getDocumentId(uri);
            k.g(documentId);
            uri2 = (Uri) k.g(c(z3));
            str = d(z3) + "=?";
            strArr = new String[]{documentId.split(":")[1]};
        } else {
            uri2 = uri;
            str = null;
            strArr = null;
        }
        Cursor cursorQuery = contentResolver.query(uri2, new String[]{b(z3)}, str, strArr, null);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst() && (columnIndexOrThrow = cursorQuery.getColumnIndexOrThrow(b(z3))) != -1) {
                    string = cursorQuery.getString(columnIndexOrThrow);
                }
            } finally {
                cursorQuery.close();
            }
        }
        return cursorQuery != null ? string : string;
    }

    public static String f(Uri uri) {
        if (uri == null) {
            return null;
        }
        return uri.getScheme();
    }

    public static Uri g(int i3) {
        return new Uri.Builder().scheme("res").path(String.valueOf(i3)).build();
    }

    public static boolean h(Uri uri) {
        return "data".equals(f(uri));
    }

    public static boolean i(Uri uri) {
        return "asset".equals(f(uri));
    }

    public static boolean j(Uri uri) {
        String string = uri.toString();
        return string.startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()) || string.startsWith(MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
    }

    public static boolean k(Uri uri) {
        return uri.getPath() != null && l(uri) && "com.android.contacts".equals(uri.getAuthority()) && !uri.getPath().startsWith((String) C0210a.e(f9380a.getPath()));
    }

    public static boolean l(Uri uri) {
        return "content".equals(f(uri));
    }

    public static boolean m(Uri uri) {
        return "file".equals(f(uri));
    }

    public static boolean n(Uri uri) {
        return "res".equals(f(uri));
    }

    public static boolean o(Uri uri) {
        String strF = f(uri);
        return "https".equals(strF) || "http".equals(strF);
    }

    public static boolean p(Uri uri) {
        return "android.resource".equals(f(uri));
    }

    public static URL q(Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e4) {
            throw new RuntimeException(e4);
        }
    }
}
