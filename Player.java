public class Player {
    String name;
    int totalPlays;
    String hashedpassowrd;
    int totalLose;
    int totalWins;

    Player(String name, String hashedpassowrd) {
        this.name = name;
        this.totalPlays = 0;
        this.hashedpassowrd = hashedpassowrd;
        this.totalLose = 0;
        this.totalWins = 0;
    }

    Player(String name, int totalWins, int totalLose, int totalPlays) {
        this.name = name;
        this.totalPlays = totalPlays;
        this.totalLose = totalLose;
        this.totalWins = totalWins;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", totalPlays=" + totalPlays +
                ", hashedpassowrd='" + hashedpassowrd + '\'' +
                ", totalLose=" + totalLose +
                ", totalWins=" + totalWins +
                '}';
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getTotalPlays() {
        return totalPlays;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLose() {
        return totalLose;
    }
}
