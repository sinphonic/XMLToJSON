package system;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainConvertorView {

	private JFrame frame;
	private JTextField txtDirectory;
	private DirectoryScanner scanner;
	private MainConvertor convertor;
	private JTextField txtOutputDir;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainConvertorView window = new MainConvertorView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainConvertorView() {
		initialize();
		convertor = new MainConvertor();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 766, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblDirectory = new JLabel("Directory: ");
		lblDirectory.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblDirectory.setBounds(21, 37, 111, 26);
		frame.getContentPane().add(lblDirectory);
		
		txtDirectory = new JTextField();
		txtDirectory.setBounds(135, 34, 420, 32);
		frame.getContentPane().add(txtDirectory);
		txtDirectory.setColumns(10);
		
		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblStatus.setBounds(21, 344, 82, 26);
		frame.getContentPane().add(lblStatus);

		JLabel lblStatusText = new JLabel("");
		lblStatusText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblStatusText.setBounds(107, 344, 610, 26);
		frame.getContentPane().add(lblStatusText);
		
		JLabel lblProgress = new JLabel("Progress: ");
		lblProgress.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblProgress.setBounds(21, 273, 111, 26);
		frame.getContentPane().add(lblProgress);
		
		JLabel lblProgressText = new JLabel("");
		lblProgressText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblProgressText.setBounds(135, 273, 582, 26);
		frame.getContentPane().add(lblProgressText);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtDirectory.setText(jfc.getSelectedFile().getAbsolutePath());
					scanner = new DirectoryScanner(jfc.getSelectedFile());
					scanner.startScan();
					lblStatusText.setText(scanner.getNumFiles() + " files found");
					convertor.setFileList(scanner.fileList);
				}
			}
		});
		btnBrowse.setBounds(576, 33, 141, 35);
		frame.getContentPane().add(btnBrowse);
		
		JButton btnBrowseOutDir = new JButton("Browse...");
		btnBrowseOutDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtOutputDir.setText(jfc.getSelectedFile().getAbsolutePath());
					convertor.setOutputFolder(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnBrowseOutDir.setBounds(576, 92, 141, 35);
		frame.getContentPane().add(btnBrowseOutDir);
		
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblProgressText.setText("Converting Files...");
				convertor.convert();
				lblProgressText.setText("Finished Conversion");
			}
		});
		btnRun.setBounds(576, 217, 141, 35);
		frame.getContentPane().add(btnRun);
		
		JLabel lblOutputDir = new JLabel("Out Dir: ");
		lblOutputDir.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblOutputDir.setBounds(43, 96, 89, 26);
		frame.getContentPane().add(lblOutputDir);
		
		txtOutputDir = new JTextField();
		txtOutputDir.setText(".");
		txtOutputDir.setBounds(135, 93, 420, 32);
		frame.getContentPane().add(txtOutputDir);
		txtOutputDir.setColumns(10);
		
	}
}
