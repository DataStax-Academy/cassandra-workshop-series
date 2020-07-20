@video_scenarios
@user_scenarios
@video_scenarios_only
Feature: Video Catalog Management

  Users (userXXX) have their id randomized.

  Background:
    Given those users already exist: user1, user2


  Scenario: Submit youtube video and retrieve it by id
    When user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video1 | b-wing-ucs.mp4 | Lego Star Wars B-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=zcryCEQfTwY |
    Then I can retrieve video1 by id


  Scenario: Submit youtube videos and get their preview
    When user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video1 | b-wing-ucs.mp4 | Lego Star Wars B-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4 | Lego Star Wars Y-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=TRyja74EXp0 |
    Then I can get preview of: video1, video2

  Scenario: user1 submits youtube videos and see them in his videos preview
    When user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video1 | b-wing-ucs.mp4 | Lego Star Wars B-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4 | Lego Star Wars Y-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=TRyja74EXp0 |
    Then user1 videos preview contains: video2, video1

  Scenario: Submit youtube videos and see them in latest videos preview
    When user1 submit Youtube videos:
      | id     | name           | description               | tags                | url                                         |
      | video1 | b-wing-ucs.mp4 | Lego Star Wars B-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4 | Lego Star Wars Y-Wing UCS | lego,star wars,space| https://www.youtube.com/watch?v=TRyja74EXp0 |
    Then latest videos preview contains: video2, video1


  Scenario: Submit youtube videos and get latest videos with paging using starting video id & added date
    #Page size = 2
    When user1 submit Youtube videos:
      | id     | name                | description                          | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4      | Lego Star Wars B-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4      | Lego Star Wars Y-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=TRyja74EXp0 |
      | video3 | x-wing-ucs.mp4      | Lego Star Wars X-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=YMJvcYDtYJQ |
      | video4 | tie-fighter-ucs.mp4 | Lego Star Wars Tie Fighter UCS       | lego,star wars,space | https://www.youtube.com/watch?v=Kcph-70owiM |
      | video5 | mil-falcon-ucs.mp4  | Lego Star Wars Millennium Falcon UCS | lego,star wars,space | https://www.youtube.com/watch?v=Q9BhA9POhBQ |
    Then latest videos preview starting from video3 contains: video3, video2


  Scenario: Submit youtube videos and get latest videos with paging using paging state
    #page size = 3 so that page 1 contains video5, video4 & video3, page 2 contains video2 & video1 (DESC ORDER)
    When user1 submit Youtube videos:
      | id     | name                | description                          | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4      | Lego Star Wars B-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4      | Lego Star Wars Y-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=TRyja74EXp0 |
      | video3 | x-wing-ucs.mp4      | Lego Star Wars X-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=YMJvcYDtYJQ |
      | video4 | tie-fighter-ucs.mp4 | Lego Star Wars Tie Fighter UCS       | lego,star wars,space | https://www.youtube.com/watch?v=Kcph-70owiM |
      | video5 | mil-falcon-ucs.mp4  | Lego Star Wars Millennium Falcon UCS | lego,star wars,space | https://www.youtube.com/watch?v=Q9BhA9POhBQ |
    Then latest videos preview at page 2 contains: video2, video1


  Scenario: user1 submits youtube videos and get see them in his videos preview with paging using starting video id & added date
    #page size = 2
    When user1 submit Youtube videos:
      | id     | name                | description                          | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4      | Lego Star Wars B-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4      | Lego Star Wars Y-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=TRyja74EXp0 |
      | video3 | x-wing-ucs.mp4      | Lego Star Wars X-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=YMJvcYDtYJQ |
      | video4 | tie-fighter-ucs.mp4 | Lego Star Wars Tie Fighter UCS       | lego,star wars,space | https://www.youtube.com/watch?v=Kcph-70owiM |
      | video5 | mil-falcon-ucs.mp4  | Lego Star Wars Millennium Falcon UCS | lego,star wars,space | https://www.youtube.com/watch?v=Q9BhA9POhBQ |
    Then user1 videos preview starting from video3 contains: video3, video2


  Scenario: user1 submits youtube videos and get see them in his videos preview with paging state
    #page size = 2 so that page 1 contains video5 & video4, page 2 contains video3 & video2 ... (DESC ORDER)
    When user1 submit Youtube videos:
      | id     | name                | description                          | tags                 | url                                         |
      | video1 | b-wing-ucs.mp4      | Lego Star Wars B-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=zcryCEQfTwY |
      | video2 | y-wing-ucs.mp4      | Lego Star Wars Y-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=TRyja74EXp0 |
      | video3 | x-wing-ucs.mp4      | Lego Star Wars X-Wing UCS            | lego,star wars,space | https://www.youtube.com/watch?v=YMJvcYDtYJQ |
      | video4 | tie-fighter-ucs.mp4 | Lego Star Wars Tie Fighter UCS       | lego,star wars,space | https://www.youtube.com/watch?v=Kcph-70owiM |
      | video5 | mil-falcon-ucs.mp4  | Lego Star Wars Millennium Falcon UCS | lego,star wars,space | https://www.youtube.com/watch?v=Q9BhA9POhBQ |
    Then user1 videos preview at page 3 contains: video1