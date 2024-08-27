import java.util.*;

public class FactoryMethod{
    public static void main(String args[]){
        BurgerStore cheeseStore = new CheeseBurgerStore();
        BurgerStore veganStore = new VeganBurgerStore();

        Burger burger = cheeseStore.orderBurger(Burgers.CHEESE);
        System.out.println("Ethan ordered a " + burger.getName() + "\n");

        burger = veganStore.orderBurger(Burgers.DELUXEVEGAN);
        System.out.println("Joel ordered a " + burger.getName() + "\n");
    }
}

enum Burgers{
    CHEESE,
    DELUXECHEESE,
    VEGAN,
    DELUXEVEGAN,
}

abstract class Burger{
    String name;
    String bread;
    String sauce;
    List<String> toppings;

    public void prepare(){
        System.out.println(this.name + " is being prepared");
    }
    public void cook(){
        System.out.println(this.name + " is being cooked");
    }
    public void serve(){
        System.out.println(this.name + " is served");
    }

    public String getName(){
        return this.name;
    }
}

class CheeseBurger extends Burger{
    public CheeseBurger(){
        this.name = "Cheese Burger";
        this.bread = "White Bread";
        this.sauce = "Chipotle";
        this.toppings = Arrays.asList("Lettuce", "Onion"); 
    }
}

class DeluxeCheeseBurger extends Burger{
    public DeluxeCheeseBurger(){
        this.name = "Deluxe Cheese Burger";
        this.bread = "White Bread";
        this.sauce = "Chipotle";
        this.toppings = Arrays.asList("Lettuce", "Onion"); 
    }
}

class VeganBurger extends Burger{
    public VeganBurger(){
        this.name = "Vegan Burger";
        this.bread = "White Bread";
        this.sauce = "Chipotle";
        this.toppings = Arrays.asList("Lettuce", "Onion"); 
    }
}

class DeluxeVeganBurger extends Burger{
    public DeluxeVeganBurger(){
        this.name = "Deluxe Vegan Burger";
        this.bread = "White Bread";
        this.sauce = "Chipotle";
        this.toppings = Arrays.asList("Lettuce", "Onion"); 
    }
}

abstract class BurgerStore{
    public abstract Burger createBurgerObject(Burgers type);

    public Burger orderBurger(Burgers type){
        Burger burger = createBurgerObject(type);
        burger.prepare();
        burger.cook();
        burger.serve();
        return burger;
    }
}

class CheeseBurgerStore extends BurgerStore{
    @Override
    public Burger createBurgerObject(Burgers type){
        if(type.equals(Burgers.CHEESE)){
            return new CheeseBurger();
        }
        else if(type.equals(Burgers.DELUXECHEESE)){
            return new DeluxeCheeseBurger();
        }
        else{
            return null;
        }
    }
}

class VeganBurgerStore extends BurgerStore{
    @Override
    public Burger createBurgerObject(Burgers type){
        if(type.equals(Burgers.VEGAN)){
            return new VeganBurger();
        }
        else if(type.equals(Burgers.DELUXEVEGAN)){
            return new DeluxeVeganBurger();
        }
        else{
            return null;
        }
    }
}