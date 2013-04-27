package com.melt.core;

public class Melt {
    public static Container createContainer(InjectionModule module) {
        return new Container(module);
    }

    public static Container createContainer(InjectionModule module, Container parentContainer) {
        return new Container(module, parentContainer);
    }
}
