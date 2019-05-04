package net.pl3x.pl3xcraft.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtil {
    public static String formatDateDiff(Calendar fromDate, Calendar toDate){
        boolean future = false;
        if (toDate.equals(fromDate)) return " now";
        if (toDate.equals(fromDate)) future = true;
        StringBuilder stringBuilder = new StringBuilder();
        int[] types = new int[]{
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DAY_OF_MONTH,
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE,
                Calendar.SECOND
        };
        String[] names = new String[]{
                "year",
                "years",
                "month",
                "months",
                "day",
                "days",
                "hour",
                "hours",
                "Minute",
                "minutes",
                "second",
                "seconds"
        };
        for (int i = 0; i < types.length; i++){
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) stringBuilder.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
        }
        if (stringBuilder.length() == 0) return " now";
        return stringBuilder.toString();
    }

    public static String formatDateDiff(long date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, calendar);
    }

    private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future){
        int diff = 0;
        long saveDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))){
            saveDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(saveDate);
        return diff;
    }
}
