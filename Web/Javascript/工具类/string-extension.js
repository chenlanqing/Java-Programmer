/**
 *  String扩展方法：替换所有字符串；
 *  
 */
String.prototype.replaceAll = function(replacedText, replaceText){
	return this.replace(new RegExp(replacedText, 'gm'), replaceText)
}