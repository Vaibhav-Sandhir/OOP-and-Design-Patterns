import java.util.*;
import java.time.LocalTime;
import java.time.Duration;

public class ParkingLot{
    public static void main(String args[]){
        ParkingConsole console = new ParkingConsole();
        console.run();
    }
}

public class Vehicle{
    String numberPlate;
    int space;
    Ticket ticket;

    public Vehicle(){
        this.space = 0;
        this.numberPlate = "";
    }

    public Vehicle(String numberPlate, int space){
        this.numberPlate = numberPlate;
        this.space = space;
    }
}

public class Car extends Vehicle{

    public Car(String numberPlate){
        super(numberPlate, 1);
    }
}

public class Limo extends Vehicle{

    public Limo(String numberPlate){
        super(numberPlate, 2);
    }
}

public class Semitruck extends Vehicle{

    public Semitruck(String numberPlate){
        super(numberPlate, 3);
    }
}

public class Ticket{
    int price_per_hour;
    int min_charge;
    int floor;
    int spot;
    String type;
    LocalTime entry_time;

    public Ticket(String type, int floor, int spot){
        this.entry_time = LocalTime.now();
        this.floor = floor;
        this.spot = spot;
        this.type = type;

        switch(type){
            case "Car":
                this.min_charge = 20;
                this.price_per_hour = 30;
                break;
            case "Limo":
                this.min_charge = 40;
                this.price_per_hour = 40;
                break;
            case "Semitruck":
                this.min_charge = 60;
                this.price_per_hour = 50;
                break;
        }
    }
}

public class ParkingSpace{
    Map<String, Vehicle> map = new HashMap<>();

    private class Floor{
        final int number_of_spaces = 4;
        boolean spaces[];

        public Floor(){
            this.spaces = new boolean[this.number_of_spaces];
        }

        public int fill(Vehicle vehicle){
            int required_space = vehicle.space;
            for(int i = 0; i < this.number_of_spaces; i++){
                if(!this.spaces[i]){
                    required_space--;
                    if(required_space == 0){
                        int start = i - vehicle.space + 1;
                        for(int j = start; j <= i; j++){
                            this.spaces[j] = true;
                        }
                        return start;
                    }
                }
                else{
                    required_space = vehicle.space;
                }
            }
            return -1;
        }
    }

    final int number_of_floors = 3;
    Floor floors[];

    public ParkingSpace(){
        this.floors = new Floor[this.number_of_floors];
        for(int i = 0; i < this.number_of_floors; i++){
            this.floors[i] = new Floor();
        }
    }

    public void parkVehicle(Vehicle vehicle, String type) throws ParkingFullException{
        for(int i = 0; i < this.number_of_floors; i++){
            int result = floors[i].fill(vehicle);
            if(result != -1){
                System.out.println("Vehicle " + vehicle.numberPlate + " can be parked at B" + i + " floor" + " at slot " + result);
                Ticket ticket = new Ticket(type, i, result);
                vehicle.ticket = ticket;
                map.put(vehicle.numberPlate, vehicle);
                return;
            }
        }
        throw new ParkingFullException("Parking is full, sorry");
    }

    public void removeVehicle(Vehicle vehicle){
        int floor = vehicle.ticket.floor;
        int spot = vehicle.ticket.spot;
        for(int i = spot; i < spot + vehicle.space; i++){
            this.floors[floor].spaces[i] = false;
        }
    }

    public void display(){
        System.out.println("===================================");
        for(int i = 0; i < this.number_of_floors; i++){
            System.out.println("Floor " + i);
            Floor floor = this.floors[i];
            for(int j = 0; j < floor.spaces.length; j++){
                char c = floor.spaces[j] ? 'T' : 'F';
                System.out.print(c + "  ");
            }
            System.out.println();
        }
        System.out.println("Cars parked currently");
        for(String car : this.map.keySet()){
            System.out.println(car);
        }
        System.out.println("===================================");
    }
}

public class ParkingConsole{
    ParkingSpace parking = new ParkingSpace();
    Scanner scanner = new Scanner(System.in);

    public void run(){
        while(true){
            System.out.println("Entry or Exit");
            String choice = scanner.next();
            switch(choice){
                case "Entry":
                    entry();
                    parking.display();
                    break;
                case "Exit":
                    exit();
                    parking.display();
                    break;
                default:
                    System.out.println("Wrong choice enter again");
                    continue;
            }
        }
    }

    public void entry(){
        System.out.println("Enter car type: ");
        String type = scanner.next();
        try{
            validateVehicleType(type);
        }
        catch(InvalidVehicleTypeException e){
            System.out.println(e);
            return;
        }
        scanner.nextLine();
        System.out.println("Enter number plate: ");
        String numberPlate = scanner.nextLine();
        Vehicle vehicle = new Vehicle();
        switch(type){
            case "Car":
                vehicle = new Car(numberPlate);
                break;
            case "Limo":
                vehicle = new Limo(numberPlate);
                break;
            case "Semitruck":
                vehicle = new Semitruck(numberPlate);
                break;
        }
        try{
            this.parking.parkVehicle(vehicle, type);
        }
        catch(ParkingFullException e){
            System.out.println(e);
            return;
        }
        return;
    }

    public void exit(){
        scanner.nextLine();
        String numberPlate = scanner.nextLine();
        if(!parking.map.containsKey(numberPlate)){
            System.out.println("This car is not parked in the parking space");
            return;
        }
        Vehicle vehicle = parking.map.get(numberPlate);
        LocalTime exit_time = LocalTime.now();
        Duration duration = Duration.between(vehicle.ticket.entry_time, exit_time);
        int hours = (int)duration.toHours();
        int price = vehicle.ticket.min_charge + vehicle.ticket.price_per_hour * hours;
        System.out.println("Please pay " + price + " rupees");
        parking.removeVehicle(vehicle);
        parking.map.remove(numberPlate);
        return;
    }

    private void validateVehicleType(String type) throws InvalidVehicleTypeException{
        if(!type.equals("Car") && !type.equals("Limo") && !type.equals("Semitruck")){
            throw new InvalidVehicleTypeException("Invalid Vehicle Type try again");
        }
    }
}

public class ParkingFullException extends Exception{

    public ParkingFullException(String msg){
        super(msg);
    }
}

public class InvalidVehicleTypeException extends Exception{

    public InvalidVehicleTypeException(String msg){
        super(msg);
    }
}