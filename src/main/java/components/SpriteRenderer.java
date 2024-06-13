package components;
import jade.Component;
public class SpriteRenderer extends Component{
    private boolean firstTime = false;
    @Override
    public void start(){
        System.out.println("Sprite renderer starting.");
    }
    @Override
    public void update(float dt){
     if(!firstTime){
         System.out.println("SPR am updating");
         firstTime = true;
     }
    }
}
