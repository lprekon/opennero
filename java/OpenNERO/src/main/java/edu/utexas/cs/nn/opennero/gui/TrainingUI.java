package edu.utexas.cs.nn.opennero.gui;

import edu.utexas.cs.nn.opennero.Command;
import edu.utexas.cs.nn.opennero.ErrorMessage;
import edu.utexas.cs.nn.opennero.Genome;
import edu.utexas.cs.nn.opennero.Message;
import edu.utexas.cs.nn.opennero.Message.Content;
import edu.utexas.cs.nn.opennero.Population;
import edu.utexas.cs.nn.opennero.SocketClient;
import edu.utexas.cs.nn.opennero.advice.Advice;
import edu.utexas.cs.nn.opennero.shaping.FitnessWeights;
import edu.utexas.cs.nn.opennero.shaping.FitnessWeights.Dimension;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Frame to control training through fitness shaping, advice and examples.
 * @author <a href="mailto:ikarpov@cs.utexas.edu">Igor Karpov</a>
 */
public class TrainingUI extends javax.swing.JFrame {
    public static final float STROKE_THICKNESS = 3.0f;

    private FitnessWeights lastWeights = new FitnessWeights();
    
    private Advice lastAdvice = new Advice();
    
    private EnumMap<Dimension, JSlider> fitnessSliders = 
            new EnumMap<Dimension, JSlider>(Dimension.class);
    
    private EnumMap<Dimension, DynamicTimeSeriesCollection> fitnessPlots = 
            new EnumMap<Dimension, DynamicTimeSeriesCollection>(Dimension.class);
    
    private EnumMap<Dimension, JFreeChart> fitnessCharts = 
            new EnumMap<Dimension, JFreeChart>(Dimension.class);
    
    private boolean weightsModified = false;
    
    private boolean adviceModified = false;
    
    private boolean recordingExample = false;
    
    private Thread thread = null;
    private Timer timer = null;
    private DynamicTimeSeriesCollection fitnessTimeSeries;
    private JFreeChart jchartFitness;
    
    private Population population = new Population();
    
    public final static int COUNT = 5*60;
    public final static float MIN = -100;
    public final static float MAX = 100;
    
    /**
     * Creates new form FitnessShapingFrame
     */
    public TrainingUI() {
        initComponents();
        fitnessSliders.put(Dimension.APPROACH_ENEMY, sliderApproachEnemy);
        fitnessSliders.put(Dimension.APPROACH_FLAG, sliderApproachFlag);
        fitnessSliders.put(Dimension.AVOID_FIRE, sliderAvoidFire);
        fitnessSliders.put(Dimension.HIT_TARGET, sliderHitTarget);
        fitnessSliders.put(Dimension.STAND_GROUND, sliderStandGround);
        fitnessSliders.put(Dimension.STICK_TOGETHER, sliderStickTogether);
        
        addCharts();

        this.textpaneAdvice.getDocument().addDocumentListener(new DocumentListener() {

            public void anyUpdate(DocumentEvent de) {
                adviceModified = true;
                bCancel.setEnabled(true);
                bApply.setEnabled(true);
            }
            
            public void insertUpdate(DocumentEvent de) {
                anyUpdate(de);
            }

            public void removeUpdate(DocumentEvent de) {
                anyUpdate(de);
            }

            public void changedUpdate(DocumentEvent de) {
                anyUpdate(de);
            }
            
        });
        this.thread = new Thread(new Runnable() {
            Serializer ser = new Persister();
            public void run() {
                String msg = socketClient.receive();
                while (msg != null) {
                    try {
                        Message m = ser.read(Message.class, msg);
                        Content c = m.getContent();
                        if (c.getClass().equals(Genome.class)) {
                            Genome genome = (Genome)c;
                            population.add(genome);
                            Genome fittest = population.getFittest();
                            log(String.format(
                                    "Population size: %d, champ: %d(%d), champ fitness: %f", 
                                    population.size(), fittest.getId(), fittest.getBodyId(), fittest.getFitness()));
                        } else if (c.getClass().equals(ErrorMessage.class)) {
                            ErrorMessage error = (ErrorMessage)c;
                            JOptionPane.showMessageDialog(TrainingUI.this, error.text, error.name, JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(TrainingUI.this, "Could not receive data from OpenNERO");
                    }
                    msg = socketClient.receive();
                }
            }

        });
    }
    
    private void log(String line) {
        logger.append(line);
    }

    public void start() {
        this.thread.start();
        this.timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paneTabs = new javax.swing.JTabbedPane();
        panelMain = new javax.swing.JPanel();
        buttonDeployNE = new javax.swing.JButton();
        buttonDeployQL = new javax.swing.JButton();
        buttonLoadTeam = new javax.swing.JButton();
        buttonSaveTeam = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        logger = new javax.swing.JTextArea();
        panelFitness = new javax.swing.JPanel();
        labStandGround = new javax.swing.JLabel();
        sliderStandGround = new javax.swing.JSlider();
        sliderStickTogether = new javax.swing.JSlider();
        sliderApproachEnemy = new javax.swing.JSlider();
        labStickTogether = new javax.swing.JLabel();
        labApproachEnemy = new javax.swing.JLabel();
        sliderApproachFlag = new javax.swing.JSlider();
        sliderHitTarget = new javax.swing.JSlider();
        sliderAvoidFire = new javax.swing.JSlider();
        labApproachFlag = new javax.swing.JLabel();
        labHitTarget = new javax.swing.JLabel();
        labAvoidFire = new javax.swing.JLabel();
        panelAdvice = new javax.swing.JPanel();
        labelAdvice = new javax.swing.JLabel();
        labelAdviceTemplate = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textpaneAdvice = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        textpaneAdviceExamples = new javax.swing.JTextPane();
        panelExample = new javax.swing.JPanel();
        buttonGiveExample = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        textExample = new javax.swing.JTextPane();
        panelProgress = new javax.swing.JPanel();
        tabsPlot = new javax.swing.JTabbedPane();
        panelFitnessPlots = new javax.swing.JPanel();
        panelWeights = new javax.swing.JPanel();
        bApply = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        buttonHelp = new javax.swing.JButton();
        buttonPause = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OpenNERO - Training Window");

        buttonDeployNE.setText("New Neuroevolution Team");
        buttonDeployNE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeployNEActionPerformed(evt);
            }
        });

        buttonDeployQL.setText("New QLearning Team");
        buttonDeployQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeployQLActionPerformed(evt);
            }
        });

        buttonLoadTeam.setText("Load Team");
        buttonLoadTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadTeamActionPerformed(evt);
            }
        });

        buttonSaveTeam.setText("Save Team");
        buttonSaveTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveTeamActionPerformed(evt);
            }
        });

        logger.setColumns(20);
        logger.setRows(5);
        jScrollPane5.setViewportView(logger);

        org.jdesktop.layout.GroupLayout panelMainLayout = new org.jdesktop.layout.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonDeployNE)
                    .add(buttonDeployQL)
                    .add(buttonLoadTeam)
                    .add(buttonSaveTeam))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMainLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(panelMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelMainLayout.createSequentialGroup()
                        .add(jScrollPane5)
                        .addContainerGap())
                    .add(panelMainLayout.createSequentialGroup()
                        .add(buttonDeployNE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonDeployQL)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonLoadTeam)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonSaveTeam)
                        .add(0, 217, Short.MAX_VALUE))))
        );

        paneTabs.addTab("Main", panelMain);

        labStandGround.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labStandGround.setText("Stand Ground");

        sliderStandGround.setMajorTickSpacing(50);
        sliderStandGround.setMinimum(-100);
        sliderStandGround.setMinorTickSpacing(10);
        sliderStandGround.setPaintLabels(true);
        sliderStandGround.setPaintTicks(true);
        sliderStandGround.setSnapToTicks(true);
        sliderStandGround.setValue(0);
        sliderStandGround.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FitnessChangeListener(evt);
            }
        });

        sliderStickTogether.setMajorTickSpacing(50);
        sliderStickTogether.setMinimum(-100);
        sliderStickTogether.setMinorTickSpacing(10);
        sliderStickTogether.setPaintLabels(true);
        sliderStickTogether.setPaintTicks(true);
        sliderStickTogether.setSnapToTicks(true);
        sliderStickTogether.setValue(0);
        sliderStickTogether.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FitnessChangeListener(evt);
            }
        });

        sliderApproachEnemy.setMajorTickSpacing(50);
        sliderApproachEnemy.setMinimum(-100);
        sliderApproachEnemy.setMinorTickSpacing(10);
        sliderApproachEnemy.setPaintLabels(true);
        sliderApproachEnemy.setPaintTicks(true);
        sliderApproachEnemy.setSnapToTicks(true);
        sliderApproachEnemy.setValue(0);
        sliderApproachEnemy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FitnessChangeListener(evt);
            }
        });

        labStickTogether.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labStickTogether.setText("Stick Together");

        labApproachEnemy.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labApproachEnemy.setText("Approach Enemy");

        sliderApproachFlag.setMajorTickSpacing(50);
        sliderApproachFlag.setMinimum(-100);
        sliderApproachFlag.setMinorTickSpacing(10);
        sliderApproachFlag.setPaintLabels(true);
        sliderApproachFlag.setPaintTicks(true);
        sliderApproachFlag.setSnapToTicks(true);
        sliderApproachFlag.setValue(0);
        sliderApproachFlag.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FitnessChangeListener(evt);
            }
        });

        sliderHitTarget.setMajorTickSpacing(50);
        sliderHitTarget.setMinimum(-100);
        sliderHitTarget.setMinorTickSpacing(10);
        sliderHitTarget.setPaintLabels(true);
        sliderHitTarget.setPaintTicks(true);
        sliderHitTarget.setSnapToTicks(true);
        sliderHitTarget.setValue(0);
        sliderHitTarget.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FitnessChangeListener(evt);
            }
        });

        sliderAvoidFire.setMajorTickSpacing(50);
        sliderAvoidFire.setMinimum(-100);
        sliderAvoidFire.setMinorTickSpacing(10);
        sliderAvoidFire.setPaintLabels(true);
        sliderAvoidFire.setPaintTicks(true);
        sliderAvoidFire.setSnapToTicks(true);
        sliderAvoidFire.setValue(0);
        sliderAvoidFire.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FitnessChangeListener(evt);
            }
        });

        labApproachFlag.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labApproachFlag.setText("Approach Flag");

        labHitTarget.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labHitTarget.setText("Hit Target");

        labAvoidFire.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labAvoidFire.setText("Avoid Fire");

        org.jdesktop.layout.GroupLayout panelFitnessLayout = new org.jdesktop.layout.GroupLayout(panelFitness);
        panelFitness.setLayout(panelFitnessLayout);
        panelFitnessLayout.setHorizontalGroup(
            panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelFitnessLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labApproachEnemy)
                    .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, labStickTogether, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, labStandGround, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, labApproachFlag, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, labHitTarget, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, labAvoidFire, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sliderStandGround, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 408, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sliderStickTogether, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 407, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sliderApproachEnemy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 408, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sliderApproachFlag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 408, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sliderHitTarget, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 408, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(sliderAvoidFire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 408, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        panelFitnessLayout.setVerticalGroup(
            panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelFitnessLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sliderStandGround, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(labStandGround))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sliderStickTogether, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(labStickTogether))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labApproachEnemy)
                    .add(sliderApproachEnemy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sliderApproachFlag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(labApproachFlag))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(sliderHitTarget, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(labHitTarget))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelFitnessLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labAvoidFire)
                    .add(sliderAvoidFire, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        paneTabs.addTab("Change Rewards", panelFitness);

        labelAdvice.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labelAdvice.setText("Type your advice here:");

        labelAdviceTemplate.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        labelAdviceTemplate.setText("Advice examples");

        jScrollPane1.setViewportView(textpaneAdvice);

        textpaneAdviceExamples.setEditable(false);
        textpaneAdviceExamples.setBackground(new java.awt.Color(204, 204, 204));
        textpaneAdviceExamples.setContentType("text/html"); // NOI18N
        textpaneAdviceExamples.setText("<html>\n  <head>\n\n  </head>\n  <body>\n<pre><tt><i><font color=\"#9A1900\"># Go around a wall</font></i>\n<b><font color=\"#0000FF\">if</font></b> s9 <font color=\"#990000\">&gt;</font> <font color=\"#993399\">0.5</font> {\n\ta0 <font color=\"#990000\">=</font> <font color=\"#993399\">1</font>\n\ta1 <font color=\"#990000\">=</font> <font color=\"#993399\">1</font>\n} <b><font color=\"#0000FF\">elif</font></b> s10 <font color=\"#990000\">&gt;</font> <font color=\"#993399\">0.5</font> {\n\ta0 <font color=\"#990000\">=</font> <font color=\"#993399\">1</font>\n\ta1 <font color=\"#990000\">=</font> -<font color=\"#993399\">1</font>\n}\n</tt></pre>\n<pre><tt><i><font color=\"#9A1900\"># Go to a target</font></i>\n<b><font color=\"#0000FF\">if</font></b> s3 <font color=\"#990000\">&lt;</font> s4 {\n\ta0 <font color=\"#990000\">=</font> <font color=\"#993399\">1</font>\n\ta1 <font color=\"#990000\">=</font> <font color=\"#993399\">1</font>\n} <b><font color=\"#0000FF\">else</font></b> {\n\ta0 <font color=\"#990000\">=</font> <font color=\"#993399\">1</font>\n\ta1 <font color=\"#990000\">=</font> -<font color=\"#993399\">1</font>\n}\n</tt></pre>\n  </body>\n</html>\n");
        jScrollPane3.setViewportView(textpaneAdviceExamples);

        org.jdesktop.layout.GroupLayout panelAdviceLayout = new org.jdesktop.layout.GroupLayout(panelAdvice);
        panelAdvice.setLayout(panelAdviceLayout);
        panelAdviceLayout.setHorizontalGroup(
            panelAdviceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAdviceLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelAdviceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(labelAdvice, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .add(jScrollPane1))
                .add(panelAdviceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelAdviceLayout.createSequentialGroup()
                        .add(17, 17, 17)
                        .add(labelAdviceTemplate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 286, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(panelAdviceLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 276, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        panelAdviceLayout.setVerticalGroup(
            panelAdviceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelAdviceLayout.createSequentialGroup()
                .add(panelAdviceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(labelAdviceTemplate)
                    .add(labelAdvice))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelAdviceLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .add(jScrollPane1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneTabs.addTab("Give Advice", panelAdvice);

        buttonGiveExample.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        buttonGiveExample.setText("Give Example");
        buttonGiveExample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGiveExampleActionPerformed(evt);
            }
        });

        textExample.setEditable(false);
        textExample.setBackground(new java.awt.Color(204, 204, 204));
        textExample.setContentType("text/html"); // NOI18N
        textExample.setText("<ul><li>Click \"Give Example\".</li>\n<li>Use the arrow keys in the OpenNERO window to drive the big agent and record a trace (in green)</li>\n<li>To apply the example to the population, click \"Apply\" below</li>\n<li>To cancel the example, click \"Cancel\" below.</li><ul>");
        jScrollPane4.setViewportView(textExample);

        org.jdesktop.layout.GroupLayout panelExampleLayout = new org.jdesktop.layout.GroupLayout(panelExample);
        panelExample.setLayout(panelExampleLayout);
        panelExampleLayout.setHorizontalGroup(
            panelExampleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelExampleLayout.createSequentialGroup()
                .addContainerGap()
                .add(buttonGiveExample)
                .addContainerGap(535, Short.MAX_VALUE))
            .add(panelExampleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(panelExampleLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panelExampleLayout.setVerticalGroup(
            panelExampleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelExampleLayout.createSequentialGroup()
                .add(130, 130, 130)
                .add(buttonGiveExample)
                .addContainerGap(174, Short.MAX_VALUE))
            .add(panelExampleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(panelExampleLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(212, Short.MAX_VALUE)))
        );

        paneTabs.addTab("Give Examples", panelExample);

        panelFitnessPlots.setLayout(new java.awt.GridLayout(2, 3));
        tabsPlot.addTab("Fitness Values", panelFitnessPlots);

        panelWeights.setLayout(new java.awt.BorderLayout());
        tabsPlot.addTab("Fitness Weights", panelWeights);

        org.jdesktop.layout.GroupLayout panelProgressLayout = new org.jdesktop.layout.GroupLayout(panelProgress);
        panelProgress.setLayout(panelProgressLayout);
        panelProgressLayout.setHorizontalGroup(
            panelProgressLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelProgressLayout.createSequentialGroup()
                .addContainerGap()
                .add(tabsPlot, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
        );
        panelProgressLayout.setVerticalGroup(
            panelProgressLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabsPlot, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
        );

        paneTabs.addTab("Monitor Progress", panelProgress);

        bApply.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        bApply.setText("Apply");
        bApply.setEnabled(false);
        bApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bApplyActionPerformed(evt);
            }
        });

        bCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        bCancel.setText("Cancel");
        bCancel.setEnabled(false);
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });

        buttonHelp.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        buttonHelp.setText("Help");

        buttonPause.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        buttonPause.setText("Pause");
        buttonPause.setEnabled(false);
        buttonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPauseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(paneTabs)
                    .add(layout.createSequentialGroup()
                        .add(buttonHelp)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(buttonPause)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(bCancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(bApply))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(paneTabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(bApply)
                    .add(bCancel)
                    .add(buttonHelp)
                    .add(buttonPause)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FitnessChangeListener(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FitnessChangeListener
        this.weightsModified = true;
        this.bCancel.setEnabled(true);
        this.bApply.setEnabled(true);
    }//GEN-LAST:event_FitnessChangeListener

    private SocketClient socketClient = SocketClient.instance;
    
    private void bApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bApplyActionPerformed
        if (weightsModified) {
            FitnessWeights weights = new FitnessWeights();
            for (Dimension d : Dimension.values()) {
                float v = this.fitnessSliders.get(d).getValue();
                weights.put(d, v);
            }
            Message m = new Message(weights);
            String xml = socketClient.toXml(m);
            if (socketClient.send(xml)) {
                this.lastWeights = weights;
                for (Dimension d : Dimension.values()) {
                    float v = weights.get(d);
                    fitnessCharts.get(d).setTitle(String.format("%s (%.01f%%)", d.toString(), v));
                }
                log(m.toString());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not set fitness weights.",
                    "Fitness/Reward error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        if (adviceModified) {
            Advice advice = new Advice(this.textpaneAdvice.getText());
            Message m = new Message(advice);
            String xml = socketClient.toXml(m);
            if (socketClient.send(xml)) {
                this.lastAdvice = advice;
                log(m.toString());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not send advice.",
                    "Advice error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        if (recordingExample) {
            sendCommand("example", "confirm");
            buttonGiveExample.setEnabled(true);
        }
        weightsModified = false;
        adviceModified = false;
        recordingExample = false;
        this.bCancel.setEnabled(false);
        this.bApply.setEnabled(false);
    }//GEN-LAST:event_bApplyActionPerformed

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        if (weightsModified) {
            for (Dimension d : Dimension.values()) {
                this.fitnessSliders.get(d).setValue((int)Math.round(this.lastWeights.get(d)));
            }
        }
        if (adviceModified) {
            this.textpaneAdvice.setText(lastAdvice.getAdvice());
        }
        if (recordingExample) {
            sendCommand("example", "cancel");
            this.buttonGiveExample.setEnabled(true);
        }
        adviceModified = false;
        weightsModified = false;
        recordingExample = false;
        this.bCancel.setEnabled(false);
        this.bApply.setEnabled(false);
    }//GEN-LAST:event_bCancelActionPerformed

    private void buttonDeployNEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeployNEActionPerformed
        buttonPause.setEnabled(true);
        sendCommand("rtneat", "");
    }//GEN-LAST:event_buttonDeployNEActionPerformed

    private void sendCommand(String command, String arg) {
        Message m = new Message(new Command(command, arg));
        String xml = socketClient.toXml(m);
        if (socketClient.send(xml)) {
            log(m.toString());
        } else {
            JOptionPane.showMessageDialog(this,
                "Could not send command.",
                "Command error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buttonDeployQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeployQLActionPerformed
        buttonPause.setEnabled(true);
        sendCommand("qlearning", "");
    }//GEN-LAST:event_buttonDeployQLActionPerformed

    private JFileChooser fileChooser = new JFileChooser();
    
    private void buttonLoadTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadTeamActionPerformed
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            sendCommand("load1", fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_buttonLoadTeamActionPerformed

    private void buttonSaveTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveTeamActionPerformed
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            sendCommand("save1", fileChooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_buttonSaveTeamActionPerformed
    
    private void buttonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPauseActionPerformed
        if ("Pause".equals(this.buttonPause.getText())) {
            sendCommand("pause", "");
            this.buttonPause.setText("Resume");
        } else {
            sendCommand("resume", "");
            this.buttonPause.setText("Pause");
        }
    }//GEN-LAST:event_buttonPauseActionPerformed

    private void buttonGiveExampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGiveExampleActionPerformed
        sendCommand("example", "start");
        this.buttonGiveExample.setEnabled(false);
        this.recordingExample = true;
        this.bCancel.setEnabled(true);
        this.bApply.setEnabled(true);
    }//GEN-LAST:event_buttonGiveExampleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrainingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrainingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrainingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrainingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                TrainingUI ui = new TrainingUI();
                ui.setVisible(true);
                ui.start();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bApply;
    private javax.swing.JButton bCancel;
    private javax.swing.JButton buttonDeployNE;
    private javax.swing.JButton buttonDeployQL;
    private javax.swing.JButton buttonGiveExample;
    private javax.swing.JButton buttonHelp;
    private javax.swing.JButton buttonLoadTeam;
    private javax.swing.JButton buttonPause;
    private javax.swing.JButton buttonSaveTeam;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labApproachEnemy;
    private javax.swing.JLabel labApproachFlag;
    private javax.swing.JLabel labAvoidFire;
    private javax.swing.JLabel labHitTarget;
    private javax.swing.JLabel labStandGround;
    private javax.swing.JLabel labStickTogether;
    private javax.swing.JLabel labelAdvice;
    private javax.swing.JLabel labelAdviceTemplate;
    private javax.swing.JTextArea logger;
    private javax.swing.JTabbedPane paneTabs;
    private javax.swing.JPanel panelAdvice;
    private javax.swing.JPanel panelExample;
    private javax.swing.JPanel panelFitness;
    private javax.swing.JPanel panelFitnessPlots;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelProgress;
    private javax.swing.JPanel panelWeights;
    private javax.swing.JSlider sliderApproachEnemy;
    private javax.swing.JSlider sliderApproachFlag;
    private javax.swing.JSlider sliderAvoidFire;
    private javax.swing.JSlider sliderHitTarget;
    private javax.swing.JSlider sliderStandGround;
    private javax.swing.JSlider sliderStickTogether;
    private javax.swing.JTabbedPane tabsPlot;
    private javax.swing.JTextPane textExample;
    private javax.swing.JTextPane textpaneAdvice;
    private javax.swing.JTextPane textpaneAdviceExamples;
    // End of variables declaration//GEN-END:variables

    private void addCharts() {
        fitnessTimeSeries = new DynamicTimeSeriesCollection(
                FitnessWeights.Dimension.values().length, COUNT, new Second());
        RegularTimePeriod now = new Second();
        for (int j = 0; j < COUNT; j++) {
            now = now.previous();
        }
        fitnessTimeSeries.setTimeBase(now);

        for (int i = 0; i < Dimension.values().length; i++) {
            Dimension d = Dimension.values()[i];
            float[] values = { lastWeights.get(d), lastWeights.get(d) };
            fitnessTimeSeries.addSeries(values, i, d.toString());

            DynamicTimeSeriesCollection ts = new DynamicTimeSeriesCollection(1, COUNT, new Second());
            ts.setTimeBase(now);
            fitnessPlots.put(d, ts);
            ts.addSeries(values, 0, d.toString());
            
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    d.toString(), null, null, ts, false, false, false);
            XYPlot plot = chart.getXYPlot();
            plot.getRenderer().setSeriesStroke(0, new BasicStroke(STROKE_THICKNESS));
            ValueAxis domain = plot.getDomainAxis();
            domain.setAutoRange(true);
            ValueAxis range = plot.getRangeAxis();
            range.setAutoRange(true);
            panelFitnessPlots.add(new ChartPanel(chart), i/3, i%3);
            fitnessCharts.put(d, chart);
        }
        
        timer = new Timer(
            1000,
            new ActionListener() {
                float[] values = new float[Dimension.values().length];
                float[] fitnessValues = new float[1];
                public void actionPerformed(ActionEvent ae) {
                    Genome fittest = population.getFittest();
                    for (int i = 0; i < Dimension.values().length; i++) {
                        Dimension d = Dimension.values()[i];
                        values[i] = lastWeights.get(d);
                        fitnessPlots.get(d).advanceTime();
                        
                        fitnessValues[0] = (fittest != null) ? fittest.getRawFitness().get(d) : 0;
                        fitnessPlots.get(d).appendData(fitnessValues);
                    }
                    fitnessTimeSeries.advanceTime();
                    fitnessTimeSeries.appendData(values);
                }
                
            }
        );
        
        jchartFitness = ChartFactory.createTimeSeriesChart(
                "Fitness Weights", "hh:mm", "weights", fitnessTimeSeries, true, true, false);
        XYPlot plot = jchartFitness.getXYPlot();
        for (int i = 0; i < Dimension.values().length; i++) {
            plot.getRenderer().setSeriesStroke(i, new BasicStroke(STROKE_THICKNESS));
        }
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(MIN * 1.1, MAX * 1.1);
        panelWeights.add(new ChartPanel(jchartFitness), BorderLayout.CENTER);
    }
}