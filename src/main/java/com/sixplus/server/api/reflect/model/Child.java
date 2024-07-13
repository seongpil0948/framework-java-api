package com.sixplus.server.api.reflect.model;

public class Child extends Parent {
    public String cstr1 = "1";
    private String cstr2 = "2";

    public Child() {
    }

    private Child(String str) {
        cstr1 = str;
    }

    public int method4(int n) {
        System.out.println("method4: " + n);
        return n;
    }

    private int method5(int n) {
        System.out.println("method5: " + n);
        return n;
    }

    private int methodMultiArgs(int n, int m) {
        System.out.println("methodMultiArgs: " + n + ", " + m);
        return n + m;
    }
}
