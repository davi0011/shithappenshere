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
import evolvingWilds.mattlock.PopulationThread;

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
  private Tribes tribeList;
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
  private JLabel sliderLabel1 = new JLabel("Tribe Number: " + tribeNum);
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

  public GUIFrame(Tribes tribeList)
  {
    this.tribeList = tribeList;
    this.creature = tribeList.getTribes()[0].getCritters()[0].clone();
    this.canvas.setCreature(this.creature);
    pause.addActionListener(this);

    nextGen.addActionListener(this);
    nextGen.setEnabled(threadPaused);
    aniCreature.addActionListener(this);
    reset.addActionListener(this);

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
    final GUIFrame frame = new GUIFrame(tribeList);
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
    sliderLabel1.setText("Tribe Number: " + tribeNum);
    sliderLabel2.setText("Creature Number: " + creatureNum);
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
      for (PopulationThread thread : this.tribeList.getTribes())
      {
        thread.setPause(threadPaused);
      }
    }
    else
    {
      threadPaused = true;
      for (PopulationThread thread : this.tribeList.getTribes())
      {
        thread.setPause(threadPaused);
      }
    }
  }

  private void toTable()
  {

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
    if (e.getSource().equals(slider1) || e.getSource().equals(slider2))
    {
      creature = this.tribeList.getTribes()[slider1.getValue() - 1]
          .getCritters()[slider2.getValue()].clone();
      canvas.setCreature(creature);
    }
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
