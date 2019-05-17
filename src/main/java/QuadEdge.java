import java.awt.geom.Point2D;

public class QuadEdge {

    Edge e[];

    public QuadEdge(){

        e[0] =new Edge(0, null, null,this);
        e[1]=new Edge(1, null, null,this);
        e[2]=new Edge(2, null, null,this);
        e[3]=new Edge(3, null, null,this);

        e[0].next=e[0];
        e[1].next=e[3];
        e[2].next=e[2];
        e[3].next=e[1];
    }

}
