package evolvingWilds.vinny;


import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author Mario LoPrinzi
 * 
 */
@SuppressWarnings("serial")
public class Canvas extends GLCanvas
{

  private static final int FPS = 30; // animator's target frames per second
  // Create a animator that drives canvas' display() at the specified FPS.
  final FPSAnimator animator;

  public Canvas()
  {
    
    animator = new FPSAnimator(this,FPS);
    this.addGLEventListener(new AnimationListener());
    animator.start();

  }

  /**
   * @return the animator
   */
  public FPSAnimator getAnimator()
  {
    return animator;
  }

}
