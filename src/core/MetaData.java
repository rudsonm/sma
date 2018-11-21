/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import jade.core.AID;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author 09952410905
 */
public class MetaData implements Serializable {
    
    public Dimension battlefieldDimension;
    public int[][] battlefield;
    public Map<AID, Point> positions;
    
    public MetaData(int[][] battlefield, Map<AID, Point> positions, Dimension battlefieldDimension) {
        this.battlefield = battlefield;
        this.positions = positions;
        this.battlefieldDimension = battlefieldDimension;
    }
}
