package de.joshua.gameobjects;

import de.joshua.util.Coordinate;
import de.joshua.util.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class Tower extends GameObject {
    public double turningDirection = 0;
    boolean ableToShoot;
    Coordinate towerCoordinate;
    double cannonLength;
    Missile[] missile = new Missile[20];
    Missile missile1;
    boolean startRound = true;

    public Tower(Coordinate towerCoordinate, double width, double height) {

        super(towerCoordinate, width, height);
        this.towerCoordinate = towerCoordinate;

        for(int i = 0; i < 20; i++) {
            missile[i] = new Missile(towerCoordinate, getWidth(), getHeight(), getWidth() * 0.75, turningDirection);
        }
    }

    @Override
    public void paintMe(java.awt.Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        paintTower(g2d);
    }

    public void setDeltaMovingAngle(String direction, double changeMovingAngle){
        if(direction.equals("+")){
            this.turningDirection = turningDirection + changeMovingAngle;
        }else{
            this.turningDirection = turningDirection - changeMovingAngle;
        }
    }

    private void paintTower(Graphics2D g2d) {
        Ellipse2D towerBody = new Ellipse2D.Double(getObjectPosition().getX(), getObjectPosition().getY(), getWidth()*0.8, getHeight()*0.8);

        RoundRectangle2D tankCannonPipe = new RoundRectangle2D.Double(getObjectPosition().getX() + getWidth()*(0.75/2),
                getObjectPosition().getY() + getHeight() *(0.16/0.5),
                getHeight() * 0.75, getWidth()
                * 0.16, 5, 5);

        double f = 0.4;
        RoundRectangle2D tankTurret = new RoundRectangle2D.Double(getObjectPosition().getX() + getWidth() * f/2,
                getObjectPosition().getY() + getHeight() * (f/2),
                getWidth() * f, getHeight() * f, 15, 8);


        AffineTransform transform = new AffineTransform();
        transform.rotate(turningDirection, towerBody.getCenterX(), towerBody.getCenterY());

        g2d.setColor(Color.decode("#02A272"));
        Shape transformed = transform.createTransformedShape(towerBody);
        g2d.fill(transformed);
        g2d.setColor(Color.DARK_GRAY);
        transformed = transform.createTransformedShape(tankCannonPipe);
        g2d.fill(transformed);

        transformed = transform.createTransformedShape(tankTurret);
        g2d.fill(transformed);


        missile[0].paintMe(g2d);
        for(int i = 0; i < 19; i++){
            if(missile[i].shootRange >= 50) {
                missile[i+1].paintMe(g2d);
            }
        }
    }

    public void shoot() {
        for(int i = 0; i < 20; i++){
            if(missile[i].shootRange > 0){
                missile[i].rotateTrue = false;
                System.out.println("Missile " + i + ":     " + missile[i].endDirection);
            }
        }
        missile[0].shootRange++;
        missile[0].turningDirection = turningDirection;
        if(startRound){
            for(int i = 0; i < 19; i++){
                if(missile[i].shootRange >= 50){
                    missile[i+1].shootRange++;
                    missile[i+1].turningDirection = turningDirection;
                }
            }
        }else{
            for(int i = 0; i < 19; i++){
                    missile[i+1].shootRange++;
                    missile[i+1].turningDirection = turningDirection;
            }
        }

        for(int i = 0; i < 20; i++){
            if(missile[i].shootRange == 1000){
                missile[i].shootRange = 0;
                missile[i].rotateTrue = true;
                missile[i]. firstRound = true;
                startRound = false;
            }
        }

    }


}
