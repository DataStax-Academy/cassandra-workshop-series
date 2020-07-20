@video_scenarios
@user_scenarios
@comments_scenarios
Feature: Comments Management

  Users (userXXX) have their id randomized.

  Background:
    Given those users already exist: user1, user2
    And user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video1 | b-wing-ucs.mp4 | Lego Star Wars B-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=zcryCEQfTwY |
    And user2 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video2 | y-wing-ucs.mp4 | Lego Star Wars Y-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=TRyja74EXp0 |


  Scenario: Creating a comment on a video and see it on video comments page
    #video1 is created by user1
    When we have the following comments:
      | user  | video  | comment                 |
      | user1 | video1 | This Y-Wing is awesome! |
    Then I can see the comment 'This Y-Wing is awesome!' on video1


  Scenario: Creating a comment on a video and see it on user comments page
      #video1 is created by user1
    When we have the following comments:
      | user  | video  | comment                 |
      | user1 | video1 | This Y-Wing is awesome! |
    Then user1 can see the comment 'This Y-Wing is awesome!' on his own comments


  Scenario: Creating a comment on a video and see it with paging on video comments
    #video2 is created by user2.
    #To test paging, we hard-coded page size == 1 so that it is easier to reason about.
    #For ex: 2nd comment = 2nd page, 3rd comment = 3rd page etc ...
    When we have the following comments:
      | user  | video  | comment                                           |
      | user2 | video2 | This is my own B-Wing UCS. Cost me a lot of bucks |
      | user1 | video2 | This B-Wing is really big                         |
      | user2 | video2 | Almost 60cm long!                                 |
      | user1 | video2 | Love the design of the cockpit                    |
    Then I can see the comment 'Love the design of the cockpit' on video2 at page 4


  Scenario: Creating a comment on a video and see it with paging on user comments
    #video2 is created by user2.
    #To test paging, we hard-coded page size == 1 so that it is easier to reason about.
    #For ex: 2nd comment = 2nd page, 3rd comment = 3rd page etc ...
    When we have the following comments:
      | user  | video  | comment                                           |
      | user2 | video2 | This is my own B-Wing UCS. Cost me a lot of bucks |
      | user1 | video2 | This B-Wing is really big                         |
      | user2 | video2 | Almost 60cm long!                                 |
      | user1 | video2 | Love the design of the cockpit                    |
    Then user2 can see the comment 'Almost 60cm long!' on his own comments at page 2