package evolvingWilds.vinny;

import javax.swing.SwingUtilities;

import evolvingWilds.mattlock.PopulationThread;

/**
 * @author Mario LoPrinzi
 * 
 */
public class Main
{

  public static void main(String[] args)
  {
    Tribes tribeList = new Tribes();
    // GUIFrame frame = new GUIFrame();
    SwingUtilities.invokeLater(new GUIFrame());
  }

}
