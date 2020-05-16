package webadv.example.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author IITII
 */
@RestController
public class TaskController {

    final String CONTENT = "17204117";

    @GetMapping("/task/{id}")
    Task task(@PathVariable("id") String title) {
        return new Task(title, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + " "
                + CONTENT
        );
    }
}
