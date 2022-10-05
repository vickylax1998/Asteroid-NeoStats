package com.ranium.pojo;

public class NeoFeed {
    String asteriod_id;
    float speed;
    float distance;
    float min_size;
    float max_size;

    public NeoFeed(String asteriod_id, float speed, float distance, float min_size, float max_size) {
        this.asteriod_id = asteriod_id;
        this.speed = speed;
        this.distance = distance;
        this.min_size = min_size;
        this.max_size = max_size;
    }

    public float getMin_size() {
        return min_size;
    }

    public void setMin_size(float min_size) {
        this.min_size = min_size;
    }

    public float getMax_size() {
        return max_size;
    }

    public void setMax_size(float max_size) {
        this.max_size = max_size;
    }

    public String getAsteriod_id() {
        return asteriod_id;
    }

    public void setAsteriod_id(String asteriod_id) {
        this.asteriod_id = asteriod_id;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }


}
