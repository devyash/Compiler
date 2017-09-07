/**
 * /**
 * JUunit tests for the Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2017.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2017 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2017
 */

package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.Scanner.Token;

import static cop5556fa17.Scanner.Kind.*;

public class ScannerTest {

	//set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	//To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}

	/**
	 *Retrieves the next token and checks that it is an EOF token. 
	 *Also checks that this was the last token.
	 *
	 * @param scanner
	 * @return the Token that was retrieved
	 */
	
	Token checkNextIsEOF(Scanner scanner) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token.kind);
		assertFalse(scanner.hasTokens());
		return token;
	}


	/**
	 * Retrieves the next token and checks that its kind, position, length, line, and position in line
	 * match the given parameters.
	 * 
	 * @param scanner
	 * @param kind
	 * @param pos
	 * @param length
	 * @param line
	 * @param pos_in_line
	 * @return  the Token that was retrieved
	 */
	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line) {
		Token t = scanner.nextToken();
		assertEquals(scanner.new Token(kind, pos, length, line, pos_in_line), t);
		return t;
	}

	/**
	 * Retrieves the next token and checks that its kind and length match the given
	 * parameters.  The position, line, and position in line are ignored.
	 * 
	 * @param scanner
	 * @param kind
	 * @param length
	 * @return  the Token that was retrieved
	 */
	Token check(Scanner scanner, Scanner.Kind kind, int length) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(length, t.length);
		return t;
	}

	/**
	 * Simple test case with a (legal) empty program
	 *   
	 * @throws LexicalException
	 */
	@Test
	public void testEmpty() throws LexicalException {
		String input = "";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}
	
	/**
	 * Test illustrating how to put a new line in the input program and how to
	 * check content of tokens.
	 * 
	 * Because we are using a Java String literal for input, we use \n for the
	 * end of line character. (We should also be able to handle \n, \r, and \r\n
	 * properly.)
	 * 
	 * Note that if we were reading the input from a file, as we will want to do 
	 * later, the end of line character would be inserted by the text editor.
	 * Showing the input will let you check your input is what you think it is.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void testSemi() throws LexicalException {
		System.out.println("testSemi");
		String input = ";;\n;;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, SEMI, 0, 1, 1, 1);
		checkNext(scanner, SEMI, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 3, 1, 2, 1);
		checkNext(scanner, SEMI, 4, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testdoubleEqualto() throws LexicalException {
		System.out.println("testdoubleEqualto");
		String input = "a==b";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, KW_a, 0, 1, 1, 1);
		checkNext(scanner, OP_EQ, 1, 2, 1, 2);
		checkNext(scanner, IDENTIFIER, 3, 1, 1, 4);
		checkNextIsEOF(scanner);

	}
	@Test
	public void testOnlyKeyword() throws LexicalException{
		System.out.println("testOnlyKeyword");
		String input="A";
		Scanner scanner= new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner,KW_A,0,1,1,1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testnewLinewithKeyword() throws LexicalException{
		System.out.println("---testnewLinewithKeyword----");
		String input="A\nA";
		show(input);
		Scanner scanner= new Scanner(input).scan();
		show(scanner);
		checkNext(scanner,KW_A,0,1,1,1);
		checkNext(scanner,KW_A,2,1,2,1);
		checkNextIsEOF(scanner);
	}
	@Test
	public void testSpacefollowedByKeyWord() throws LexicalException{
		System.out.println("testSpacefollowedByKeyWord");
		String input="    \nA";
		show(input);
		Scanner scanner= new Scanner(input).scan();
		show(scanner);
		checkNext(scanner,KW_A,5,1,2,1);
		checkNextIsEOF(scanner);
	}
	@Test
	public void testReturnCharacterInInput() throws LexicalException{
		System.out.println("---testReturnCharacterInInput---");
		String input="Test\rabc"; //https://ufl.instructure.com/courses/342882/discussion_topics/1532830
		show(input);
		Scanner scanner= new Scanner(input).scan();
		show(scanner);
		checkNext(scanner,IDENTIFIER,0,4,1,1);
		checkNext(scanner,IDENTIFIER,5,3,2,1);
		checkNextIsEOF(scanner);
	}
	@Test
	public void testSingleCommentLine() throws LexicalException{
		System.out.println("---testSingleCommentLine---");
		String input="//testabc";
		show(input);
		Scanner scanner= new Scanner(input).scan();
		show(scanner);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEmptyComment() throws LexicalException{
		System.out.println("---testEmptyComment---");
		String input="//";
		show(input);
		Scanner scanner= new Scanner(input).scan();
		show(scanner);
		checkNextIsEOF(scanner);
	}

	@Test
	public void testVeryBigIntwithPrecedingCharacters() throws LexicalException {
		System.out.println("---testVeryBigIntwithPrecedingCharacters---");
		String input="abcd 999191991991001001101010101001111111";
		Scanner scanner=new Scanner(input);
		show(input);		
		try {
			scanner.scan();
			
		} catch (LexicalException e) {  //
			show(e);
			//assertEquals(13,e.getPos()); pos inLine will not be checked https://ufl.instructure.com/courses/342882/discussion_topics/1529604 
			// They will check that an exception is thrown and that the preceding tokens have been created properly.
			throw e;
		}
		checkNext(scanner,IDENTIFIER,0,4,1,1);
		thrown.expect(LexicalException.class); 
	}
	@Test
	public void testBasicIndentifierAndKeyword() throws LexicalException{
		System.out.println("---testBasicIndentifierAndKeyword---");
		String input="file fwbcs";
		show(input);
		Scanner scanner= new Scanner(input).scan();
		show(scanner);
		checkNext(scanner,KW_file,0,4,1,1);
		checkNext(scanner,IDENTIFIER,5,5,1,6);
		checkNextIsEOF(scanner);
	}
	

	/**
	 * This example shows how to test that your scanner is behaving when the
	 * input is illegal.  In this case, we are giving it a String literal
	 * that is missing the closing ".  
	 * 
	 * Note that the outer pair of quotation marks delineate the String literal
	 * in this test program that provides the input to our Scanner.  The quotation
	 * mark that is actually included in the input must be escaped, \".
	 * 
	 * The example shows catching the exception that is thrown by the scanner,
	 * looking at it, and checking its contents before rethrowing it.  If caught
	 * but not rethrown, then JUnit won't get the exception and the test will fail.  
	 * 
	 * The test will work without putting the try-catch block around 
	 * new Scanner(input).scan(); but then you won't be able to check 
	 * or display the thrown exception.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void failUnclosedStringLiteral() throws LexicalException {
		String input = "\" greetings  ";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //
			show(e);
			assertEquals(13,e.getPos());
			throw e;
		}
	}
	@Test     
    public void testCRLFInOneLine() throws LexicalException {
		System.out.println("testCRLFInOneLine");
        String input = "cd\r\ncos";         
        Scanner scanner = new Scanner(input).scan();         
        show(input);         
        show(scanner);         
        checkNext(scanner,IDENTIFIER, 0,2, 1, 1); 
        checkNext(scanner,KW_cos, 4,3, 2, 1);
        checkNextIsEOF(scanner);     
        }
	@Test
    public void testSemi1() throws LexicalException {
        String input = "a==b";
        Scanner scanner = new Scanner(input).scan();
        show(input);
        show(scanner);
        checkNext(scanner, KW_a, 0, 1, 1, 1);
        checkNext(scanner, OP_EQ, 1, 2, 1, 2);
        checkNext(scanner, IDENTIFIER, 3, 1, 1, 4);
        checkNextIsEOF(scanner);
    }
    
    @Test
    public void testSemi2() throws LexicalException {
        String input = "afile afile";
        Scanner scanner = new Scanner(input).scan();
        show(input);
        show(scanner);
        checkNext(scanner, IDENTIFIER, 0, 5, 1, 1);
        checkNext(scanner,IDENTIFIER,6,5,1,7);
        checkNextIsEOF(scanner);
    }
    
  
    
    @Test
    public void testSemi41() throws LexicalException {
        String input = "file afile";
        Scanner scanner = new Scanner(input).scan();
        show(input);
        show(scanner);
        checkNext(scanner,KW_file,0,4,1,1);
        checkNext(scanner,IDENTIFIER, 5,5, 1, 6);
        checkNextIsEOF(scanner);
    }
    
    @Test
    public void testSemi4() throws LexicalException {
        String input = "atan\natan";
        Scanner scanner = new Scanner(input).scan();
        show(input);
        show(scanner);
        checkNext(scanner,KW_atan,0,4,1,1);
        checkNext(scanner,KW_atan,5,4,2,1);
        checkNextIsEOF(scanner);
    }
    
    @Test
    public void testSemiSD1() throws LexicalException {
        String input = "+hello\natan cart_y";
        Scanner scanner = new Scanner(input).scan();
        show(input);
        show(scanner);
        checkNext(scanner,OP_PLUS,0,1,1,1);
        checkNext(scanner,IDENTIFIER,1,5,1,2);
        checkNext(scanner,KW_atan,7,4,2,1);
        checkNext(scanner,KW_cart_y,12,6,2,6);
        checkNextIsEOF(scanner);
    }
    
    @Test
    public void testSemiaa() throws LexicalException {
        String input = "file+afile";
        Scanner scanner = new Scanner(input).scan();
        show(input);
        show(scanner);
        checkNext(scanner, KW_file, 0, 4, 1, 1);
        checkNext(scanner, OP_PLUS, 4, 1, 1, 5);
        checkNext(scanner, IDENTIFIER, 5, 5, 1, 6);
        checkNextIsEOF(scanner);
    }
    
    @Test
    public void testSemiSD2() throws LexicalException {
            String input = "++\n\r\r\n\ncart_y";
            Scanner scanner = new Scanner(input).scan();
            show(input);
            show(scanner);
            checkNext(scanner,OP_PLUS,0,1,1,1);
            checkNext(scanner,OP_PLUS,1,1,1,2);
            checkNext(scanner,KW_cart_y,7,6,5,1);
            checkNextIsEOF(scanner);
        }
    
    @Test
       public void testSemi10() throws LexicalException {
           String input = "atan\n\r\r+ abc";
           Scanner scanner = new Scanner(input).scan();
           show(input);
           show(scanner);
           checkNext(scanner,KW_atan,0,4,1,1);
           checkNext(scanner,OP_PLUS,7,1,4,1);
           checkNext(scanner,IDENTIFIER,9,3,4,3);
           checkNextIsEOF(scanner);
       }
    
    @Test     
    public void testSemiSDK() throws LexicalException {         
        String input = "\n\r\r\natan A";         
        Scanner scanner = new Scanner(input).scan();    
        System.out.println("a\nbcd\t\feg\bgh");
        show(input);         
        show(scanner);         
        checkNext(scanner,KW_atan,4,4,4,1);                
        checkNext(scanner,KW_A,9,1,4,6);         
        checkNextIsEOF(scanner);     
        }
    
    @Test
    public void testSemi8() throws LexicalException {
           String input = "ABC\r\nabc";
           Scanner scanner = new Scanner(input).scan();
           show(input);
           show(scanner);
           checkNext(scanner,IDENTIFIER, 0,3, 1, 1);
           checkNext(scanner,IDENTIFIER, 5,3, 2, 1);
           checkNextIsEOF(scanner);
       }
    
    @Test     
    public void testSemiSDK1() throws LexicalException {         
        String input = "==\nabc=abc==";         
        Scanner scanner = new Scanner(input).scan();    
        show(input);         
        show(scanner);         
        checkNext(scanner,OP_EQ,0,2,1,1);                
        checkNext(scanner,IDENTIFIER,3,3,2,1); 
        checkNext(scanner,OP_ASSIGN,6,1,2,4);                
        checkNext(scanner,IDENTIFIER,7,3,2,5);
        checkNext(scanner,OP_EQ,10,2,2,8);                
        checkNextIsEOF(scanner);     
        }
    
    @Test
    public void testSemi15() throws LexicalException {
         String input = "AB==";         
            Scanner scanner = new Scanner(input).scan();    
            show(input);         
            show(scanner);                        
            checkNext(scanner,IDENTIFIER,0,2,1,1); 
            checkNext(scanner,OP_EQ,2,2,1,3);                
            checkNextIsEOF(scanner); 
        }
    
    @Test        
    public void testSemi19() throws LexicalException {                
        String input = "abc>x";                
        Scanner scanner = new Scanner(input).scan();            
        show(input);                
        show(scanner);                                
        checkNext(scanner,IDENTIFIER,0,3,1,1);        
        checkNext(scanner,OP_GT,3,1,1,4);                        
        checkNext(scanner,KW_x,4,1,1,5);                        
        checkNextIsEOF(scanner);      
        }
    

     @Test        
     public void testSemi23() throws LexicalException {            
         String input = "?atan\n\r<->";            
         Scanner scanner = new Scanner(input).scan();            
         show(input);            
         show(scanner);
         checkNext(scanner,OP_Q,0,1,1,1);
         checkNext(scanner,KW_atan,1,4,1,2);            
         checkNext(scanner,OP_LARROW,7,2,3,1);            
         checkNext(scanner,OP_GT,9,1,3,3);               
         checkNextIsEOF(scanner);        
         }
     
     @Test        
     public void testSemikkk() throws LexicalException {            
         String input = "  ==abc<=\n=><--> A !==! ==";            
         Scanner scanner = new Scanner(input).scan();            
         show(input);            
         show(scanner);
         checkNext(scanner,OP_EQ,2,2,1,3);
         checkNext(scanner,IDENTIFIER,4,3,1,5);            
         checkNext(scanner,OP_LE,7,2,1,8);            
         checkNext(scanner,OP_ASSIGN,10,1,2,1);     
         checkNext(scanner,OP_GT,11,1,2,2);
         checkNext(scanner,OP_LARROW,12,2,2,3);     
         checkNext(scanner,OP_RARROW,14,2,2,5);
         checkNext(scanner,KW_A,17,1,2,8);     
         checkNext(scanner,OP_NEQ,19,2,2,10);
         checkNext(scanner,OP_ASSIGN,21,1,2,12);
         checkNext(scanner,OP_EXCL,22,1,2,13);
         checkNext(scanner,OP_EQ,24,2,2,15);
         checkNextIsEOF(scanner);        
         }
    
     @Test        
        public void testSemiSD6() throws LexicalException {                
            String input = "abc==x?=(sin x)";                
            Scanner scanner = new Scanner(input).scan();            
            show(input);                
            show(scanner);                                 
            checkNext(scanner,IDENTIFIER,0,3,1,1);        
            checkNext(scanner,OP_EQ,3,2,1,4);                        
            checkNext(scanner,KW_x,5,1,1,6);
            checkNext(scanner,OP_Q,6,1,1,7);
            checkNext(scanner,OP_ASSIGN,7,1,1,8);
            checkNext(scanner,LPAREN,8,1,1,9);
            checkNext(scanner,KW_sin,9,3,1,10);
            checkNext(scanner,KW_x,13,1,1,14);
            checkNext(scanner,RPAREN,14,1,1,15);
            checkNextIsEOF(scanner);            
            }


}
