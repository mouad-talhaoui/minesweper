public class Player {
    String name;
    int totalPlays;
    String hashedpassowrd;
    int toalLose;
    int totalWins;

    Player(String name, String hashedpassowrd) {
        this.name = name;
        this.totalPlays = 0;
        this.hashedpassowrd = hashedpassowrd;
        this.toalLose = 0;
        this.totalWins = 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", totalPlays=" + totalPlays +
                ", hashedpassowrd='" + hashedpassowrd + '\'' +
                ", toalLose=" + toalLose +
                ", totalWins=" + totalWins +
                '}';
    }
}
