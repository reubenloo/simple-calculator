package Calculator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Calculator {
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel();
	private ArrayList<JButton> buttonList = new ArrayList<JButton>();
	private ArrayList<String> stringList = new ArrayList<String>();
	
	//create literal characters for button
	private JButton b0 = new JButton("0");
	private JButton b1 = new JButton("1");
	private JButton b2 = new JButton("2");
	private JButton b3 = new JButton("3");
	private JButton b4 = new JButton("4");
	private JButton b5 = new JButton("5");
	private JButton b6 = new JButton("6");
	private JButton b7 = new JButton("7");
	private JButton b8 = new JButton("8");
	private JButton b9 = new JButton("9");
	private JButton plus = new JButton("+");
	private JButton minus = new JButton("-");
	private JButton multiply = new JButton("*");
	private JButton divide = new JButton("/");
	private JButton decimal = new JButton(".");
	
	//create non-literal characters for button
	private JButton equals = new JButton("=");
	private JButton backspace = new JButton("B");
	private JButton clear = new JButton("C");
	private JButton filler1 = new JButton();
	private JButton filler2 = new JButton();
	

	public static void main(String[] args) {
		new Calculator();

	}
	
	public Calculator() {
		frame.setLayout(null);
		panel.setLayout(null);
		
		//add buttons to buttonList
		buttonList.addAll(Arrays.asList(b0, b1, b2, b3, b4, b5, b6,
				b7, b8, b9, plus, minus, multiply, divide, equals,
				backspace, decimal, clear, filler1, filler2));
		
		//set font details of buttons & label
		Font f = new Font("Arial", Font.BOLD, 30);
		for (JButton J : buttonList) {
			J.setFont(f);
		}
		label.setFont(f);
		
		//set position of label
		label.setBounds(0, 0, 400, 100);
		
		//set position of buttons
		filler1.setBounds(0, 100, 100, 100);
		clear.setBounds(100, 100, 100, 100);
		backspace.setBounds(200, 100, 100, 100);
		divide.setBounds(300, 100, 100, 100);
		
		b7.setBounds(0, 200, 100, 100);
		b8.setBounds(100, 200, 100, 100);
		b9.setBounds(200, 200, 100, 100);
		multiply.setBounds(300, 200, 100, 100);
		
		b4.setBounds(0, 300, 100, 100);
		b5.setBounds(100, 300, 100, 100);
		b6.setBounds(200, 300, 100, 100);
		minus.setBounds(300, 300, 100, 100);
		
		b1.setBounds(0, 400, 100, 100);
		b2.setBounds(100, 400, 100, 100);
		b3.setBounds(200, 400, 100, 100);
		plus.setBounds(300, 400, 100, 100);
		
		filler2.setBounds(0, 500, 100, 100);
		b0.setBounds(100, 500, 100, 100);
		decimal.setBounds(200, 500, 100, 100);
		equals.setBounds(300, 500, 100, 100);
		
		//add buttons & label to panel
		for (JButton J : buttonList) {
			panel.add(J);
		}
		panel.add(label);
		
		//add character from button to label & refresh after each click
		label.setText("");
		for (JButton J : buttonList) {
			//exclude non-literal characters
			if (J != equals && J != backspace && J != clear) {
				String character = J.getText();
				ActionListener addText = (ActionEvent e) -> AddLabelText(character);
				J.addActionListener(addText);
			}
		}
		
		//backspace one character
		ActionListener Backspace = (ActionEvent e) -> BackspaceCharacter();
		backspace.addActionListener(Backspace);
		
		//clear all characters
		ActionListener Clear = (ActionEvent e) -> ClearCharacters();
		clear.addActionListener(Clear);
		
		//start calculation when "=" is pressed, check for invalid expressions
		ActionListener InvalidExpression = (ActionEvent e) -> CheckInvalidExpression();
		equals.addActionListener(InvalidExpression);
		
		//add input to stringList
		ActionListener StringList = (ActionEvent e) -> AddStringList();
		equals.addActionListener(StringList);

		//set panel to frame, etc
		frame.setContentPane(panel);
		
		frame.setTitle("Calculator");
		frame.setSize(416, 632);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	

	public void AddLabelText(String character) {
		label.setText(label.getText() + character);
		
	}
	
	
	//invalid expressions constraints:
	//-no operator in front of the first number e.g. -9
	//-for double, user does not input multiple ".", e.g. 9.9.9
	public void CheckInvalidExpression() {
		//reason I am doing pattern in terms of */+- instead of
		//\d is to not complicate with int & double regex patterns
		String invalidPattern = "[^*/+-]+[*/+-.]{2,}[^*/+-]*";
		if (Pattern.matches(invalidPattern, label.getText())) {
			label.setText("Invalid Expression!");
		}
	}
	
	
	//
	public void AddStringList() {
		//unable to regex the below pattern. HELP!
//		String[] stringArray = label.getText().split("(?<=[/*+-])|(?=[/*+-])");
		
		//thus using workaround via replace, followed by regex
		String delimiterStr1 = label.getText().replace("*", "&*&");
		String delimiterStr2 = delimiterStr1.replace("/", "&/&");
		String delimiterStr3 = delimiterStr2.replace("+", "&+&");
		String delimiterStr4 = delimiterStr3.replace("-", "&-&");
		String[] stringArray = delimiterStr4.split("&");
		Collections.addAll(stringList, stringArray);
		

		//ConcurrentModificationException encountered. I opted to iterate over
		// a copy of the collection, and modify the original list after
		ArrayList<String> stringListCopy = new ArrayList<String>(stringList);

		// count number of * operator in stringList
		int multiplyNum = Collections.frequency(stringListCopy, "*");
		for (int x = 0; x < multiplyNum; x++) {
			for (int i = 0; i < stringListCopy.size(); i++) {
				if (stringListCopy.get(i).equals("*")) {
					double bef = Double.parseDouble(stringList.get(i - 1));
					double aft = Double.parseDouble(stringList.get(i + 1));

					String sum = Double.toString(bef * aft);

					// e.g. remove 3*3 and add 9
					stringList.remove(i + 1);
					stringList.remove(i);
					stringList.add(i, sum);
					stringList.remove(i - 1);
					break;
				}

			}
			stringListCopy = stringList;
		}

		int divideNum = Collections.frequency(stringListCopy, "/");
		for (int x = 0; x < divideNum; x++) {
			for (int i = 0; i < stringListCopy.size(); i++) {
				if (stringListCopy.get(i).equals("/")) {
					double bef = Double.parseDouble(stringList.get(i - 1));
					double aft = Double.parseDouble(stringList.get(i + 1));

					String sum = Double.toString(bef / aft);
					stringList.remove(i + 1);
					stringList.remove(i);
					stringList.add(i, sum);
					stringList.remove(i - 1);
					break;
				}

			}
			stringListCopy = stringList;
		}

		int plusNum = Collections.frequency(stringListCopy, "+");
		for (int x = 0; x < plusNum; x++) {
			for (int i = 0; i < stringListCopy.size(); i++) {
				if (stringListCopy.get(i).equals("+")) {
					double bef = Double.parseDouble(stringList.get(i - 1));
					double aft = Double.parseDouble(stringList.get(i + 1));

					String sum = Double.toString(bef + aft);
					stringList.remove(i + 1);
					stringList.remove(i);
					stringList.add(i, sum);
					stringList.remove(i - 1);
					break;
				}

			}
			stringListCopy = stringList;
		}

		int minusNum = Collections.frequency(stringListCopy, "-");
		for (int x = 0; x < minusNum; x++) {
			for (int i = 0; i < stringListCopy.size(); i++) {
				if (stringListCopy.get(i).equals("-")) {
					double bef = Double.parseDouble(stringList.get(i - 1));
					double aft = Double.parseDouble(stringList.get(i + 1));

					String sum = Double.toString(bef - aft);
					stringList.remove(i + 1);
					stringList.remove(i);
					stringList.add(i, sum);
					stringList.remove(i - 1);
					break;
				}

			}
			stringListCopy = stringList;
		}

		label.setText(stringList.get(0));
	}
	
	//backspace only updates front end
	public void BackspaceCharacter() {
		label.setText(label.getText().substring(0, label.getText().length()-1));
	}
	
	//clear only updates front end
	public void ClearCharacters() {
		label.setText("");
	}

}
