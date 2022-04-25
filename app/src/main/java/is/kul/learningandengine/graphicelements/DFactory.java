package is.kul.learningandengine.graphicelements;

public class DFactory {

	
	public enum DrawingType // Let's avoid java.lang.* clashes
	{
		TYPEPOINT(0), TYPEJOINT1(1), TYPEJOINT2(2);

	    private final int value;

	    DrawingType(int value)
	    {
	        this.value = value;
	    }

	    public int getValue()
	    {
	        return this.value;
	    }
	}
	
	
	private static final DFactory INSTANCE = new DFactory();

	private DFactory() {
	}

	public static 	DFactory getInstance() {
		return DFactory.INSTANCE;
	}
	
	public void create() {

        this.createCircleModels();
	}
	static final int[] N_POINT_VERTICES= {4,8,4};
	static final int[] POINT_THICKNESS= {1,2,2};
	static final float[] POINT_RAY = {3f,3f,1.5f};

	static float[][][] circle_vertices;
	void createCircleModels() 
	{
        DFactory.circle_vertices = new float[3][10][2];
		for(int j=0;j<3;j++){
	
		 
		for(int i = 0; i < DFactory.N_POINT_VERTICES[j]; i++)
		{ 
			float theta = (float) (2* Math.PI* (float)i / (float) DFactory.N_POINT_VERTICES[j]);//get the current angle

            DFactory.circle_vertices[j][i][0] = (float) (DFactory.POINT_RAY[j] * Math.cos(theta));//calculate the x component
            DFactory.circle_vertices[j][i][1] = (float) (DFactory.POINT_RAY[j] * Math.sin(theta));//calculate the y component
} 
		}	
		
	}



}
