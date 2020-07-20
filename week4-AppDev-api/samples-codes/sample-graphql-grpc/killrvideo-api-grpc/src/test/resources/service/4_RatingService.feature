@video_scenarios
@user_scenarios
@ratings_scenarios
Feature: Video Rating Management

  Users (userXXX) have their id randomized

  Background:
    Given those users already exist: user1, user2, user3


  Scenario: Get video rating for unrated video
    When user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video1 | b-wing-ucs.mp4 | Lego Star Wars B-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=zcryCEQfTwY |
    Then video1 has 0 ratings and total 0 stars
    And user1 rating for video1 has 0 stars


  Scenario: Rate video and get video rating
    When user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video2 | y-wing-ucs.mp4 | Lego Star Wars Y-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=TRyja74EXp0 |
    And user1 rates video2 4 stars
    And user2 rates video2 2 stars
    And user3 rates video2 5 stars
    Then video2 has 3 ratings and total 11 stars
    And user3 rating for video2 has 5 stars




