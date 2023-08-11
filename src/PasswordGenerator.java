public class PasswordGenerator
{
	int capacity = 0,add = 0;
	
	String caps[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	String small[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	String numbers[] = {"0","1","2","3","4","5","6","7","8","9"};
	String special[] = {"!","@","#","&","*","-","_","=","+"};
	
	String generator[] = new String[71];
	
	String password = "";
	
	PasswordGenerator(int length,boolean caps,boolean small,boolean number,boolean special)
	{
		if(caps)
		{
			for(int gc=capacity, c=0; gc<capacity+26 && c<26;gc++, c++)
			{
				generator[gc] = this.caps[c];
				capacity = gc;
				add = 1;
			}
		}
		
		if(small)
		{
			if(add == 1)
			{
				capacity =capacity+1;
			}
			
			for(int gc=capacity, c=0; gc<capacity+26 && c<26;gc++, c++)
			{
				generator[gc] = this.small[c];
				capacity = gc;
				add = 1;
			}
		}
		
		if(number)
		{
			if(add == 1)
			{
				capacity =capacity+1;
			}
			
			for(int gc=capacity, c=0; gc<capacity+10 && c<10;gc++, c++)
			{
				generator[gc] = numbers[c];
				capacity = gc;
				add = 1;
			}
		}
		
		if(special)
		{
			if(add == 1)
			{
				capacity =capacity+1;
			}
			
			for(int gc=capacity, c=0; gc<capacity+9 && c<9;gc++, c++)
			{
				generator[gc] = this.special[c];
				capacity = gc;
				add = 1;
			}
		}
		
		for(int i=0; i<length; i++)
		{
			password = password + generator[(int)(Math.random() * capacity)];
		}
	}
}