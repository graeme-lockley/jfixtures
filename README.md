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

Initially when I have my database underneath, the data is not created for the test but rather cherry picked from an
existing running system - anonymised of course - however these tests remain brittle and the air around the development
team is often punctuated with expletives when a formally passing test suddenly fails.  Following that my tests then start to
make use of test fixtures - data that is setup for a specific test.

In order to make this easy I have pulled together a couple of tools to assist.

This project is what I use.


Example
-------

Let's use an example to illustrate how jfixtures can be used.  We wish to create a test which is dependent on a table
being in place.  The table in question is called PEOPLE with an autoincrement ID and a second field NAME to store the person's
name.  For each test I need this table to be created and two rows to be inserted.  The other bit of info is that the database
to be used is an H2 in-memory database - this will make the setup on each test as fast as is possible.

This following fixture file supports this requirement:

    - use-handler: com.no9.jfixture.JDBCHandler

    - jdbc-connect:
        driver: org.h2.Driver
        url: 'jdbc:h2:mem:'
        username: sa
        autoclose: false

    - jdbc-create-table:
        name: people
        fields:
            - id: 'bigint not null primary key auto_increment'
            - name: 'varchar(50)'

    - jdbc-insert:
        name: people
        rows: [{name: Graeme}, {name: Tim}]

As you can see the jfixture input is a YAML sequence where each sequence element is a command with a set of associated
arguments.  The commands used in this fixture are:

- use-handler: adds a handler to the set of available handlers that this fixture has access to.
- jdbc-connect: opens a JDBC database connection to the named database.  In this example the driver is the H2 driver and the
    URL refers to an in-memory database.
- jdbc-create-table: creates a table with the passed name and the passed fields.
- jdbc-insert: inserts rows into the named table using the values contained within the rows sequence.  The ID is not passed
    as it is automatically assigned a value.

A couple of things to note:

- I have used this technique using H2 even when the application that I am testing is deployed on an Oracle database.
- The parameter autoclose has been set to false in the jdbc-connect.  This is to ensure that when the fixture has run
    to completion the database is not automatically closed and, is it is an in-memory database, the contents would therefore
    be lost.  In the 'unit' test code below you will note that after the fixture has been run in the database connection
    is then retrieved from the JDBC handler and used to interrogate the database.
- I very seldom add primary key and foreign key constraints unless I specifically need these constraints to test the application - I try
    and keep my fixtures as small and simple as possible given that they are executed on every test.

In order to use this fixture drop it in the resources directory off of test called "fixtures.ymal".  The following is a
junit 4 test that makes use of this file:

    class MyTest {
        Fixtures fixtures = Fixtures.process(FixturesInput.fromLocation("resource:fixtures.yaml"));

        @Test
        public void should_test_people_present() throws Exception {
            SQLConnection connection = fixtures.getHandler(JDBCHandler.class).connection();

            ...test against connection...
        }
    }

Handler Reference
-----------------

echo

Displays the content of the parameter to the console.  Used for tracking progress in a fixture's execution.

Example:

    - echo: Some or other message

    - echo
        - Message line 1
        - Message line 2

The purpose of this command is for reference only as it adds a time overhead to the processes of fixtures.

include

Import a sequence of commands from another file into the fixture. Typically this is used to hold common schema and lookup
table definitions before inserting data specific to a test scenario.

Example:

    - include: resource:test/connection.yaml

Includes all of the commands within the file test/connection.yaml off of the test resources directory into the current fixture.

jdbc-connect

Connects to a database using JDBC.

Parameters:

- driver: the class name of the JDBC driver that is to be used.  The JDBC driver will need to be included in the class path
    prior to this command.
- url: the URL used to connect to the desired database using the above driver.
- username: an optional parameter that is used as the user name to the database connection.  If this parameter is not supplied
    then an empty string is passed.
- password: an optional parameter that is used as the password to the database connection.  If this parameter is not supplied
    then an empty string is passed.
- autoclose: an optional parameter that, if set, will automatically close the database connection at the end of the completed
    fixture.  This parameter defaults to true.

Example:

    - jdbc-connect:
        driver: org.h2.Driver
        url: 'jdbc:h2:mem:'
        username: sa

Note that given that fixtures are described using YAML the above example can be compressed onto a single line as shown below.

    - jdbc-connect: {driver: org.h2.Driver, url: 'jdbc:h2:mem:', username: sa}


jdbc-create-table


jdbc-insert

jdbc-sql

use-handler
