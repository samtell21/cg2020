package cust;

public class Cancel extends Throwable{
	private static final long serialVersionUID = 1L;
        private final Object controlVal;
        
        public Cancel(){
            controlVal = null;
        }
        public Cancel(Object o){
            controlVal = o;
        }
        public Object controlVal(){
            return controlVal;
        }
}
