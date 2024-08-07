import java.util.*;

public class Connect4{
    public static void main(String args[]){
        Player p1 = new Player("Vaibhav", "R");
        Player p2 = new Player("Vicky", "B");
        Game game = new Game(6, 7, 4, p1, p2, 3);
        game.play(); 
    }
}

public class Game{
    Grid grid;
    Player p1;
    Player p2;
    int target;
    
    public Game(int n, int m, int x, Player p1, Player p2, int target){
        this.grid = new Grid(n, m, x);
        this.p1 = p1;
        this.p2 = p2;
        this.target = target;
    }

    public void play(){
        Player players[] = new Player[]{p1, p2};
        Scanner sc = new Scanner(System.in);
        for(int i = 0; i < 2; i = (i + 1) % 2){
            System.out.println(players[i].name + "'s turn! \nEnter column where you want to insert");
            int j = sc.nextInt();
            try{
                boolean result = grid.insert(j, players[i]);
                grid.display();
                if(result){
                    players[i].score++;
                    if(players[i].score == target){
                        System.out.println(players[i].name + " is the winner");
                        return;
                    }
                    else{
                        System.out.println("This round was won by " + players[i].name);
                        grid.initialize();
                        i = -1;
                        continue;
                    }
                }
            }
            catch(ColumnFullException ce){
                System.out.println(ce);
                i--;
                continue;
            }
            catch(OutOfBoundsException oe){
                System.out.println(oe);
                i--;
                continue;
            }
        }
    }
}

public class Player{
    String name;
    String color;
    int score;

    public Player(String name, String color){
        this.name = name;
        this.color = color;
        this.score = 0;
    }
}

public class Grid{
    int n;
    int m;
    int x;
    String grid[][];

    public Grid(int n, int m, int x){
        this.n = n;
        this.m = m;
        this.x = x;
        initialize();
    }

    public void display(){
        for(String arr[] : grid){
            System.out.println(Arrays.toString(arr));
        }
        return;
    }

    public void initialize(){
        grid = new String[this.n][this.m];
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j < this.m; j++){
                grid[i][j] = "X";
            }
        }
    }

    public boolean insert(int j, Player player) throws ColumnFullException, OutOfBoundsException{
        if(j >= this.m || j < 0){
            throw new OutOfBoundsException("Entered Column is out of bounds try again");
        }
        if(!this.grid[0][j].equals("X")){
            throw new ColumnFullException("This column is full try another one");
        }
        for(int i = this.n - 1; i >= 0; i--){
            if(grid[i][j].equals("X")){
                grid[i][j] = player.color;
                return checkWin(player.color, i, j);
            }
        }
        return false;
    }

    private boolean checkWin(String color, int i, int j){
        int vertical_count = count(i, j, new int[][]{{1, 0}, {-1, 0}}, color);
        if(vertical_count == this.x){
            return true;
        }
        int horizontal_count = count(i, j, new int[][]{{0, 1}, {0, -1}}, color);
        if(horizontal_count == this.x){
            return true;
        }
        int diagonal_count = count(i, j, new int[][]{{-1, -1}, {1, 1}}, color);
        if(diagonal_count == this.x){
            return true;
        }
        int anti_diagonal_count = count(i, j, new int[][]{{-1, 1}, {1, -1}}, color);
        if(anti_diagonal_count == this.x){
            return true;
        }
        return false;
    }

    private int count(int i, int j, int direction[][], String color){
        int sum = 0;
        for(int k = 0; k < 2; k++){
            int r = i;
            int c = j;
            while(r >= 0 && r < this.n && c >= 0 && c < this.m && this.grid[r][c].equals(color)){
                sum++;
                r = r + direction[k][0];
                c = c + direction[k][1];
            }
        }
        return sum - 1;
    }
}

public class ColumnFullException extends Exception{
    public ColumnFullException(String msg){
        super(msg);
    }
}

public class OutOfBoundsException extends Exception{
    public OutOfBoundsException(String msg){
        super(msg);
    }
}