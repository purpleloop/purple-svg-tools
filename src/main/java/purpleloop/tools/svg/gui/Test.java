package purpleloop.tools.svg.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import purpleloop.commons.swing.SwingUtils;
import purpleloop.tools.svg.model.SvgBuilder;
import purpleloop.tools.svg.model.SvgDocument;

public class Test extends JFrame implements ActionListener {

    /** Serialization tag. */
    private static final long serialVersionUID = -3484307058487432693L;

    /** Class logger. */
    public static final Log LOG = LogFactory.getLog(Test.class);

    private static final String ACTION_OPEN_SVG = "ACTION_OPEN_SVG";

    private static final String ACTION_SAVE_GIF = "ACTION_SAVE_GIF";

    private static final String CMD_REFRESH = "CMD_REFRESH";

    private SvgDocument svgDoc;

    private SvgRenderPanel rp;

    private JFileChooser fileChooser;

    private FileFilter ffSVG;

    private FileFilter ffPNG;

    private File loadedSvgFile;

    public Test() {
        super("SVG Tools");

        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        rp = new SvgRenderPanel();

        mainPanel.add(rp, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuFichier = new JMenu("File");

        SwingUtils.addMenuItem("Open", ACTION_OPEN_SVG, this, menuFichier);
        SwingUtils.addMenuItem("Export as Gif", ACTION_SAVE_GIF, this, menuFichier);

        JPanel cmdPanel = new JPanel();
        SwingUtils.createButton("Refresh", CMD_REFRESH, cmdPanel, this, true);
        mainPanel.add(cmdPanel, BorderLayout.SOUTH);

        menuBar.add(menuFichier);

        pack();

        fileChooser = new JFileChooser(new File("."));
        ffSVG = new FileFilter() {

            public boolean accept(File f) {

                return f.isDirectory() || f.getName().endsWith(".svg");
            }

            @Override
            public String getDescription() {
                return "SVG Files";
            }

        };

        ffPNG = new FileFilter() {

            public boolean accept(File f) {

                return f.isDirectory() || f.getName().endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "PNG Files";
            }

        };
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    }

    public static void main(String[] args) {
        
        LOG.info("Starting app");

        LOG.info("Registering PNG converters :");
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");                
        while (it.hasNext()) {
            LOG.info("- ImageWriter : "+it.next());            
        }

        Test tst = new Test();
        tst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tst.setVisible(true);

    }

    public void actionPerformed(ActionEvent evt) {

        String command = evt.getActionCommand();

        if (command.equals(ACTION_OPEN_SVG)) {

            openSvgFile();

        } else if (command.equals(ACTION_SAVE_GIF)) {
            saveGifFile();
        } else if (command.equals(CMD_REFRESH)) {

            if (loadedSvgFile!=null) {
                updateDocument();               
            }

        }
    }

    private void saveGifFile() {

        fileChooser.setFileFilter(ffPNG);
        int res = fileChooser.showSaveDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            svgDoc.savePNGToFile(file);
        }

    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);

    }

    private void openSvgFile() {

        fileChooser.setFileFilter(ffSVG);
        int res = fileChooser.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION) {

            loadedSvgFile = fileChooser.getSelectedFile();

            updateDocument();

        }

    }

    private void updateDocument() {
        
        svgDoc = SvgBuilder.loadFromFile(loadedSvgFile);

        if (svgDoc != null) {

            rp.setDocument(svgDoc);
            repaint();
        } else {
            showMessage("Unable to open the document in file " + loadedSvgFile.getAbsoluteFile() + ".");
        }
    }

}