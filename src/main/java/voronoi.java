import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.awt.geom.Point2D;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class voronoi extends JFrame {
    static double p = 3;
    static BufferedImage Imagen;
    static int px[], py[], color[], cells = 127, size = 1000;

    static Point2D points[];
    static double sz=1000;

    public voronoi() {
        super("Voronoi Diagram");
        setBounds(0, 0, size, size);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int n = 0;
        Random rand = new Random();
        Imagen = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        px = new int[cells];
        py = new int[cells];
        points=new Point2D[cells];
        color = new int[cells];

        for (int i = 0; i < cells; i++) {
            //px[i] = rand.nextInt(size);
            //py[i] = rand.nextInt(size);
            points[i]= new Point2D.Double(rand.nextInt(size),rand.nextInt(size));
            color[i] = rand.nextInt(16777215);
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                n = 0;
                for (byte i = 0; i < cells; i++) {
                    //if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) {
                    if (points[i].distance(x,y)< points[n].distance(x,y)){
                        n = i;
                    }
                }
                Imagen.setRGB(x, y, color[n]);
            }
        }
        Graphics2D g = Imagen.createGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < cells; i++) {
            g.fill(new Ellipse2D.Double(points[i].getX() - 2.5, points[i].getY() - 2.5, 5, 5));
        }

        try {
            ImageIO.write(Imagen, "png", new File("voronoi.png"));
        } catch (IOException e) {

        }

    }

    public void paint(Graphics g) {
        g.drawImage(Imagen, 0, 0, this);
    }

/*
    static double distance(int x1, int x2, int y1, int y2) {
        double d;
        d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidian
        //  d = Math.abs(x1 - x2) + Math.abs(y1 - y2); // Manhattan
        //  d = Math.pow(Math.pow(Math.abs(x1 - x2), p) + Math.pow(Math.abs(y1 - y2), p), (1 / p)); // Minkovski
        return d;
    }
*/

    public static void main(String[] args) {
        new voronoi().setVisible(true);
    }
}
