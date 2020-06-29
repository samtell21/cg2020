package cust;

import javax.swing.JOptionPane;

public class Jop 
{
    public static int capturedMessageDialog(String s, String t, Object[] c) throws Cancel{
        int o = JOptionPane.showOptionDialog(null, s, t, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, 
                null, c, null);
        if(o== -1 ) throw new Cancel();
        return o;
        
    }
    
    public static Object capture(String s, String t, Object[] c) throws Cancel{
        return c[capturedMessageDialog(s,t,c)];
    }
    
    public static Object dropDownInputDialog(String s, String t, Object[] c, Object i) throws Cancel{
        Object o = JOptionPane.showInputDialog(null,s,t,JOptionPane.QUESTION_MESSAGE,null,c, i);
        if(o==null) throw new Cancel();
        return o;
    }
    
    public static Object dropDownNums(String s, String t, int first, int last, Object initial) throws Cancel{
        Object[] cs= new Object[last-first+1];
        int x = first;
        for(int i=0;i<cs.length;i++) cs[i]=x++;
        return dropDownInputDialog(s,t,cs,initial);
    }
}
