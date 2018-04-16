package com.example.orost.redditclient.common;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility api
 * Created by o.rostovtseva on 14.04.2018.
 */

public class Utils {
    public static String convertTimeCreated(long time) {
        Date dateTime = new Date(time * 1000);
        StringBuilder sb = new StringBuilder();
        Date current = Calendar.getInstance().getTime();
        int diffInSeconds = (int) ((current.getTime() - dateTime.getTime()) / 1000);

        int sec = (diffInSeconds >= 60) ? (diffInSeconds % 60) : diffInSeconds;
        diffInSeconds /= 60;
        int min = (diffInSeconds >= 60) ? (diffInSeconds % 60) : diffInSeconds;
        diffInSeconds /= 60;
        int hrs = (diffInSeconds >= 24) ? (diffInSeconds % 24) : diffInSeconds;
        diffInSeconds /= 24;
        int days = (diffInSeconds >= 30) ? (diffInSeconds % 30) : diffInSeconds;
        diffInSeconds /= 30;
        int months = (diffInSeconds >= 12) ? (diffInSeconds % 12) : diffInSeconds;
        diffInSeconds /= 12;
        int years = diffInSeconds;

        if (years > 0) {
            if (years == 1) {
                sb.append("a year");
            } else {
                sb.append(" years");
            }
            if (years <= 6 && months > 0) {
                if (months == 1) {
                    sb.append(" and a month");
                } else {
                    sb.append(" and ").append(months).append(" months");
                }
            }
        } else if (months > 0) {
            if (months == 1) {
                sb.append("a month");
            } else {
                sb.append(months).append(" months");
            }
            if (months <= 6 && days > 0) {
                if (days == 1) {
                    sb.append(" and a day");
                } else {
                    sb.append(" and ").append(days).append(" days");
                }
            }
        } else if (days > 0) {
            if (days == 1) {
                sb.append("a day");
            } else {
                sb.append(days).append(" days");
            }
            if (days <= 3 && hrs > 0) {
                if (hrs == 1) {
                    sb.append(" and an hour");
                } else {
                    sb.append(" and ").append(hrs).append("hours");
                }
            }
        } else if (hrs > 0) {
            if (hrs == 1) {
                sb.append("an hour");
            } else {
                sb.append(hrs).append(" hours");
            }
            if (min > 1) {
                sb.append(" and ").append(min).append(" minutes");
            }
        } else if (min > 0) {
            if (min == 1) {
                sb.append("a minute");
            } else {
                sb.append(min).append(" minutes");
            }
            if (sec > 1) {
                sb.append(" and ").append(sec).append(" seconds");
            }
        } else {
            if (sec <= 1) {
                sb.append("about a second");
            } else {
                sb.append("about ").append(sec).append(" seconds");
            }
        }

        sb.append(" ago");

        return sb.toString();
    }
}
