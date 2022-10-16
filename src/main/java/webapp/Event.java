package webapp;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record Event(String name, Date date, int maxTickets, UUID id) {

    public static Event of(String name, Date date, int maxTickets){
        return new Event(name, date, maxTickets, UUID.randomUUID());
    }
}
