package ca.qc.cvm.dba.grumpy;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import ca.qc.cvm.dba.grumpy.dao.DBConnection;
import ca.qc.cvm.dba.grumpy.event.UIEvent;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.util.SkyFactory;

import de.lessvoid.nifty.Nifty;

public class MainFrame extends SimpleApplication implements Observer {
	private boolean triggerDance = false;
	private boolean triggerNope = false;
	
	private AnimChannel topChannel;
	private AnimChannel baseChannel;
	private Long animCooldown;
	private Long messageCooldown; 
	
	private ChaseCamera chaseCam = null;
	
	public MainFrame() {
		Controller.setMainFrame(this);
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		AppSettings settings = new AppSettings(true);
        settings.setTitle("Grumpy AI");
   	  	setSettings(settings);

    	settings.setWidth(800);
   	  	settings.setHeight(500);
   	  	settings.setFrameRate(60);
   	  	
   	  	start(Type.Display);
   	  	
		this.setPauseOnLostFocus(false);
		this.setDisplayFps(false);
		
	    setDisplayStatView(false);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	@Override
	public void simpleInitApp() {
		// ===========================================================================================
		// Setting camera
		flyCam.setDragToRotate(true);		
		flyCam.setEnabled(false);
	    
	    // ===========================================================================================
        // Adding world
	    Spatial skyNode = SkyFactory.createSky(assetManager, "assets/world/sky.dds", false);
	    rootNode.attachChild(skyNode);
	    
		Node floor = (Node) assetManager.loadModel("assets/world/FlatFloor.j3o");
		floor.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(floor);
	    		
	    // ===========================================================================================
	    // Adding GUI	    
	    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
	    Nifty nifty = niftyDisplay.getNifty();
	    nifty.fromXml("assets/interfaces/gui.xml","main");
	    guiViewPort.addProcessor(niftyDisplay);

        // ===========================================================================================
        // Adding lights	    
		AmbientLight sun = new AmbientLight();
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);

        Vector3f lightDir = new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
    	DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(lightDir);
        sunLight.setColor(ColorRGBA.White.clone().multLocal(1));
        rootNode.addLight(sunLight);

    	DirectionalLight sunLight2 = new DirectionalLight();
        Vector3f lightDir2 = new Vector3f(0.37352666f, -0.50444174f, 0.7784704f);
        sunLight2.setDirection(lightDir2);
        sunLight2.setColor(ColorRGBA.White.clone().multLocal(0.2f));
        rootNode.addLight(sunLight2);
        
        // ===========================================================================================
        // Adding grumpy
        Node grumpy = new Node();
		
		Node character = (Node) assetManager.loadModel("assets/models/grumpy/human.mesh.xml");
		chaseCam = new ChaseCamera(cam, character, inputManager);
		chaseCam.setLookAtOffset(new Vector3f(0, 1f, 0));
		chaseCam.setDefaultDistance(3);
		chaseCam.setDefaultHorizontalRotation(FastMath.DEG_TO_RAD * 90);
		chaseCam.setDefaultVerticalRotation(0);

		AnimControl control = character.getControl(AnimControl.class);

		topChannel = control.createChannel();
		baseChannel = control.createChannel();
		
		baseChannel.setAnim("IdleBase");
		topChannel.setAnim("IdleTop");
		
		character.setLocalScale(0.4f);
		character.setLocalTranslation(0, 2.0f, 0);
		grumpy.attachChild(character);

		rootNode.attachChild(grumpy);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		chaseCam.setDefaultHorizontalRotation(chaseCam.getHorizontalRotation() + FastMath.DEG_TO_RAD * 0.05f);
		
		if (triggerDance) {
			baseChannel.setAnim("Dance");
			topChannel.setAnim("Dance");
			triggerDance = false;
			animCooldown = System.currentTimeMillis() +  2500;
		}
		else if (triggerNope) {
			baseChannel.setAnim("SliceHorizontal");
			topChannel.setAnim("SliceHorizontal");
			triggerNope = false;
			animCooldown = System.currentTimeMillis() +  500;			
		}
		else if (animCooldown != null) {
			if (animCooldown < System.currentTimeMillis()) {
				baseChannel.setAnim("IdleBase");
				topChannel.setAnim("IdleTop");
				animCooldown = null;
			}
		}
		
		if (messageCooldown != null && messageCooldown < System.currentTimeMillis()) {
			messageCooldown = null;
			Controller.setMessage("");
		}
	}

	public static void main(String[] args) throws Exception {
        new MainFrame();
 	}

	@Override
	public void update(Observable o, Object arg) {
		if (((UIEvent)arg).getUIType() == UIEvent.UIType.TriggerDance) {
			triggerDance = true;
		}
		else if (((UIEvent)arg).getUIType() == UIEvent.UIType.Nope) {
			triggerNope = true;
		}
		
		if (((UIEvent)arg).getData() != null) {
			messageCooldown = System.currentTimeMillis() + 4000;
			Controller.setMessage(((UIEvent)arg).getData().toString());
		}
	}
}