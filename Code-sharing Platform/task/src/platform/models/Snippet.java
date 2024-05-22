package platform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Snippet implements Comparable<Snippet>, Cloneable {
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JsonIgnore
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String code;
    private LocalDateTime date;
    private int views;
    private long time;
    @JsonIgnore
    private LocalDateTime expiration;
    @JsonIgnore
    private boolean isTimeRestricted;
    @JsonIgnore
    private boolean isViewRestricted;

    @JsonProperty("date")
    public String getDateAsString() {
        return DATE_TIME_FORMATTER.format(date);
    }

    @JsonProperty("time")
    public void setTime(Long time) {
        this.isTimeRestricted = time > 0;
        this.time = time;
        this.expiration = LocalDateTime.now().plusSeconds(time);
    }

//    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("time")
    public long getTimeAsLong() {
//        long time = Duration.between(LocalDateTime.now(), expiration).toSeconds();
//        return time > 0 ? time : null;
        return isTimeRestricted ? Math.max(Duration.between(LocalDateTime.now(), expiration).toSeconds(), 0) : time;
    }

    @JsonProperty("views")
    public void setViews(Integer visitCountdown) {
        this.views = visitCountdown < 0 ? 0 : visitCountdown;
        this.isViewRestricted = views > 0;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("views")
    public int getViews() {
//        return views > 0 ? views : null;
        return views;
    }

    public void decrementVisitCountdown() {
        this.views--;
    }

    @JsonIgnore
    public boolean isTimeExpired() {
        return isTimeRestricted && !expiration.isAfter(LocalDateTime.now());
    }

    public boolean areViewsExpired() {
        return isViewRestricted && views <= 0;
    }

    @Override
    public int compareTo(Snippet other) {
        return other.date.compareTo(this.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snippet snippet)) return false;
        return Objects.equals(id, snippet.id)
                && Objects.equals(code, snippet.code)
                && Objects.equals(date, snippet.date)
                && Objects.equals(expiration, snippet.expiration)
                && Objects.equals(isTimeRestricted, snippet.isTimeRestricted)
                && Objects.equals(views, snippet.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, date, expiration, isTimeRestricted, views);
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", expiration=" + expiration +
                ", hasTimeout=" + isTimeRestricted +
                ", views=" + views +
                '}';
    }
}
