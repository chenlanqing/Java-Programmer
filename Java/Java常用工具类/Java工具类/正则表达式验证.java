import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * 正则表达式验证类
 * 
 */
public class RegexTestHarnessV5 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Enter your regex: ");
			Pattern pattern = Pattern.compile(scanner.nextLine());
			System.out.print("Enter input string to search: ");
			Matcher matcher = pattern.matcher(scanner.nextLine());
			
			boolean found = false;			
			while (matcher.find()) {
				System.out.println(
						"Found \""+matcher.group()+
						"\" starting index "+matcher.start()+
						" ending index "+ matcher.end());
				found = true;
			}
			if (!found) {
				System.out.println("No match found.%n");
			}			
		}
	}
}