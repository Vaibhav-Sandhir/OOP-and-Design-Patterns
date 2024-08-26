import java.util.*;

public class Blackjack{
    public static void main(String args[]){
        Player player = new Player("Vaibhav", 1000);
        Dealer dealer = new Dealer(1000000000);
        Game game = new Game(dealer, player);
        game.play();
    }
}

public class Game{
    Dealer dealer;
    Player player;

    Game(Dealer dealer, Player player){
        this.dealer = dealer;
        this.player = player;
    }

    public void play(){
        int status = 0;
        while(status != 1){
            status = round();
        }
        return;
    }

    public int round(){
        int pot = 200;
        player.money -= 100;
        if(player.money <= 0){
            System.out.println(player.name + "'s money has been finished!");
        }
        dealer.money -= 100;
        boolean dealer_turn = false;
        Card cards[] = dealer.initialDeal();
        List<Card> player_cards = new ArrayList<>(Arrays.asList(cards[0], cards[1]));
        List<Card> dealer_cards = new ArrayList<>(Arrays.asList(cards[2], cards[3]));
        int p_sum = sum(player_cards);
        int d_sum = sum(dealer_cards);
        displayStatus(pot);
        displayCards(dealer_cards, player_cards, dealer_turn);
        while(true){
            if(!dealer_turn){
                if(p_sum > 21){
                    System.out.println("PLAYER BUST!, DEALER WON");
                    dealer.money += pot;
                    return 0;
                }
                int option = selectOptions();
                if(option == 3){
                    System.out.println(player.name + " ended with " + player.money);
                    return 1;
                }
                if(option == 1){
                    player_cards.add(dealer.hit());
                    p_sum = sum(player_cards);
                }
                if(option == 2){
                    dealer_turn = true;
                }
                displayCards(dealer_cards, player_cards, dealer_turn);
            }
            else{
                if(d_sum > 21){
                    System.out.println("DEALER BUST!, PLAYER WON");
                    player.money += pot;
                    return 0;
                }
                else if(d_sum >= p_sum){
                    System.out.println("DEALER WON");
                    dealer.money += pot;
                    return 0;
                }
                dealer_cards.add(dealer.hit());
                d_sum = sum(dealer_cards);
                displayCards(dealer_cards, player_cards, dealer_turn);
            }
        }
    }

    private void displayStatus(int pot){
        System.out.println("Dealer     -------------------------  " + player.name + " (" + player.money + ")");
        System.out.println("Pot = " + pot);
    }

    private void displayCards(List<Card> dealer_cards, List<Card> player_cards, boolean dealer_turn){
        StringBuffer buffer = new StringBuffer();
        if(!dealer_turn){
            buffer.append("X ");
            for(int i = 1; i < dealer_cards.size(); i++){
                buffer.append(dealer_cards.get(i).toString());
            }
        }
        else{
            for(int i = 0; i < dealer_cards.size(); i++){
                buffer.append(dealer_cards.get(i).toString());
            }
        }
        buffer.append("----------------------------------------");
        for(int i = 0; i < player_cards.size(); i++){
            buffer.append(player_cards.get(i).toString());
        }
        System.out.println(buffer.toString());
    }

    private int selectOptions(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Option 1 : Hit");
        System.out.println("Option 2 : Complete");
        System.out.println("Option 3 : Quit");
        System.out.println("Enter your option");
        int option = sc.nextInt();
        return option;
    }

    private int sum(List<Card> cards){
        int sum = 0;
        int aces = 0;
        for(Card card : cards){
            if(card.value.equals("J") || card.value.equals("K") || card.value.equals("Q")){
                sum += 10;
            }
            else if(card.value.equals("A")){
                aces++;
            }
            else{
                sum += Integer.valueOf(card.value);
            }
        }
        for(int i = 0; i < aces; i++){
            if(11 + sum > 21){
                sum += 1;
            }
            else{
                sum += 11;
            }
        }
        return sum;
    }
}

public class Dealer{
    int money;
    Deck deck;

    Dealer(int money){
        this.money = money;
        this.deck = new Deck();
    }

    public Card[] initialDeal(){
        Card cards[] = new Card[4];
        for(int i = 0; i < 4; i++){
            cards[i] = this.deck.drawCard();
        }
        return cards;
    }

    public Card hit(){
        return this.deck.drawCard();
    }

    public void reset(){
        deck = new Deck();
    }
}

public class Player{
    String name;
    int money;

    Player(String name, int money){
        this.name = name;
        this.money = money;
    }
}

public class Deck{
    Card deck[];
    private Random random;
    
    Deck(){
        this.deck = new Card[52];
        random = new Random();
        initDeck();
    }

    private void initDeck(){
        String suits[] = {"Hearts", "Spades", "Diamonds", "Clubs"};
        String values[] = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int k = 0;
        for(String suit : suits){
            for(String value : values){
                deck[k++] = new Card(suit, value);
            }
        }
    }

    public Card drawCard(){
        while(true){
            int ind = random.nextInt(52);
            Card card = deck[ind];
            if(!card.used){
                return card;
            }
        }
    }
}

public class Card{
    String suit;
    String value;
    boolean used;

    Card(String suit, String value){
        this.suit = suit;
        this.value = value;
        this.used = false;
    }

    @Override
    public String toString(){
        return value + "{" + suit + "}";
    }
}