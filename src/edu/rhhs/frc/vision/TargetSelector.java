package edu.rhhs.frc.vision;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * @author rhhs
 * 
 * This class takes the identified targets and tries to which position
 * they are and which one is the "best"
 */
public class TargetSelector {
   
    private Target[] m_targets;
    private Target m_bestTarget = null;
    
    public TargetSelector(ParticleAnalysisReport[] reports, int selectedTarget, double cameraFOVHorizontalAngleDeg) {
        m_targets = new Target[reports.length];
        for (int i = 0; i < reports.length; i++) {
            m_targets[i] = new Target(reports[i], cameraFOVHorizontalAngleDeg);
        }
        evaluateTargets();
    }

    // Find the "best" target and identify the location of each target
    private void evaluateTargets() { 
        for (int i = 0; i < m_targets.length; i++) {           
            Target curTarget = m_targets[i];

            // The "best" target is based on the composite score.  We may want to include a term
            // in the composite score that includes the distance from the current target to the
            // selected target.
            if (m_bestTarget == null || (curTarget.getCompositeScore() < m_bestTarget.getCompositeScore())) {
                m_bestTarget = m_targets[i];
            }
        }
    }
 
    public double getBestTargetCameraDistanceFt() {
        if (m_bestTarget != null) {
            return m_bestTarget.getCameraDistanceFt();
        }
        
        return 0;
    }
        
    public double getBestHorizontalAngleToSelectedTargetDeg() {
        if (m_bestTarget != null) {
            return m_bestTarget.getHorizontalAngleToSelectedTargetDeg();
        }
        
        return 0;
    }
    

}
