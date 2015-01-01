package xqt.adapters.builtin.functions;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */

// maybe it is better to put these function pack classes into a standalone jar.
public class String {
    public static int indexOf(java.lang.String s1, java.lang.String s2){
        if(s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty())
            return -1;
        return s1.indexOf(s2);
    }

    public static boolean isDate(java.lang.String s){ // needs work
        return false;
    }
    
    public static boolean isEmpty(java.lang.String s){
        if(s == null)
            return true;
        return s.isEmpty();
    }

    public static boolean isNumeric(java.lang.String s) {
       if (s == null) {
           return false;
       }
       int sz = s.length();
       for (int i = 0; i < sz; i++) {
           if (Character.isDigit(s.charAt(i)) == false) {
               return false;
           }
       }
       return true;
    }

    public static int lastIndexOf(java.lang.String s1, java.lang.String s2){
        if(s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty())
            return -1;
        return s1.lastIndexOf(s2);
    }

    public static int length(java.lang.String s){
        if(s == null)
            return 0;
        return s.length();
    }

    public static java.lang.String lower(java.lang.String s){
        if(s == null)
            return null;
        return s.toLowerCase();
    }

    public static java.lang.String ltrim(java.lang.String s) {
        if (s == null) {
            return null;
        }
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }    
    
    public static java.lang.String replace(java.lang.String source, java.lang.String target, java.lang.String replacement) {
        if(source == null)
            return null;
        return source.replace(target, replacement);
    }    
    
    public static java.lang.String reverse(java.lang.String s) {
        if (s == null) {
            return null;
        }
        return new StringBuilder(s).reverse().toString();
    }

    public static java.lang.String rtrim(java.lang.String s) {
        if (s == null) {
            return null;
        }
        int i = s.length()-1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0,i+1);
    }

    public static java.lang.String substring(java.lang.String s, int beginIndex, int endIndex) {
        if (s == null) {
            return null;
        }
        return s.substring(beginIndex, endIndex);
    }

    public static java.lang.String trim(java.lang.String s) {
        if (s == null) {
            return null;
        }
        return s.trim();
    }    
    
    public static java.lang.String upper(java.lang.String string){
        if(string == null)
            return null;
        return string.toUpperCase();
    }
}
