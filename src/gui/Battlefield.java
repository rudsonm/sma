package gui;

import agent.TronRunner;
import agent.WallAgent;
import jade.core.AID;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author 5966868
 */
public class Battlefield extends javax.swing.JFrame implements Serializable {
    public final Integer LINES = 40;
    public final Integer COLUMNS = 40;
    
    /**
     * Creates new form Battlefield
     */
    public Battlefield() {
        int WIDTH = this.getWidth();
        int HEIGHT = this.getHeight();
        
//        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        initComponents();
        this.setLayout(new GridBagLayout());
        
        this.setLocationRelativeTo(null);
        GridBagConstraints cons = new GridBagConstraints();
        
        cons.fill = GridBagConstraints.BOTH;
        
        cons.ipadx = 50;
        cons.ipady = 50;
        
        cons.weightx = 100;
        cons.weighty = 100;
        for(int i = 0; i < LINES; i++) {
            cons.gridx = i;
            for(int j = 0; j < COLUMNS; j++) {
                cons.gridy = j;
                
                GroundPanel panel = new GroundPanel(Color.BLACK, new GridBagLayout());
                panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                panel.setSize(WIDTH / COLUMNS, HEIGHT / LINES);
                
                this.add(panel, cons);
            }
        }        
    }
    
    public void putAgent(Color color, Point point) {        
        Component c = this.getComponentAt(point);
        c.setBackground(color);
    }
    
    @Override
    public Component getComponentAt(int x, int y) {
        return this.getContentPane().getComponent(y * this.LINES + x);
    }
    
    @Override
    public Component getComponentAt(Point point) {
        return this.getContentPane().getComponent(point.y * this.LINES + point.x);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Battlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Battlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Battlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Battlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Battlefield().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
