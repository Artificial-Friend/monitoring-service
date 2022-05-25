Feature: junit5 test

  Background:
    * url baseUrl

  Scenario: get all monitored endpoints by user
    Given header accessToken = '1337-riddler-1337'
    Given path '/monitored-endpoint/all'
    When method GET
    Then status 200

  Scenario: forbidden access on getting monitored endpoint by non-existed user
    Given header accessToken = 'wrong token'
    Given path '/monitored-endpoint/all'
    When method GET
    Then status 403
