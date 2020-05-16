package webadv.example;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import webadv.example.rest.Task;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/task/GITHUB_IITII"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", containsString("17204117")))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String jsonstr = result.getResponse().getContentAsString();
		Task task = mapper.readValue(jsonstr, Task.class);
		assertNotNull(task);
    }
}
