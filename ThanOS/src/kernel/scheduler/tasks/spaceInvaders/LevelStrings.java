package kernel.scheduler.tasks.spaceInvaders;

public class LevelStrings {
    public static int currentString = 0;
    public static boolean hasCheated = true;

    public static String getNextString() {
        currentString += 1;
        if(hasCheated) {
            return "You cheated. So what is your victory worth?\nNOTHING! Are you proud of yourself?";
        }
        else {
            switch (currentString) {
                case 1:
                    return "Congratz, you noob, you completed the first level. Do you feel awesome now? I can't even express how impressed I am.\nWhat, you actually believed me? HAHAHAHAHA!";
                case 2:
                    return "Did I trigger you after the last level?";
                case 3:
                    return "Oh, now you're going for it, right? Want to see the end? Well, let me put it this way:\nPREPARE TO SUFFER!";
                case 4:
                    return "Don't you have anything better to do? I nearly pity you, if it wasn't so funny!";
                case 5:
                    return "Even though you are constantly insulted by me, you still keep playing? You should do an IQ test.\nNo, I don't think it will be higher than average...";
                case 6:
                    return "Now we're getting at it. You want the hard stuff, you get it.";
                case 7:
                    return "Time for the endgame. Your chance of winning is 1 in 4000605.";
                default:
                    return "CHEATER!";

            }
        }
    }

    public static void reset() {
        currentString = 0;
    }
}
