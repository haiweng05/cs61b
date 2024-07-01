public class Planet{
    static double G = 6.67e-11;
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos));
    }    

    public double calcForceExertedBy(Planet p) {
        double dis = calcDistance(p);
        return Planet.G * mass * p.mass / (dis * dis);
    }

    public double calcForceExertedByX(Planet p) {
        return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double netX = 0d;
        for(int i = 0; i < planets.length; ++ i) {
            if(this.equals(planets[i])){
                continue;
            }
            netX += calcForceExertedByX(planets[i]);
        }
        return netX;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double netY = 0d;
        for(int i = 0; i < planets.length; ++ i) {
            if(this.equals(planets[i])){
                continue;
            }
            netY += calcForceExertedByY(planets[i]);
        }
        return netY;
    }

    public void update(double dt, double forceX, double forceY) {
        double aX = forceX / mass;
        double aY = forceY / mass;
        xxVel += aX * dt;
        yyVel += aY * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }
}