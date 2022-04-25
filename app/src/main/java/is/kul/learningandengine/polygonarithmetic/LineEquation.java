package is.kul.learningandengine.polygonarithmetic;

import com.badlogic.gdx.math.Vector2;

public class LineEquation{
    public LineEquation(Vector2 start, Vector2 end){
        Start = start;
        End = end;

        IsVertical = Math.abs(End.x - start.x) < 0.00001f;
        M = (End.y - Start.y)/(End.x - Start.x);
        A = -M;
        B = 1;
        C = Start.y - M*Start.x;
    }

    public boolean IsVertical;

    public float M;

    public Vector2 Start;
    public Vector2 End;

    public float A;
    public float B;
    public float C;

    public Vector2 IntersectsWithLine(LineEquation otherLine){
        Vector2 intersectionPoint = new Vector2(0, 0);
        if (IsVertical && otherLine.IsVertical)
            return null;
        if (IsVertical || otherLine.IsVertical)return
            intersectionPoint = GetIntersectionPointIfOneIsVertical(otherLine, this);
            
        
        float delta = A*otherLine.B - otherLine.A*B;
        boolean hasIntersection = Math.abs(delta - 0) > 0.0001f;
        if (hasIntersection){
            float x = (otherLine.B*C - B*otherLine.C)/delta;
            float y = (A*otherLine.C - otherLine.A*C)/delta;
            intersectionPoint = new Vector2(x, y);
        }
        return intersectionPoint;
    }

    private static Vector2 GetIntersectionPointIfOneIsVertical(LineEquation line1, LineEquation line2){
        LineEquation verticalLine = line2.IsVertical ? line2 : line1;
        LineEquation nonVerticalLine = line2.IsVertical ? line1 : line2;
        float y = (verticalLine.Start.x - nonVerticalLine.Start.x)*
                   (nonVerticalLine.End.y - nonVerticalLine.Start.y)/
                   ((nonVerticalLine.End.x - nonVerticalLine.Start.x)) +
                   nonVerticalLine.Start.y;
        float x = line1.IsVertical ? line1.Start.x : line2.Start.x;
        return new Vector2(x, y);
    }

 
   
}
