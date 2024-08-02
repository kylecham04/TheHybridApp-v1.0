/**
 * Author - Kyle Chambers
 * File name - LiftsForRecommending
 * Date of last update - 7/13/2024
 * LiftsForRecommending file description - The file is responsible for holding all the potential
 * lifts and recommending them randomly to the user depending on what body part they want to hit
 * stored and accessible.
 * Updates to come - None as of now
 **/
import java.util.*;

public class LiftsForRecommending {
    private Map<String, String[]> liftRecommender;

    /**
     * Default Constructor
     * Description - Initializes the hash map used to recommend a lift and adds all the data to it
     */
    public LiftsForRecommending() {
        liftRecommender = new HashMap<>();
        setLegLifts(liftRecommender);
        setChestAndTriLifts(liftRecommender);
        setBackAndBiLifts(liftRecommender);
        setShouldersAndArmsLifts(liftRecommender);
    }

    /**
     * setsXReps
     * Description - Chooses a random set and rep range used to give to the user
     * @param rand - Random number generator used to get a set and rep range for the user
     * @return - a string representing the number of sets and reps for the user
     */
    private String setsXReps(Random rand) {
        String[] setsXReps = {"3 x 8", "4 x 10", "4 x 12", "1 x 10, 2 x 8, 1 x 6"};
        return setsXReps[rand.nextInt(setsXReps.length)];
    }

    /**
     * getLift
     * Description - Creates a 2D array of type string used to put the users recommended lift on
     * screen
     * @param lift - the string of the lift the user wants
     * @return - a 2D array used to put the reccommended lift on screen
     */
    public String[][] getLift(String lift) {
        String[][] workouts = new String[8][2];
        if (lift.equals("Chest")) {
            setLifts(workouts, "chest", "tricepLifts");
        } else if (lift.equals("Back")) {
            setLifts(workouts, "mainBackLifts", "backAccessories");
        } else if (lift.equals("Legs")) {
            setLifts(workouts, "mainLegLifts", "legAccessories");
        } else {
            setLifts(workouts, "shoulderLifts", "bicepLifts");
        }
        return workouts;
    }

    /**
     * setLifts
     * Description - Uses a random number generator and inputted string to set the 2D array with
     * all the necessary lifts to recommend to the user
     * @param workouts - 2D array holding the users recommended lifts
     * @param mainWorkout - String representing the key of the main workout
     * @param secondaryWorkout - String representing the key of the secondary workouts
     */
    private void setLifts(String[][] workouts, String mainWorkout, String secondaryWorkout) {
        Random rand = new Random();
        Set<Integer> numsChosen = new HashSet<>();
        String[] mainBodyPart = liftRecommender.get(mainWorkout);
        String[] secondary = liftRecommender.get(secondaryWorkout);

        for (int i = 0; i < 4; i++) {
            int num;
            do {
                num = rand.nextInt(mainBodyPart.length);
            } while (numsChosen.contains(num));
            numsChosen.add(num);
            workouts[i][0] = mainBodyPart[num];
            workouts[i][1] = setsXReps(rand);
        }

        numsChosen.clear();

        for (int i = 4; i < 8; i++) {
            int num;
            do {
                num = rand.nextInt(secondary.length);
            } while (numsChosen.contains(num));
            numsChosen.add(num);
            workouts[i][0] = secondary[num];
            workouts[i][1] = setsXReps(rand);
        }
    }

    /**
     * setChestAndTriLifts
     * Description - Creates the key and values associated for both triceps and chest lifts
     * @param liftRecommender - Map holding all the lifts
     */
    private void setChestAndTriLifts(Map<String, String[]> liftRecommender) {
        String[] chestLifts = {"Barbell bench press", "Barbell Incline press", "Barbell decline",
                "Dumbbell Bench", "Dumbbell incline", "Pec Dec", "Any variation of cable fly's",
                "Close grip bench x Chest Fly's Super set"};
        String[] tricepLifts = {"Skull Crusher", "Seated Single Arm Skull Crusher", "Rope pushdowns",
                "Rope overhead", "Straight bar push down", "Dips", "Diamond Push ups",
                "V-Bar push down", "Dip machine"};
        liftRecommender.put("chest", chestLifts);
        liftRecommender.put("tricepLifts", tricepLifts);
    }

    /**
     * setLefLifts
     * Description - Creates the key and values associated for both main and secondary leg lifts
     * @param liftRecommender - Map holding all the lifts
     */
    private void setLegLifts(Map<String, String[]> liftRecommender) {
        String[] mainLegLifts = {"Back Squat", "Front Squat", "Dead lift",
                "Power Clean", "Hack Squat", "Heavy Lunges", "Dumbbell Squat"};
        String[] legAccessories = {"Dumbbell Lunges", "Bulgarian Split Squats", "RDL",
                "Single leg RDL", "Split squat", "Box Jumps", "Leg Press", "Groin Machine",
                "Hip Thrusts", "Calf Raises", "Single Leg Squats", "20 Min Stair Master(Finisher)"};
        liftRecommender.put("mainLegLifts", mainLegLifts);
        liftRecommender.put("legAccessories", legAccessories);
    }

    /**
     * setBackAndBiLifts
     * Description - Creates the key and values associated for both back and bicep lifts
     * @param liftRecommender - Map holding all the lifts
     */
    private void setBackAndBiLifts(Map<String, String[]> liftRecommender) {
        String[] mainBackLifts = {"Barbell Row", "Machine Cable Row", "Lat Pull down", "Pull ups"};
        String[] backAccessories = {"Single Arm Dumbbell Row", "Seated dumbbell row",
                "Chest supported rows", "Single arm lat pull down", "Lat pullover",
                "Dumbbell Back Squeezes", "Shrugs"};
        String[] bicepLifts = {"Bicep curl (Dumbbell)", "Spider curl", "Hammer curl",
                "Incline curl", "Barbell curl", "Bicep Pull ups", "Rope Curls", "Preacher Curls"};
        liftRecommender.put("mainBackLifts", mainBackLifts);
        liftRecommender.put("backAccessories", backAccessories);
        liftRecommender.put("bicepLifts", bicepLifts);
    }

    /**
     * setShouldersAndArmsLifts
     * Description - Creates the key and values associated for shoulder lifts
     * @param liftRecommender - Map holding all the lifts
     */
    private void setShouldersAndArmsLifts(Map<String, String[]> liftRecommender) {
        String[] shoulderLifts = {"Barbell shoulder press", "Dumbbell shoulder press", "Lat raises",
                "Front raises (Barbell or Dumbbell)", "Cable Lat raises", "Rear Delt Machine",
                "Bench Shoulder superset", "Arnold Press"};
        liftRecommender.put("shoulderLifts", shoulderLifts);
    }
}