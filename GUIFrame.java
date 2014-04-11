package evolvingWilds.vinny;

/**
 * @author Mario LoPrinzi
 * @date 3/24/14
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import creature.phenotype.Block;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.EnumNeuronInputType;
import creature.phenotype.EnumOperatorBinary;
import creature.phenotype.EnumOperatorUnary;
import creature.phenotype.Joint;
import creature.phenotype.NeuronInput;
import creature.phenotype.Rule;
import creature.phenotype.Vector3;

@SuppressWarnings("serial")
public class GUIFrame extends JFrame implements ActionListener, Runnable,
    ChangeListener
{
  Timer timer = new Timer(1000, this);
  Timer timeUpdate = new Timer(1000 / 30, this);
  // Define constants for the top-level container
  private static String TITLE = "Genetic GUI"; // window's
  static final int PANEL_WIDTH = 200; // title
  private static final int CANVAS_WIDTH = 640 - PANEL_WIDTH; // width of the
  private Critter creature; // drawable
  private static final int CANVAS_HEIGHT = 480; // height of the drawable
  private static long startTime = System.currentTimeMillis();
  private static long elapsedTime = System.currentTimeMillis();
  // Create the OpenGL rendering canvas
  private int creatureNum = 0;
  private int tribeNum = Runtime.getRuntime().availableProcessors();
  private final Canvas canvas = new Canvas();
  private JPanel rightPanel = new JPanel();
  private JPanel graphicsPanel = new JPanel();
  private JButton pause = new JButton("Pause");
  private JButton nextGen = new JButton("Next Gen");
  private JButton reset = new JButton("Reset");
  private JToggleButton aniCreature = new JToggleButton("Animate Creature");
  private JButton table = new JButton("Print Table");
  private JButton save = new JButton("Save");
  private JLabel sliderLabel1 = new JLabel("Tribe Number: " +tribeNum);
  private JSlider slider1 = new JSlider();
  private JLabel sliderLabel2 = new JLabel("Creature Number: " + creatureNum);
  private JSlider slider2 = new JSlider();
  private JTable creatureTable = new JTable();
  private JLabel time = new JLabel("");
  private boolean aniPaused = false;
  private boolean threadPaused = false;

  @Override
  public void actionPerformed(ActionEvent e)
  {

    if (e.getSource() == aniCreature)
    {
      this.pauseGL();
    }

    else if (e.getSource() == pause)
    {
      // stop entire evolution process
      this.pauseThread();
      nextGen.setEnabled(threadPaused);
    }

    else if (e.getSource() == nextGen)
    {
      // call resources necessary for running 1 generation
      this.updateData();
    }

    else if (e.getSource() == table)
    {
      // show the phenotype of a table
      this.toTable();
    }

    else if (e.getSource().equals(timer))
    {
      // System.out.println(System.currentTimeMillis());
      this.updateData();
    }

    else if (e.getSource().equals(timeUpdate))
    {
      time.setText("elapsed time:" + getTime());

    }
    else if (e.getSource().equals(reset))
    {
      creature.resetSimulation();
    }

  }

  public GUIFrame()
  {
    pause.addActionListener(this);

    nextGen.addActionListener(this);
    nextGen.setEnabled(threadPaused);
    aniCreature.addActionListener(this);

    table.addActionListener(this);

    save.addActionListener(this);

    slider1.addChangeListener(this);

    slider2.addChangeListener(this);

    timer.start();
    timeUpdate.start();
  }

  /**
   * runs the intial start up of the gui and maintains it as events are called
   */
  public void run()
  {

    slider1.setMaximum(tribeNum);
    slider1.setMinimum(1);
    graphicsPanel.setLayout(new BorderLayout());

    graphicsPanel.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    graphicsPanel.setMaximumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    graphicsPanel.setMinimumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

    rightPanel.setSize(PANEL_WIDTH, CANVAS_HEIGHT);
    rightPanel.setMaximumSize(new Dimension(PANEL_WIDTH, CANVAS_HEIGHT));
    rightPanel.setMinimumSize(new Dimension(PANEL_WIDTH, CANVAS_HEIGHT));
    rightPanel.setBackground(new Color(255, 0, 0));

    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    canvas.setMinimumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    canvas.setMaximumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

    // Create the top-level container
    final GUIFrame frame = new GUIFrame();
    frame.setLayout(new BorderLayout());
    frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    frame.setMinimumSize(new Dimension(CANVAS_WIDTH + PANEL_WIDTH,
        CANVAS_HEIGHT));
    frame.setMaximumSize(new Dimension(CANVAS_WIDTH + PANEL_WIDTH,
        CANVAS_HEIGHT));
    rightPanel.add(pause);
    rightPanel.add(nextGen);
    rightPanel.add(aniCreature);
    rightPanel.add(reset);
    rightPanel.add(table);
    rightPanel.add(save);
    rightPanel.add(sliderLabel1);
    rightPanel.add(slider1);
    rightPanel.add(sliderLabel2);
    rightPanel.add(slider2);
    rightPanel.add(creatureTable);
    rightPanel.add(time);

    graphicsPanel.add(canvas);
    frame.add(rightPanel);
    frame.add(graphicsPanel);
    frame.setResizable(false);
    frame.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        // Use a dedicate thread to run the stop() to ensure that the
        // animator stops before program exits.
        new Thread()
        {
          @Override
          public void run()
          {
            if (canvas.getAnimator().isStarted())
              canvas.getAnimator().stop();
            System.exit(0);
          }
        }.start();
      }
    });
    frame.setTitle(TITLE);
    frame.pack();
    frame.setVisible(true);
  }

  public void updateData()
  {
    // grab creature and populate the data
    tribeNum = slider1.getValue();
    creatureNum = slider2.getValue();
    sliderLabel1.setText("Tribe Number: " +tribeNum);
    sliderLabel2.setText("Creature Number: " +creatureNum);
  }

  private void pauseGL()
  {
    if (aniPaused)
    {
      canvas.animator.resume();
      aniPaused = false;
    }
    else
    {
      canvas.animator.pause();
      aniPaused = true;
    }
  }

  private void pauseThread()
  {
    if (threadPaused)
    {
      threadPaused = false;
    }
    else
    {
      threadPaused = true;
    }
  }

  private void toTable()
  {
    Vector3 rootForward = Vector3.FORWARD;
    Vector3 rootUp = Vector3.UP;

    Block[] body = new Block[12];
    body[0] = new Block(Block.PARENT_INDEX_NONE, null, 3, 1, 4);

    // Joint(type, siteOnParent, siteOnChild, orientation)
    // Joint(type, siteOnParent, siteOnChild, orientation)
    Joint joint1 = new Joint(EnumJointType.HINGE,
        EnumJointSite.VERTEX_FRONT_SOUTHEAST,
        EnumJointSite.VERTEX_BACK_SOUTHWEST, (float) (Math.PI / 2));
    Joint joint2 = new Joint(EnumJointType.HINGE,
        EnumJointSite.VERTEX_FRONT_SOUTHWEST,
        EnumJointSite.VERTEX_BACK_SOUTHEAST, -(float) (Math.PI / 2));
    Joint joint3 = new Joint(EnumJointType.HINGE,
        EnumJointSite.VERTEX_BACK_SOUTHEAST,
        EnumJointSite.VERTEX_FRONT_SOUTHWEST, (float) (5 * Math.PI / 6));
    Joint joint4 = new Joint(EnumJointType.HINGE,
        EnumJointSite.VERTEX_BACK_SOUTHWEST,
        EnumJointSite.VERTEX_FRONT_SOUTHEAST, -(float) (5 * Math.PI / 6));

    Joint joint5 = new Joint(EnumJointType.TWIST, EnumJointSite.FACE_NORTH,
        EnumJointSite.FACE_BACK, 0);
    Joint joint6 = new Joint(EnumJointType.TWIST, EnumJointSite.FACE_FRONT,
        EnumJointSite.FACE_EAST, 0);
    Joint joint7 = new Joint(EnumJointType.TWIST, EnumJointSite.FACE_WEST,
        EnumJointSite.FACE_WEST, 0);

    Joint joint8 = new Joint(EnumJointType.RIGID, EnumJointSite.FACE_NORTH,
        EnumJointSite.FACE_NORTH, 0);
    Joint joint9 = new Joint(EnumJointType.RIGID, EnumJointSite.FACE_NORTH,
        EnumJointSite.FACE_NORTH, 0);
    Joint joint10 = new Joint(EnumJointType.RIGID, EnumJointSite.FACE_NORTH,
        EnumJointSite.FACE_NORTH, 0);
    Joint joint11 = new Joint(EnumJointType.RIGID, EnumJointSite.FACE_NORTH,
        EnumJointSite.FACE_NORTH, 0);

    body[1] = new Block(0, joint1, 1, 2, 1);
    body[2] = new Block(0, joint2, 1, 2, 1);
    body[3] = new Block(0, joint3, 1, 2, 1);
    body[4] = new Block(0, joint4, 1, 2, 1);
    body[5] = new Block(0, joint5, 1, 1, 1);
    body[6] = new Block(5, joint6, 1, 1, 1);
    body[7] = new Block(6, joint7, 1, 1, 1);

    body[8] = new Block(1, joint8, 1, 1, 1);
    body[9] = new Block(2, joint9, 1, 1, 1);
    body[10] = new Block(3, joint10, 1, 1, 1);
    body[11] = new Block(4, joint11, 1, 1, 1);

    Rule rule1 = new Rule();
    NeuronInput neuron1A = new NeuronInput(EnumNeuronInputType.CONSTANT, 1f);
    NeuronInput neuron1B = new NeuronInput(EnumNeuronInputType.CONSTANT, 0f);
    NeuronInput neuron1C = new NeuronInput(EnumNeuronInputType.CONSTANT, 0f);
    NeuronInput neuron1D = new NeuronInput(EnumNeuronInputType.CONSTANT, 0f);
    NeuronInput neuron1E = new NeuronInput(EnumNeuronInputType.CONSTANT,
        Float.MAX_VALUE);

    rule1.setInput(neuron1A, NeuronInput.A);
    rule1.setInput(neuron1B, NeuronInput.B);
    rule1.setInput(neuron1C, NeuronInput.C);
    rule1.setInput(neuron1D, NeuronInput.D);
    rule1.setInput(neuron1E, NeuronInput.E);

    rule1.setOp1(EnumOperatorBinary.ADD);
    rule1.setOp2(EnumOperatorUnary.IDENTITY);
    rule1.setOp3(EnumOperatorBinary.ADD);
    rule1.setOp4(EnumOperatorUnary.IDENTITY);

    Rule rule5 = new Rule();
    Rule rule6 = new Rule();
    Rule rule7 = new Rule();

    NeuronInput neuron5A = new NeuronInput(EnumNeuronInputType.CONSTANT, 1f);
    NeuronInput neuron5B = new NeuronInput(EnumNeuronInputType.CONSTANT, 0f);
    NeuronInput neuron5C = new NeuronInput(EnumNeuronInputType.CONSTANT, 0f);
    NeuronInput neuron5D = new NeuronInput(EnumNeuronInputType.CONSTANT, 0f);
    NeuronInput neuron5E = new NeuronInput(EnumNeuronInputType.CONSTANT, 100.0f);
    NeuronInput neuron6E = new NeuronInput(EnumNeuronInputType.CONSTANT,
        -150.0f);
    NeuronInput neuron7E = new NeuronInput(EnumNeuronInputType.CONSTANT, 100.0f);

    rule5.setInput(neuron5A, NeuronInput.A);
    rule5.setInput(neuron5B, NeuronInput.B);
    rule5.setInput(neuron5C, NeuronInput.C);
    rule5.setInput(neuron5D, NeuronInput.D);
    rule5.setInput(neuron5E, NeuronInput.E);

    rule6.setInput(neuron5A, NeuronInput.A);
    rule6.setInput(neuron5B, NeuronInput.B);
    rule6.setInput(neuron5C, NeuronInput.C);
    rule6.setInput(neuron5D, NeuronInput.D);
    rule6.setInput(neuron6E, NeuronInput.E);

    rule7.setInput(neuron5A, NeuronInput.A);
    rule7.setInput(neuron5B, NeuronInput.B);
    rule7.setInput(neuron5C, NeuronInput.C);
    rule7.setInput(neuron5D, NeuronInput.D);
    rule7.setInput(neuron7E, NeuronInput.E);

    rule5.setOp1(EnumOperatorBinary.ADD);
    rule5.setOp2(EnumOperatorUnary.IDENTITY);
    rule5.setOp3(EnumOperatorBinary.ADD);
    rule5.setOp4(EnumOperatorUnary.IDENTITY);

    rule6.setOp1(EnumOperatorBinary.ADD);
    rule6.setOp2(EnumOperatorUnary.IDENTITY);
    rule6.setOp3(EnumOperatorBinary.ADD);
    rule6.setOp4(EnumOperatorUnary.IDENTITY);

    rule7.setOp1(EnumOperatorBinary.ADD);
    rule7.setOp2(EnumOperatorUnary.IDENTITY);
    rule7.setOp3(EnumOperatorBinary.ADD);
    rule7.setOp4(EnumOperatorUnary.IDENTITY);

    joint1.addRule(rule1, 0);
    joint2.addRule(rule1, 0);
    joint3.addRule(rule1, 0);
    joint4.addRule(rule1, 0);

    joint5.addRule(rule5, 0);
    joint6.addRule(rule6, 0);
    joint7.addRule(rule7, 0);

    creature = new Critter(body, rootForward, rootUp, true);
    // ///////////////////

    String rows[] = creature.toString().split("\n");

    Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
    for (String row : rows)
    {
      row = row.trim(); // UPDATE

      Vector<String> data = new Vector<String>();
      data.addAll(Arrays.asList(row));
      dataVector.add(data);
    }
    Vector<String> header = new Vector<String>();

    creatureNum = slider2.getValue();
    header.add("Creature: " + creatureNum);

    TableModel model = new DefaultTableModel(dataVector, header);
    creatureTable = new JTable(model);
    JScrollPane creaturePane = new JScrollPane(creatureTable);
    JFrame frame2 = new JFrame("PhenoType");
    frame2.add(creaturePane);
    frame2.setSize(350, 250);
    frame2.setVisible(true);
    frame2.setLocation(640, 0);
    frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  @Override
  public void stateChanged(ChangeEvent e)
  {

    this.updateData();
  }

  private String getTime()
  {
    StringBuilder time = new StringBuilder("");
    elapsedTime = System.currentTimeMillis() - startTime;
    long elapsedSecs = elapsedTime / 1000;
    long elapsedMins = elapsedSecs / 60;
    long mins = elapsedMins % 60;
    long secs = elapsedSecs % 60;

    // Minutes.
    if (mins < 10)
    {
      time.append("0");
    }
    time.append(mins);

    time.append(":");
    // Seconds.
    if (secs < 10)
    {
      time.append("0");
    }
    time.append(secs);
    return time.toString();
  }
}
