Feature: Is my document correct?

  Verify some aspects of our document

  Scenario: Check expected meta data
    Given the document "./src/test/resources/SampleLetterToTest.pdf"
    When i open the document
    Then i expect 1 pages
    Then i expect "Sample DocumentUnderTest" as title
#    Then i expect the author to be "Robby"

  Scenario: Check some text in the document
    Given the document "./src/test/resources/SampleLetterToTest.pdf"
    When i open the document
    Then i expect 1 pages
    Then i expect text "Dear Ms Johnson:" at page 1
    
  Scenario: Check embedded image data
    Given the document "./src/test/resources/SampleLetterToTest.pdf"
    When i open the document
    Then i expect 1 pages