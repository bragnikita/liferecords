package jp.bragnikita.liferecords.backend.services;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayKey implements StorageDayId {
    private String year;
    private String month;
    private String day;

    private static Pattern PARAMETER_REGEX = Pattern.compile("(\\d{4})_?(\\d{2})_?/?(\\d{2})");

    private DayKey(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getStoragePath() {
        return String.format("%s_%s/%s",
                this.year, this.month, this.day);
    }

    public static DayKey fromParameter(String parameter) throws IllegalArgumentException {
        Matcher m = PARAMETER_REGEX.matcher(parameter);
        if (!m.matches()) {
            throw new IllegalArgumentException(parameter);
        }
        return new DayKey(m.group(1), m.group(2), m.group(3));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayKey dayKey = (DayKey) o;
        return year.equals(dayKey.year) &&
                month.equals(dayKey.month) &&
                day.equals(dayKey.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String getStorageDayPath() {
        return String.format("%s_%s/%s",
                this.year, this.month, this.day);
    }

    @Override
    public String getStorageDayId() {
        return StringUtils.join(year,month,day);
    }

    public Path resolveDir(Path root) {
        return root.resolve(this.getStorageDayPath());
    }
    public Path resolveDir(String root) {
        return Path.of(root).resolve(this.getStorageDayPath());
    }

    public String getMonthFolderName() {
        return String.format("%s_%s",
                this.year, this.month);
    }

    public String getDayFolderName() {
        return this.day;
    }

}
