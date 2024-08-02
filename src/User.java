/**
 * Author - Kyle Chambers
 * File name - User
 * Date of last update - 7/12/2024
 * User file description - The user file represents a single user, where all the user data is
 * stored and accessible.
 * Updates to come - None as of now
 **/

import java.time.*;
import java.time.format.DateTimeFormatter;

public class User {
    public String name;
    public String email;
    public String userName;
    public String password;
    public int age;
    public int weight;
    public int maxBench;
    public int maxSquat;
    public int maxDeadlift;
    public int maxPullups;
    public int totalWeight;
    public int maxBarbellRow;
    public String todaysDate;
    public int caloriesBurned;
    public double timeWorkout;
    public double todaysPace;
    public double milesRanToday;
    public String todaysLift;

    /**
     * Default constructor
     * Description - Initializes all the users data to the baseline data
     */
    public User(){
        age = 0;
        email = "";
        userName = "";
        password = "";
        name = "";
        weight = 0;
        maxBarbellRow = 0;
        maxPullups = 0;
        maxBench = 0;
        maxDeadlift = 0;
        maxSquat = 0;
        todaysDate = setTodaysDate();
        timeWorkout = 0;
        todaysPace = 0;
        milesRanToday = 0;
        todaysLift = "";
    }

    /**
     * Alternate Constructor
     * Description - Initializes the users data to the passed values given for each data point
     * @param userName - String representing the username of the user
     * @param name - String representing the name of the user
     * @param age - Int representing the age of the user
     * @param weight - Int representing the weight of the user
     * @param maxBench - Int representing the bench of the user
     * @param maxSquat  - Int representing the squat of the user
     * @param maxDeadlift - Int representing the dead lift of the user
     * @param maxPullups - Int representing the pullups of the user
     * @param maxBarbellRow - Int representing the barbell row of the user
     * @param totalWeight - Int representing the total weight of the user
     */
    public User(String userName, String name, int age, int weight, int maxBench, int maxSquat,
                int maxDeadlift, int maxPullups, int maxBarbellRow, int totalWeight) {
        todaysDate = setTodaysDate();
        this.userName = userName;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.maxBench = maxBench;
        this.maxSquat = maxSquat;
        this.maxDeadlift = maxDeadlift;
        this.maxPullups = maxPullups;
        this.maxBarbellRow = maxBarbellRow;
        this.totalWeight = totalWeight;
    }

    /**
     * setTodaysDate
     * Description - Sets the users date to today's date using the local date object
     * @return a string representing today's date
     */
    private String setTodaysDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return today.format(formatter);
    }
}
