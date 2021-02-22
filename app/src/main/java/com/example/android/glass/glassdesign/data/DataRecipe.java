package com.example.android.glass.glassdesign.data;

public class DataRecipe{

    private String step;
    private int temp1;
    private int time1;
    private int time2;
    private int pressure;

    public DataRecipe() {
    }

    public DataRecipe(String step, int temp1, int time1, int time2, int pressure) {
        this.step = step;
        this.temp1 = temp1;
        this.time1 = time1;
        this.time2 = time2;
        this.pressure = pressure;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public int getTemp1() {
        return temp1;
    }

    public void setTemp1(int temp1) {
        this.temp1 = temp1;
    }

    public int getTime1() {
        return time1;
    }

    public void setTime1(int time1) {
        this.time1 = time1;
    }

    public int getTime2() {
        return time2;
    }

    public void setTime2(int time2) {
        this.time2 = time2;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
}
