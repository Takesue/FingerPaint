package sample.application.fingerpaint;

public enum Boolean2 {
	TRUE(true), FALSE(false);
	
	private boolean value = true;
	
	Boolean2(boolean bool) {
		this.value = bool;
	}
	
	public boolean toBoolean(){
		return this.value;
	}
	
}
