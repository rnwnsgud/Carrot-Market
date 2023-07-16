package com.guCoding.carrotMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guCoding.carrotMarket.config.dummy.DummyObject;
import com.guCoding.carrotMarket.domain.user.UserRepository;

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

import javax.persistence.EntityManager;

import static com.guCoding.carrotMarket.dto.stuff.StuffReqDto.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@Sql("classpath:db/teardown.sql") // @Transactional 대신 사용 : PK 초기화를 위해 테이블을 날림
@AutoConfigureMockMvc // MockMvc 객체 구성 : 실제 서버 없이 컨트롤러 동작 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class StuffControllerTest extends DummyObject {

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

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void save_stuff_test() throws Exception {
        //given
        StuffSaveReqDto stuffSaveReqDto = new StuffSaveReqDto();
        stuffSaveReqDto.setTitle("TV");
        stuffSaveReqDto.setTransactionEnum("판매하기");
        stuffSaveReqDto.setPrice(10000);
        stuffSaveReqDto.setDescription("중고 tv 팝니다.");
        stuffSaveReqDto.setGettingPriceOffer(true);
        stuffSaveReqDto.setTownEnum("삼산동");

        String requestBody = om.writeValueAsString(stuffSaveReqDto);

        //when
        ResultActions resultActions = mvc
                .perform(post("/api/s/stuff").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void save_stuff_transaction_change_test() throws Exception {
        //given // Transaction 판매하기 -> 나눔하기
        StuffSaveReqDto stuffSaveReqDto = new StuffSaveReqDto();
        stuffSaveReqDto.setTitle("TV");
        stuffSaveReqDto.setTransactionEnum("판매하기");
        stuffSaveReqDto.setPrice(0);
        stuffSaveReqDto.setDescription("중고 tv 팝니다.");
        stuffSaveReqDto.setGettingPriceOffer(true);
        stuffSaveReqDto.setTownEnum("삼산동");

        String requestBody = om.writeValueAsString(stuffSaveReqDto);

        //when
        ResultActions resultActions = mvc
                .perform(post("/api/s/stuff").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        
        //then
        resultActions.andExpect(jsonPath("$.data.transaction").value("나눔하기"));
//        System.out.println("테스트 : " + responseBody);

    }

    @WithUserDetails(value = "01012345678", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void save_stuff_price_change_test() throws Exception {
        //given // price 10000 -> 0
        StuffSaveReqDto stuffSaveReqDto = new StuffSaveReqDto();
        stuffSaveReqDto.setTitle("TV");
        stuffSaveReqDto.setTransactionEnum("나눔하기");
        stuffSaveReqDto.setPrice(10000);
        stuffSaveReqDto.setDescription("중고 tv 팝니다.");
        stuffSaveReqDto.setGettingPriceOffer(true);
        stuffSaveReqDto.setTownEnum("삼산동");

        String requestBody = om.writeValueAsString(stuffSaveReqDto);

        //when
        ResultActions resultActions = mvc
                .perform(post("/api/s/stuff").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        //then
        resultActions.andExpect(jsonPath("$.data.price").value("0"));
//        System.out.println("테스트 : " + responseBody);

    }
}
