package in.vineetsirohi.utility;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vineet on 10/10/13.
 */
public class CommonUtils {

    public static String getHumanReadableDate(Calendar cal) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        dateFormat.setCalendar(cal);
        return dateFormat.format(new Date(0));
    }

    public static <T> boolean isArrayEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isArrayEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static ObjectMapper getNonFailingObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
