@video_scenarios
@user_scenarios
@search_scenarios
Feature: Video Search Management

  Users (userXXX) have their id randomized

  Background:
    Given those users already exist: user1

  Scenario: Search video by tag
    When user1 submit Youtube videos:
      | id     | name                      | description                        | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4            | Lego Star Wars B-Wing UCS          | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | 2017-police-station.mp4   | Lego 2017 Police Staton            | lego,police          | https://www.youtube.com/watch?v=fpKyfwnkKko |
      | video3 | every-breath-you-take.mp4 | The Police - Every breath you take | police,pop,eighties  | https://www.youtube.com/watch?v=OMOGaugKpzs |
      | video4 | yesterday.mp4             | The Beattles - Yesterday           | pop,beattles,top     | https://www.youtube.com/watch?v=Ho2e0zvGEWE |
    Then searching videos with tag police gives: video2, video3


  Scenario: Get tags suggestions
    When user1 submit Youtube videos:
      | id     | name                      | description                        | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4            | Lego Star Wars B-Wing UCS          | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | 2017-police-station.mp4   | Lego 2017 Police Staton            | lego,police          | https://www.youtube.com/watch?v=fpKyfwnkKko |
      | video3 | every-breath-you-take.mp4 | The Police - Every breath you take | police,pop,eighties  | https://www.youtube.com/watch?v=OMOGaugKpzs |
      | video4 | yesterday.mp4             | The Beattles - Yesterday           | pop,beattles,top     | https://www.youtube.com/watch?v=Ho2e0zvGEWE |
    Then I should be suggested tags police,pop for the word po
