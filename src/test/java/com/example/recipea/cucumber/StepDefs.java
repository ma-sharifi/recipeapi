package com.example.recipea.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mahdi Sharifi
 */

public class StepDefs {
    private  String today;
    private  String actualAnswer;
    @Given("today is {string}")
    public void today_is(String string) {
        // Write code here that turns the phrase above into concrete actions
        today = string;
    }

    @When("I ask whether it's Friday yet")
    public void i_ask_whether_it_s_friday_yet() {
        actualAnswer = isItFriday(today);
    }

    private String isItFriday(String today) {
        return "Nope";
    }

    @Then("I should be told {string}")
    public void i_should_be_told(String string) {
        assertEquals(string, actualAnswer);
    }
}
