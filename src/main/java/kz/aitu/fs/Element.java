package kz.aitu.fs;

public interface Element {

    String name();

    void accept(Visitor visitor);
}