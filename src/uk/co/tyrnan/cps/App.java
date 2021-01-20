import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class App extends JPanel implements Runnable {

  private final JLabel results;
  private Thread thread;
  private long startTime = -1;
  private int clickCount = 0;

  public App() {
    this.setLayout(new BorderLayout(5,5));

    // Top label
    this.add(new JLabel("Click the box below to measure your CPS"), BorderLayout.NORTH);

    // Centre clickable canvas
    JPanel target = new JPanel();
    target.setBorder(new BevelBorder(BevelBorder.LOWERED));
    target.setBackground(Color.blue);
    target.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent mouseEvent) {
        if (startTime == -1) {
          startTime = System.currentTimeMillis();
        }

        clickCount++;
      }
      // We need these to fulfill the contract of the mouse listener interface
      @Override
      public void mousePressed(MouseEvent mouseEvent) {}
      @Override
      public void mouseReleased(MouseEvent mouseEvent) {}
      @Override
      public void mouseEntered(MouseEvent mouseEvent) { }
      @Override
      public void mouseExited(MouseEvent mouseEvent) {}
    });
    this.add(target, BorderLayout.CENTER);

    // Bottom info/button panel
    JPanel bottom = new JPanel(new GridLayout(1, 3, 5, 5));
    bottom.add(new JLabel("Current CPS"));
    results = new JLabel("0");
    bottom.add(results);
    JButton reset =new JButton("Reset");
    bottom.add(reset);
    this.add(bottom, BorderLayout.SOUTH);

    reset.addActionListener(actionEvent -> {
      startTime = -1;
      clickCount = 0;
    });
  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void run() {
    while (this.thread != null ) {
      try {
        System.out.println("================\n" + this.startTime + ", " + this.clickCount);
        if (this.startTime != -1 && this.clickCount != 0) {
          long offsetSeconds = (System.currentTimeMillis() - this.startTime) / 1000;
          float cps = ((float)this.clickCount / offsetSeconds);
          this.results.setText(String.format("Time: %ds Clicks: %d, CPS: %f", offsetSeconds, this.clickCount, cps));
        }
        thread.sleep(1000l);
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }

  public void stop() {
    this.thread = null;
  }

  public static void main(String [] args) {
    JFrame jFrame = new JFrame("CPS Calculator");
    App app = new App();
    jFrame.add(app);
    jFrame.setSize(800, 600);
    jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.setVisible(true);
    app.start();
  }
}
