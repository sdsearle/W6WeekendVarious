package com.example.admin.w6weekendvarious;

import com.orm.SugarRecord;

/**
 * Created by admin on 10/9/2017.
 */

public class Person extends SugarRecord {
    String name, age;

    public Person() {}

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
}
