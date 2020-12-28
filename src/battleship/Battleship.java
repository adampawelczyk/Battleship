package battleship;
import java.util.Scanner;

public class Battleship {
    Battlefield battleship;

    public Battleship() {
        battleship = new Battlefield();
    }

    void game() {
        Scanner scanner = new Scanner(System.in);
        battleship.printBattlefield();
        battleship.initPlayerBattlefield();
        battleship.initComputerBattlefield();
        String player = "player";

        System.out.println("\nThe game starts:");

        while (true) {

            System.out.println();
            battleship.printComputerBattlefield();
            System.out.println("-----------------------");
            battleship.printBattlefield();

            if (player.equals("player")) {

                System.out.println("It's your turn:");
                String coordinates = scanner.nextLine();
                int x = coordinates.charAt(0) - 65;
                int y = Integer.parseInt(coordinates.substring(1)) - 1;
                while ((x < 0 || x > 9) || (y < 0 || y > 9) || battleship.computerBattlefield[x][y] == 'M'
                        || battleship.computerBattlefield[x][y] == 'X') {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    coordinates = scanner.nextLine();
                    x = coordinates.charAt(0) - 65;
                    y = Integer.parseInt(coordinates.substring(1)) - 1;
                }

                if (battleship.playerShot(x, y)) {
                    battleship.printComputerBattlefield();
                    System.out.println("---------------------");
                    battleship.printBattlefield();
                    if (battleship.isAlive(x, y, battleship.computerShips)) {
                        System.out.println("You hit a ship!");
                    } else {
                        System.out.println("You sank a ship!");
                    }
                    if (battleship.isGameEnd(battleship.computerBattlefield)) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        System.exit(0);
                    }
                } else {
                    battleship.printComputerBattlefield();
                    System.out.println("---------------------");
                    battleship.printBattlefield();
                    System.out.println("You missed");
                    player = "computer";
                }
            } else {
                System.out.println("It's computer turn:\n");

                int[] coordinates = battleship.coordinatesToShoot();

                int x = coordinates[0];
                int y = coordinates[1];

                if (battleship.computerShot(x, y)) {
                    battleship.printComputerBattlefield();
                    System.out.println("---------------------");
                    battleship.printBattlefield();
                    if (battleship.isAlive(x, y, battleship.playerShips)) {
                        System.out.printf("Computer shoots on %c%d and hits your ship!\n", x + 65, y + 1);

                        battleship.computerLastShot[0] = x;
                        battleship.computerLastShot[1] = y;
                    } else {
                        System.out.printf("Computer shoots on %c%d and sinks your ship!\n", x + 65, y + 1);

                        battleship.computerLastShot[0] = -1;
                        battleship.computerLastShot[1] = -1;
                    }
                    if (battleship.isGameEnd(battleship.playerBattlefield)) {
                        System.out.println("Computer sank your last ship. You lost!");
                        System.exit(0);
                    }
                } else {
                    battleship.printComputerBattlefield();
                    System.out.println("---------------------");
                    battleship.printBattlefield();
                    System.out.printf("Computer shoots on %c%d and misses!\n", x + 65, y + 1);
                    player = "player";
                }
            }
        }
    }
}
