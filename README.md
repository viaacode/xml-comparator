# xml-comparator
Java library that compares two XML fragments and finds the difference.

This is a work in progress. Basic diff functionality works but there are flaws. When a new XML node is added somewhere in the middle of another XML node (see test: FragmentsTest), the comparison result says a new node was created, but thinks it's the last one, and that all nodes after the inserted one changed values.

## Running tests
The JUnit tests can be executed using maven: `mvn test`
