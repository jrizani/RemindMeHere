package id.juliannr.remindmehere.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by juliannr on 20/04/18.
 */

public class EdittextRegexFilter implements InputFilter {
    private Pattern pattern;

    public EdittextRegexFilter(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        String textToCheck = spanned.subSequence(0, i2).
                toString() + charSequence.subSequence(i, i1) +
                spanned.subSequence(
                        i3, spanned.length()).toString();

        Matcher matcher = pattern.matcher(textToCheck);

        // Entered text does not match the pattern
        if(!matcher.matches()){

            // It does not match partially too
            if(!matcher.hitEnd()){
                return "";
            }

        }

        return null;
    }
}
