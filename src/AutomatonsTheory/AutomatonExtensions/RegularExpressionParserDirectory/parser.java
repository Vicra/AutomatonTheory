package AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory;

import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.tree.*;

import java.util.Hashtable;

/** CUP v0.11b 20140808 (SVN rev 54) generated parser.
 */
@SuppressWarnings({"rawtypes"})
public class parser extends java_cup.runtime.lr_parser {

    public final Class getSymbolContainer() {
        return sym.class;
    }

    /** Default constructor. */
    public parser() {super();}

    /** Constructor which sets the default scanner. */
    public parser(java_cup.runtime.Scanner s) {super(s);}

    /** Constructor which sets the default scanner. */
    public parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

    /** Production table. */
    protected static final short _production_table[][] =
            unpackFromStrings(new String[] {
                    "\000\011\000\002\002\005\000\002\002\004\000\002\002" +
                            "\003\000\002\003\005\000\002\003\003\000\002\004\004" +
                            "\000\002\004\003\000\002\005\003\000\002\005\005" });

    /** Access to production table. */
    public short[][] production_table() {return _production_table;}

    /** Parse-action table. */
    protected static final short[][] _action_table =
            unpackFromStrings(new String[] {
                    "\000\017\000\006\007\011\011\010\001\002\000\014\002" +
                            "\ufffb\004\ufffb\005\ufffb\006\ufffb\010\ufffb\001\002\000\014" +
                            "\002\ufffd\004\ufffd\005\ufffd\006\020\010\ufffd\001\002\000" +
                            "\012\002\uffff\004\uffff\005\016\010\uffff\001\002\000\006" +
                            "\002\021\004\014\001\002\000\014\002\ufffa\004\ufffa\005" +
                            "\ufffa\006\ufffa\010\ufffa\001\002\000\006\007\011\011\010" +
                            "\001\002\000\006\004\014\010\013\001\002\000\014\002" +
                            "\ufff9\004\ufff9\005\ufff9\006\ufff9\010\ufff9\001\002\000\006" +
                            "\007\011\011\010\001\002\000\012\002\001\004\001\005" +
                            "\016\010\001\001\002\000\006\007\011\011\010\001\002" +
                            "\000\014\002\ufffe\004\ufffe\005\ufffe\006\020\010\ufffe\001" +
                            "\002\000\014\002\ufffc\004\ufffc\005\ufffc\006\ufffc\010\ufffc" +
                            "\001\002\000\004\002\000\001\002" });

    /** Access to parse-action table. */
    public short[][] action_table() {return _action_table;}

    /** <code>reduce_goto</code> table. */
    protected static final short[][] _reduce_table =
            unpackFromStrings(new String[] {
                    "\000\017\000\012\002\006\003\005\004\004\005\003\001" +
                            "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                            "\000\002\001\001\000\002\001\001\000\012\002\011\003" +
                            "\005\004\004\005\003\001\001\000\002\001\001\000\002" +
                            "\001\001\000\010\003\014\004\004\005\003\001\001\000" +
                            "\002\001\001\000\006\004\016\005\003\001\001\000\002" +
                            "\001\001\000\002\001\001\000\002\001\001" });

    /** Access to <code>reduce_goto</code> table. */
    public short[][] reduce_table() {return _reduce_table;}

    /** Instance of action encapsulation class. */
    protected CUP$parser$actions action_obj;

    /** Action encapsulation object initializer. */
    protected void init_actions()
    {
        action_obj = new CUP$parser$actions(this);
    }

    /** Invoke a user supplied parse action. */
    public java_cup.runtime.Symbol do_action(
            int                        act_num,
            java_cup.runtime.lr_parser parser,
            java.util.Stack            stack,
            int                        top)
            throws java.lang.Exception
    {
    /* call code in generated class */
        return action_obj.CUP$parser$do_action(act_num, parser, stack, top);
    }

    /** Indicates start state. */
    public int start_state() {return 0;}
    /** Indicates start production. */
    public int start_production() {return 1;}

    /** <code>EOF</code> Symbol index. */
    public int EOF_sym() {return 0;}

    /** <code>error</code> Symbol index. */
    public int error_sym() {return 1;}



    Hashtable<String,Integer > variables = new Hashtable<String, Integer>();

    public void report_error(String message, Object info) {

        StringBuilder m = new StringBuilder("Error");

        if (info instanceof java_cup.runtime.Symbol) {
            java_cup.runtime.Symbol s = ((java_cup.runtime.Symbol) info);

            if (s.left >= 0) {
                m.append(" in line "+(s.left+1));
                if (s.right >= 0)
                    m.append(", column "+(s.right+1));
            }
        }

        m.append(" : "+message);

        System.err.println(m);
    }

    public void report_fatal_error(String message, Object info) {
        report_error(message, info);
        System.exit(1);
    }


    /** Cup generated class to encapsulate user supplied action code.*/
    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    class CUP$parser$actions {
        private final parser parser;

        /** Constructor */
        CUP$parser$actions(parser parser) {
            this.parser = parser;
        }

        /** Method 0 with the actual generated action code for actions 0 to 300. */
        public final java_cup.runtime.Symbol CUP$parser$do_action_part00000000(
                int                        CUP$parser$act_num,
                java_cup.runtime.lr_parser CUP$parser$parser,
                java.util.Stack            CUP$parser$stack,
                int                        CUP$parser$top)
                throws java.lang.Exception
        {
      /* Symbol object for return from actions */
            java_cup.runtime.Symbol CUP$parser$result;

      /* select the action based on the action number */
            switch (CUP$parser$act_num)
            {
          /*. . . . . . . . . . . . . . . . . . . .*/
                case 0: // s ::= s OR t
                {
                    Node RESULT =null;
                    int s0left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
                    int s0right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
                    Node s0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
                    int t0left = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
                    int t0right = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
                    Node t0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
                    RESULT = new ORNode(s0, t0);
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("s",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 1: // $START ::= s EOF
                {
                    Object RESULT =null;
                    int start_valleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
                    int start_valright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
                    Node start_val = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
                    RESULT = start_val;
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
          /* ACCEPT */
                CUP$parser$parser.done_parsing();
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 2: // s ::= t
                {
                    Node RESULT =null;
                    int t0left = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
                    int t0right = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
                    Node t0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
                    RESULT = t0;
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("s",0, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 3: // t ::= t AND u
                {
                    Node RESULT =null;
                    int t0left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
                    int t0right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
                    Node t0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
                    int u0left = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
                    int u0right = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
                    Node u0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
                    RESULT = new ANDNode(t0, u0);
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("t",1, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 4: // t ::= u
                {
                    Node RESULT =null;
                    int u0left = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
                    int u0right = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
                    Node u0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
                    RESULT = u0;
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("t",1, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 5: // u ::= u ASTERISK
                {
                    Node RESULT =null;
                    int u0left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
                    int u0right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
                    Node u0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
                    RESULT = new RepeatNode(u0);
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("u",2, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 6: // u ::= w
                {
                    Node RESULT =null;
                    int w0left = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
                    int w0right = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
                    Node w0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
                    RESULT = w0;
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("u",2, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 7: // w ::= CARACTER
                {
                    Node RESULT =null;
                    int cleft = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).left;
                    int cright = ((java_cup.runtime.Symbol)CUP$parser$stack.peek()).right;
                    String c = (String)((java_cup.runtime.Symbol) CUP$parser$stack.peek()).value;
                    RESULT = new CharNode(c);
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("w",3, ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 8: // w ::= LPAREN s RPAREN
                {
                    Node RESULT =null;
                    int s0left = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
                    int s0right = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
                    Node s0 = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
                    RESULT = s0;
                    CUP$parser$result = parser.getSymbolFactory().newSymbol("w",3, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)), ((java_cup.runtime.Symbol)CUP$parser$stack.peek()), RESULT);
                }
                return CUP$parser$result;

          /* . . . . . .*/
                default:
                    throw new Exception(
                            "Invalid action number "+CUP$parser$act_num+"found in internal parse table");

            }
        } /* end of method */

        /** Method splitting the generated action code into several parts. */
        public final java_cup.runtime.Symbol CUP$parser$do_action(
                int                        CUP$parser$act_num,
                java_cup.runtime.lr_parser CUP$parser$parser,
                java.util.Stack            CUP$parser$stack,
                int                        CUP$parser$top)
                throws java.lang.Exception
        {
            return CUP$parser$do_action_part00000000(
                    CUP$parser$act_num,
                    CUP$parser$parser,
                    CUP$parser$stack,
                    CUP$parser$top);
        }
    }

}
