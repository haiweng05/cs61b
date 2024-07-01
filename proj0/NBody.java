import java.util.function.ToDoubleFunction;

public class NBody {
    public static int readN(String path) {
        In in = new In(path);
        return in.readInt();
    }
    public static double readRadius(String path) {
        In in = new In(path);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String path) {
        In in = new In(path);
        int N = in.readInt();
        in.readDouble();
        Planet[] planets = new Planet[N];
        for(int i = 0; i < N; ++ i){
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String img = in.readString();

            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, img);
        }

        return planets;
    }

    public static void main(String[] argv) {
        double T = Double.parseDouble(argv[0]);
        double dt = Double.parseDouble(argv[1]);
        String filename = argv[2];
        int N = readN(filename);
        double R = readRadius(filename);
        Planet[] planets = readPlanets(filename); 

        StdDraw.setScale(-R,R);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        
        for(int i = 0; i < N; ++ i){
            StdDraw.picture(planets[i].xxPos,planets[i].yyPos,"images/" + planets[i].imgFileName);
        }
        StdDraw.show();

        StdDraw.enableDoubleBuffering();

        double t = 0d;
        while(t <= T) {
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for(int i = 0; i < N; ++ i){
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for(int i = 0; i < N; ++ i){
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.clear();
            StdDraw.picture(0, 0, "images/starfield.jpg");
            
            for(int i = 0; i < N; ++ i){
                StdDraw.picture(planets[i].xxPos,planets[i].yyPos,"images/" + planets[i].imgFileName);
            }
            StdDraw.show();
            StdDraw.pause(10);

            t += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", R);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                          planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
    }
}
