import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;

/*
 * File Name: MyShapes.java
 * Date: February 7, 2020
 * Author: Alexander Szelestey
 * Purpose: Create a Unique JOGL with 6 different shapes that utilize at least 6 different transformation methods
 */

public class MyShapes extends GLJPanel implements GLEventListener, KeyListener {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame window = new JFrame("Project2 - JOGL Project");
		MyShapes panel = new MyShapes();
		window.setContentPane(panel);
		window.pack();
		window.setLocation(50, 50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		System.out.println("Left and Right Arrow Keys Rotate Image on its Y-Axis.");
		System.out.println("Up and Down Arrow Keys Rotate Image on its X-Axis.");
		System.out.println("Page Up and Page Down Keys Rotate Image on its Z-Axis.");
		System.out.println("Home Key Makes all the Axis Equal to 0.");
		System.out.println("Plus and Minus Keys Make the Triangle Bigger or Smaller.");
		System.out.println("W and S Letter Keys Move the Rectangle Up and Down.");
		System.out.println("A and D Letter Keys Move the Rectangle Left and Right");
		panel.requestFocusInWindow();
	}

	// Create minimum size of 640x480
	public MyShapes() {
		super(new GLCapabilities(null));
		setPreferredSize(new Dimension(800, 800));
		addGLEventListener(this);
		addKeyListener(this);
	}

	// Variables
	double rotateX = 15;
	double rotateY = -15;
	double rotateZ, translateX, translateY, translateZ = 0;
	double scale = 0.15;

	// -------------------- DRAW A CUBE ----------------------
	private void square(GL2 gl2, double r, double g, double b) {
		gl2.glColor3d(r, g, b);
		gl2.glBegin(GL2.GL_QUADS);
		gl2.glVertex3d(-0.5, -0.5, 0.5);
		gl2.glVertex3d(0.5, -0.5, 0.5);
		gl2.glVertex3d(0.5, 0.5, 0.5);
		gl2.glVertex3d(-0.5, 0.5, 0.5);
		gl2.glEnd();
	}

	private void cube(GL2 gl2, double size) {
		gl2.glPushMatrix();
		gl2.glScaled(0.15, 0.15, 0.15); // scale unit cube to desired size

		square(gl2, 1, 0, 0); // red front face

		gl2.glPushMatrix();
		gl2.glRotated(90, 0, 1, 0);
		square(gl2, 0, 1, 0); // green right face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(-90, 1, 0, 0);
		square(gl2, 0, 0, 1); // blue top face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(180, 0, 1, 0);
		square(gl2, 0, 1, 1); // cyan back face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(-90, 0, 1, 0);
		square(gl2, 1, 0, 1); // magenta left face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(90, 1, 0, 0);
		square(gl2, 1, 1, 0); // yellow bottom face
		gl2.glPopMatrix();

		gl2.glPopMatrix(); // Restore matrix to its state before cube() was called.
	}

	// ------------------------DRAW THE TRIANGLE-----------------------
	public void triangle(GL2 gl2, double scale, double r, double g, double b) {

		gl2.glBegin(GL2.GL_TRIANGLE_FAN);
		gl2.glScaled(scale, scale, scale);
		gl2.glColor3f(1.0f, 0.0f, 0.0f);
		gl2.glVertex3f(0.0f, 1.0f, 0.0f); // V0(red)
		gl2.glColor3f(0.0f, 1.0f, 0.0f);
		gl2.glVertex3f(-1.0f, -1.0f, 1.0f); // V1(green)
		gl2.glColor3f(0.0f, 0.0f, 1.0f);
		gl2.glVertex3f(1.0f, -1.0f, 1.0f); // V2(blue)
		gl2.glColor3f(0.0f, 1.0f, 0.0f);
		gl2.glVertex3f(1.0f, -1.0f, -1.0f); // V3(green)
		gl2.glColor3f(0.0f, 0.0f, 1.0f);
		gl2.glVertex3f(-1.0f, -1.0f, -1.0f); // V4(blue)
		gl2.glColor3f(0.0f, 1.0f, 0.0f);
		gl2.glVertex3f(-1.0f, -1.0f, 1.0f); // V1(green)
		gl2.glEnd();
	}

	// --------------------------DRAW THE SPHERE----------------------
	// https://stackoverflow.com/questions/5799609/3d-sphere-opengl
	@SuppressWarnings("static-access")
	public void sphere(GL2 gl2, double size, double r, int lats, int longs) {
		gl2.glScaled(0.15, 0.15, 0.15);
		int i, j;
		for (i = 0; i <= lats; i++) {

			double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
			double z0 = Math.sin(lat0);
			double zr0 = Math.cos(lat0);

			double lat1 = Math.PI * (-0.5 + (double) i / lats);
			double z1 = Math.sin(lat1);
			double zr1 = Math.cos(lat1);

			gl2.glBegin(gl2.GL_QUAD_STRIP);
			for (j = 0; j <= longs; j++) {
				double lng = 2 * Math.PI * (double) (j - 1) / longs;
				double x = Math.cos(lng);
				double y = Math.sin(lng);

				gl2.glColor3f(.0f, 0.0f, 0.0f);
				gl2.glNormal3d(x * zr0, y * zr0, z0);
				gl2.glVertex3d(x * zr0, y * zr0, z0);
				gl2.glColor3f(0.0f, 1.0f, 1.0f);
				gl2.glNormal3d(x * zr1, y * zr1, z1);
				gl2.glVertex3d(x * zr1, y * zr1, z1);
			}
			gl2.glEnd();
		}
	}

	// ---------------------DRAW A RING---------------------
	// Used from the source code TexturedShapes.java
	public static void ring(GL2 gl2, double size, double innerRadius, double outerRadius, int slices, int rings,
			boolean makeColor) {
		gl2.glScaled(0.25, 0.25, 0.25);
		double dr = (outerRadius - innerRadius) / rings;
		for (int j = 0; j < rings; j++) {
			double d1 = innerRadius + dr * j;
			double d2 = innerRadius + dr * (j + 1);
			gl2.glBegin(GL2.GL_QUAD_STRIP);
			for (int i = 0; i <= slices; i++) {
				double angle = (2 * Math.PI / slices) * i;
				double sin = Math.sin(angle);
				double cos = Math.cos(angle);
				if (makeColor)
					gl2.glColor3f(1.0f, 1.0f, 1.0f);
				gl2.glVertex3d(cos * d1, sin * d1, 0);
				if (makeColor)
					gl2.glColor3f(1.0f, 1.0f, 0.0f);
				gl2.glVertex3d(cos * d2, sin * d2, 0);
			}
			gl2.glEnd();
		}
	}

	// ---------------------DRAW A CYLINDER------------------------
	// //Used from the source code TexturedShapes.java
	public static void cylinder(GL2 gl2, double size, double radius, double height, int slices, int stacks, int rings,
			boolean makeColor) {
		gl2.glScaled(0.25, 0.25, 0.25);
		for (int j = 0; j < stacks; j++) {
			double z1 = (height / stacks) * j;
			double z2 = (height / stacks) * (j + 1);
			gl2.glBegin(GL2.GL_QUAD_STRIP);
			for (int i = 0; i <= slices; i++) {
				double longitude = (2 * Math.PI / slices) * i;
				double sinLong = Math.sin(longitude);
				double cosLong = Math.cos(longitude);
				double x = cosLong;
				double y = sinLong;
				gl2.glNormal3d(x, y, 0);
				if (makeColor)
					gl2.glColor3f(0.0f, 1.0f, 0.0f);
				gl2.glVertex3d(radius * x, radius * y, z2);
				if (makeColor)
					gl2.glColor3f(1.0f, 0.0f, 0.0f);
				gl2.glVertex3d(radius * x, radius * y, z1);
			}
			gl2.glEnd();
		}
		if (rings > 0) { // draw top and bottom
			gl2.glNormal3d(0, 0, 1);
			for (int j = 0; j < rings; j++) {
				double d1 = (1.0 / rings) * j;
				double d2 = (1.0 / rings) * (j + 1);
				gl2.glBegin(GL2.GL_QUAD_STRIP);
				for (int i = 0; i <= slices; i++) {
					double angle = (2 * Math.PI / slices) * i;
					double sin = Math.sin(angle);
					double cos = Math.cos(angle);
					if (makeColor)
						gl2.glColor3f(0.0f, 0.0f, 1.0f);
					gl2.glVertex3d(radius * cos * d1, radius * sin * d1, height);
					if (makeColor)
						gl2.glColor3f(0.0f, 0.0f, 0.0f);
					gl2.glVertex3d(radius * cos * d2, radius * sin * d2, height);
				}
				gl2.glEnd();
			}
			gl2.glNormal3d(0, 0, -1);
			for (int j = 0; j < rings; j++) {
				double d1 = (1.0 / rings) * j;
				double d2 = (1.0 / rings) * (j + 1);

				gl2.glBegin(GL2.GL_QUAD_STRIP);
				for (int i = 0; i <= slices; i++) {
					double angle = (2 * Math.PI / slices) * i;
					double sin = Math.sin(angle);
					double cos = Math.cos(angle);
					if (makeColor)
						gl2.glColor3f(0.0f, 1.0f, 1.0f);
					gl2.glVertex3d(radius * cos * d2, radius * sin * d2, 0);
					if (makeColor)
						gl2.glColor3f(1.0f, 1.0f, 1.0f);
					gl2.glVertex3d(radius * cos * d1, radius * sin * d1, 0);
				}
				gl2.glEnd();
			}
		}
	} // end cylinder

	// ----------------------DRAW A RECTANGLE-------------------
	// https://www.tutorialspoint.com/jogl/jogl_3d_graphics.htm
	private void rectangle(GL2 gl2) {
		gl2.glScaled(0.25, 0.25, 0.25);
		gl2.glColor3f(1.0f, 1.0f, 1.0f);
		gl2.glTranslatef(0f, 0f, -2.5f);
		gl2.glBegin(GL2.GL_LINES); // Edge 1
		gl2.glVertex3f(-0.75f, 0f, 0);
		gl2.glVertex3f(0f, -0.75f, 0);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(-0.75f, 0f, 3f);
		gl2.glVertex3f(0f, -0.75f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(-0.75f, 0f, 0);
		gl2.glVertex3f(-0.75f, 0f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0f, -0.75f, 0);
		gl2.glVertex3f(0f, -0.75f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES); // Edge 2
		gl2.glVertex3f(0f, -0.75f, 0);
		gl2.glVertex3f(0.75f, 0f, 0);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0f, -0.75f, 3f);
		gl2.glVertex3f(0.75f, 0f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0f, -0.75f, 0);
		gl2.glVertex3f(0f, -0.75f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0.75f, 0f, 0);
		gl2.glVertex3f(0.75f, 0f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES); // Edge 3
		gl2.glVertex3f(0.0f, 0.75f, 0);
		gl2.glVertex3f(-0.75f, 0f, 0);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0.0f, 0.75f, 3f);
		gl2.glVertex3f(-0.75f, 0f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0.0f, 0.75f, 0);
		gl2.glVertex3f(0.0f, 0.75f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(-0.75f, 0f, 0);
		gl2.glVertex3f(-0.75f, 0f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES); // Edge 4
		gl2.glVertex3f(0.75f, 0f, 0);
		gl2.glVertex3f(0.0f, 0.75f, 0);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0.75f, 0f, 3f);
		gl2.glVertex3f(0.0f, 0.75f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0.75f, 0f, 0);
		gl2.glVertex3f(0.75f, 0f, 3f);
		gl2.glEnd();
		gl2.glBegin(GL2.GL_LINES);
		gl2.glVertex3f(0.0f, 0.75f, 0);
		gl2.glVertex3f(0.0f, 0.75f, 3f);
		gl2.glEnd();
	}

	// -------------------- GLEventListener Methods -------------------------
	// Drawing the image, using OpenGL commands.
	public void display(GLAutoDrawable drawable) {
		GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.
		gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// ----------------------PRINT RECTANGLE-----------------
		gl2.glLoadIdentity();
		gl2.glTranslated(0, -0.75, 0);
		gl2.glRotated(rotateZ, 0, 0, 1);
		gl2.glRotated(rotateY, 1, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);
		gl2.glTranslated(translateX, translateY, translateZ);

		rectangle(gl2);

		// -------------------PRINT CYLINDER-----------------
		gl2.glLoadIdentity();
		gl2.glTranslated(0, -0.25, 0);
		gl2.glRotated(rotateZ, 0, 0, 1);
		gl2.glRotated(rotateY, 0, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);

		cylinder(gl2, 0, 0.2, 2, 50, 10, 5, true);

		// ---------------------PRINT RING------------------
		gl2.glLoadIdentity();
		gl2.glTranslated(0, 0.80, 0);
		gl2.glRotated(rotateZ, 0, 0, 1);
		gl2.glRotated(rotateY, 0, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);

		ring(gl2, 0, 0.2, 0.6, 30, 1, true);

		// -------------------PRINT SHERE------------------
		gl2.glLoadIdentity();
		gl2.glTranslated(0, 0.5, 0);
		gl2.glRotated(rotateZ, 0, 0, 1);
		gl2.glRotated(rotateY, 0, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);

		sphere(gl2, 0, 0, 100, 100);

		// -------------------PRINT CUBE-----------------
		gl2.glLoadIdentity(); // Set up modelview transform.
		gl2.glTranslated(0, 0.25, 0);
		gl2.glRotated(rotateZ, 0, 0, 0);
		gl2.glRotated(rotateY, 0, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);

		cube(gl2, 0);

		// ------------------PRINT TRIANGLE---------------
		gl2.glLoadIdentity(); // Set up modelview transform.
		gl2.glTranslated(0, 0, 0);
		gl2.glRotated(rotateZ, 0, 0, 0);
		gl2.glRotated(rotateY, 0, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);
		gl2.glScaled(scale, scale, scale);

		triangle(gl2, 0, 0, 0, 0);

	} // end display()

	public void init(GLAutoDrawable drawable) {
		// called when the panel is created
		GL2 gl2 = drawable.getGL().getGL2();
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glOrtho(-1, 1, -1, 1, -1, 1);
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glClearColor(0, 0, 0, 1);
		gl2.glEnable(GL2.GL_DEPTH_TEST);
	}

	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// called when user resizes the window
	}

	// ---------------- Methods from the KeyListener interface --------------

	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		if (key == KeyEvent.VK_LEFT)
			rotateY -= 15;
		else if (key == KeyEvent.VK_RIGHT)
			rotateY += 15;
		else if (key == KeyEvent.VK_DOWN)
			rotateX += 15;
		else if (key == KeyEvent.VK_UP)
			rotateX -= 15;
		else if (key == KeyEvent.VK_PAGE_UP)
			rotateZ += 15;
		else if (key == KeyEvent.VK_PAGE_DOWN)
			rotateZ -= 15;
		else if (key == KeyEvent.VK_HOME)
			rotateX = rotateY = rotateZ = 0;
		else if (key == KeyEvent.VK_PLUS || key == KeyEvent.VK_EQUALS)
			scale += 0.01;
		else if (key == KeyEvent.VK_MINUS)
			scale -= 0.01;
		else if (key == KeyEvent.VK_W)
			translateY += 0.25;
		else if (key == KeyEvent.VK_A)
			translateX -= 0.25;
		else if (key == KeyEvent.VK_S)
			translateY -= 0.25;
		else if (key == KeyEvent.VK_D)
			translateX += 0.25;

		repaint();
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}
}
