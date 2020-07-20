@video_scenarios
@user_scenarios
@suggested_videos_scenarios
Feature: Recommendation Engine

  Users (userXXX) have their id randomized

  Background:
    Given those users already exist: user1
  
  Scenario: Get related videos
    When user1 submit Youtube videos:
      | id     | name                      | description                        | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4            | Lego Star Wars B-Wing UCS          | lego,police,space    | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | x-wing-ucs.mp4            | Lego 2017 Police Staton            | lego,police          | https://www.youtube.com/watch?v=fpKyfwnkKko |
      | video3 | tie-fighter-ucs.mp4       | Lego fighter for police            | police,pop,eighties  | https://www.youtube.com/watch?v=OMOGaugKpzs |
      | video4 | yesterday.mp4             | The Beattles - Yesterday           | pop,beattles,top     | https://www.youtube.com/watch?v=Ho2e0zvGEWE |
      | video5 | mars-curiosity.mp4        | The Curious Life of a Mars Rover   | space,mars,rover     | https://www.youtube.com/watch?v=7zpojhD4hpI |
    Then target video2 should have related : video1, video3
      
  Scenario: Get suggested videos for user
    When user1 submit Youtube videos:
      | id     | name                      | description                        | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4            | Lego Star Wars B-Wing UCS          | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | 2017-police-station.mp4   | Lego 2017 Police Staton            | lego,police          | https://www.youtube.com/watch?v=fpKyfwnkKko |
      | video3 | every-breath-you-take.mp4 | The Police - Every breath you take | police,pop,eighties  | https://www.youtube.com/watch?v=OMOGaugKpzs |
      | video4 | yesterday.mp4             | The Beattles - Yesterday           | pop,beattles,top     | https://www.youtube.com/watch?v=Ho2e0zvGEWE |
      | video5 | mars-curiosity.mp4        | The Curious Life of a Mars Rover   | space,mars,rover     | https://www.youtube.com/watch?v=7zpojhD4hpI |
    Then user1 who likes video2 should be suggested: video1, video3