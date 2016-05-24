package com.gemcity.xpd.utility;

public class ProgressBar {
	
	public int total;
	public String title;
	public int current;
	private int size = 50;
	private int pct = 0;
	
	public ProgressBar(String title, int total){
		this.title = title;
		this.total = total;
		this.current = 0;
	}
	
	public void update(int a){
		this.current = a-1;
		this.update();
	}
	
	public void update(){
		String bar = "";
		this.current++;
		int temp = ((this.current * 100) / this.total);		
		if(temp > this.pct){
			this.pct = temp;
			int x = (this.pct * this.size) / 100;
			while(x-- > 0)
				bar += "=";
			bar = String.format("%1$-"+this.size+"s", bar);
			String str = title + ": [" + bar + "] " + ((this.current * 100) / this.total) + "% \r";
			System.out.print(str);
			if(this.current == this.total){
				System.out.flush();
				String space = "";
				for(int i=0; i< str.length(); i++)
					space += " ";				
				System.out.print(space+"\r");
			}
		}		
	}
	
	public static void main(String[] args){
		
		try {
			ProgressBar p = new ProgressBar("Doing something", 500);
			for(int i=0; i< 100; i++){
				Thread.sleep(500);                 //1000 milliseconds is one second.
				p.update();
			}
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
	}

}
