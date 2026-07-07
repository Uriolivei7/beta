package K2;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class b {
    public static int a(int i3) {
        if (2 <= i3 && i3 < 37) {
            return i3;
        }
        throw new IllegalArgumentException("radix " + i3 + " was not in valid range " + new H2.c(2, 36));
    }

    public static final int b(char c4, int i3) {
        return Character.digit((int) c4, i3);
    }

    public static final boolean c(char c4) {
        return Character.isWhitespace(c4) || Character.isSpaceChar(c4);
    }
}
