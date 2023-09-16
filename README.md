# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing.

## Inspiration
This project was started after years of great experience working with (Logzio Sawmill)[https://github.com/logzio/sawmill], which is an awesome project and worth checking out.

## Design rules:
- #1 keep everything fast! steps are measured in nanos, and a decent sized pipeline should run in double-digit millis.
- #2 minimal boilerplate in the actual steps, use OO + Jackson
- #3 i'll think of something
- as much as possible, it should look like a bash script in yaml format
- keep the names small
- keep the naming of parameters consistent and simple: source, dest, to, from, dict, list
- when dealing with lists, allow partial success but track each success/failure individually, otherwise its one success/failure
- make more, smaller steps vs bigger ones with lots of options. ~50 lines for a baseStep is normal/average.
- use jackson annotations for all parsing rules, optional fields, etc
- https://en.wikipedia.org/wiki/Principle_of_least_astonishment
- 
- it is ok to do stateful things in Statements/Predicates, so long as 
  - they output their state (can be set by user in a backed-up state object)
  - they can be restored properly (user can checkpoint the entire pipeline)
- 

# TODO
- add expression parsing to crunch, so expressions can be {{a.a + a.b}} and the constructor find/replaces, just like was done for exp4j
- add a command line testing environment, like a shell where users get an empty context but can run commands that are parsed and run as they are entered
- documentation
- move all restore() methods to just below the constructor - will be more obvious if/when an error is made
- move all metrics tracking into the pipeline object (will make serialization simpler, just the pipeline)
- rip out exp4j and just use crunch? crunch is A LOT faster, plus has more built-in
- parse a markdown with yaml blocks
- the ability to serialize and restore the entire pipeline to/from a b64 string
- benchmark tests
- timing performance analysis, this must be fast!
- make everything serializable
- unit tests, including serializable
- https://github.com/topics/expression-parser?l=java
- https://mvnrepository.com/artifact/org.ahocorasick/ahocorasick/0.4.0
- https://github.com/Redempt/Crunch (to replace exp4j)
- https://github.com/addthis/stream-lib
  - output rare and common values for strings
- simplify everything
- add new steps
- look into other DSLs/ELs like SawMill, https://github.com/google/cel-spec, and https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html
- looking into DQ libraries like https://aws.amazon.com/blogs/big-data/test-data-quality-at-scale-with-deequ/
- 