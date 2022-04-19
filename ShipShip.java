 package shipShip;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.JFrame;

public class ShipShip implements ActionListener,MouseListener,KeyListener {
	
    public static  ShipShip shipShip;/*I created a new ship object for our new game*/
    public Renderer renderer;/*this is for being real to our ship and sea :))*/
    public Rectangle ship;
    public Random rand;
    
    public final int WIDTH = 800,HEIGHT=800;
   
    public boolean gameisEnd,started;
    public int ticks,yMotion,score;
    
    public ArrayList<Rectangle> flowers;
    
    public ShipShip(){
    	
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20,this);
        renderer = new Renderer();
        rand =new Random();
        
        jframe.add(renderer);
        jframe.setTitle("Ship Ship!");  
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH,HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        ship = new Rectangle(200,200,25,25);
        
        flowers= new ArrayList<Rectangle>();

        AddFlower(true);
        AddFlower(true);
        AddFlower(true);
        AddFlower(true);
        
       timer.start();
        
    }
    public void AddFlower(boolean start) {
        int width = 100, height = 100;
        int midfloheight=height+rand.nextInt(375);
        if(start){
            flowers.add(new Rectangle(WIDTH + width + flowers.size() * 300, HEIGHT - height - 150, width, height));/*the flower which is bottom side*/
            flowers.add(new Rectangle(WIDTH + width + flowers.size() * 300+50,midfloheight, width, height));
            flowers.add(new Rectangle(WIDTH + width + (flowers.size() - 1) * 300+100, 0, width,height));/*the flower which is top side*/
        }
        else{
            flowers.add(new Rectangle(flowers.get(flowers.size() - 1).x + 300, HEIGHT - height - 150, width, height));
            flowers.add(new Rectangle(WIDTH + width + flowers.size() * 300+50,midfloheight, width, height));
            flowers.add(new Rectangle(flowers.get(flowers.size() - 1).x+250, 0, width, height));
        }
    }
    
    public void paintFlower(Graphics g,Rectangle flower){/*the method which is creating new flowers :)*/
    	
        g.setColor(new Color(223, 171, 239));
        g.fillRect(flower.x,flower.y,flower.width,flower.height);

    }
    
    public void jump() {
	
		if (gameisEnd)
		{
			ship = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			flowers.clear();
			yMotion = 0;
			score = 0;

			AddFlower(true);
			AddFlower(true);
			AddFlower(true);
			AddFlower(true);
			
			gameisEnd = false;
		}

		if (!started)
		{
			started = true;
		}
		
		else if (!gameisEnd)/*there's for make the ship 10 down while the game keep going*/
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
		}
	}

    @Override
    public void actionPerformed(ActionEvent e) {
    	
        int speed =10;
        ticks++;
        if(started) {
    		for (int i = 0; i < flowers.size(); i++)//this code is for make flowers move on x coordinate 
    		{
    			Rectangle flower = flowers.get(i);

    			flower.x -= speed;
    		}

    		if (ticks % 2 == 0 && yMotion < 15)//this code is for make ship move on y coordinate
    		{
    			yMotion += 2;
    		}

    		for (int i = 0; i < flowers.size(); i++)
    		{
    			Rectangle flower = flowers.get(i);

    			if (flower.x + flower.width < 0)//When flower went to outside from screen this code remove that flower
    			{
    				flowers.remove(flower);

    				if (flower.y == 0)
    				{
    					AddFlower(false);
    				}
    			}
    		}
        }

		ship.y += yMotion; 
		for (Rectangle flower : flowers)
		{
			if (flower.y == 0 && ship.x + ship.width / 2 > flower.x + flower.width / 2 - 10 && ship.x + ship.width / 2 < flower.x + flower.width / 2 + 10)
			{
				score++;
			}

			if (flower.intersects(ship))//if flower and ship touch each other the game will be ended
			{
				gameisEnd = true;

				if (ship.x <= flower.x)
				{
					ship.x = flower.x - ship.width;

				}
				else
				{
					if (flower.y != 0)
					{
						ship.y = flower.y - ship.height;
					}
					else if (ship.y < flower.height)
					{
						ship.y = flower.height;
					}
				}
			}
			
		}

		if (ship.y > HEIGHT - 150 || ship.y < 0)
		{
			gameisEnd = true;
		}

		if (ship.y + yMotion >= HEIGHT - 150)
		{
			ship.y = HEIGHT - 150 - ship.height;
			gameisEnd = true;
		}
		renderer.repaint();
	}
        
    

    public void repaint(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(0,0,WIDTH,HEIGHT);

        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT-150, WIDTH, 150);

        g.setColor(Color.YELLOW);
        g.fillRect(0, HEIGHT-150, WIDTH, 25);

        g.setColor(new Color(171, 239, 213));
        g.fillRect(ship.x,ship.y,ship.width,ship.height);

        for(Rectangle flower : flowers){
            paintFlower(g,flower);
        }
        g.setColor(Color.decode("#F4FCAE"));
        g.setFont(new Font("Bank Gothic",Font.ITALIC,50));
        if(!started) {
        	g.drawString("Click to Start <3 ",WIDTH/2-200,HEIGHT/2-50);
        }
        if(gameisEnd) {
            g.drawString("): Game is Over :(",WIDTH/2-200,HEIGHT/2-50);
        }
        if (!gameisEnd && started)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}


    }

    public static void main(String[] args){
        shipShip = new ShipShip();
    }

	@Override
	public void mouseClicked(MouseEvent e)
	{
		jump();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}
	}