package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.zip.DeflaterInputStream;

/** An implementation of a motile pacifist photosynthesizer.
 *  @author Josh Hug
 */
public class Plip extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    private Random random;

    /** creates plip with energy equal to E. */
    public Plip(double e) {
        super("plip");
        r = 99;
        g = 0;
        b = 76;
        energy = e;
        random = new Random();
    }

    /** creates a plip with energy equal to 1. */
    public Plip() {
        this(1);
    }

    /** Should return a color with red = 99, blue = 76, and green that varies
     *  linearly based on the energy of the Plip. If the plip has zero energy,
     *  it should have a green value of 63. If it has max energy, it should
     *  have a green value of 255. The green value should vary with energy
     *  linearly in between these two extremes. It's not absolutely vital
     *  that you get this exactly correct.
     */
    public Color color() {
        g = (int) (63 + (255 - 63) * energy / 2);
        return color(r, g, b);
    }

    /** Do nothing with C, Plips are pacifists. */
    public void attack(Creature c) {
    }

    /** Plips should lose 0.15 units of energy when moving. If you want to
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        energy -= 0.15d;
        if (energy < 0d) {
            energy = 0d;
        }
    }


    /** Plips gain 0.2 energy when staying due to photosynthesis. */
    public void stay() {
        energy += 0.2d;
        if (energy > 2d) {
            energy = 2.0d;
        }
    }

    /** Plips and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Plip.
     */
    public Plip replicate() {
        Plip p = new Plip(energy / 2);
        energy /= 2;
        return p;
    }

    /** Plips take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if energy >= 1, REPLICATE.
     *  3. Otherwise, if any Cloruses, MOVE with 50% probability.
     *  4. Otherwise, if nothing else, STAY
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
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
        } else if (energy > 1.0d && !listEmpty.isEmpty()) {
            Direction d = listEmpty.get(random.nextInt(0, listEmpty.size()));
            return new Action(Action.ActionType.REPLICATE, d);
        } else {
            boolean leftClorus = left.equals("clorus");
            boolean rightClorus = right.equals("clorus");
            boolean upClorus = up.equals("clorus");
            boolean downClorus = down.equals("clorus");

            if (leftClorus || rightClorus || upClorus || downClorus) {
                double r = random.nextGaussian();
                if (r < 0.5d && !listEmpty.isEmpty()) {
                    return new Action(Action.ActionType.STAY);
                } else {
                    Direction d = listEmpty.get(random.nextInt(0, listEmpty.size()));
                    return new Action(Action.ActionType.MOVE, d);
                }
            } else {
                return new Action(Action.ActionType.STAY);
            }
        }
    }

}
