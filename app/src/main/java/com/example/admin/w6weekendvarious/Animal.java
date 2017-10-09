package com.example.admin.w6weekendvarious;

/**
 * Created by admin on 10/7/2017.
 */

public class Animal {

    private int energy;

    public Animal(){
        energy = 10;
    }

    public void makeSound(){
        System.out.println("Sound");
        energy -= 3;
    }
    public void eatFood(Food food){
        System.out.println("Om nom nom");
        energy += 5;
    }
    public void sleep(){
        System.out.println("ZzZzz...");
        energy += 10;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }
}
