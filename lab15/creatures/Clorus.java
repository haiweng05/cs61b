package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;
    private double energy;

    private Random random;

    public Clorus() {
        this(1);
    }
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
        random = new Random();
    }

    @Override
    public double energy() {
        return energy;
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }
    @Override
    public void move() {
        energy -= 0.03d;
        if (energy < 0) {
            energy = 0d;
        }
    }

    @Override
    public void stay() {
        energy -= 0.01d;
        if (energy < 0) {
            energy = 0d;
        }
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public String name() {
        return "clorus";
    }

    @Override
    public Clorus replicate() {
        Clorus c = new Clorus(energy / 2);
        energy /= 2;
        return c;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        String left = neighbors.get(Direction.LEFT).name();
        String right = neighbors.get(Direction.RIGHT).name();
        String up = neighbors.get(Direction.TOP).name();
        String down = neighbors.get(Direction.BOTTOM).name();

        boolean leftPass = left.equals("empty");
        boolean rightPass = right.equals("empty");
        boolean upPass = up.equals("empty");
        boolean downPass = down.equals("empty");
        List<Direction> listEmpty = new ArrayList<>();
        if (leftPass) {
            listEmpty.add(Direction.LEFT);
        }
        if (rightPass) {
            listEmpty.add(Direction.RIGHT);
        }
        if (upPass) {
            listEmpty.add(Direction.TOP);
        }
        if (downPass) {
            listEmpty.add(Direction.BOTTOM);
        }

        if (listEmpty.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else{
            boolean leftPlip = left.equals("plip");
            boolean rightPlip = right.equals("plip");
            boolean upPlip = up.equals("plip");
            boolean downPlip = down.equals("plip");
            List<Direction> listPlip = new ArrayList<>();
            if (leftPlip) {
                listPlip.add(Direction.LEFT);
            }
            if (rightPlip) {
                listPlip.add(Direction.RIGHT);
            }
            if (upPlip) {
                listPlip.add(Direction.TOP);
            }
            if (downPlip) {
                listPlip.add(Direction.BOTTOM);
            }

            if (!listPlip.isEmpty()) {
                Direction d = listPlip.get(random.nextInt(0, listPlip.size()));
                return new Action(Action.ActionType.ATTACK, d);
            } else if (energy > 1.0d) {
                Direction d = listEmpty.get(random.nextInt(0, listEmpty.size()));
                return new Action(Action.ActionType.REPLICATE, d);
            } else {
                Direction d = listEmpty.get(random.nextInt(0, listEmpty.size()));
                return new Action(Action.ActionType.MOVE, d);
            }
        }
    }
}
