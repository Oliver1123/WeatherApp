/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oliver.weatherapp.utils;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Class for handling date conversions that are useful for Sunshine.
 */
public abstract class DateUtils {

    /* Milliseconds in a day */
    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    /**
     * This method returns the number of milliseconds (UTC time) for today's date at midnight in
     * the local time zone. For example, if you live in California and the day is September 20th,
     * 2016 and it is 6:30 PM, it will return 1474329600000. Now, if you plug this number into an
     * Epoch time converter, you may be confused that it tells you this time stamp represents 8:00
     * PM on September 19th local time, rather than September 20th. We're concerned with the GMT
     * date here though, which is correct, stating September 20th, 2016 at midnight.
     * <p>
     * As another example, if you are in Hong Kong and the day is September 20th, 2016 and it is
     * 6:30 PM, this method will return 1474329600000. Again, if you plug this number into an Epoch
     * time converter, you won't get midnight for your local time zone. Just keep in mind that we
     * are just looking at the GMT date here.
     * <p>
     * This method will ALWAYS return the date at midnight (in GMT time) for the time zone you
     * are currently in. In other words, the GMT date will always represent your date.
     * <p>
     * Since UTC / GMT time are the standard for all time zones in the world, we use it to
     * normalize our dates that are stored in the database. When we extract values from the
     * database, we adjust for the current time zone using time zone offsets.
     *
     * @return The number of milliseconds (UTC / GMT) for today's date at midnight in the local
     * time zone
     */
    public static long getNormalizedUtcMsForToday() {

        /*
         * This number represents the number of milliseconds that have elapsed since January
         * 1st, 1970 at midnight in the GMT time zone.
         */
        long utcNowMillis = System.currentTimeMillis();

        /*
         * This TimeZone represents the device's current time zone. It provides us with a means
         * of acquiring the offset for local time from a UTC time stamp.
         */
        TimeZone currentTimeZone = TimeZone.getDefault();

        /*
         * The getOffset method returns the number of milliseconds to add to UTC time to get the
         * elapsed time since the epoch for our current time zone. We pass the current UTC time
         * into this method so it can determine changes to account for daylight savings time.
         */
        long gmtOffsetMillis = currentTimeZone.getOffset(utcNowMillis);

        /*
         * UTC time is measured in milliseconds from January 1, 1970 at midnight from the GMT
         * time zone. Depending on your time zone, the time since January 1, 1970 at midnight (GMT)
         * will be greater or smaller. This variable represents the number of milliseconds since
         * January 1, 1970 (GMT) time.
         */
        long timeSinceEpochLocalTimeMillis = utcNowMillis + gmtOffsetMillis;

        /* This method simply converts milliseconds to days, disregarding any fractional days */
        long daysSinceEpochLocal = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis);

        /*
         * Finally, we convert back to milliseconds. This time stamp represents today's date at
         * midnight in GMT time. We will need to account for local time zone offsets when
         * extracting this information from the database.
         */

        return TimeUnit.DAYS.toMillis(daysSinceEpochLocal);
    }


    public static Date getNormalizedUtcDateForToday() {
        long normalizedMilli = getNormalizedUtcMsForToday();
        return new Date(normalizedMilli);
    }

}