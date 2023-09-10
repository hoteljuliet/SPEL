# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing

## Inspiration
This project was started after years of great experience working with (Logzio Sawmill)[https://github.com/logzio/sawmill], which is an awesome project and worth checking out.

## Design rules:
- #1 keep everything fast!
- as much as possible, it should look like a bash script in yaml format
- keep the names small
- keep the naming of parameters consistent and simple: source, dest, to, from, dict, list
- when dealing with lists, allow partial success but track each success/failure individually, otherwise its one success/failure
- make more, smaller steps vs bigger ones with lots of options. ~50 lines for a step is normal/average.
- use jackson annotations for all parsing rules, optional fields, etc
- https://en.wikipedia.org/wiki/Principle_of_least_astonishment
- 
- it is ok to do stateful things in Statements/Predicates, so long as 
  - they output their state (can be set by user in a backed-up state object)
  - they can be restored properly (user can checkpoint the entire pipeline)
- 

# TODO
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
- add a command line testing environment, like a shell?
- look into other DSLs/ELs like SawMill, https://github.com/google/cel-spec, and https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html
- looking into DQ libraries like https://aws.amazon.com/blogs/big-data/test-data-quality-at-scale-with-deequ/
- 