@user_scenarios
@user_scenarios_only
Feature: User Management

  Scenario: Creating a non-existing user
    Given user with email johndoe@gmail.com does not exist
    When I create 1 users with email johndoe@gmail.com and password 123abc
    Then I should be able to login with johndoe@gmail.com/123abc

  Scenario: Fails creating an existing user
    Given user with credentials helensue@gmail.com/456def already exists
    When I create 1 users with email helensue@gmail.com and password 123abc
    Then I receive the 'Exception creating user because it already exists' error message for helensue@gmail.com account

  Scenario: Concurrent users creation
    Given user with email richarsmith@gmail.com does not exist
    When I create 2 users with email richarsmith@gmail.com and password 123abc
    Then I receive the 'Exception creating user because it already exists' error message for richarsmith@gmail.com account

  Scenario: Get user profile
    Given user with credentials janedoe@gmail.com/789ghi already exists
    When I get profile of janedoe@gmail.com
    Then the profile janedoe@gmail.com exists