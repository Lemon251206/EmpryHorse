package nade.empty.horse.utils.strings;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class DateFormat {
    public static String format(Date date) {
        Date current = new Date();
        if (date.getTime() <= current.getTime()) {
            return "0 giây";
        }
        return format((date.getTime() - current.getTime()) / 1000);
    }

    public static String format(long totalSeconds) {
        long days = totalSeconds / (24 * 3600);
        long dayRemainingSeconds = totalSeconds % (24 * 3600);

        long hours = dayRemainingSeconds / 3600;
        long remainingSeconds = dayRemainingSeconds % 3600;

        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;

        List<String> format = Lists.newLinkedList();
        if (days > 0) {
            format.add(days + " ngày");
        } 
        if (hours > 0) {
            format.add(hours + " giờ");
        } 
        if (minutes > 0) {
            format.add(minutes + " phút");
        } 
        if (seconds > 0) {
            format.add(seconds + " giây");
        }
        if (format.size() <= 0) {
            format.add(0 + " giây");
        }

        return format.toString().replace("[", "").replace("]", "");
    }
}
