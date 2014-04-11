package evolvingWilds.vinny;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2GL3.GL_QUADS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import creature.phenotype.Block;
import creature.phenotype.Creature;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.EnumNeuronInputType;
import creature.phenotype.EnumOperatorBinary;
import creature.phenotype.EnumOperatorUnary;
import creature.phenotype.Joint;
import creature.phenotype.NeuronInput;
import creature.phenotype.Rule;
import creature.phenotype.Vector3;

/**
 * @author Mario LoPrinzi
 * 
 */
public class AnimationListener implements GLEventListener
{
  private GLU glu; // for the GL Utility
  Creature curCreature;
  private static final int PANEL_WIDTH = 200; // title
  private static final int CANVAS_WIDTH = 640 - PANEL_WIDTH; // width of the
                                                             // drawable
  private static final int CANVAS_HEIGHT = 480;

  Block body[];
  Critter creature;

  /**
   * Called back by the animator to perform rendering. Automatically called to
   * render each frame every iteration. This is where boxes are drawn from and
   * recreated after they have hit the edge of view and exploded
   */
  @Override
  public void display(GLAutoDrawable drawable)
  {
    GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context
    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and
    gl.glLoadIdentity(); // reset the current model-view matrix
                         // depth buffers
    creature.advanceSimulation();

    // TODO
    // //////
    // draw creatures here
    // /////
    gl.glTranslatef(0, -10, 0);
    for (int i = 0; i < creature.getBody().length; i++)
    {
      this.drawBlock(gl, i);
    }
  }

  /**
   * Called back before the OpenGL context is destroyed. Release resource such
   * as buffers. not used because no buffers are created in this program.
   */
  public void dispose(GLAutoDrawable drawable)
  {
  }

  /**
   * Called back immediately after the OpenGL context is initialized. Can be
   * used to perform one-time initialization. Run only once.
   */
  @Override
  public void init(GLAutoDrawable drawable)
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
    new Thread()
    {
      @Override
      public void run()
      {
        while (true)
        {
          creature.hillClimb();
        }
      }
    }.start();
    // ///////////////////

    GL2 gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
    glu = new GLU(); // get GL Utilities
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
    gl.glClearDepth(1.0f); // set clear depth value to farthest
    gl.glEnable(GL_DEPTH_TEST); // enables depth testing
    gl.glDepthFunc(GL_LEQUAL); // the type of depth test to do
    gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective
    gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out //

    // initialize the light positions and color values
    float[] lightPos = { 0, 2, -10, 1 };
    float[] lightColorAmbient = { 0.2f, 0.2f, 0.2f, 1f };
    float[] lightColorSpecular = { 0.8f, 0.8f, 0.8f, 1f };
    float[] diffuse = { .4f, .4f, .4f, .2f };
    // Set light parameters.
    gl.glLightfv(GL_LIGHT1, GL_POSITION, lightPos, 0);
    gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightColorAmbient, 0);
    gl.glLightfv(GL_LIGHT1, GL_SPECULAR, lightColorSpecular, 0);
    gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, diffuse, 0);

    // Enable lighting in GL.
    gl.glEnable(GL_LIGHT1);
    gl.glEnable(GL_LIGHTING); // lighting

  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int width,
      int height)
  {
    GL2 gl = drawable.getGL().getGL2(); // get the OpenGL 2 graphics context

    if (height == 0)
    {
      height = 1; // prevent divide by zero

    }

    float aspect = (float) CANVAS_WIDTH / CANVAS_HEIGHT;

    // Set the view port (display area) to cover the entire window
    gl.glViewport(PANEL_WIDTH, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

    // Setup perspective projection, with aspect ratio matches viewport
    gl.glMatrixMode(GL_PROJECTION); // choose projection matrix

    gl.glLoadIdentity(); // reset projection matrix

    glu.gluPerspective(50.0, aspect, 0.1, 200.0); // fovy, aspect, zNear, zFar
    glu.gluLookAt(0, 0, -40, 0, 0, 0, 0, 1, 0);
    // Enable the model-view transform
    gl.glMatrixMode(GL_MODELVIEW);

    gl.glLoadIdentity(); // reset

  }

  private void drawBlock(GL2 gl, int i)
  {

    float boxSizeX = creature.getBody()[i].getLength();
    float boxSizeY = creature.getBody()[i].getHeight();
    float boxSizeZ = creature.getBody()[i].getWidth();
    float[] rotationMatrix = new float[16];
    Vector3.vectorsToRotationMatrix(rotationMatrix,
        creature.getBlockForwardVector(i), creature.getBlockUpVector(i));

    float color[] = { (boxSizeX), (boxSizeY), (boxSizeZ) };
    // ----- Render the Color box -----

    gl.glPushMatrix();
    gl.glTranslatef(creature.getBlockCenter(i).x * 2,
        creature.getBlockCenter(i).y * 2, creature.getBlockCenter(i).z * 2);

    gl.glMultMatrixf(rotationMatrix, 0);

    gl.glScalef(boxSizeX, boxSizeY, boxSizeZ);

    gl.glBegin(GL_QUADS); // of the color box

    gl.glMaterialfv(GL_FRONT, GL_AMBIENT, color, 0);
    gl.glMaterialfv(GL_FRONT, GL_SPECULAR, color, 0);
    gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, color, 0);
    gl.glMaterialf(GL_FRONT, GL_SHININESS , .5f);

    // Top-face
    gl.glNormal3f(0, 1, 0);
    gl.glVertex3f(1, 1, -1);
    gl.glVertex3f(-1, 1, -1);
    gl.glVertex3f(-1, 1, 1);
    gl.glVertex3f(1, 1, 1);

    // Bottom-face
    gl.glNormal3f(0, -1, 0);
    gl.glVertex3f(1, -1, 1);
    gl.glVertex3f(-1, -1, 1);
    gl.glVertex3f(-1, -1, -1);
    gl.glVertex3f(1, -1, -1);

    // Front-face
    gl.glNormal3f(0, 0, 1);
    gl.glVertex3f(1, 1, 1);
    gl.glVertex3f(-1, 1, 1);
    gl.glVertex3f(-1, -1, 1);
    gl.glVertex3f(1, -1, 1);

    // Back-face
    gl.glNormal3f(0, 0, -1);
    gl.glVertex3f(1, -1, -1);
    gl.glVertex3f(-1, -1, -1);
    gl.glVertex3f(-1, 1, -1);
    gl.glVertex3f(1, 1, -1);

    // Left-face
    gl.glNormal3f(-1, 0, 0);
    gl.glVertex3f(-1, 1, 1);
    gl.glVertex3f(-1, 1, -1);
    gl.glVertex3f(-1, -1, -1);
    gl.glVertex3f(-1, -1, 1);

    // Right-face
    gl.glNormal3f(1, 0, 0);
    gl.glVertex3f(1, 1, -1);
    gl.glVertex3f(1, 1, 1);
    gl.glVertex3f(1, -1, 1);
    gl.glVertex3f(1, -1, -1);

    gl.glEnd(); // end of colored cube

    gl.glPopMatrix();

  }

}
