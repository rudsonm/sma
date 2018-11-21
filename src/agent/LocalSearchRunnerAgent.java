package agent;

import java.awt.Point;

public class LocalSearchRunnerAgent extends TronRunner {
    
    private class Move {
        public final String move;
        public final int moveX;
        public final int moveY;
        public Move(String move, int moveX, int moveY) {
            this.move = move;
            this.moveX = moveX;
            this.moveY = moveY;
        }
        Point getNewPoint(){
            Point p = positions.get(getAID());
            return new Point(p.x + moveX, p.y + moveY);
        }
        double getScore(Point enemy){
            Point newPoint = getNewPoint();
            if (isOutOfBound(newPoint.x, newPoint.y) || hasComponent(newPoint.x, newPoint.y)){
                return Double.MAX_VALUE;
            }
            return getDistanceBetween(newPoint, enemy);
        }
    }
    
    private Move moves[] = new Move[]{
        new Move("R", 0, 1),
        new Move("L", 0,-1),
        new Move("B", 1, 0),
        new Move("T",-1, 0),
    };
    
    @Override
    protected String getNextMove() {
        Point enemy = getEnemyPosition();
        String bestMove = "T";
        double bestScore = Double.MAX_VALUE;
        for (Move move : moves) {
            double score = move.getScore(enemy);
            if (score < bestScore){
                System.out.println(score + " < " + bestScore + " - " + bestMove + " -> " + move.move);
                bestScore = score;
                bestMove = move.move;
            }
        }
        return bestMove;
    }
    
}
