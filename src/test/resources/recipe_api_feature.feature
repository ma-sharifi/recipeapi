Feature: Recipe API
  It execute query

  Scenario: Cook rice on fire is not a Vegetarian Recipe
    Given recipe is "Kabab"
    When I ask whether it's Vegetarian
    Then I should be said "No"

#  https://www.toolsqa.com/rest-assured/rest-api-test-in-cucumber/
#  Get List of available books in the library
#  Add a book from the list to the user


#    Given A list of books are available
#
#    When I add a book to my reading list
#
#    Then the book is added