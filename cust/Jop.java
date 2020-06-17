package cust;

import javax.swing.JOptionPane;

public class Jop 
{
    public static int capturedMessageDialog(String s, String t, Object[] c)
    {
        return JOptionPane.showOptionDialog(null, s, t, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, 
                null, c, null);
    }
    
    public static Object capture(String s, String t, Object[] c)
    {
        return c[capturedMessageDialog(s,t,c)];
    }
    
    public static Object dropDownInputDialog(String s, String t, Object[] c, int i)
    {
        return JOptionPane.showInputDialog(null,s,t,JOptionPane.QUESTION_MESSAGE,null,c,c[i]);
    }
    
    public static Object dropDownNums(String s, String t, int first, int last, int initial)
    {
        Object[] cs= new Object[last-first+1];
        int x = first;
        for(int i=0;i<cs.length;i++)
            cs[i]=x++;
        return JOptionPane.showInputDialog(null, s, t, JOptionPane.QUESTION_MESSAGE, null, cs, cs[initial]);
    }
}
