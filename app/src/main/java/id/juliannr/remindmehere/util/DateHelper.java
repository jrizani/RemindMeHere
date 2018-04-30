package id.juliannr.remindmehere.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by juliannr on 19/04/18.
 */

public class DateHelper {

    public static String toDueDate(String oldDate) {
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(oldDate);
            formatter = new SimpleDateFormat("dd MMM, yyyy", new Locale("id"));
            return formatter.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Date fromDueDate(String dueDate){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", new Locale("id"));
            return formatter.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
