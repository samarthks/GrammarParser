
import java.lang.IllegalArgumentException;
import java.io.*;
/*
The following code is based on the grammar as follows.
<program> ::={<statement_list>}
<statement_list> ::=<statement>;<statement_list’>
<statement_list’> ::=<statement_list>
<statement_list’> ::=ε
<statement> ::=call: <procedure call>
<statement> ::=compute: <expression>
<procedure_call> ::=id(<parameters>)
 <parameters> ::=<factor><parameters’>
 <parameters’> ::=,<parameters>
<parameters’> ::=ε
<expression> ::=id=<factor><expression’>
<expression’> ::=+<factor>
<expression’> ::=-<factor>
<expression’> ::=ε
<factor> ::=id
<factor> ::=num
*/
public class GrammarParser {

	private static final boolean SUCCESS = true;
	private static final boolean ERROR = false;
	private BufferedReader file = null;
	private String file_name = null;
	private String token = "";

	public GrammarParser(String file_name) {

		try {
			this.file_name = file_name;
			this.openfile();

		} catch (FileNotFoundException e) {
			System.out.println("Error,File name incorrect.");
		}
	}

	private void openfile() throws IllegalArgumentException, FileNotFoundException {
		if (this.file_name == null)
			throw new IllegalArgumentException("Incorrect file name");
		this.file = new BufferedReader(new FileReader(this.file_name));
	}

	public String gettoken() {
		String tokens = "";
		try {
			tokens = (String) this.file.readLine();

		} catch (IOException exception) {
			// Prints out a message and returns an empty string.
			System.err.println("Exception occured");
			System.exit(1);
		}
		return tokens != null ? tokens : "";
	}

	private void updatetoken() {
		String tmp = this.gettoken();
		if (tmp != "") {
			this.token = tmp;
		}

	}

	public boolean validate() {
		updatetoken();
		if (!program() || !checktoken("$")) {
			return ERROR;
		}
		else {
			return SUCCESS;
		}
	}

	private boolean checktoken(String value) {

		if (this.token == "")
			return false;

		// return if the string equals the value.
		return this.token.equals(value);
	}

	public boolean program() {
		if (checktoken("{")) {
			updatetoken();
			if (!statementlist()) {
				return ERROR;
			}
			else {

				if (checktoken("}")) {
					updatetoken();
					return SUCCESS;
				}
				else {
					return ERROR;
				}
			}
		}
		else
			return ERROR;

	}

	public boolean statementlist() {
		if (!statement()) {
			return ERROR;
		}
		else if (checktoken(";")) {
			updatetoken();
			return statementlist_prime();
		}
		else {
			return ERROR;
		}
	}

	public boolean statementlist_prime() {
		if (checktoken("}")) {
			return SUCCESS;
		}
		else
			return statementlist();
	}

	public boolean statement() {
		if (checktoken("call")) {
			updatetoken();
			if (checktoken(":")) {
				updatetoken();
				return procedure_call();
			}

		}

		else if (checktoken("compute")) {
			updatetoken();
			if (checktoken(":")) {
				updatetoken();
				return expression();
			}
		}
		return ERROR;

	}

	public boolean procedure_call() {
		if (checktoken("id")) {
			updatetoken();
			if (token.equals("(")) {
				updatetoken();
				if (parameters()) {
					if (checktoken(")")) {
						updatetoken();
						return SUCCESS;
					}
				}
			}
		}
		return ERROR;
	}

	public boolean expression() {
		if (checktoken("id")) {
			updatetoken();
			if (checktoken("=")) {
				updatetoken();
				if (factor()) {
					return expression_prime();
				}
				else {
					return ERROR;
				}
			}
		}
		return ERROR;
	}

	public boolean parameters() {

		if (factor()) {
			return parameters_prime();
		}
		else
			return ERROR;
	}

	public boolean factor() {

		if (checktoken("id")) {
			updatetoken();
			return SUCCESS;
		}
		else if (checktoken("num")) {
			updatetoken();
			return SUCCESS;
		}
		else {
			return ERROR;
		}
	}

	public boolean parameters_prime() {
		if (token.equals(",")) {
			updatetoken();
			return parameters();
		}
		else if (checktoken(")")) {
			return SUCCESS;
		}
		else {
			return ERROR;
		}
	}

	public boolean expression_prime() {

		if (checktoken("+")) {
			updatetoken();
			return factor();
		}
		else if (checktoken("-")) {
			updatetoken();
			return factor();
		}
		else if (checktoken(";")) {
			return SUCCESS;
		}
		else {
			return ERROR;
		}
	}

	public static void main(String[] args) {
		if (args.length == 0 || args[0].equals(""))
			throw new IllegalArgumentException("Invalid arguments passed.");

		String file_name = (String) args[0];
		GrammarParser parser = new GrammarParser(file_name);

		boolean x = parser.validate();
		if (x == SUCCESS) {
			System.out.println("SUCCESS: the code has been successfully parsed");
		} else {
			System.out.println("ERROR: the code contains a syntax mistake");
		}

	}
}
