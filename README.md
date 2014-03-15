jfixtures
=========

Having spent some time in the rails and play world I missed a simple yet effective fixtures tool to load data into 
Java systems that I work on.
Generally these applications are legacy making it very difficult, off the bat, to create 
isolated unit tests.

My approach then is typically:

- Start off creating exploratory tests,
- Evolve these exploratory tests into integration tests - across multiple systems,
- Evolve these integration tests into in application integration tests - clipping all of the outbound integration
  points leaving the application isolated from 3rd party systems other than the database, and, finally,
- Through refactoring and constantly chipping away at the monolith, unit tests.

Initially when I have my database underneath the data is not created for the test but rather cherry picked from an
existing running system - annonomised of course - however these tests remain brittle and the air around the development
is often punctuated with explitives when an passing test suddenly fails.  Following that my tests then start to
make use of test fixtures - data that is setup for a specific test.

In order to make this easy I have pulled together a couple of tools to assist.

This project is what I use.
