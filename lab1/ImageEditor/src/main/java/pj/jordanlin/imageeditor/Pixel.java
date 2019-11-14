package pj.jordanlin.imageeditor;

public class Pixel
{
    public int r,g,b;

    Pixel(int r, int g,int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    int getAverage()
    {
        int c = (r+b+g)/3;
        return c;
    }
}


