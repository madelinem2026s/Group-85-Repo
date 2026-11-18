
package robotassembly;

public class GameTimer {

    private int timeRemaining;
    private int startingTime;
    private double multiplier;

    public GameTimer(String difficulty) {
        switch (difficulty) {
            case "Medium":
                startingTime = 450; // 45 seconds
                break;
            case "Hard":
                startingTime = 300; // 30 seconds
                break;
            default:
                startingTime = 600; // 60 seconds
                break;
        }

        timeRemaining = startingTime;
        updateMultiplier();
    }

    // Decrease time by 0.1 seconds
    public void tick() {
        if (isFinished()) {
            return;
        }

        timeRemaining--;
        updateMultiplier();
    }

    // Calculate multiplier based on time remaining
    private void updateMultiplier() {
        if (timeRemaining <= 0) {
            multiplier = 1.0;
            return;
        }

        double[] values;
        int interval;

        if (startingTime == 600) {
            values = new double[] {4, 3.5, 3, 2.5, 2, 1.5};
            interval = 100;
        }
        else if (startingTime == 450) {
            values = new double[] {7, 6, 5, 4, 3, 2};
            interval = 75;
        }
        else {
            values = new double[] {12, 10, 8, 6, 4, 2};
            interval = 50;
        }

        int index = Math.max(0,
            (startingTime - timeRemaining - 1) / interval);
        index = Math.min(index, 5);

        multiplier = values[index];
    }

    public double getTimeRemaining() {
        return timeRemaining / 10.0;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public boolean isFinished() {
        return timeRemaining <= -50;
    }

    public double calculateDamage(int score) {
        return score * multiplier;
    }
}

