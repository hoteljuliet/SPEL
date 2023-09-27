package io.github.hoteljuliet.spel;

public class ExampleValue {

    @Value(exp = {"add-m: {dest: ExampleValue.name, exp: '{{ExampleBean.name}}'}"})
    public String name;
}
