package com.example.admin.w6weekendvarious;

/**
 * Created by admin on 10/7/2017.
 */

public class Monkey extends Animal {
    private static int count = 0;

    public Monkey(){
        super();
        count += 1;
    }

    @Override
    public void makeSound() {
        super.setEnergy(getEnergy() - 4);
    }

    @Override
    public void eatFood(Food food) {
        setEnergy(getEnergy() + 2);
    }

    public void play(){
        if (getEnergy() > 7){
            System.out.println("Ooo Ooo Ooo");
        }else
            System.out.println("Monkey is too tired");
    }

}
