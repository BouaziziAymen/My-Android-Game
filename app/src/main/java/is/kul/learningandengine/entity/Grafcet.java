package is.kul.learningandengine.entity;

import android.util.Log;

class Grafcet{
	/*
	 * gr = new grafcet
			(new int[][]{{15,15},{15,15},{15,15},{15,15}},
					new int[][]{{3,3},{-3,-3},{3,-3},{-3,3}},	
					
					new int[][]{{3,1},{3,1},{4,2},{4,2}},new int[]{2,2,2,2}, 4);
					
					
					
					
					
					
					
					
					
					for(int i=0;i<gr.numStat[gr.current];i++){
			performAction(gr.actions[gr.current][i],gr.speeds[gr.current][i]);
		Log.e("actions",""+gr.actions[gr.current][i]);
		}
					
					
					*/
	
	
	boolean[][] state;
	int[][]actions;
	int[][]speeds;
	int[] numStat;
	boolean[] transition;
	int[][] time;
	int[][] ltime;
	int current;
	int j;
	int NTot;
	public void showActions(){
		String s= "";
		for(int i = 0; i< this.numStat[this.current]; i++){
			s+=","+ this.actions[this.current][i];
		}
		Log.e("State",s);
	}
	
	public void run(){
		
	
		if(this.transition[this.current])
		{   if(this.current < this.NTot -1)
            this.current++;else this.current =0;
            this.showActions();
			for(int i = 0; i< this.numStat[this.current]; i++)
                this.state[this.current][i]=false;
            this.transition[this.current]=false;
			
			for(int i = 0; i< this.numStat[this.current]; i++){
                this.state[this.current][i]=true;
                this.time[this.current][i]=0;}
		}
        this.j =0;
		for(int i = 0; i< this.numStat[this.current]; i++){
            this.time[this.current][i]++;
		if(this.time[this.current][i]>= this.ltime[this.current][i]) this.j++;
		}
		if(this.j == this.numStat[this.current]) this.transition[this.current]=true;
				
	}
		public Grafcet(int[][] ltime,int[][] speeds,int[][] actions,int[] numStat,int NTot){
			this.NTot=NTot;
            this.current = 0;

            this.time = new int[NTot][4];
            this.state =new boolean[NTot][4];
			for(int i=0;i<numStat[0];i++)
                this.state[0][i]=true;
            this.transition = new boolean[NTot];
			this.numStat= new int[NTot];
			for(int i=0;i<NTot;i++)
				this.numStat[i]=numStat[i];	
			
			this.ltime= new int[NTot][4];
			for(int i=0;i<NTot;i++)
				for(int k=0;k<numStat[i];k++)
				this.ltime[i][k]=ltime[i][k];
			
			this.speeds= new int[NTot][4];
			for(int i=0;i<NTot;i++)
				for(int k=0;k<numStat[i];k++)
				this.speeds[i][k]=speeds[i][k];
			
			
			this.actions= new int[NTot][4];
			for(int i=0;i<NTot;i++)
				for(int k=0;k<numStat[i];k++)
				this.actions[i][k]=actions[i][k];

            this.showActions();
		}
		
	}
