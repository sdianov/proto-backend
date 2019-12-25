package com.example.demo.util

class QueryExpression {

    String code;

    QueryExpression(String queryText) {
        println(">>> " + queryText);

        if (queryText == null || queryText.isEmpty()) {
            queryText = " true ";
        }
        code = "{->$queryText}";
    }

    boolean matches(Object object) {
        def binding = new Binding();
        def closure = new GroovyShell(binding).evaluate(code) as Closure;

        closure.delegate = object;
        def res = closure();
    }
}
