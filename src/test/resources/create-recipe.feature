Feature: Create Recipe

  Scenario: WITH ALL REQUIRED FIELDS IS SUCCESSFUL

  --------------------------------------------------------------------------------
  IMPORTANT NOTE - Field name in Recipe.java is used as DataTable column header.
  --------------------------------------------------------------------------------

    Given user wants to create an recipe with the following attributes
      | id  | title | instruction | serve |
      | 100 | cake1 | Cook cake with stove  | 2 |

    And with the following ingredients
      | id  | title   |
      | 102 | oil1 |
      | 103 | Salt1 |

    When user saves the new recipe 'WITH ALL REQUIRED FIELDS'
    Then the save 'IS SUCCESSFUL'
    