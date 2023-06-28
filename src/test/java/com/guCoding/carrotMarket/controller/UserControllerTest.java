package com.guCoding.carrotMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guCoding.carrotMarket.config.dummy.DummyObject;
import com.guCoding.carrotMarket.domain.user.UserRepository;
import com.guCoding.carrotMarket.dto.user.UserReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;

import static com.guCoding.carrotMarket.dto.user.UserReqDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@Sql("classpath:db/teardown.sql") // @Transactional 대신 사용 : PK 초기화를 위해 테이블을 날림
@AutoConfigureMockMvc // MockMvc 객체 구성 : 실제 서버 없이 컨트롤러 동작 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        userRepository.save(newUser(1L, "01012345678", "ssar"));
        em.clear();
    }

    @Test
    public void join_success_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setPhoneNumber("01099999999");
        joinReqDto.setNickname("love");
        joinReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(joinReqDto);
        //when
        ResultActions resultActions = mvc
                .perform(post("/api/users/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void join_fail_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setPhoneNumber("01012345678");
        joinReqDto.setNickname("ssar");
        joinReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(joinReqDto);
        //when
        ResultActions resultActions = mvc
                .perform(post("/api/users/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void WithUserDetails_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(get("/api/s/users"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isOk());
    }

}