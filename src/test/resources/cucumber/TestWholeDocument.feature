Feature: Is my document correct?

  Verify some meta data

  Scenario: Check expected meta data
    Given the document "./src/test/resources/SampleLetterToTest.pdf"
    When i open the document
    Then i expect "Sample DocumentUnderTest" as title
