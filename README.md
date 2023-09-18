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
9. Make more, smaller steps vs bigger ones with lots of options. ~50 lines for a stepBase is normal/average.
10. Use jackson annotations for all parsing rules, optional fields, etc
11. Steps must be completely serializable (otherwise they can't be stored in a Value State)
12. All Step class attributes must be either final or transient, if transient they must be re-initialized 
13. minimize logging (use metrics instead). Only log at error and debug levels, never info.
14. When dealing with Step's state:
  - most Steps don't have state, although they might create state (by outputting something to _state). Ex: add-m can add a field under _state.
  - examples of state used by Steps: HyperLogLog, SummaryStatistics, TDigest
  - Steps can't have State that is non-Serializable 
  - if a Step has non-Serializable fields (that are not state) just check for null and re-initialize in doExecute()
  - if a Step has Serializable fields (that are state) use the "externalize" methods
  - Steps will piggyback on Flink's state mechanisms, (i.e.,their data will be stored/fetched to/from something like a Flink ListState or ValueState)
    - if a keyed operator, then MapState else ListState
  - "volatile" state will be cleared by an operator when _clear is true. For example: in a windowed function doing de-duplication - we'd want to clear the Bloom Filter on windowClose(), so we would store the filter in volatile state.
