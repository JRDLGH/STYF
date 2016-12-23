/**
 * LAGACHE JORDAN GROUPE 16 
 */
package chat.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chat.model.Model;
import chat.view.Console;

/**
 * This class do a JUNIT Test for the class controller.
 * @author JRDN
 *
 */
public class ControllerTest {
	private  Model m;
	private Controller ctrl2;
	/**
	 * Test method for {@link chat.controller.Controller#Controller(chat.model.Model)}.
	 */
	@Before 
	public void setup(){
		Model m = new Model();
		ctrl2 = new Controller(m);
		//Création des vues
		Console viewC = new Console(m,ctrl2);
		//On donne la ref à la vue pour chaque controlleur
		ctrl2.addView(viewC);
	} // sert à réinstancier une nouvelle fois notre objet.
	/*@Test
	public void testController() {
		fail();
	}*/

	/**
	 * Test method for {@link chat.controller.Controller#sPort(int)}.
	 */
	@Test
	public void testIsPortValid() {
		assertEquals(false,ctrl2.isPortValid(785));
		assertEquals(false,ctrl2.isPortValid(888));
		assertFalse(ctrl2.isPortValid(65536));
		assertFalse(ctrl2.isPortValid(-5));
		assertFalse(ctrl2.isPortValid(0));
	}

	/**
	 * Test method for {@link chat.controller.Controller#isActive(int)}.
	 */
	@Test
	public void testIsActive() {
		assertFalse(ctrl2.isActive(5000));
		assertFalse(ctrl2.isActive(0));
		assertTrue(ctrl2.isActive(6666666));
	}

	/**
	 * Test method for {@link chat.controller.Controller#startServer(int)}.
	 */
	@Test
	public void testStartServer() {
		ctrl2.startServer(2);
		ctrl2.stopServer();
		ctrl2.startServer(789);
		ctrl2.stopServer();
		ctrl2.startServer(462);
		ctrl2.stopServer();
		/*ctrl2.startServer(25);
		ctrl2.stopServer();
		ctrl2.startServer(0);
		ctrl2.stopServer();*/
	}
	/**
	 * Test method for {@link chat.controller.Controller#isServerStarted()}.
	 */
	@Test
	public void testIsServerStarted() {
		ctrl2.startServer(182);
		assertTrue(ctrl2.isServerStarted());
		ctrl2.startServer(462); // Port utilisé
		assertFalse(ctrl2.isServerStarted());
		//ctrl2.startServer(-5);
	}

	/**
	 * Test method for {@link chat.controller.Controller#setServerStarted(boolean)}.
	 */
	@Test
	public void testSetServerStarted() {
		ctrl2.setServerStarted(true);
	}

	/**
	 * Test method for {@link chat.controller.Controller#isClientStarted()}.
	 */
	/*@Test
	public void testIsClientStarted() {
		fail("Not yet implemented");
	}*/

	/**
	 * Test method for {@link chat.controller.Controller#setClientStarted(boolean)}.
	 */
	/*@Test
	public void testSetClientStarted() {
		fail("Not yet implemented");
	}*/

}
