# Info

Super basic toy matching engine using Disruptor in Java.

Of course this is super basic example and a TON of things can be optimised. Like the order book pre-allocation etc.
But just an example how to use Disruptor in a bit more non-trivial manner.

- Like obviously we would not write to files and do stdout in the consumers that would be super slow.
- Same with networking etc.
- No allocations (like obviously no 'clone' etc. no 'new' on the hot path). Zero GC ideally. Otherwise makes little sense.
- Did not have time to really add tests although this should have a TON of unit tests. Again - quick one.
- Ideally spinning strategies or at least yield... I mean... there's ton of optimisations to be made :)
- No benchmarks etc. because that makes little point at this time but can discuss.

Again just a bit more than basic example how to use it.

# Build

Generate SBE (make sure you're in correct directory as output is relative):

    java -jar -Dsbe.output.dir=./java/org/fun/generated ~/.m2/repository/uk/co/real-logic/sbe-all/1.33.1/sbe-all-1.33.1.jar resources/sbe-schema.xml

Build & Run:

    mvn clean package exec:java