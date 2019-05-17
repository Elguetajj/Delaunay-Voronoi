import org.graalvm.compiler.asm.sparc.SPARCAssembler;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Subdivision {

    private Edge StartingEdge;

    private Edge Locate(Point2D x){
        Edge e=StartingEdge;

        while (true){

            if (x==e.Org2d() || x==e.Dest2d()){
                return e;
            }
            else if (RightOf(x,e)){
                e=e.Sym();
            }
            else if (!RightOf(x,e.Onext())){
                e=e.Onext();
            }
            else if (!RightOf(x,e.Dprev())){
                e=e.Dprev();
            }
            else{
                return e;
            }
        }


    }
    public Subdivision(Point2D a, Point2D b, Point2D c){
        Point2D da, db, dc;
        da= a;
        db=b;
        dc=c;

        Edge ea= Edge.MakeEdge();
        ea.EndPoints(da,db);
        Edge eb=Edge.MakeEdge();
        Edge.Splice(ea.Sym(),eb);
        eb.EndPoints(db,dc);
        Edge ec=Edge.MakeEdge();
        Edge.Splice(eb.Sym(),ec);
        ec.EndPoints(dc,da);
        Edge.Splice(ec.Sym(),ea);
        StartingEdge=ea;
    }
    public void InsertSite(Point2D x){
        Edge e=Locate(x);
        if ((x==e.Org2d())||(x==e.Dest2d())){
            return;
        } else if (OnEdge(x,e)){
            e=e.Oprev();
            Edge.DeleteEdge(e.Onext());
        }
        Edge base=Edge.MakeEdge();
        Point2D newPoint=x;
        base.EndPoints(e.Org(),newPoint);
        Edge.Splice(base,e);
        StartingEdge=base;
        do{
            base=Edge.Connect(e, base.Sym());
            e=base.Oprev();
        } while (e.Lnext()!=StartingEdge);

        do{
            Edge t= e.Oprev();
            if (RightOf(t.Dest2d(),e) && InCircle(e.Org2d(),t.Dest2d(),e.Dest2d(),x)){
                Edge.Swap(e);
                e=e.Oprev();
            }else if (e.Onext()==StartingEdge){
                return;
            }else {
                e=e.Onext().Lprev();
            }
        }while (true);

    }
    public void Draw(){

    };

    public static double TriArea(Point2D a, Point2D b, Point2D c){
        return (b.getX()-a.getX())*(c.getY()-a.getY())-(b.getY()-a.getY())*(c.getX()-a.getX());
    }

    public static boolean InCircle(Point2D a, Point2D b, Point2D c, Point2D d){
        return ((a.getX()*a.getX()+a.getY()*a.getY())* TriArea(b,c,d)-
                (b.getX()*b.getX()+b.getY()*b.getY())*TriArea(a,c,d)+
                (c.getX()*c.getX()+c.getY()*c.getY())*TriArea(a,b,d)-
                (d.getX()*d.getX()+d.getY()*d.getY())*TriArea(a,b,c) )>0;
    }

    public static boolean CCW(Point2D a, Point2D b, Point2D c){
        return (TriArea(a,b,c)>0);
    }

    public static boolean RightOf(Point2D x, Edge e){
        return CCW(x,e.Dest2d(),e.Org2d());
    }

    public static boolean LeftOf(Point2D x, Edge e){
        return CCW(x,e.Org2d(),e.Dest2d());
    }

    public static boolean OnEdge(Point2D x, Edge e){
        double t1,t2,t3;
        t1=(((x.getX()-e.Org2d().getX())*(x.getX()-e.Org2d().getX()))+((x.getY()-e.Org2d().getY())*(x.getY()-e.Org2d().getY())));
        t2=(((x.getX()-e.Dest2d().getX())*(x.getX()-e.Dest2d().getX()))+((x.getY()-e.Dest2d().getY())*(x.getY()-e.Dest2d().getY())));
        if (t1<0.000001 || t2<0.000001){
            return true;
        }
        t3=(((e.Org2d().getX()-e.Dest2d().getX())*(e.Org2d().getX()-e.Dest2d().getX()))+((e.Org2d().getY()-e.Dest2d().getY())*(e.Org2d().getY()-e.Dest2d().getY())));
        if (t1>t3 || t2>t3){
            return false;
        }
        Line2D line=new Line2D.Double(e.Org2d(),e.Dest2d());
        return (line.ptLineDist(x)<0.000001);
    }

}
