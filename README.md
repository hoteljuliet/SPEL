# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing.

## Inspiration
This project was started after years of great experience working with (Logzio Sawmill)[https://github.com/logzio/sawmill], which is an awesome project and worth checking out.

## Design Goals and Rules:
1. https://en.wikipedia.org/wiki/Principle_of_least_astonishment
2. Keep everything fast! Steps are measured in nanos, pipelines in millis. A decent sized pipeline of 50-100 Steps should run in < 100 ms. 
3. We want to constantly improve the speed/efficiency of the steps 
4. Steps should be easy to add, just the logic of the new step. Minimize boilerplate code in the actual steps. Leverage base classes + Jackson serializer.
5. Keep the steps atomic - they should just do one thing, and do it very well.
6. As much as possible, a pipeline should look like a bash script in yaml format
7. Keep the names of steps and parameters short
8. Keep the naming of parameters consistent and simple: source(s), dest(s), to, from, dict, list, action (which is an enumeration of common actions)
9. Make more, smaller steps vs bigger ones with lots of options. ~50 lines for a baseStep is normal/average.
10. Use jackson annotations for all parsing rules, optional fields, etc
11. It's fine/great to do stateful things in Statements/Predicates, so long as they
  - don't require too much RAM
  - output their state (i.e., to something like Flink's ValueState - aka stream state) *OR*
  - they can be snapshot + restored (i.e., to/from something like a Flink ListState - aka operator state)
 