package com.kata.acceptance;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.kata.bank.BankApplication;
import com.kata.bank.domain.repository.JpaTransactionRepository;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(
    classes = {BankApplication.class},
    webEnvironment = WebEnvironment.RANDOM_PORT
)
public class BankAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JpaTransactionRepository jpaTransactionRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jpaTransactionRepository.deleteAll();
    }

    @Test
    void getTransactions() {
        requestDeposit(1000);
        requestDeposit(300);

        requestGetTransactions()
            .statusCode(HttpStatus.OK.value())
            .body("transactions", hasSize(2))
            .body("transactions[0].date", notNullValue())
            .body("transactions[0].amount", is(1000))
            .body("transactions[0].type", is("DEPOSIT"))
            .body("transactions[1].date", notNullValue())
            .body("transactions[1].amount", is(300))
            .body("transactions[1].type", is("DEPOSIT"))
            .body("balance", is(1300));
    }

    @Test
    void depositByInvalidAmount() {
        requestDeposit(0)
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void depositByValidAmount() {
        requestDeposit(1)
            .statusCode(HttpStatus.OK.value())
            .body("date", notNullValue())
            .body("amount", is(1))
            .body("type", is("DEPOSIT"));
    }

    @Test
    void withdrawInvalidAmount() {
        requestWithdraw(0)
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void withdrawValidAmount() {
        requestDeposit(1000);

        requestWithdraw(1)
            .statusCode(HttpStatus.OK.value())
            .body("date", notNullValue())
            .body("amount", is(1))
            .body("type", is("WITHDRAW"));
    }

    @Test
    void withdrawOverBalanceAmount() {
        requestWithdraw(1)
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ValidatableResponse requestDeposit(int amount) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("amount", amount))
            .when().log().all()
            .post("/deposit")
            .then().log().all();
    }

    private ValidatableResponse requestWithdraw(int amount) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("amount", amount))
            .when().log().all()
            .post("/withdraw")
            .then().log().all();
    }

    private ValidatableResponse requestGetTransactions() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/transactions")
            .then().log().all();
    }
}
