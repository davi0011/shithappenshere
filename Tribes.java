package evolvingWilds.vinny;

import evolvingWilds.mattlock.PopulationThread;

public class Tribes
{
  private static final int POPULATIONSIZE = 1000;

  private PopulationThread tribes[] = new PopulationThread[Runtime.getRuntime()
      .availableProcessors()];

  private void initializeTribes()
  {
    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++)
    {
      tribes[i] = new PopulationThread(POPULATIONSIZE);
    }
  }

  /**
   * @return the tribes
   */
  public PopulationThread[] getTribes()
  {
    return tribes;
  }

  public Tribes()
  {
    this.initializeTribes();
  }
}
