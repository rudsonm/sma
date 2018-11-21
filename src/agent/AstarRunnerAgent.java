/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import jade.core.AID;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 *
 * @author 09952410905
 */
public class AstarRunnerAgent extends TronRunner {
    List<Point> path = new ArrayList<Point>();
    private Boolean enemyReached = false;
    private Boolean enemyTraped = false;
    
    @Override
    protected String getNextMove() {
        Point currentPos = positions.get(getAID());
        
        Point enemyPos = getEnemyPosition();
        Point goal = null;
        if(currentPos.y + 4 >= enemyPos.y)
            enemyReached = true;
        if(currentPos.x == 1 && currentPos.y == 2) {
            enemyTraped = true;
            return "T";
        }
        
        if(enemyReached) {
            goal = new Point(2, fieldDimension.height - 2);
        } else if(enemyTraped) {
            return super.getNextMove();
        } else {
            goal = new Point(fieldDimension.height - 2, enemyPos.y - 2);
        }        
        path = Astar(currentPos, goal);
        
        if(path.size() == 0)
            return super.getNextMove();
        
        path.remove(0);
                
        Point nextPos = path.get(0);
        path.remove(0);
        
        if(currentPos.x + 1 == nextPos.x)
            return "B";
        if(currentPos.x - 1 == nextPos.x)
            return "T";
        if(currentPos.y + 1 == nextPos.y)
            return "R";
        if(currentPos.y - 1 == nextPos.y)
            return "L";
        return "";
    }
    
    private Point getEnemyPosition() {
        Point enemyP = null;
        for(Map.Entry<AID, Point> entry : positions.entrySet()) {
            if(entry.getKey() != getAID())
                enemyP = entry.getValue();
        }
        return enemyP;
    }
    
    private List<Point> Astar(Point start, Point goal) {
        List<Point> path = new ArrayList();
        
        List<Point> closedSet = new ArrayList();
        List<Point> openSet = new ArrayList();
        
        Map<Point, Double> gScore = new HashMap();
        Map<Point, Double> fScore = new HashMap();
        Map<Point, Point> cameFrom = new HashMap();
        
        gScore.put(start, 0.0);
        openSet.add(start);
        
        fScore.put(start, heuristicCoast(start, goal));
        
        while(openSet.size() > 0) {
            Point current = getClosestPoint(openSet, fScore);
            if(current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
            
            openSet.remove(current);
            closedSet.add(current);
            
            for(Point neighbor : getNeighborhood(current)) {                
                if(closedSet.contains(neighbor))
                    continue;
                
                double distance = gScore.get(current) + getDistanceBetween(current, neighbor);
                
                if(!openSet.contains(neighbor))
                    openSet.add(neighbor);
                else if (distance >= gScore.get(neighbor))
                    continue;
                                
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, distance);                
                fScore.put(neighbor, distance + heuristicCoast(neighbor, goal));
            }
//            System.out.println("OpenSet: " + openSet.size() + ", ClosedSet: " + closedSet.size());
        }
        return path;
    }
    
    private double heuristicCoast(Point current, Point goal) {
        return getDistanceBetween(current, goal) * 3;
    }
    
    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new ArrayList<>();
        while(current != null) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        return path;
    }
    
    private Point getClosestPoint(List<Point> openSet, Map<Point, Double> score) {
        Double bestD = Double.MAX_VALUE;
        Point bestP = null;
        for(Point p : openSet) {
            if(bestP == null || score.get(p) < bestD) {
                bestP = p;
                bestD = score.get(p);
            }
        }
        return bestP;
    }
    
    private Point getFurthestPoint(List<Point> openSet, Map<Point, Double> score) {
        Double bestD = 0.0;
        Point bestP = null;
        for(Point p : openSet) {
            if(bestP == null || score.get(p) > bestD) {
                bestP = p;
                bestD = score.get(p);
            }
        }
        return bestP;
    }
    
    private List<Point> getNeighborhood(Point p) {
        List<Point> points = new ArrayList();
        Point north = new Point(p.x - 1, p.y);
        Point south = new Point(p.x + 1, p.y);
        Point east = new Point(p.x, p.y + 1);
        Point west = new Point(p.x, p.y - 1);
        
        if(!isOutOfBound(north.x, north.y) && !hasComponent(north.x, north.y))
            points.add(north);
        if(!isOutOfBound(south.x, south.y) && !hasComponent(south.x, south.y))
            points.add(south);
        if(!isOutOfBound(east.x, east.y) && !hasComponent(east.x, east.y))
            points.add(east);
        if(!isOutOfBound(west.x, west.y) && !hasComponent(west.x, west.y))
            points.add(west);
        return points;
    }
    
    private double getDistanceBetween(Point a, Point b) {
        return Math.sqrt(
            Math.pow(a.x - b.x, 2) + Math.pow(a.x - b.x, 2)
        );
    }
    
    private List<Point> getSmallestPathBetween(Point a, Point b) {
        List<Point> path = new ArrayList<Point>();
        
        return path;
    }
}