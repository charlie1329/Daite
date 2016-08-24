package logger;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**class is used as a small GUI with which messages to/from bot can be shown 
 * to us in the background. This is useful for not only us but also for the gadget show
 * as they can see what's being said without interrupting the subjects.
 * @author Charlie Street
 *
 */
public class ConvoLogger extends JFrame {
	
	private static final long serialVersionUID = 6604869877417235253L;//making eclipse happy
	private JTextArea textBox; //self-explanatory fields
	private JScrollPane scroller;
	
	/**constructor will set up JFrame with appropriate text box inside
	 * 
	 */
	public ConvoLogger(){
		super();
		setLookAndFeel();
		
		this.textBox = new JTextArea();
		this.textBox.setEditable(false);
		this.scroller = new JScrollPane(this.textBox);
		
		this.setTitle("Gadget Show Bot Logger");
		this.setSize(600, 400);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//when we close window, we may still want bot to be running
		this.add(this.scroller);//add scroll pane
		
		try {
		    setIconImage(ImageIO.read(new File("images/logo.png"))); //adding icon image :)
		}
		catch (IOException exc) {
		    exc.printStackTrace();
		}
		
		this.setVisible(true);
	}
	
	
	/**method sets look and feel of JFrame to Nimbus which is slightly less horrible
	 * 
	 */
	private void setLookAndFeel() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    System.out.println("Default Swing Look and Feel it is then! :(");
		}
	}
	
	/**method will take a message and append it to the text area for display
	 * 
	 * @param toLog the message to log
	 */
	public void logMessage(String toLog) {
		this.textBox.append(toLog+"\n\n");//adding some new lines for ease of reading
	}
}
