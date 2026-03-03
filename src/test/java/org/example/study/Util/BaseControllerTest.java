package org.example.study.Util;

import org.example.study.DTOs.PageResponseDTO;
import org.example.study.DTOs.UserDto;
import org.example.study.controller.UserController;
import org.example.study.enums.Endpoints;
import org.example.study.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.example.study.testData.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
public class BaseControllerTest extends BaseTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper mapper;
    @MockitoBean
    protected UserService service;
    protected PageResponseDTO<UserDto> users;
    protected UserDto user;
    protected UserDto invalidUser;
    protected final Endpoints usersEndpoint = Endpoints.USERS;
    protected String usersJson;
    protected String singleUserJson;
    protected String singleInvalidUserJson;
    protected Steps steps;

    @BeforeEach
    protected void init() {
        users = getValidUserDtoPage();
        user = getSingleValidUser();
        invalidUser = getSingleUserWithEmptyName();

        usersJson = mapper.writeValueAsString(users);
        singleUserJson = mapper.writeValueAsString(user);
        singleInvalidUserJson = mapper.writeValueAsString(invalidUser);

        steps = new Steps(mvc, usersEndpoint);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    protected class Steps {
        private final MockMvc mvc;
        private final Endpoints usersEndpoint; // having usersEndpoint here by default - weird

        protected Steps(MockMvc mvc, Endpoints usersEndpoint) {
            this.mvc = mvc;
            this.usersEndpoint = usersEndpoint;
        }

        public ResultActions mvcPost(String content) throws Exception {
            return mvc.perform(post(usersEndpoint.getEndpoint())
                    .contentType(MediaType.APPLICATION_JSON).content(content));
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
