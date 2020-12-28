package battleship;
import java.util.Arrays;
import java.util.Scanner;

public class Battlefield {
    char[][] playerBattlefield;
    char[][] computerBattlefield;
    Ship[] playerShips;
    Ship[] computerShips;
    int[] computerLastShot;

    Battlefield() {
        this.playerBattlefield = new char[10][10];
        this.computerBattlefield = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.playerBattlefield[i][j] = '~';
                this.computerBattlefield[i][j] = '~';
            }
        }
        this.computerLastShot = new int[2];
        computerLastShot[0] = -1;
        computerLastShot[1] = -1;
    }

    void printBattlefield() {
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print((char) (i + 65));
            System.out.print("  ");
            for (int j = 0; j < 10; j++) {
                System.out.print(playerBattlefield[i][j]);
                System.out.print(' ');
            }
            System.out.print('\n');
        }
    }

    void printComputerBattlefield() {
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print((char) (i + 65));
            System.out.print("  ");
            for (int j = 0; j < 10; j++) {
                if (computerBattlefield[i][j] == 'X' || computerBattlefield[i][j] == 'M') {
                    System.out.print(computerBattlefield[i][j]);
                } else {
                    System.out.print('~');
                }
                System.out.print(' ');
            }
            System.out.print('\n');
        }
    }

    void initPlayerBattlefield() {
        Scanner scanner = new Scanner(System.in);

        playerShips = new Ship[5];
        playerShips[0] = new Ship("Aircraft Carrier", 5);
        playerShips[1] = new Ship("Battleship", 4);
        playerShips[2] = new Ship("Submarine", 3);
        playerShips[3] = new Ship("Cruiser", 3);
        playerShips[4] = new Ship("Destroyer", 2);

        for (Ship ship : playerShips) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getSize());
            while (true) {
                String[] coordinates = scanner.nextLine().split(" ");
                int rowBegin = coordinates[0].charAt(0) - 65;
                int columnBegin = Integer.parseInt(coordinates[0].substring(1)) - 1;
                int rowEnd = coordinates[1].charAt(0) - 65;
                int columnEnd = Integer.parseInt(coordinates[1].substring(1)) - 1;

                if (rowBegin > rowEnd) {
                    int temp = rowBegin;
                    rowBegin = rowEnd;
                    rowEnd = temp;
                }
                if (columnBegin > columnEnd) {
                    int temp = columnBegin;
                    columnBegin = columnEnd;
                    columnEnd = temp;
                }

                if (rowBegin == rowEnd || columnBegin == columnEnd) {
                    if (checkSize(rowBegin, columnBegin, rowEnd, columnEnd, ship.getSize())) {
                        if (checkCoordinates(rowBegin, columnBegin, rowEnd, columnEnd, playerShips, playerBattlefield)) {
                            placeShip(rowBegin, columnBegin, rowEnd, columnEnd, ship, playerBattlefield);
                            printBattlefield();
                            break;
                        } else {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                        }
                    } else {
                        System.out.printf("Error wrong length of the %s! Try again:\n", ship.getName());
                    }
                } else {
                    System.out.println("Error! Wrong ship location! Try again:");
                }
            }
        }
    }

    void initComputerBattlefield() {
        computerShips = new Ship[5];
        computerShips[0] = new Ship("Aircraft Carrier", 5);
        computerShips[1] = new Ship("Battleship", 4);
        computerShips[2] = new Ship("Submarine", 3);
        computerShips[3] = new Ship("Cruiser", 3);
        computerShips[4] = new Ship("Destroyer", 2);

        for (Ship ship : computerShips) {
            while (true) {
                int rowBegin;
                int columnBegin;
                int rowEnd;
                int columnEnd;

                int shipLocation = getRandomNumber(0, 2);
                if (shipLocation == 1) {
                    rowBegin = getRandomNumber(0, 10);
                    columnBegin = getRandomNumber(0, 10);
                    rowEnd = rowBegin;
                    columnEnd = columnBegin + ship.getSize() - 1;

                } else {
                    rowBegin = getRandomNumber(0, 10);
                    columnBegin = getRandomNumber(0, 10);
                    rowEnd = rowBegin + ship.getSize() - 1;
                    columnEnd = columnBegin;
                }

                if (columnEnd <= 9 && rowEnd <= 9) {
                    if (checkSize(rowBegin, columnBegin, rowEnd, columnEnd, ship.getSize())) {
                        if (checkCoordinates(rowBegin, columnBegin, rowEnd, columnEnd, computerShips, computerBattlefield)) {
                            placeShip(rowBegin, columnBegin, rowEnd, columnEnd, ship, computerBattlefield);
                            break;
                        }
                    }
                }
            }
        }
    }

    int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    void placeShip(int rowBegin, int columnBegin, int rowEnd, int columnEnd, Ship ship, char[][] battlefield) {
        if (rowBegin == rowEnd) {
            for (int i = columnBegin; i <= columnEnd; i++) {
                battlefield[rowBegin][i] = 'O';
            }
        } else {
            for (int i = rowBegin; i <= rowEnd; i++) {
                battlefield[i][columnBegin] = 'O';
            }
        }
        ship.place(rowBegin, columnBegin, rowEnd, columnEnd);
    }

    boolean checkSize(int rowBegin, int columnBegin, int rowEnd, int columnEnd, int size) {
        if (rowBegin == rowEnd || columnBegin == columnEnd) {
            return rowEnd - rowBegin + 1 == size || columnEnd - columnBegin + 1 == size;
        }
        else {
            return false;
        }
    }

    boolean checkCoordinates(int rowBegin, int columnBegin, int rowEnd, int columnEnd, Ship[] ships, char[][] battlefield) {
        for (Ship ship : ships) {
            if (ship.isPlaced()) {
                for (int i = rowBegin - 1; i <= rowEnd + 1; i++) {
                    for (int j = columnBegin - 1; j <= columnEnd + 1; j++) {
                        if (isOccupied(i, j, battlefield)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    boolean isOccupied(int i, int j, char[][] battlefield) {
        i = Math.min(i, 9);
        i = Math.max(i, 0);
        j = Math.min(j, 9);
        j = Math.max(j, 0);

        return battlefield[i][j] == 'O';
    }

    boolean isGameEnd(char[][] battlefield) {
        for (char[] row : battlefield) {
            for (char element : row) {
                if (element == 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    boolean playerShot(int x, int y) {
        if (computerBattlefield[x][y] == 'O' || computerBattlefield[x][y] == 'X') {
            computerBattlefield[x][y] = 'X';
            return true;
        } else {
            computerBattlefield[x][y] = 'M';
            return false;
        }
    }

    int[] coordinatesToShoot() {
        int[] coordinates = new int[2];
        if (computerLastShot[0] != -1) {
            int x = computerLastShot[0];
            int y = computerLastShot[1];

            if (x - 1 >= 0 && playerBattlefield[x - 1][y] == 'X' || x + 1 < 10 && playerBattlefield[x + 1][y] == 'X') {
                while (x > 0) {
                    x--;
                    if (playerBattlefield[x][y] == 'M') {
                        break;
                    }
                    if (playerBattlefield[x][y] != 'X' && playerBattlefield[x][y] != 'M') {
                        coordinates[0] = x;
                        coordinates[1] = y;
                        return coordinates;
                    }
                }
                while (x < 10) {
                    x++;
                    if (playerBattlefield[x][y] == 'M') {
                        break;
                    }
                    if (playerBattlefield[x][y] != 'X' && playerBattlefield[x][y] != 'M') {
                        coordinates[0] = x;
                        coordinates[1] = y;
                        return coordinates;
                    }
                }
            } else if (y - 1 >= 0 && playerBattlefield[x][y - 1] == 'X' || y + 1 < 10 && playerBattlefield[x][y + 1] == 'X') {
                while (y > 0) {
                    y--;
                    if (playerBattlefield[x][y] == 'M') {
                        break;
                    }
                    if (playerBattlefield[x][y] != 'X' && playerBattlefield[x][y] != 'M') {
                        coordinates[0] = x;
                        coordinates[1] = y;
                        return coordinates;
                    }
                }
                while (y < 10) {
                    y++;
                    if (playerBattlefield[x][y] == 'M') {
                        break;
                    }
                    if (playerBattlefield[x][y] != 'X' && playerBattlefield[x][y] != 'M') {
                        coordinates[0] = x;
                        coordinates[1] = y;
                        return coordinates;
                    }
                }
            } else {
                int choice = getRandomNumber(0, 5);

                while (true) {
                    if (x - 1 >= 0 && playerBattlefield[x - 1][y] != 'M' && choice == 1) {
                        x = x - 1;
                        break;
                    } if (x + 1 < 10 && playerBattlefield[x + 1][y] != 'M' && choice == 2) {
                        x = x + 1;
                        break;
                    } if (y - 1 >= 0 && playerBattlefield[x][y - 1] != 'M' && choice == 3) {
                        y = y - 1;
                        break;
                    } if (y + 1 < 10 && playerBattlefield[x][y + 1] != 'M' && choice == 4) {
                        y = y + 1;
                        break;
                    }
                    choice = getRandomNumber(0, 5);
                }
            }
            coordinates[0] = x;
            coordinates[1] = y;
        } else {
            int x = getRandomNumber(0, 10);
            int y = getRandomNumber(0, 10);

            while ((playerBattlefield[x][y] == 'X' || playerBattlefield[x][y] == 'M') || checkShootingCoordinates(x, y)) {
                x = getRandomNumber(0, 10);
                y = getRandomNumber(0, 10);
            }
            coordinates[0] = x;
            coordinates[1] = y;
        }
        return coordinates;
    }

    boolean checkShootingCoordinates(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i >= 0 && i <= 9) && (j >= 0 && j <= 9)) {
                    if (playerBattlefield[i][j] == 'X') {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean computerShot(int x, int y) {
    if (playerBattlefield[x][y] == 'O' || playerBattlefield[x][y] == 'X') {
            playerBattlefield[x][y] = 'X';
            return true;
        } else {
            playerBattlefield[x][y] = 'M';
            return false;
        }
    }

    boolean isAlive(int x, int y, Ship[] ships) {
        for (Ship ship : ships) {
            if (ship.getRowBegin() == ship.getRowEnd() && x == ship.getRowBegin()) {
                if (ship.getColumnBegin() <= y && ship.getColumnEnd() >= y) {
                    if (ship.getLives() == 1) {
                        return false;
                    } else {
                        ship.hit();
                    }
                }
            } else if (ship.getColumnBegin() == ship.getColumnEnd() && y == ship.getColumnBegin()) {
                if (ship.getRowBegin() <= x && ship.getRowEnd() >= x) {
                    if (ship.getLives() == 1) {
                        return false;
                    } else {
                        ship.hit();
                    }
                }
            }
        }
        return true;
    }
}
