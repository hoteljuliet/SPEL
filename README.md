# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing

## Inspiration
This project was started after years of great experience working with (Logzio Sawmill)[https://github.com/logzio/sawmill], which is an awesome project.

## Design rules:
- keep the names small
- keep the naming of parameters consistent and simple: source, dest, to, from, dict, list
- when dealing with lists, allow partial success but track each success/failure individually, otherwise its one success/fauilure
- make more, smaller steps. < 50 lines each is normal.
- use jackson annotations for all parsing rules, optional fields, etc
- https://en.wikipedia.org/wiki/Principle_of_least_astonishment
- 
- it is ok to do stateful things in Statements/Predicates, so long as 
  - they output their state (can be set by user in a backed-up state object)
  - they can be restored properly (user can checkpoint the entire pipeline)
- 

# TODO
- the ability to serialize and restore the entire pipeline to/from a b64 string
- benchmark tests
- timing performance analysis, this must be fast!
- make everything serializable
- unit tests, including serializable
- https://mvnrepository.com/artifact/org.ahocorasick/ahocorasick/0.4.0
- https://github.com/Redempt/Crunch (to replace exp4j)
- https://github.com/addthis/stream-lib
  - output rare and common values for strings
- simplify everything
- add new steps
- add a command line testing environment, like a shell?
- look into other DSLs/ELs like SawMill, https://github.com/google/cel-spec, and https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html
- looking into DQ libraries like https://aws.amazon.com/blogs/big-data/test-data-quality-at-scale-with-deequ/