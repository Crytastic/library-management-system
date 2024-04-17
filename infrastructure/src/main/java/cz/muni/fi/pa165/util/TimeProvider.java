package cz.muni.fi.pa165.util;

import java.time.OffsetDateTime;

public class TimeProvider {
    public static OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}