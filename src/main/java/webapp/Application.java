package webapp;

import com.google.gson.JsonObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@Controller
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //startDate=2000-10-31
    //http://localhost:8080/addevent?name=aa&startDate=2000-10-31
    @GetMapping("/addevent")
    public String addEvent(@RequestParam(name = "name") String name,
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                           @RequestParam("startDate") Date date,
                           @RequestParam(name = "tickets") int tickets,
                           Model model) {

        Event event = Event.of(name, date, tickets);
        if(EventsManager.addEvent(event)){
            var json = new JsonObject();
            json.addProperty("id", event.id()+"");
            model.addAttribute("text", json);
        }else{
            model.addAttribute("text", "error: event on date "+date+" already exists");
        }
        return "template";
    }


    //http://localhost:8080/addevent?name=aa&c=1&a=3
    @GetMapping("/test")
    public String about(@RequestParam(name = "name", required = true) String eventName,
                        //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                        //@RequestParam("startDate") LocalDate startDate,
                        Test startDate,

                        Model model) {

        String aboutUs = null;
        if (aboutUs == null) {
            model.addAttribute("test", "Could not fetch AboutUs.." + startDate + eventName);
        } else {
            model.addAttribute("about", aboutUs);
        }
        return "test";
    }


    @GetMapping("/contact")
    public String contact(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {

        String contactUs = null;
        if (contactUs == null) {
            model.addAttribute("contact", "Could not fetch contactUs..");
        } else {
            model.addAttribute("contact", contactUs);
        }
        return "contact";
    }


    public record Test(String a, String c) {
    }

    public static class StringToTestConverter implements Converter<String, Test> {
        @Override
        public Test convert(String source) {
            return new Test(source, source);
        }
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new StringToTestConverter());
        }
    }
}
