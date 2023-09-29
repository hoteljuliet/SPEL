# SPEL
A DSL/EL designed for json transforms, configurable business logic, and appropriate for stream processing.

## Concepts
The idea was to allow a config-driven approach to implementing Business Logic in web and stream/batch-processing apps using a simple and fast DSL. 
Something like [Logstash](https://www.elastic.co/guide/en/logstash/current/filter-plugins.html), but with more functionality, and implemented in a way that is fully Serializable, 
incredibly fast, and easy/safe to drop into a Spring Boot webapp or [Apache Flink](https://flink.apache.org/). We also wanted a DSL that looks a lot like a Bash script, or at the 
very least is "easy on the eyes".

We also wanted a DSL that addresses the entire swath of data's lifecycle - unstructured -> structured, un-enriched -> enriched, raw -> canonical/validated, etc.
It was hard to find a DSL that handles operations like sampling, grok, deduplication, data quality checking, enrichment - so we set out to build one!

A central idea to SPEL is that in Java any event/record can be generalized into a Map<String, Object>. This is central to how SPEL works, because 
SPEL [Statements](https://github.com/hoteljuliet/SPEL/tree/main/src/main/java/io/github/hoteljuliet/spel/statements) and
[Predicates](https://github.com/hoteljuliet/SPEL/tree/main/src/main/java/io/github/hoteljuliet/spel/predicates) mostly operator over a Map<String, Object> by either modifying, 
adding, or removing values to/from the Map. A simple [CoR pattern](https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern) glues the entire SPEL pipeline together.

Perhaps best of all, you can create your own Statements/Predicates, and mix/match them with the ones found here. 

SPEL is not Turing Complete by any means, but it does support a set of useful actions (called Statements) and if/else flow control with complex Predicates.
A SPEL pipeline is a YAML document, for example:

```yaml
name: test_pipeline
stopOnFailure: true
config:
  - add: {out: "myString", in: "t"}
```
Running this pipeline would result in a Map<String, Object> that has a new field called "myString", with the value "t".

- ### Env Var Replacement
Statement config strings are checked for a ```${x}``` pattern, are those values are replaced with environment variables.
```yaml
  - add: {out: "user", in: "${USER}"}
```
This Statement would add the value of the Env Var "USER" to the Context.

- ### Metrics 
Every Statement and Predicate in the DSL generates metric about its performance - how long it takes to run, soft/hard failures, etc. Predicates report on how often they 
evaluate to true or false. We have used this small feature to very easily implement in-stream detection of complex conditions. It also makes Data Quality Checks like "Compliance"
really easy.

We implemented metrics in a way that lets the calling application provide Metrics to the DSL. It should be easy to plug into a platform's framework, like 
[Flink's Metrics Subsystem](https://nightlies.apache.org/flink/flink-docs-master/docs/ops/metrics/).

- ### Boolean Logic

If/else blocks let you pivot around values in the Context, for example:
```yaml
  - if:
    matches: {in: "user", regex: ".*truck*"}
    then:
      - add: {out: "matches", in: "user does not match .*truck*"}
```

```yaml
  - if:
    not:
      - matches: {in: "user", regex: ".*truck*"}
    then:
      - add: {out: "matches", in: "user does not match .*truck*"}
```


- ### Serializable
- ### Mermaid Representation
- ### Friendly Names
- ### Complex/Stateful Statements
  - #### Deduplication
  - #### Sorting
  - #### KMeans
  - #### Cardinality



## Installation
### As of September 2023, we're in Maven Central!
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

## Inspiration
This project was started after years of great experience working with [Logzio Sawmill](https://github.com/logzio/sawmill), which is an awesome project and worth checking out.
