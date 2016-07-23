Java Developer Test
===================

I developed a spring boot application that can be started from the command line as
specified in the test definition:

java -jar GoEuroTest.jar "CITY_NAME"

The csv output is writen to a file named "output.csv" on the execution directory.

To keep things simple and limit implementation time I have only written Unit Tests
(no Integration Tests)

Things that could be done
-------------------------
Some things that could be done with more implementation time:

- Use parameter names as input
- Make output file configurable
- Create a simple web interface as UI
- Add an Integration Test