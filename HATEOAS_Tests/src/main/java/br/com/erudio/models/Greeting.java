package br.com.erudio.models;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Greeting extends ResourceSupport {

    private final String content;
    private String foo;
    private String bar;

    @JsonCreator
    public Greeting(String content) {
        this.content = content;
        this.foo = "Foo";
        this.bar = "Bar";
    }

    public String getContent() {
        return content;
    }

	public String getFoo() {
		return foo;
	}

	public String getBar() {
		return bar;
	}
    
    
}
