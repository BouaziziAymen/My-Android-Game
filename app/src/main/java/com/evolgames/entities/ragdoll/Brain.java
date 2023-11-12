package com.evolgames.entities.ragdoll;


class Brain {

  boolean[][] state;
  int[][] actions;
  int[][] speeds;
  int[] numStat;
  boolean[] transition;
  int[][] time;
  int[][] ltime;
  int current;
  int j;
  int NTot;

  public Brain(int[][] ltime, int[][] speeds, int[][] actions, int[] numStat, int NTot) {
    this.NTot = NTot;
    this.current = 0;

    this.time = new int[NTot][4];
    this.state = new boolean[NTot][4];
    for (int i = 0; i < numStat[0]; i++) this.state[0][i] = true;
    this.transition = new boolean[NTot];
    this.numStat = new int[NTot];
    System.arraycopy(numStat, 0, this.numStat, 0, NTot);

    this.ltime = new int[NTot][4];
    for (int i = 0; i < NTot; i++)
      if (numStat[i] >= 0) System.arraycopy(ltime[i], 0, this.ltime[i], 0, numStat[i]);

    this.speeds = new int[NTot][4];
    for (int i = 0; i < NTot; i++)
      if (numStat[i] >= 0) System.arraycopy(speeds[i], 0, this.speeds[i], 0, numStat[i]);

    this.actions = new int[NTot][4];
    for (int i = 0; i < NTot; i++)
      if (numStat[i] >= 0) System.arraycopy(actions[i], 0, this.actions[i], 0, numStat[i]);

    this.showActions();
  }

  public void showActions() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < this.numStat[this.current]; i++) {
      s.append(",").append(this.actions[this.current][i]);
    }
  }

  public void run() {

    if (this.transition[this.current]) {
      if (this.current < this.NTot - 1) this.current++;
      else this.current = 0;
      this.showActions();
      for (int i = 0; i < this.numStat[this.current]; i++) this.state[this.current][i] = false;
      this.transition[this.current] = false;

      for (int i = 0; i < this.numStat[this.current]; i++) {
        this.state[this.current][i] = true;
        this.time[this.current][i] = 0;
      }
    }
    this.j = 0;
    for (int i = 0; i < this.numStat[this.current]; i++) {
      this.time[this.current][i]++;
      if (this.time[this.current][i] >= this.ltime[this.current][i]) this.j++;
    }
    if (this.j == this.numStat[this.current]) this.transition[this.current] = true;
  }
}
