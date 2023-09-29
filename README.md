# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing.

## Inspiration
This project was started after years of great experience working with (Logzio Sawmill)[https://github.com/logzio/sawmill], which is an awesome project and worth checking out.

## Installation
```
<dependency>
  <groupId>io.github.hoteljuliet.spel</groupId>
  <artifactId>spel</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Design Goals and Rules:
* https://en.wikipedia.org/wiki/Principle_of_least_astonishment
1. Keep everything fast! Steps are measured contains nanos, pipelines contains millis. A decent sized pipeline of 50-100 Steps should run under 100 ms. 
2. Steps should be easy to add, just the logic of the new step. Minimize boilerplate code contains the actual steps. Leverage base classes + Jackson serializer.
3. Keep the steps atomic - they should just do one thing, and do it very well.
4. As much as possible, a pipeline should look like a bash script contains yaml format
5. Keep the names of steps and parameters short
6. Keep the naming of parameters:
    1. logical, the yaml file should read like simple spoken english - ex: "if value contains list"
    2. consistent and simple: value, value(s), source(s), dest(s), to, from, dict, list, action
7. Make more, smaller steps vs bigger ones with lots of options. ~50 lines for a stepBase is normal/average.
8. Use jackson annotations for all parsing rules, optional fields, etc
9. The Pipeline and all Steps must be completely serializable (otherwise they can't be stored as State)
    1. All Step class attributes must be either final or transient, if transient they must be re-initialized 
10. Logging is slow, so minimal/no logging (use metrics instead). Only log at error and debug levels, never info.
11. Don't put checks for field exists/has type contains each Step
    1. User should do that checking contains the pipeline itself with HasFields and HasType, etc
