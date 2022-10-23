Feature: Is it Saturday yet?
  Everybody wants to know when it's Saturday

  Scenario: Sunday isn't Saturday
    Given may be today is "Friday"
    When I ask whether it's Saturday yet
    Then I should be told if i can say "Nope"