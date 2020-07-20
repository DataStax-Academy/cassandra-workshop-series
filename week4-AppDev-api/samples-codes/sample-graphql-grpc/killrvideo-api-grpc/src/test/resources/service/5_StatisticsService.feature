@video_scenarios
@user_scenarios
@stats_scenarios
Feature: Video Statistics Management

  Users (userXXX) have their id randomized

  Background:
    Given those users already exist: user1


  Scenario: Record playback and get number of plays
    When user1 submit Youtube videos:
      | id     | name                | description                          | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4      | Lego Star Wars B-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4      | Lego Star Wars Y-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=TRyja74EXp0 |
      | video3 | x-wing-ucs.mp4      | Lego Star Wars X-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=YMJvcYDtYJQ |
      | video4 | tie-fighter-ucs.mp4 | Lego Star Wars Tie Fighter UCS       | lego,star wars,space | https://www.youtube.com/watch?v=Kcph-70owiM |
      | video5 | mil-falcon-ucs.mp4  | Lego Star Wars Millennium Falcon UCS | lego,star wars,space | https://www.youtube.com/watch?v=Q9BhA9POhBQ |
    And video1 is watched 13 times
    And video2 is watched 17 times
    And video3 is watched 9 times
    And video4 is watched 21 times
    And video5 is watched 15 times
    Then video1,video2,video3,video4,video5 statistics shows 13,17,9,21,15 plays





