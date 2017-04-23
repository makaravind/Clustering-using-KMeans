package Kmeans;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Formatter;

public class ClusterHandler {

	private int k; // number of clusters
	private ArrayList<Point> allpoints;
	private ArrayList<Cluster> clusters;
	
	public ClusterHandler(int k, ArrayList<Point> allpoints) {
		this.k = k;
		this.allpoints = allpoints;
		
		clusters = new ArrayList<>();
		for(int i=0; i<k; i++) {
			// create k number of clusters
			clusters.add(new Cluster());
		}
	}
	
	public boolean clusterify(int maxIterations) {
		// apply kmeans algo now
		if( !initialize() ) {
			return false; 
		}
		for(int i=0; i<maxIterations; i++) {
			clearMemberPointsOfClusters();
			allocatePointsToClusters();
//			printStep(i);
			for(int j=0; j<k; j++) {
				clusters.get(j).remapCentriod();
			}
		}
		return true;
	}

	private boolean initialize() {
		
		if(k > allpoints.size()) {
			return false;
		}
		// init algo with centriods of clusters with first k points in allpoints 
		for(int i=0; i<k; i++) {
			clusters.get(i).updateCentriod(allpoints.get(i));
		}
		return true;
	}
	
	private void clearMemberPointsOfClusters() {
		for(int j=0; j<k; j++) {
			clusters.get(j).clearMemberPoints();
		}
	}
	
	private void allocatePointsToClusters() {
		
		for(int p=0; p<allpoints.size(); p++) {
			
			Point currentPoint = allpoints.get(p);
			
			// find the point distance to cluster centriod 
			double minDistance = 99999;
			int minCIndex = -1;
			for(int clusterID=0; clusterID<k; clusterID++) {
				Point currentCentriod = clusters.get(clusterID).getCentriod();
				double d = currentCentriod.distance(currentPoint);
				if( d < minDistance ) {
					minDistance = d;
					minCIndex = clusterID;
				}
			}
			
			if( minCIndex >= 0 ) {
				// assign the point to the lowest distance cluster
				clusters.get(minCIndex).addNewPoint(currentPoint);
			}
		}
	}
	
	private void printStep(int step) {
		String out = step + ": ";
		for(int i=0; i<k; i++) {
			out += clusters.get(i).getCentriod();
			out += " points: ";
			for(int j=0; j<clusters.get(i).getMemberPoints().size(); j++) {
				out += clusters.get(i).getMemberPoints().get(j);
			}
			System.out.println(out);
			out = "";
		}
	}

	public void prettyPrintClusters() {
	
		String out = "centriod : %s; \nPoints: %s";
		for(int i=0; i<k; i++) {
			System.out.println("---------------cluster " + i+1  + "----------------------");
			String cent = clusters.get(i).getCentriod().toString();
			String points = "";
			for(int j=0; j<clusters.get(i).getMemberPoints().size(); j++) {
				points += clusters.get(i).getMemberPoints().get(j) + "\n";
			}
			StringBuilder sbuf = new StringBuilder();
			Formatter fmt = new Formatter(sbuf);
			fmt.format(out, cent, points);
			System.out.println(sbuf.toString());
		}
		
	}
	
}
