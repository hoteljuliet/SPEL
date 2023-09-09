# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing

## Inspiration
This project saw started after years of great experience working with (Logzio Sawmill)[https://github.com/logzio/sawmill], which is an awesome project.


## Design rules: 
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
- https://github.com/Redempt/Crunch
- https://github.com/addthis/stream-lib
- 


# - TODO
## 1. start with their parser and executor
## 2. add refactoring from JPMC
## 3. redo refactoring of each processor/conditional
## 4. simplify everything
## 5. add new processors




