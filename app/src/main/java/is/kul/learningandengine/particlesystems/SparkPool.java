package is.kul.learningandengine.particlesystems;



	import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

	public class SparkPool {
		// ===========================================================
		// PhysicsConstants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private static final GenericPool<Spark> POOL = new GenericPool<Spark>() {

			@Override
			protected Spark onAllocatePoolItem() {
				// TODO Auto-generated method stub
				return new Spark();
			}
		
		};

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

	

		public static Spark obtain(float pX, float pY, float temp, FireParticleSystem parent) {
			Spark spark = SparkPool.POOL.obtainPoolItem();
			spark.setSystem(parent);
			spark.setPosition(pX, pY);
			spark.setTemperature(temp);
			spark.age = 0;
			
			return spark;
		}

		public static void recycle(Spark spark) {
            SparkPool.POOL.recyclePoolItem(spark);
			
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	
}
