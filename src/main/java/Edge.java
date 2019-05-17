import java.awt.geom.Point2D;


public class Edge implements EdgeInterface {

    public int num;
    Edge next;
    Point2D data;
    QuadEdge QuadEdge;

    public Edge(int num, Edge next, Point2D data, QuadEdge quadEdge) {
        this.num = num;
        this.next = next;
        this.data = data;
        QuadEdge = quadEdge;
    }


    public Edge Rot() {
        if (num<3){
            return this.QuadEdge.e[num+1];
        }
        return this.QuadEdge.e[num-3];
    }

    public Edge invRot() {
        if (num>0){
            return this.QuadEdge.e[num-1];
        }
        return this.QuadEdge.e[num+3];
    }

    public Edge Sym() {
        if (num<2){
            return this.QuadEdge.e[num+2];
        }
        return this.QuadEdge.e[num-2];
    }

    public Edge Onext() {
        return this.QuadEdge.e[num].next;
    }

    public Edge Oprev() {
        return this.Rot().Onext().Rot();
    }

    public Edge Dnext() {
        return this.Sym().Onext().Sym();
    }

    public Edge Dprev() {
        return this.invRot().Onext().invRot();
    }

    public Edge Lnext() {
        return this.invRot().Onext().Rot();
    }

    public Edge Lprev() {
        return this.Onext().Sym();
    }

    public Edge Rnext() {
        return this.Rot().Onext().invRot();
    }

    public Edge Rprev() {
        return this.Sym().Onext();
    }

    public Point2D Org(){
        return this.data;
    }

    public Point2D Dest(){
        return this.Sym().data;
    }

    public Point2D Org2d(){
        return this.data;
    }

    public Point2D Dest2d(){
        if (this.num<2){
            return this.QuadEdge.e[num+2].data;
        }
        return  this.QuadEdge.e[num-2].data;
    }

    public void EndPoints(Point2D or, Point2D de){
        this.data=or;
        this.Sym().data=de;
    }

    public static Edge MakeEdge(){
        QuadEdge qe= new QuadEdge();
        return qe.e[0];
    }

    public static void Splice(Edge a, Edge b){
        Edge alpha=a.Onext().Rot();
        Edge beta=b.Onext().Rot();

        Edge t1= b.Onext();
        Edge t2= a.Onext();
        Edge t3=beta.Onext();
        Edge t4=alpha.Onext();

        a.next=t1;
        b.next=t2;
        alpha.next=t3;
        beta.next=t4;
    }

    public static void DeleteEdge(Edge e){
        Splice(e,e.Oprev());
        Splice(e.Sym(),e.Sym().Oprev());
    }

    public static Edge Connect(Edge a, Edge b){
        Edge e=Edge.MakeEdge();
        Edge.Splice(e,a.Lnext());
        Edge.Splice(e.Sym(),b);
        e.EndPoints(a.Dest(),b.Org());
        return e;
    }

    public static void Swap(Edge e){
        Edge a=e.Oprev();
        Edge b=e.Sym().Oprev();
        Edge.Splice(e,a);
        Splice(e.Sym(),b);
        Splice(e,a.Lnext());
        Splice(e.Sym(),b.Lnext());
        e.EndPoints(a.Dest(),b.Dest());
    }
}
