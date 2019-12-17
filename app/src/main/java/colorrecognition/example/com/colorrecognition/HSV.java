package colorrecognition.example.com.colorrecognition;

public class HSV {
    public static int[] HSVnum(  int[] buf ){
        float red=buf[0],green=buf[1],blue=buf[2];
        float redgreen=red>green?red:green;
        float max=redgreen>blue?redgreen:blue;
        float min=redgreen<blue?redgreen:blue;
        int colorV= (int) max;
        int colorS= (int) (225*(max-min)/max);
        int colorH=0;
        if(red==max){
            colorH= (int) ((green-blue)/(max-min)*60);
        }
        if(green==max){
            colorH= (int) (120+(blue-red)/(max-min)*60);
        }
        if(blue==max){
            colorH= (int) (240+(red-green)/(max-min)*60);
        }
        if(colorH<0){colorH=colorH+360;}

        return new int[]{colorH,colorS,colorV};
    }




}
