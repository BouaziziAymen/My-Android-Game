package com.evolgames.dollmutilationgame.helpers;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.HashSet;

public class UnionFind {
    /*
     * This class implements the union-find data structure
     * with union by rank and find with path compression
     */

    private final int[] parent;
    private final int[] rank;
    public HashMap<Integer, HashSet<Integer>> myDict;
    public int size;

    @SuppressLint("UseSparseArrays")
    public UnionFind(int n) {
        this.parent = new int[n];
        for (int i = 0; i < n; i++) this.parent[i] = i;
        this.rank = new int[n];
        for (int i = 0; i < n; i++) this.rank[i] = 0;

        this.myDict = new HashMap<Integer, HashSet<Integer>>();
        this.size = n;
    }

    public int find(int v) {
        if (v != this.parent[v]) {
            this.parent[v] = this.find(this.parent[v]);
        }
        return this.parent[v];
    }

    public void union(int x, int y) {
        int xRoot = this.find(x);
        int yRoot = this.find(y);
        if (xRoot == yRoot) return;
        if (this.rank[xRoot] > this.rank[yRoot]) {
            this.parent[yRoot] = xRoot;
        } else {
            this.parent[xRoot] = yRoot;
            if (this.rank[xRoot] == this.rank[yRoot]) {
                this.rank[yRoot]++;
            }
        }
    }

    public void compute() {

        for (int node = 0; node < this.size; node++) {
            int root = this.find(node);
            if (!this.myDict.containsKey(root)) {
                HashSet<Integer> mySet = new HashSet<Integer>();
                mySet.add(node);
                this.myDict.put(root, mySet);
            } else {
                this.myDict.get(root).add(node);
            }
        }
    }

    public String toString() {

        String result = null;
        for (HashSet<Integer> mySet : this.myDict.values()) {
            result += mySet + "\n";
        }
        return result;
    }
}
