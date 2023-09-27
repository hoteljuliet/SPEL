package io.github.hoteljuliet.spel.mustache;

import com.github.mustachejava.*;

import java.util.Collections;

public class SafeMustacheCustomVisitorFactory extends SafeMustacheFactory {
    public SafeMustacheCustomVisitorFactory() {
        super(Collections.emptySet(), "."); // disallow any resource reference
    }
    @Override
    public MustacheVisitor createMustacheVisitor() {
        return new DefaultMustacheVisitor(this) {
            public void pragma(TemplateContext tc, String pragma, String args) {
                throw new MustacheException("Disallowed: pragmas in templates");
            }
        };
    }
}
