/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import agent.RunnerAgent;
import agent.WallAgent;
import java.awt.Color;
import java.awt.Container;
import java.awt.LayoutManager;

/**
 *
 * @author 09952410905
 */
public class GroundPanel extends javax.swing.JPanel {
    
    private WallAgent wallAgent;
    private RunnerAgent runnerAgent;
    
    public GroundPanel(Color color, LayoutManager layout) {        
        this.setBackground(color);
        this.setLayout(layout);
        initComponents();
    }

    public WallAgent getWallAgent() {
        return wallAgent;
    }

    public void setWallAgent(WallAgent wallAgent) {
        this.wallAgent = wallAgent;
    }

    public RunnerAgent getRunnerAgent() {
        return runnerAgent;
    }

    public void setRunnerAgent(RunnerAgent runnerAgent) {
        this.runnerAgent = runnerAgent;
    }
    
    public Container getBattleField() {
        return this.getParent();
    }
    
    public boolean hasRunnerAgent() {
        if(this.runnerAgent == null)
            return false;
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
