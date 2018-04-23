package com.example.heartbrers.air;

/**
 * Created by nrgz on 10.04.2018.
 */

public class RoboInfo {
    private int robotIconId;
    private String robotName;
    private String robotId;
    private int robotIndex;

    public RoboInfo(int robotIconId, String robotName, String robotId, int robotIndex) {
        this.robotIconId = robotIconId;
        this.robotName = robotName;
        this.robotId = robotId;
        this.robotIndex = robotIndex;
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

