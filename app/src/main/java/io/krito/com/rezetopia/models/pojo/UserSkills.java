package io.krito.com.rezetopia.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 6/11/2018.
 */

public class UserSkills {
    @SerializedName("attack")
    @Expose
    private int attack;

    @SerializedName("stamina")
    @Expose
    private int stamina;

    @SerializedName("defence")
    @Expose
    private int defence;

    @SerializedName("speed")
    @Expose
    private int speed;

    @SerializedName("ball_control")
    @Expose
    private int ballControl;

    @SerializedName("low_pass")
    @Expose
    private int lowPass;

    @SerializedName("lofted_pass")
    @Expose
    private int loftedPass;

    @SerializedName("shoot_accuracy")
    @Expose
    private int shootAccuracy;

    @SerializedName("shoot_power")
    @Expose
    private int shootPower;

    @SerializedName("free_kicks")
    @Expose
    private int freeKicks;

    @SerializedName("header")
    @Expose
    private int header;

    @SerializedName("jump")
    @Expose
    private int jump;

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getBallControl() {
        return ballControl;
    }

    public void setBallControl(int ballControl) {
        this.ballControl = ballControl;
    }

    public int getLowPass() {
        return lowPass;
    }

    public void setLowPass(int lowPass) {
        this.lowPass = lowPass;
    }

    public int getLoftedPass() {
        return loftedPass;
    }

    public void setLoftedPass(int loftedPass) {
        this.loftedPass = loftedPass;
    }

    public int getShootAccuracy() {
        return shootAccuracy;
    }

    public void setShootAccuracy(int shootAccuracy) {
        this.shootAccuracy = shootAccuracy;
    }

    public int getShootPower() {
        return shootPower;
    }

    public void setShootPower(int shootPower) {
        this.shootPower = shootPower;
    }

    public int getFreeKicks() {
        return freeKicks;
    }

    public void setFreeKicks(int freeKicks) {
        this.freeKicks = freeKicks;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }
}
