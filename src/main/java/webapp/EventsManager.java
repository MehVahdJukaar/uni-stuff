package webapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventsManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File("events.json");

    public static boolean addEvent(Event e) {
        var schedule = loadFile();
        if(schedule.eventMap == null){
            schedule = EventSchedule.empty();
        }
        if (schedule.eventMap.containsKey(e.date())) {
            return false;
        }
        schedule.eventMap.put(e.date(), e);
        saveFile(schedule);
        return true;
    }

    public static EventSchedule loadFile() {
        if (FILE.exists() && FILE.isFile()) {
            try (FileInputStream fileInputStream = new FileInputStream(FILE);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {

                return GSON.fromJson(inputStreamReader, EventSchedule.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load events", e);
            }
        }
        throw new RuntimeException("Failed to load events");
    }

    public static void saveFile(EventSchedule schedule) {
        try (FileOutputStream stream = new FileOutputStream(FILE);
             Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            GSON.toJson(schedule, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save events", e);
        }
    }

    public record EventSchedule(Map<Date, Event> eventMap) {
        public static EventSchedule empty(){
            return new EventSchedule(new HashMap<>());
        }
    }
}
