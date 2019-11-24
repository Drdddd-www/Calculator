
/**
 * @class: CalculateController.java
 * @extends: java.lang.Object
 * @author: Lagomoro <Yongrui Wang>
 * @submit: 2019/11/11
 * @description: Calculate expression.
 **/
package org.sdumsc.course201907.controller;

import java.util.Stack;

import org.sdumsc.course201907.ui.components.EditText;
import org.sdumsc.course201907.ui.components.Label;

public class CalculateController {

	private static EditText editText;
	@SuppressWarnings("unused")
	private static Label label;

	public static void setEditText(EditText text) {
		editText = text;
	}

	public static void setLabel(Label l) {
		label = l;
	}

	public static void pushText(String text) {
		editText.setText(editText.getText() + text);
		editText.refresh();
	}

	public static void push(String text) {
		switch (text) {
			case "C":
				String temp = editText.getText();
				if (temp.length() > 0) {
					editText.setText(temp.substring(0, temp.length() - 1));
				}
				break;
			case "CE":
				editText.setText("");
				break;
			case "=":
				editText.setText(calculate(editText.getText()));
				break;
			default:
				pushText(text);
		}
	}

	public static String calculate(String text) {
		try {
			return Integer.toString(calculateSuffix(toSuffix(text)));
		} catch (Exception e) {
			return "Error!!!";
		}
	}

	public static boolean isNumber(String input) {
		return (input.charAt(0) >= '0' && input.charAt(0) <= '9') || input.charAt(0) == '-';
	}//修改isNumber的定义

	public static int compareSymbol(String symbol) {
		return symbol.equals("+") || symbol.equals("-") ? 0 : (symbol.equals("×") || symbol.equals("÷") || symbol.equals("*") || symbol.equals("/") ? 1 : -1);
	}          //符号为+-返回0，为*/返回1，其他符号返回-1

	public static Stack<String> toSuffix(String expression) throws Exception {
		Stack<String> result = new Stack<String>();
		Stack<String> temp = new Stack<String>();
		int length = expression.length();
		String current;
		//更改表达式，把减号看成加号（负数）
		for(int i = 0; i < length; i++){
			if (expression.charAt(i) == '-' && i != 0 &&(expression.charAt(i-1)==')'||(expression.charAt(i-1) >= '0' && expression.charAt(i-1) <= '9'))) {
				current = expression.charAt(i) + "";
				String value ="";
				if (isNumber(current)) {
					value = current;
					for (int j = i + 1; j < length; j++) {
						if (isNumber(expression.charAt(j) + "")) {
							value += expression.charAt(j);
						} else {
							break;
						}
					}
				}
				String newExpress = "";
				for (int j = 0; j <i; j++){ newExpress = newExpress+expression.charAt(j) ; }
				newExpress = newExpress + "+("+value+")";
				for (int j = i+value.length(); j < length; j++) newExpress = newExpress+expression.charAt(j) ;
				expression = newExpress;
				length = length + 3;
			}
		}
		for (int i = 0; i < length; i++) {
			current = expression.charAt(i) + "";
			if (isNumber(current)) {
				String value = current;
				for (int j = i + 1; j < length; j++) {
					if (isNumber(expression.charAt(j) + "")) {
						i++;
						value += expression.charAt(j);
					} else {
						break;
					}
				}
				temp.push(value);// 读取计算式的每一位数，并放在temp栈中
			} else if (current.equals("+") || current.equals("-") || current.equals("×") || current.equals("÷") || current.equals("*") || current.equals("/")) {
				while (true) {
					if (result.isEmpty() || result.get(result.size() - 1).equals("("))//如果result栈为空或者result栈的栈顶为（
					{
						result.push(current);//把当前+ - * / 存入
						break;
					} else if (compareSymbol(current) > compareSymbol(result.get(result.size() - 1))) //如果当前运算级大于栈顶的
					{
						result.push(current);//把当前运算符存入
						break;
					} else {
						temp.push(result.pop());//把result的栈顶存入temp栈中
					}
				}
			} else  //考虑（）
			{
				if (current.equals("(")) {
					result.push(current);//如果是（，直接存入result栈中
				} else //删除与结算符
				{
					while (!result.isEmpty() && !result.get(result.size() - 1).equals("("))
						temp.push(result.pop());//如果result栈不为空，且result栈顶不为（，则将result栈顶存入temp中
					if (!result.isEmpty())
						result.pop();//如果result栈不为空，出栈
				}
			}
		}
		while (!result.isEmpty())
			temp.push(result.pop());
		while (!temp.isEmpty())
			result.push(temp.pop());
		return result;
	}

	public static int calculateSuffix(Stack<String> suffixExpression) throws Exception {
		int temp;
		String current;
		Stack<Integer> result = new Stack<Integer>();
		while (!suffixExpression.isEmpty()) {
			current = suffixExpression.pop();//把result栈里的东西取出
			switch (current) {
				case "+":
					temp = result.pop();
					temp = result.pop() + temp;
					break;
				case "-":
					temp = result.pop();
					temp = result.pop() - temp;
					break;
				case "×":
				case "*":
					temp = result.pop();
					temp = result.pop() * temp;
					break;
				case "÷":
				case "/":
					temp = result.pop();
					temp = result.pop() / temp;
					break;
				default:
					temp = Integer.parseInt(current);
			}
			result.push(temp);
		}
		return result.get(result.size() - 1);
	}

}

