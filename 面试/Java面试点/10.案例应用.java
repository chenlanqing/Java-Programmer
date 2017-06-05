1.大数据的四则运算(不适用 BigInteger)


2.一个字节输出流转换为字节输入流
	Singleton singleton = Singleton.getInstance();
	
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	ObjectOutputStream oo = new ObjectOutputStream(out);
	oo.writeObject(singleton);
	
	ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
	ObjectInputStream oi = new ObjectInputStream(in);

	Singleton two = (Singleton) oi.readObject();















