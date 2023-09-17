package net.hoteljuliet.spel;

public class ExampleValue {

    @Value(exp = {"add-m: {dest: ExampleValue.name, exp: '{{_input.ExampleBean.name}}'}"})
    public String name;
}
