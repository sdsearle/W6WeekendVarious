package com.example.admin.w6weekendvarious;

/**
 * Created by admin on 10/7/2017.
 */

public class Tiger extends Animal {
    private static int count = 0;

    public Tiger(){
        super();
        count += 1;
    }

    @Override
    public void sleep() {
        super.setEnergy(super.getEnergy() + 5);
    }

    @Override
    public void eatFood(Food food) {
        if (food == Food.GRAIN){
            return;
        }
        super.eatFood(food);
    }
}
