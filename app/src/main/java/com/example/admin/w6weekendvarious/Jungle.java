package com.example.admin.w6weekendvarious;

import java.util.ArrayList;

/**
 * Created by admin on 10/7/2017.
 */

public class Jungle {

    private static ArrayList<Animal> animals;

    public void soundOff(){
        for (Animal a :
                animals) {
            a.makeSound();
            System.out.println(a.toString() + ": "+ a.getEnergy());
        }
    }

    public static void main(String[] args) {
        animals = new ArrayList<>();

    }

}
