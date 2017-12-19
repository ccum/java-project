public class Rectangulator{
 public static void main(String[] args) {
     int length = Integer.parseInt(args[0]);
     int width = Integer.parseInt(args[1]);

     Rectangle myRectangle = new Rectangle(length, width);
     String output = String.format("Your rectagle *** Length: %d, Width: %d Area: %d , Parimeter: %d",myRectangle.length,myRectangle.width,myRectangle.getArea(),myRectangle.getPermiter() );
     System.out.println(output);
 }

}