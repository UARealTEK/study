package org.example.study.BaseTestPages;

import org.example.study.DTOs.UserDto;
import org.example.study.controller.UserController;
import org.example.study.enums.Endpoints;
import org.example.study.service.BorrowService;
import org.example.study.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
public abstract class BaseUserControllerTest extends BaseControllerTest {

    @Autowired
    protected MockMvc mvc;
    @MockitoBean
    protected UserService service;
    @MockitoBean
    protected BorrowService borrowService;
    protected final Endpoints usersEndpoint = Endpoints.USERS;
    protected Steps steps;

    //TODO: look into this chain of initialization. make sure it works
    // Look into how JUnit works with ParameterResolver and how it uses declared fields in those custom resolvers
    @BeforeEach
    protected void init() {
        steps = new Steps(mvc, usersEndpoint);
    }

    @AfterEach
    protected void cleanUp() {
        steps = null;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    protected class Steps {
        private final MockMvc mvc;
        private final Endpoints usersEndpoint; // having usersEndpoint here by default - problematic if we want to test other endpoints in the future

        protected Steps(MockMvc mvc, Endpoints usersEndpoint) {
            this.mvc = mvc;
            this.usersEndpoint = usersEndpoint;
        }

        public ResultActions mvcPost(UserDto content) throws Exception {
            return mvc.perform(post(usersEndpoint.getEndpoint())
                    .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(content)));
        }

        //Default Endpoint -> prefilled in this class
        public ResultActions mvcGet() throws Exception {
            return mvc.perform(get(usersEndpoint.getEndpoint()));
        }

        public ResultActions mvcGet(Endpoints endpoints) throws Exception {
            return mvc.perform(get(endpoints.getEndpoint()));
        }

        public ResultActions mvcGet(Long id) throws Exception {
            return mvc.perform(get(usersEndpoint.getEndpoint() + "/" + id));
        }

        public ResultActions mvcGet(Map<String,String> params) throws Exception {
            var request = get(usersEndpoint.getEndpoint());

            for (Map.Entry<String, String> entry : params.entrySet()) {
                request.param(entry.getKey(),entry.getValue());
            }
            return mvc.perform(request);
        }

        public ResultActions mvcDelete(Long id) throws Exception {
            return mvc.perform(delete(usersEndpoint.getEndpoint() + "/" + id));
        }

        public ResultActions mvcPut(Long id, UserDto content) throws Exception {
            return mvc.perform(put(usersEndpoint.getEndpoint() + "/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(content)));
        }
    }
}
