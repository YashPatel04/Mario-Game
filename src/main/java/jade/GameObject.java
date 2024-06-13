package jade;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;

public class GameObject {
    // Name of the game object just for human readability purposes
    private String name;
    //Arraylist of Components that the object will possess or have
    private ArrayList<Component> components;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
    }

    public <T extends  Component> T getComponent(Class<T> componentClass){
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try{
                    return componentClass.cast(c);
                }catch (ClassCastException e){
                    e.printStackTrace();
                    assert false : "Error: Casting component. ";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class <T> componentClass){
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        this.components.add(c);
        c.gameObject = this;
    }
    public void update(float dt){
        for(int i=0; i < components.size(); i++){
            components.get(i).update(dt);
        }
    }
    public void start(){
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }
}
