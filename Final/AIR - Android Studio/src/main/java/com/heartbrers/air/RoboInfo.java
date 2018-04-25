package com.heartbrers.air;

/**
 * Created by nrgz on 10.04.2018.
 */

 //This class contains information about the robot
public class RoboInfo {
    private int robotIconId; //Id for icon of the robot
    private String robotName; //Name of the robot
    private String robotId; //Unique identifier of the robot
    private int robotIndex; //Index of the robot in SharedPreferences
    private long robotTimestamp; //Timestamp that robot was last modified


    //Constructor
    public RoboInfo(int robotIconId, String robotName, String robotId, int robotIndex) {
        this.robotIconId = robotIconId;
        this.robotName = robotName;
        this.robotId = robotId;
        this.robotIndex = robotIndex;
    }

    //Getters & Setters
    public long getRobotTimestamp() {
        return robotTimestamp;
    }

    public void setRobotTimestamp(long robotTimestamp) {
        this.robotTimestamp = robotTimestamp;
    }

    public int getRobotIconId() {
        return robotIconId;
    }

    public String getRobotName() {
        return robotName;
    }

    public String getRobotId() {
        return robotId;
    }

    public int getRobotIndex() {
        return robotIndex;
    }
}

